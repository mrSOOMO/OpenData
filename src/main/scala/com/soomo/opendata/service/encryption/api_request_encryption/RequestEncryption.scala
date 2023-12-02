package com.soomo.opendata.service.encryption.api_request_encryption

import com.soomo.opendata.domain.ServiceError.DecryptionError
import zio.{IO, ZIO}

trait RequestEncryption {
  def encrypt(str: String): IO[DecryptionError, String]
  def decrypt(str: String): IO[DecryptionError, String]

}

object RequestEncryption:
  def encrypt(str: String): ZIO[RequestEncryption, DecryptionError, String] =
    ZIO.serviceWithZIO(_.encrypt(str))

  def decrypt(str: String): ZIO[RequestEncryption, DecryptionError, String] =
    ZIO.serviceWithZIO(_.decrypt(str))
