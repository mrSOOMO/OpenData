package com.soomo.opendata.dao.realstate

import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.model.realstate.*
import zio.{IO, ZIO}
trait RealEstateDao {
  def addAll(realEstates: Seq[RealEstate]): IO[ServiceError, Unit]

  def getAll(): IO[ServiceError, Seq[RealEstate]]

  def getById(id: Int): IO[ServiceError, Option[RealEstate]]

  def getByCoordinates(coordinates: Coordinates): IO[ServiceError, Seq[RealEstate]]

  def deleteById(id: Int): IO[ServiceError, Long]
}

object RealEstateDao {

  def addAll(realEstates: Seq[RealEstate]): ZIO[RealEstateDao, ServiceError, Unit] =
    ZIO.serviceWithZIO[RealEstateDao](_.addAll(realEstates))

  def getAll(): ZIO[RealEstateDao, ServiceError, Seq[RealEstate]] =
    ZIO.serviceWithZIO[RealEstateDao](_.getAll())

  def getById(id: Int): ZIO[RealEstateDao, ServiceError, Option[RealEstate]] =
    ZIO.serviceWithZIO[RealEstateDao](_.getById(id))

  def getByCoordinates(coordinates: Coordinates): ZIO[RealEstateDao, ServiceError, Seq[RealEstate]] =
    ZIO.serviceWithZIO[RealEstateDao](_.getByCoordinates(coordinates))

  def deleteById(id: Int): ZIO[RealEstateDao, ServiceError, Long] =
    ZIO.serviceWithZIO[RealEstateDao](_.deleteById(id))
}

