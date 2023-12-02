package com.soomo.opendata.api.ws

import com.soomo.opendata.dao.account.{AccountDao, AccountDaoQuill}
import com.soomo.opendata.dao.person.PersonDao
import com.soomo.opendata.domain.model.account.{Account, Credentials}
import com.soomo.opendata.domain.{DbConfigPath, ServiceError}
import com.soomo.opendata.flyway.FlywayProvider
import com.soomo.opendata.service.encryption.password.{BCryptEncryption, Encryption}
import com.soomo.opendata.service.encryption.token.jwt_scala_zio.JwtScalaZio
import io.getquill.*
import io.getquill.jdbczio.Quill
import zio.*
import zio.config.*
import zio.http.*
import zio.http.ChannelEvent.ChannelRead
import zio.http.socket.{WebSocketChannelEvent, WebSocketFrame}
import zio.json.*

trait WsApi {
  def webSocketHandshake: Http[AccountDao
                                 with PersonDao
                                 with Encryption,
                               Nothing,
                               Request,
                               Response
  ]
  def webSocket: Http[AccountDao
                        with PersonDao
                        with Encryption,
                      Throwable,
                      WebSocketChannelEvent,
                      Unit
  ]

}
