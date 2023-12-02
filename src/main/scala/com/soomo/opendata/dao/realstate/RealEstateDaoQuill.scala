package com.soomo.opendata.dao.realstate

import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.model.realstate.*
import zio.{IO, Task}
import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.ServiceError.DaoError
import io.getquill.*
import zio.*
import zio.Task


import scala.concurrent.Future

final class RealEstateDaoQuill(ctx: PostgresJAsyncContext[SnakeCase]) extends RealEstateDao {

  import ctx.*

  private inline def insertRealEstate(realEstate: RealEstate): Quoted[Insert[RealEstate]] = quote {
    query[RealEstate]
      .insert(_.size -> lift(realEstate.size),
        _.address -> lift(realEstate.address),
        _.ownerIds -> lift(realEstate.ownerIds),
        _.propertyType -> lift(realEstate.propertyType),
        _.additionalInfo -> lift(realEstate.additionalInfo),
        _.coordinates -> lift(realEstate.coordinates))
      .onConflictUpdate(estate => estate.id)(
        (t, e) => t.address -> e.address,
        (t, e) => t.coordinates -> e.coordinates,
        (t, e) => t.propertyType -> e.propertyType,
        (t, e) => t.size -> e.size,
        (t, e) => t.ownerIds -> e.ownerIds,
        (t, e) => t.additionalInfo -> e.additionalInfo
      )
  }
  def addAll(realEstates: Seq[RealEstate]): IO[ServiceError, Unit] =
    for {
      _ <- ZIO.fromFuture { implicit ec =>
        Future.sequence(realEstates.map(re => run(insertRealEstate(re))))
      }.mapError(DaoError)
    } yield ()

  override def getAll(): IO[ServiceError, Seq[RealEstate]] =
    ZIO.fromFuture { implicit ec =>
      run(query[RealEstate])
    }.mapError(DaoError)

  override def getById(id: Int): IO[ServiceError, Option[RealEstate]] =
    ZIO.fromFuture { implicit ec =>
      run(query[RealEstate].filter(_.id == lift(id))).map(_.headOption)
    }.mapError(DaoError)

  override def getByCoordinates(coordinates: Coordinates): IO[ServiceError, Seq[RealEstate]] =
    ZIO.fromFuture { implicit ec =>
      run(query[RealEstate].filter(_.coordinates.contains(lift(coordinates))))
    }.mapError(DaoError)

  override def deleteById(id: Int): IO[ServiceError, Long] =
    ZIO.fromFuture { implicit ec =>
      run(query[RealEstate].filter(_.id == lift(id)).delete)
    }.mapError(DaoError)
}

object RealEstateDaoQuill {
  val live: ZLayer[PostgresJAsyncContext[SnakeCase], Nothing, RealEstateDaoQuill] =
    ZLayer(
      for ctx <- ZIO.service[PostgresJAsyncContext[SnakeCase]]
        yield RealEstateDaoQuill(ctx)
    )
}

