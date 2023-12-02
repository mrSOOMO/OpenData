package com.soomo.opendata.dao.account

import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.model.account.Account
import zio.{IO, ZIO}

trait AccountDao:
  def addAll(accounts: Seq[Account]): IO[ServiceError, Unit]

  def getAll(): IO[ServiceError, Seq[Account]]

  def getByLogin(login: String): IO[ServiceError, Account]

  def getById(id: Int): IO[ServiceError, Account]

  def deleteById(id: Int): IO[ServiceError, Long]

/**
 * Companion object for the AccountDao class.
 */

object AccountDao:
  def addAll(accounts: Seq[Account]): ZIO[AccountDao, ServiceError, Unit] =
    ZIO.serviceWithZIO(_.addAll(accounts))
  def getAll(): ZIO[AccountDao, ServiceError, Seq[Account]] =
    ZIO.serviceWithZIO(_.getAll())
  def getByLogin(login: String): ZIO[AccountDao, ServiceError, Account] =
    ZIO.serviceWithZIO(_.getByLogin(login))
  def getById(id: Int): ZIO[AccountDao, ServiceError, Account] =
    ZIO.serviceWithZIO(_.getById(id))
  def deleteById(id: Int): ZIO[AccountDao, ServiceError, Long] =
    ZIO.serviceWithZIO(_.deleteById(id))
