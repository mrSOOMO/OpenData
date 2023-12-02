//package com.soomo.opendata.handlers.ws_handlers
//
//import com.soomo.opendata.dao.account.AccountDao
//import com.soomo.opendata.dao.animation.animation.AnimationDao
//import com.soomo.opendata.domain.ServiceError
//import com.soomo.opendata.domain.model.account.Credentials
//import com.soomo.opendata.domain.model.payload.{Command, CommandWithCredentials, CommandWithJwt, LoginSuccessPayload}
//import com.soomo.opendata.service.encryption.password.Encryption
//import com.soomo.opendata.service.encryption.token.jwt_scala_zio.{JwtScala, JwtScalaZio}
//import pdi.jwt.JwtClaim
//import zio.*
//import zio.config.*
//import zio.http.*
//import zio.http.ChannelEvent.ChannelRead
//import zio.http.socket.{WebSocketChannelEvent, WebSocketFrame}
//import zio.json.*
//import akka.actor.typed.{ActorRef, ActorSystem}
//import akka.actor.typed.scaladsl.Behaviors
////import com.soomo.opendata.actors.UserSessionActor
//
//import java.time.{Duration, Instant}
//
//object Handle {
//  def login(ch: Channel[WebSocketFrame.Text], creds: Credentials): Unit = {
//
//    /**
//     * Create a UserSessionActor for this user
//     */
//    val userSessionActor: ActorRef[UserSessionActor.Command] = ??? // Implement actor creation logic
//
//    // Tell the actor to authenticate the user
//    userSessionActor ! UserSessionActor.Authenticate(creds.username, creds.password, ???)
//    // You will need to provide a way to get the actor's response to the login request. This might require
//    // changing the definition of Authenticate or adding another message type to the UserSessionActor.
//    // For now, I've put ??? as a placeholder for this.
//  }
//
//}
