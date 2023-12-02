package com.soomo.opendata.dao.organization

import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.ServiceError.DaoError
import com.soomo.opendata.domain.model.organization.Organization
import io.getquill.*
import zio.*

import scala.concurrent.Future

final class OrganizationDaoQuill(ctx: PostgresJAsyncContext[SnakeCase]) extends OrganizationDao:

  import ctx.*

  private inline def insertOrganization(organization: Organization): Quoted[Insert[Organization]] = quote {
    query[Organization]
      .insert(
        _.name                -> lift(organization.name),
        _.address             -> lift(organization.address),
        _.identificationDocs  -> lift(organization.identificationDocs),
        _.associatedPersonIds -> lift(organization.associatedPersonIds)
      )
      .onConflictUpdate(org => org.id)(
        (t, e) => t.name                -> e.name,
        (t, e) => t.address             -> e.address,
        (t, e) => t.identificationDocs  -> e.identificationDocs,
        (t, e) => t.associatedPersonIds -> e.associatedPersonIds
      )
  }


  def addAll(organizations: Seq[Organization]): IO[ServiceError, Unit] =
    for {
      _ <- ZIO.fromFuture { implicit ec =>
        Future.sequence(organizations.map(org => run(insertOrganization(org))))
      }.mapError(DaoError)
    } yield ()

  def getAll(): IO[ServiceError, Seq[Organization]] =
    ZIO.fromFuture { implicit ec =>
      run (quote(query[Organization]))
    }.mapError(DaoError)

  def getByName(name: String): IO[ServiceError, Organization] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Organization].filter(_.name.contains(lift(name))))).map(_.head)
    }.mapError(DaoError)

  def getById(id: Int): IO[ServiceError, Organization] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Organization].filter(_.id == lift(id)))).map(_.head)
    }.mapError(DaoError)

  def deleteById(id: Int): IO[ServiceError, Long] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Organization].filter(_.id == lift(id))).delete)
    }.mapError(DaoError)

object OrganizationDaoQuill:

  val live: ZLayer[PostgresJAsyncContext[SnakeCase], Nothing, OrganizationDaoQuill] =
    ZLayer(
      for ctx <- ZIO.service[PostgresJAsyncContext[SnakeCase]]
        yield OrganizationDaoQuill(ctx)
    )
