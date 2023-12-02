package com.soomo.opendata.domain.model.account

import zio.json.*

case class Credentials(login: String, password: String)

object Credentials {
  given JsonCodec[Credentials] = DeriveJsonCodec.gen[Credentials]
}
