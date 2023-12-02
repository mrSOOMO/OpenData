package com.soomo.opendata.service.encryption.token

import com.soomo.opendata.domain.ServiceError.LogicError
import zio.{IO, ZIO}

trait TokenEncryption {
  def encrypt(str: String): IO[LogicError, String]
  def decrypt(str: String): IO[LogicError, String]

}

object TokenEncryption:
  def encrypt(str: String): ZIO[TokenEncryption, LogicError, String] =
    ZIO.serviceWithZIO(_.encrypt(str))

  def decrypt(str: String): ZIO[TokenEncryption, LogicError, String] =
    ZIO.serviceWithZIO(_.decrypt(str))
