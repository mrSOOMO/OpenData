package com.soomo.opendata.dao.person

import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.ServiceError.DaoError
import com.soomo.opendata.domain.model.person.Person
import io.getquill.*
import zio.*

import scala.concurrent.Future
final class PersonDaoQuill(ctx: PostgresJAsyncContext[SnakeCase]) extends PersonDao:

  import ctx.*
  private inline def insertPerson(person: Person): Quoted[Insert[Person]] = quote {
    query[Person]
      .insert(
        _.firstName          -> lift(person.firstName),
        _.lastName           -> lift(person.lastName),
        _.middleName         -> lift(person.middleName),
        _.phones             -> lift(person.phones),
        _.addresses          -> lift(person.addresses),
        _.socialIds          -> lift(person.socialIds),
        _.additionalInfo     -> lift(person.additionalInfo),
        _.photos             -> lift(person.photos),
        _.associatedPersonIds  -> lift(person.associatedPersonIds)
      )
      .onConflictIgnore
  }

  def addAll(persons: Seq[Person]): IO[ServiceError, Unit] =
    for {
      _ <- ZIO.fromFuture { implicit ec =>
        Future.sequence(persons.map(person => run(insertPerson(person))))
      }.mapError(DaoError)
    } yield ()

  def getAll(): IO[ServiceError, Seq[Person]] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Person]))
    }.mapError(DaoError)

  def getByName(name: String): IO[ServiceError, Person] =
    ZIO.fromFuture { implicit ec =>
      run(quote(query[Person].filter(_.lastName.contains(lift(name))))).map(_.head)
    }.mapError(DaoError)

  def getById(id: Int): IO[ServiceError, Person] = ZIO.fromFuture { implicit ec =>
    run(quote(query[Person]).filter(_.id == lift(id))).map(_.head)
  }.mapError(DaoError)

  def deleteById(id: Int): IO[ServiceError, Long] = ZIO.fromFuture { implicit ec =>
    run(quote(query[Person].filter(_.id == lift(id))).delete)
  }.mapError(DaoError)

object PersonDaoQuill:

  val live: ZLayer[PostgresJAsyncContext[SnakeCase], Nothing, PersonDaoQuill] =
    ZLayer(
      for ctx <- ZIO.service[PostgresJAsyncContext[SnakeCase]]
        yield PersonDaoQuill(ctx)
    )
