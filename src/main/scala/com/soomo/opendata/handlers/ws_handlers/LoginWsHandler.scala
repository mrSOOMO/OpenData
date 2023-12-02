package com.soomo.opendata.handlers.ws_handlers

import com.soomo.opendata.dao.account.AccountDao
import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.model.account.Credentials
import com.soomo.opendata.domain.model.payload.{Command, CommandWithCredentials, CommandWithJwt, LoginSuccessPayload}
import com.soomo.opendata.service.encryption.password.Encryption
import com.soomo.opendata.service.encryption.token.jwt_scala_zio.{JwtScala, JwtScalaZio}
import pdi.jwt.JwtClaim
import zio.*
import zio.config.*
import zio.http.*
import zio.http.ChannelEvent.ChannelRead
import zio.http.socket.{WebSocketChannelEvent, WebSocketFrame}
import zio.json.*

import java.time.{Duration, Instant}
object LoginWsHandler:
  def login(ch: Channel[WebSocketFrame.Text], creds: Credentials): ZIO[AccountDao with Encryption, Throwable, Unit] =
    (for {
      user       <- AccountDao.getByLogin(creds.login)
      encryption <- ZIO.service[Encryption]
      success = encryption.check(creds.password, user.hashedPassword)
      _ <-
        if (success)
          val payload =
            LoginSuccessPayload(user.id, user.accessLevel, Instant.now().plus(Duration.ofHours(24))).toJson
          val jwt = JwtScala.encodeJWT(JwtClaim(content = payload))
          ch.writeAndFlush(WebSocketFrame.text(s"Login successful, JWT: $jwt").asInstanceOf[WebSocketFrame.Text])
        else ch.writeAndFlush(WebSocketFrame.text("Login failed").asInstanceOf[WebSocketFrame.Text])
    } yield ())
      .catchAll(e => ch.writeAndFlush(WebSocketFrame.text(e.toString).asInstanceOf[WebSocketFrame.Text]))
      .logError("Workflow Error " + ("=" * 20))
