package com.soomo.opendata.handlers.ws_handlers

import com.soomo.opendata.dao.account.AccountDao
import com.soomo.opendata.domain.ServiceError
import com.soomo.opendata.domain.model.account.{Account, Credentials}
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

/**
 * The AccountWsHandler object serves as a command handler for WebSocket
 * communications related to account activities. It takes WebSocket commands
 * wrapped in the CommandWithJwt case class and processes them according to
 * their command type.
 *
 * Functions:
 *
 *   - processCommand: This function takes in a CommandWithJwt object and a
 *     WebSocket channel, then matches the command to its appropriate handler
 *     function. The handler function is selected based on the command name.
 *
 * Handler Functions:
 *   - handleAddAll: Triggered when the command name is "addAll".
 *
 *   - handleGetAll: Triggered when the command name is "getAll". It handles the
 *     process of retrieving all accounts.
 *
 *   - handleGetByLogin: Triggered when the command name is "getByLogin". It
 *     handles the process of retrieving a specific account based on the
 *     provided login parameter.
 *
 *   - handleGetById: Triggered when the command name is "getById". It handles
 *     the process of retrieving a specific account based on the provided id
 *     parameter.
 *
 *   - handleDeleteById: Triggered when the command name is "deleteByIds". It
 *     handles the process of deleting a specific account or accounts based on
 *     the provided id parameter(s).
 */
object AccountWsHandler:
  def processCommand(command: CommandWithJwt, ch: Channel[WebSocketFrame]): ZIO[AccountDao
                                                                                  with Encryption,
                                                                                Throwable,
                                                                                Unit
  ] =
    command match
      case CommandWithJwt(_, "addAll", jwt, Some(parameter))     => handleAddAll(ch, jwt, parameter)
      case CommandWithJwt(_, "getAll", jwt, None)                => handleGetAll(ch, jwt)
      case CommandWithJwt(_, "getByLogin", jwt, Some(parameter)) => handleGetByLogin(ch, jwt, parameter)
      case CommandWithJwt(_, "getById", jwt, Some(parameter))    => handleGetById(ch, jwt, parameter)
      case CommandWithJwt(_, "deleteById", jwt, Some(parameter)) => handleDeleteById(ch, jwt, parameter)

  def handleAddAll(ch: Channel[WebSocketFrame.Text], jwt: String, param: String) =
    (for {
      claim   <- ZIO.fromEither(JwtScala.decodeJWT(jwt))
      payload <- ZIO.fromEither(claim.content.fromJson[LoginSuccessPayload])
      _       <- ZIO.fail(new Exception("Access level is not sufficient")) unless payload.accessLevel > 1
      _       <- ZIO.fail(new Exception("Token is expired")) unless payload.expireAt.isAfter(Instant.now())
      accs    <- ZIO.fromEither(param.fromJson[Seq[Account]])
      _       <- AccountDao.addAll(accs)
      _       <- ch.writeAndFlush(WebSocketFrame.text("Animation added successfully").asInstanceOf[WebSocketFrame.Text])
    } yield ())
      .catchAll(e => ch.writeAndFlush(WebSocketFrame.text(e.toString).asInstanceOf[WebSocketFrame.Text]))
      .logError("Workflow Error " + ("=" * 20))
  def handleGetAll(ch: Channel[WebSocketFrame.Text], jwt: String): ZIO[AccountDao with Encryption, Throwable, Unit] =
    (for {
      claim    <- ZIO.fromEither(JwtScala.decodeJWT(jwt))
      payload  <- ZIO.fromEither(claim.content.fromJson[LoginSuccessPayload])
      _        <- ZIO.fail(new Exception("Access level is not sufficient")) unless payload.accessLevel > 4
      _        <- ZIO.fail(new Exception("Token is expired")) unless payload.expireAt.isAfter(Instant.now())
      accounts <- AccountDao.getAll()
      accountsJson = accounts.map(_.toJson).mkString("[", ",", "]")
      _ <- ch.writeAndFlush(WebSocketFrame.text(accountsJson).asInstanceOf[WebSocketFrame.Text])
    } yield ())
      .catchAll(e => ch.writeAndFlush(WebSocketFrame.text(e.toString).asInstanceOf[WebSocketFrame.Text]))
      .logError("Workflow Error " + ("=" * 20))

  def handleGetByLogin(ch: Channel[WebSocketFrame.Text], jwt: String, parameter: String) = (for {
    claim   <- ZIO.fromEither(JwtScala.decodeJWT(jwt))
    payload <- ZIO.fromEither(claim.content.fromJson[LoginSuccessPayload])
    _       <- ZIO.fail(new Exception("Access level is not sufficient")) unless payload.accessLevel > 4
    _       <- ZIO.fail(new Exception("Token is expired")) unless payload.expireAt.isAfter(Instant.now())
    account <- AccountDao.getByLogin(parameter)
    accountJson = account.toJson
    _ <- ch.writeAndFlush(WebSocketFrame.text(accountJson).asInstanceOf[WebSocketFrame.Text])
  } yield ())
    .catchAll(e => ch.writeAndFlush(WebSocketFrame.text(e.toString).asInstanceOf[WebSocketFrame.Text]))
    .logError("Workflow Error " + ("=" * 20))

  def handleGetById(ch: Channel[WebSocketFrame.Text], jwt: String, parameter: String) = (for {
    claim   <- ZIO.fromEither(JwtScala.decodeJWT(jwt))
    payload <- ZIO.fromEither(claim.content.fromJson[LoginSuccessPayload])
    _       <- ZIO.fail(new Exception("Access level is not sufficient")) unless payload.accessLevel > 4
    _       <- ZIO.fail(new Exception("Token is expired")) unless payload.expireAt.isAfter(Instant.now())
    account <- AccountDao.getById(parameter.toInt)
    accountJson = account.toJson
    _ <- ch.writeAndFlush(WebSocketFrame.text(accountJson).asInstanceOf[WebSocketFrame.Text])
  } yield ())
    .catchAll(e => ch.writeAndFlush(WebSocketFrame.text(e.toString).asInstanceOf[WebSocketFrame.Text]))
    .logError("Workflow Error " + ("=" * 20))

  def handleDeleteById(ch: Channel[WebSocketFrame.Text], jwt: String, parameter: String) = (for {
    claim         <- ZIO.fromEither(JwtScala.decodeJWT(jwt))
    payload       <- ZIO.fromEither(claim.content.fromJson[LoginSuccessPayload])
    _             <- ZIO.fail(new Exception("Access level is not sufficient")) unless payload.accessLevel > 4
    _             <- ZIO.fail(new Exception("Token is expired")) unless payload.expireAt.isAfter(Instant.now())
    deletedAccNum <- AccountDao.deleteById(parameter.toInt)
    _ <- ch.writeAndFlush(
      WebSocketFrame
        .text(s"Deleted successfully. Number of records affected: $deletedAccNum")
        .asInstanceOf[WebSocketFrame.Text]
    )
  } yield ())
    .catchAll(e => ch.writeAndFlush(WebSocketFrame.text(e.toString).asInstanceOf[WebSocketFrame.Text]))
    .logError("Workflow Error " + ("=" * 20))
