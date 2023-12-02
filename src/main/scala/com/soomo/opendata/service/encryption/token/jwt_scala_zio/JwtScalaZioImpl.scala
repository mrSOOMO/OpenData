package com.soomo.opendata.service.encryption.token.jwt_scala_zio

import com.soomo.opendata.service.encryption.token.jwt_scala_zio.JwtScalaZioImpl
import zio.{URLayer, ZLayer}

/**
 * Implementation of the JwtScalaZio service using JSON Web Tokens (JWTs). JWTs
 * are used to create access tokens for client-side authentication.
 *
 * @note
 *   The current implementation uses a test key for the JWTs and HS256 (HMAC
 *   with SHA-256) as the signing algorithm. This should be replaced with a
 *   secure key and appropriate algorithm in a production setting.
 *
 * @example
 *   Encoding a JWT:
 *   {{{
 * encodeJWT(jwtClaim)
 *   }}}
 * @example
 *   Decoding a JWT:
 *   {{{
 * decodeJWT(jwtString)
 *   }}}
 */

final class JwtScalaZioImpl extends JwtScalaZio:

  import com.soomo.opendata.domain.ServiceError.LogicError
  import com.soomo.opendata.domain.model.payload.LoginSuccessPayload
  import pdi.jwt.*
  import zio.*
  import zio.json.*

  import java.time.{Duration, Instant}
  import javax.crypto.SecretKey
  import scala.util.{Failure, Success}

  /**
   * TODO: Test key MOCK. needs to be replaced with The key.
   */
  private val key       = "secretKey"
  private val algorithm = JwtAlgorithm.HS256

  /**
   * Encodes a JWT claim into a JWT string.
   *
   * @param claim
   *   The JWT claim to be encoded.
   * @return
   *   A ZIO effect that either fails with a `LogicError` or succeeds with a JWT
   *   string.
   */

  def encodeJWT(claim: JwtClaim): IO[LogicError, String] =
    ZIO.from(Jwt.encode(claim, key, algorithm)).mapError(LogicError)

  /**
   * Decodes a JWT string into a JWT claim.
   *
   * @param jwt
   *   The JWT string to be decoded.
   * @return
   *   A ZIO effect that either fails with a `LogicError` or succeeds with a JWT
   *   claim.
   */
  def decodeJWT(jwt: String): IO[LogicError, JwtClaim] = ZIO.fromTry {
    Jwt.decode(jwt, key, Seq(algorithm))
  }.mapError(LogicError)

  /**
   * Companion object for `JwtScalaZioImpl`.
   */
object JwtScalaZioImpl {
  val live: URLayer[Any, JwtScalaZioImpl] =
    ZLayer.succeed(new JwtScalaZioImpl())
}
