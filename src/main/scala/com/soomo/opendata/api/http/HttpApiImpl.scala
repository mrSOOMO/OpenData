package com.soomo.opendata.api.http

import com.soomo.opendata.api.ws.WsApi
import com.soomo.opendata.dao.account.{AccountDao, AccountDaoQuill}
import com.soomo.opendata.domain.DbConfigPath
import zio.http.model.Method
//import com.soomo.opendata.domain.config.ConfigurationTest.ServerConfig
import com.soomo.opendata.domain.model.account.{Account, Credentials}
import com.soomo.opendata.flyway.FlywayProvider
import com.soomo.opendata.handlers.api_handlers.AccountHttpHandler
import com.soomo.opendata.service.encryption.password.{BCryptEncryption, Encryption}
import io.getquill.*
import io.getquill.jdbczio.Quill
import zio.*
import zio.config.*
import zio.http.*
import zio.http.ChannelEvent.ChannelRead
import zio.http.socket.{WebSocketChannelEvent, WebSocketFrame}
import zio.json.*

import scala.tools.tasty.TastyName.Root

final class HttpApiImpl extends HttpApi {

  override def appHttp: Http[AccountDao with Encryption, Nothing, Request, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> !! / "greet" / name => ZIO.succeed(Response.text(s"Greetings, $name!"))
      case req @ method -> !! / "account"    => AccountHttpHandler.handle(req)
    }
}

object HttpApiImpl:

  val live: ZLayer[Any, Nothing, HttpApiImpl] =
    ZLayer.succeed(new HttpApiImpl)
