package com.soomo.opendata.dao.organization

import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.model.organization.Organization
import zio.{IO, ZIO}

trait OrganizationDao:
  def addAll(organizations: Seq[Organization]): IO[ServiceError, Unit]

  def getAll(): IO[ServiceError, Seq[Organization]]

  def getByName(name: String): IO[ServiceError, Organization]

  def getById(id: Int): IO[ServiceError, Organization]

  def deleteById(id: Int): IO[ServiceError, Long]

/**
 * Companion object for the OrganizationDao class.
 */
object OrganizationDao:
  def addAll(organizations: Seq[Organization]): ZIO[OrganizationDao, ServiceError, Unit] =
    ZIO.serviceWithZIO(_.addAll(organizations))

  def getAll(): ZIO[OrganizationDao, ServiceError, Seq[Organization]] =
    ZIO.serviceWithZIO(_.getAll())

  def getByName(name: String): ZIO[OrganizationDao, ServiceError, Organization] =
    ZIO.serviceWithZIO(_.getByName(name))

  def getById(id: Int): ZIO[OrganizationDao, ServiceError, Organization] =
    ZIO.serviceWithZIO(_.getById(id))

  def deleteById(id: Int): ZIO[OrganizationDao, ServiceError, Long] =
    ZIO.serviceWithZIO(_.deleteById(id))
