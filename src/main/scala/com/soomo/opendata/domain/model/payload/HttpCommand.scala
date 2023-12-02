package com.soomo.opendata.domain.model.payload

import zio.json.{DeriveJsonCodec, JsonCodec}

case class HttpCommand(command: String, parameter: Option[String], target: Option[String])
object HttpCommand:
  given JsonCodec[HttpCommand] = DeriveJsonCodec.gen[HttpCommand]
