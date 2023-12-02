package com.soomo.opendata.domain.config

import zio.config.magnolia.deriveConfig
import zio.http.ServerConfig
import zio.{Config, ZIO, ZLayer}

import java.net.InetSocketAddress

object Configuration:

  val serverConfig: ZLayer[Any, Config.Error, ServerConfig] =
    ZLayer
      .fromZIO(
        ZIO.config[HttpServerConfig](HttpServerConfig.config).map { c =>
          ServerConfig(
            address = new InetSocketAddress(c.port),
            nThreads = c.nThreads
          )
        }
      )

  final case class HttpServerConfig(host: String, port: Int, nThreads: Int)

  object HttpServerConfig {
    val config: Config[HttpServerConfig] =
      deriveConfig[HttpServerConfig].nested("HttpServerConfig")
  }
