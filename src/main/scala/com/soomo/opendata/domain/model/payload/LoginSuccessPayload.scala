package com.soomo.opendata.domain.model.payload

import zio.json.{DeriveJsonCodec, JsonCodec}

import java.time.Instant
case class LoginSuccessPayload(accountId: Int, accessLevel: Int, expireAt: Instant)

/**
 * Companion object for the LoginSuccessPayload class.
 */
object LoginSuccessPayload:
  given JsonCodec[LoginSuccessPayload] = DeriveJsonCodec.gen[LoginSuccessPayload]
