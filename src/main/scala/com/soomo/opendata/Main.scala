package com.soomo.opendata

import com.soomo.opendata.api.http.HttpApiImpl
import com.soomo.opendata.api.ws.WsApiImpl
import com.soomo.opendata.dao.account.{AccountDao, AccountDaoQuill}
import com.soomo.opendata.domain.DbConfigPath
import com.soomo.opendata.domain.config.Configuration
import com.soomo.opendata.domain.config.Configuration.serverConfig
import com.soomo.opendata.domain.config.Datasource.{dataSourceLayer, quillCtxLayer}
import com.soomo.opendata.domain.model.account.{Account, Credentials}
import com.soomo.opendata.flyway.FlywayProvider
import com.soomo.opendata.service.encryption.password.{BCryptEncryption, Encryption}
import com.soomo.opendata.service.encryption.token.jwt_scala_zio.JwtScalaZioImpl
import com.soomo.opendata.service.util.console.printWithFrame
import io.getquill.*
import io.getquill.jdbczio.Quill
import zio.*
import zio.config.*
import zio.config.magnolia.deriveConfig
import zio.config.typesafe.TypesafeConfigProvider
import zio.http.*
import zio.http.ChannelEvent.ChannelRead
import zio.http.middleware.RequestHandlerMiddlewares
import zio.http.socket.{WebSocketChannelEvent, WebSocketFrame}
import com.typesafe.config.{Config, ConfigFactory}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.soomo.opendata.dao.person.PersonDaoQuill

import java.io.IOException
import java.net.InetSocketAddress

object Main extends ZIOAppDefault {

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.setConfigProvider(
      TypesafeConfigProvider
        .fromResourcePath()
    )

//  /**
//   * Create the ActorSystem
//   */
//
//  val actorSystem = ActorSystem(Behaviors.empty, "MyActorSystem")
//
//  /**
//   * terminate the actor system when the application stops
//   */
//  override def onStop(): ZIO[Any, Nothing, Unit] =
//    ZIO.fromFuture(_ => actorSystem.terminate()).ignore

  private val app = for {
    flyway  <- FlywayProvider.flyway
    _       <- flyway.migrate
    appHttp <- ZIO.service[HttpApiImpl]
    appWs   <- ZIO.service[WsApiImpl]
    host   = sys.env.getOrElse("HOST", ConfigFactory.load().getString("HttpServerConfig.host"))
    port   = sys.env.getOrElse("PORT", ConfigFactory.load().getString("HttpServerConfig.port"))
    hostDB = sys.env.getOrElse("POSTGRES_HOST", ConfigFactory.load().getString("opendata.db.host"))
    portDB = sys.env.getOrElse("POSTGRES_PORT", ConfigFactory.load().getString("opendata.db.port"))
    _ <- printWithFrame(s"Opendata Started")
    _ <- printWithFrame(s"HTTP Server starting at $host:$port")
    _ <- printWithFrame(s"WS Server starting at $host:$port")
    res <- AccountDao
      .getAll()
      .fold(error => s"DB access ERROR using $hostDB:$portDB: $error",
        accounts => s"DB Connected using $hostDB:$portDB"
      )
    _ <- printWithFrame(res)
    _ <- Server.serve(appHttp.appHttp ++ appWs.webSocketHandshake @@ RequestHandlerMiddlewares.debug)
  } yield ()

  override val run: ZIO[Any, Throwable, Unit] = app.provide(
    serverConfig,
    Server.live,
    dataSourceLayer,
    quillCtxLayer,
    AccountDaoQuill.live,
    PersonDaoQuill.live,
    BCryptEncryption.live,
    FlywayProvider.live,
    WsApiImpl.live,
    HttpApiImpl.live,
    JwtScalaZioImpl.live
  )
}
