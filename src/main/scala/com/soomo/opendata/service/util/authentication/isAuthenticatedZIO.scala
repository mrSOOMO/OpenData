package com.soomo.opendata.service.util.authentication

import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.ServiceError.LogicError
import com.soomo.opendata.domain.model.payload.*
import com.soomo.opendata.service.encryption.token.jwt_scala_zio.JwtScala
import pdi.jwt.JwtClaim
import zio.ZIO
import zio.json.*

import java.time.Instant

/**
 * Checks whether a JWT payload is authenticated.
 *
 * The function decodes the given JWT payload and verifies its content based on
 * three criteria:
 *   - Account ID should be greater than or equal to 1.
 *   - Access level should be greater than or equal to 1.
 *   - The expiration time (expireAt) should be after the current time.
 *
 * If the JWT payload fails to satisfy any of these conditions, or if there is
 * an error during the decoding or parsing process, the function will return a
 * ZIO effect encapsulating `false`.
 *
 * @example
 *   {{{
 * isAuthenticatedZIO(jwtPayload)
 *   }}}
 *
 * @param payloadToCheck
 *   The JWT payload string to be verified.
 * @return
 *   A ZIO effect that encapsulates a boolean value. The value will be `true` if
 *   the JWT payload satisfies all the authentication criteria, and `false`
 *   otherwise.
 */
def isAuthenticatedZIO(payloadToCheck: String): ZIO[Any, Nothing, Boolean] =
  (for {
    jwt <- ZIO
      .fromEither(JwtScala.decodeJWT(payloadToCheck))
      .mapError(_ => LogicError(new Exception("Failed to decode JWT")))
    payload <- ZIO
      .fromEither(jwt.content.fromJson[LoginSuccessPayload])
      .mapError(_ => LogicError(new Exception("Failed to parse JWT content")))
  } yield payload.accountId >= 1 &&
    payload.accessLevel >= 1 &&
    payload.expireAt.isAfter(Instant.now()))
    .fold(_ => false, identity)
