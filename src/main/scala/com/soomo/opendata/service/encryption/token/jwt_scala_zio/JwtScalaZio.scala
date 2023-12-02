package com.soomo.opendata.service.encryption.token.jwt_scala_zio

import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.ServiceError.LogicError
import com.soomo.opendata.domain.model.payload.LoginSuccessPayload
import pdi.jwt.*
import zio.*
import zio.json.*

import java.time.{Duration, Instant}
import javax.crypto.SecretKey
import scala.util.{Failure, Success}

trait JwtScalaZio:

  def encodeJWT(claim: JwtClaim): IO[LogicError, String]

  def decodeJWT(jwt: String): IO[LogicError, JwtClaim]

/**
 * Companion object for the JwtScalaZio trait
 */

object JwtScalaZio:
  def encodeJWT(claim: JwtClaim): ZIO[JwtScalaZio, LogicError, String] =
    ZIO.serviceWithZIO(_.encodeJWT(claim))

  def decodeJWT(jwt: String): ZIO[JwtScalaZio, LogicError, JwtClaim] =
    ZIO.serviceWithZIO(_.decodeJWT(jwt))
