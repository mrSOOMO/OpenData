package com.soomo.opendata.api.http

import com.soomo.opendata.api.ws.WsApi
import com.soomo.opendata.dao.account.{AccountDao, AccountDaoQuill}
import com.soomo.opendata.domain.DbConfigPath
import com.soomo.opendata.domain.model.account.Account
import com.soomo.opendata.flyway.FlywayProvider
import com.soomo.opendata.service.encryption.password.{BCryptEncryption, Encryption}
import io.getquill.*
import io.getquill.jdbczio.Quill
import zio.*
import zio.config.*
import zio.http.*
import zio.http.ChannelEvent.ChannelRead
import zio.http.socket.{WebSocketChannelEvent, WebSocketFrame}
import zio.json.*

trait HttpApi {
  def appHttp: Http[WsApi with AccountDao with Encryption, Nothing, Request, Response]
}
