package com.soomo.opendata.domain.model.payload

//import com.soomo.opendata.dao.account.{AccountDao, AccountDaoQuill}
//import com.soomo.opendata.domain.DbConfigPath
//import com.soomo.opendata.domain.model.account.{Account, Credentials}
//import com.soomo.opendata.flyway.FlywayProvider
//import com.soomo.opendata.service.encryption.password.{BCryptEncryption, Encryption}
import com.soomo.opendata.domain.model.account.Credentials
import io.getquill.*
import io.getquill.jdbczio.Quill
import zio.*
import zio.config.*
import zio.http.*
import zio.http.ChannelEvent.ChannelRead
import zio.http.socket.{WebSocketChannelEvent, WebSocketFrame}
import zio.json.*
import zio.json.ast.Json

sealed trait Command {
  def command: String
}
case class CommandWithCredentials(command: String, credentials: Credentials) extends Command
object CommandWithCredentials:
  given JsonCodec[CommandWithCredentials] = DeriveJsonCodec.gen[CommandWithCredentials]

case class CommandWithJwt(target: String, command: String, jwt: String, parameter: Option[String]) extends Command
object CommandWithJwt:
  given JsonCodec[CommandWithJwt] = DeriveJsonCodec.gen[CommandWithJwt]

object Command {
  def decode(json: String): Either[String, Command] = {
    given JsonCodec[CommandWithCredentials] = DeriveJsonCodec.gen[CommandWithCredentials]
    given JsonCodec[CommandWithJwt]         = DeriveJsonCodec.gen[CommandWithJwt]

    json
      .fromJson[CommandWithCredentials]
      .fold(
        _ => json.fromJson[CommandWithJwt],
        c => Right(c)
      )
  }
}
