package com.soomo.opendata.handlers.api_handlers

import com.soomo.opendata.api.ws.WsApi
import com.soomo.opendata.dao.account.{AccountDao, AccountDaoQuill}
import com.soomo.opendata.domain.model.account.{Account, Credentials}
import com.soomo.opendata.domain.model.payload.HttpCommand
import com.soomo.opendata.domain.{DbConfigPath, ServiceError}
import com.soomo.opendata.flyway.FlywayProvider
import com.soomo.opendata.service.encryption.api_request_encryption.ProcessMessageEcnription
import com.soomo.opendata.service.encryption.password.{BCryptEncryption, Encryption}
import io.getquill.*
import io.getquill.jdbczio.Quill
import zio.*
import zio.config.*
import zio.http.*
import zio.http.ChannelEvent.ChannelRead
import zio.http.codec.HttpCodecType.Status
import zio.http.model.Status
import zio.http.socket.{WebSocketChannelEvent, WebSocketFrame}
import zio.json.*

object AccountHttpHandler:
  def handle(request: Request): ZIO[AccountDao, Nothing, Response] =
    request.body.asString
      .foldZIO(
        _ => ZIO.succeed(Response.status(Status.BadRequest)),
        body =>
          ProcessMessageEcnription.decodePayloadToRequest(body) match
            case None => ZIO.succeed(Response.status(Status.BadRequest))
            case Some(arr: Array[Byte]) =>
              ProcessMessageEcnription.decrypt(arr) match
                case None => ZIO.succeed(Response.status(Status.BadRequest))
                case Some(srt: String) =>
                  srt.fromJson[HttpCommand] match {
                    case Left(decodingFailure) =>
                      ZIO.succeed(Response.text(s"Failed to decode JSON with error: $decodingFailure from: $body"))
                    case Right(httpCommand) =>
                      httpCommand.command match
                        case "addAll" =>
                          (for {
                            parameter <- ZIO.fromEither(httpCommand.parameter.getOrElse("").fromJson[Seq[Account]])
                            _         <- AccountDao.addAll(parameter)
                          } yield Response.text(s"'${httpCommand.command}' Action successful.")).catchAll(se =>
                            ZIO.succeed(
                              Response.text(s"'${httpCommand.command}' Action Failed with error: ${se.toString}")
                            )
                          )
                        case "getAll" =>
                          (for {
                            res <- AccountDao.getAll()
                          } yield Response.text(res.toJson)).catchAll(se =>
                            ZIO.succeed(
                              Response.text(s"'${httpCommand.command}' Action Failed with error: ${se.toString}")
                            )
                          )
                        case "getByLogin" =>
                          (for {
                            res <- AccountDao.getByLogin(httpCommand.parameter.getOrElse(""))
                          } yield Response.text(res.toJson)).catchAll(se =>
                            ZIO.succeed(
                              Response.text(s"'${httpCommand.command}' Action Failed with error: ${se.toString}")
                            )
                          )
                        case "getById" =>
                          (for {
                            res <- AccountDao.getById(httpCommand.parameter.getOrElse("").toInt)
                          } yield Response.text(res.toJson)).catchAll(se =>
                            ZIO.succeed(
                              Response.text(s"'${httpCommand.command}' Action Failed with error: ${se.toString}")
                            )
                          )
                        case "deleteById" =>
                          (for {
                            res <- AccountDao.deleteById(httpCommand.parameter.getOrElse("").toInt)
                            result =
                              if res == 0L then
                                (s"No record with given id=${httpCommand.parameter.getOrElse("")} found.")
                              else (s"With $res records affected.")
                          } yield Response.text(s"'${httpCommand.command}' Action successful. $result"))
                            .catchAll(se =>
                              ZIO.succeed(
                                Response.text(s"'${httpCommand.command}' Action Failed with error: ${se.toString}")
                              )
                            )
                        case _ => ZIO.succeed(Response.text("Unsupported request"))
                  }
      )
      .logError("Workflow Error " + ("=" * 20))
