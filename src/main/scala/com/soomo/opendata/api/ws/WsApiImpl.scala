package com.soomo.opendata.api.ws

import com.soomo.opendata.dao.account.AccountDao
import com.soomo.opendata.dao.person.PersonDao
import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.model.account.Credentials
import com.soomo.opendata.domain.model.payload.{Command, CommandWithCredentials, CommandWithJwt, LoginSuccessPayload}
import com.soomo.opendata.handlers.ws_handlers.*
import com.soomo.opendata.service.encryption.password.Encryption
import com.soomo.opendata.service.encryption.token.jwt_scala_zio.{JwtScala, JwtScalaZio}
import pdi.jwt.JwtClaim
import zio.*
import zio.config.*
import zio.http.*
import zio.http.ChannelEvent.ChannelRead
import zio.http.model.Method
import zio.http.socket.{WebSocketChannelEvent, WebSocketFrame}
import zio.json.*

import java.time.{Duration, Instant}

final class WsApiImpl extends WsApi {

  override def webSocketHandshake: Http[AccountDao
                                          with PersonDao
                                          with Encryption,
                                        Nothing,
                                        Request,
                                        Response
  ] =
    Http.collectZIO[Request] { case Method.GET -> !! / "connect" =>
      webSocket.toSocketApp.toResponse
    }

  def webSocket: Http[AccountDao
                        with PersonDao
                        with Encryption,
                      Throwable,
                      WebSocketChannelEvent,
                      Unit
  ] =
    Http.collectZIO[WebSocketChannelEvent] { case ChannelEvent(ch, ChannelRead(WebSocketFrame.Text(text))) =>
      Command.decode(text) match {
        case Left(_) =>
          ch.writeAndFlush(WebSocketFrame.text("Invalid JSON"))
        case Right(cmd) =>
          cmd match {
            case CommandWithCredentials("login", creds) =>
              //Handle.login(ch, creds)
              LoginWsHandler.login(ch, creds)
            case commandWithJwt @ CommandWithJwt("Account", _, _, _) =>
              AccountWsHandler.processCommand(commandWithJwt, ch)
//            case commandWithJwt @ CommandWithJwt("Animation", _, _, _) =>
//              AnimationWsHandler.processCommand(commandWithJwt, ch)
//            case commandWithJwt @ CommandWithJwt("AnimationDeveloper", _, _, _) =>
//              AnimationDeveloperWsHandler.processCommand(commandWithJwt, ch)
//            case commandWithJwt @ CommandWithJwt("AnimationInstance", _, _, _) =>
//              AnimationInstanceWsHandler.processCommand(commandWithJwt, ch)
//            case commandWithJwt @ CommandWithJwt("AnimationVersion", _, _, _) =>
//              AnimationVersionWsHandler.processCommand(commandWithJwt, ch)
            case _ =>
              ch.writeAndFlush(WebSocketFrame.text("Invalid command"))
          }
      }
    }
}

object WsApiImpl {
  val live: ZLayer[AccountDao with PersonDao with Encryption with JwtScalaZio, Nothing, WsApiImpl] =
    ZLayer.succeed(new WsApiImpl)
}
