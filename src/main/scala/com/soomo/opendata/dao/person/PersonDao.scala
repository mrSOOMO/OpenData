package com.soomo.opendata.dao.person

import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.model.person.Person
import zio.{IO, ZIO}

trait PersonDao:
  def addAll(persons: Seq[Person]): IO[ServiceError, Unit]

  def getAll(): IO[ServiceError, Seq[Person]]

  def getByName(name: String): IO[ServiceError, Person]

  def getById(id: Int): IO[ServiceError, Person]

  def deleteById(id: Int): IO[ServiceError, Long]

/**
 * Companion object for the PersonDao class.
 */
object PersonDao:
  def addAll(persons: Seq[Person]): ZIO[PersonDao, ServiceError, Unit] =
    ZIO.serviceWithZIO(_.addAll(persons))

  def getAll(): ZIO[PersonDao, ServiceError, Seq[Person]] =
    ZIO.serviceWithZIO(_.getAll())

  def getByName(name: String): ZIO[PersonDao, ServiceError, Person] =
    ZIO.serviceWithZIO(_.getByName(name))

  def getById(id: Int): ZIO[PersonDao, ServiceError, Person] =
    ZIO.serviceWithZIO(_.getById(id))

  def deleteById(id: Int): ZIO[PersonDao, ServiceError, Long] =
    ZIO.serviceWithZIO(_.deleteById(id))
