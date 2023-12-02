package com.soomo.opendata.service.encryption.token.jwt_scala_zio

import com.soomo.opendata.domain.ServiceError
import zio.{URLayer, ZLayer}

object JwtScala:

  import ServiceError.LogicError
  import com.soomo.opendata.domain.model.payload.LoginSuccessPayload
  import pdi.jwt.*
  import zio.*
  import zio.json.*

  import java.time.{Duration, Instant}
  import javax.crypto.SecretKey
  import scala.util.{Failure, Success}

  /**
   * TODO: Test key MOCK. need to be replaced with The key.
   */
  private val key       = "secretKey"
  private val algorithm = JwtAlgorithm.HS256

  def encodeJWT(claim: JwtClaim): String =
    Jwt.encode(claim, key, algorithm)
  def decodeJWT(jwt: String): Either[ServiceError, JwtClaim] =
    Jwt.decode(jwt, key, Seq(algorithm)) match
      case Success(claim) => Right(claim)
      case Failure(e)     => Left(ServiceError.LogicError(e)) // pass the exception to the LogicError
