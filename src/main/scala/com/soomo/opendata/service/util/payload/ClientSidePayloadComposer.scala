package com.soomo.opendata.service.util.payload

import com.soomo.opendata.domain.model.account.Account
import com.soomo.opendata.domain.model.payload.HttpCommand
import com.soomo.opendata.service.encryption.api_request_encryption.ProcessMessageEcnription

import scala.annotation.tailrec
import scala.io.StdIn

object ClientSidePayloadComposer {

//  val httpCmd: HttpCommand = ???
//
//  val cmd: String = ???
//
//  val acc: Account = ???
//
//  def compose(cmd: String, acc: Account): String = ???

  def processInput(): Unit = {

    println("""Please chose command for Account API:
        |1 addAll
        |2 getAll
        |3 getByLogin
        |4 getById
        |5 deleteById
        |0 To Stop App""".stripMargin)
    val input = StdIn.readLine() match {
      case "1" =>
        println("Input Login")
        val login = StdIn.readLine()
        println("Input hashedPassword")
        val hashedPassword = StdIn.readLine()
        println("You may Input info")
        val info = StdIn.readLine()
        println("You may Input accessLevel")
        val accessLevel = StdIn.readLine().toInt
        val parameter =
          s"""[{\\\"login\\\":\\\"$login\\\",\\\"hashedPassword\\\":\\\"$hashedPassword\\\", \\\"info\\\":\\\"$info\\\",\\\"accessLevel\\\":$accessLevel, \\\"id\\\": 0}]"""
        println(parameter)
        val encMsg = ProcessMessageEcnription.encrypt(s"""{
             |    "command": "addAll",
             |    "parameter": "$parameter"
             |    }""".stripMargin)
        val payload = ProcessMessageEcnription.encodeRequestToPayload(encMsg)
        println("Here is your postmen payload:")
        println(payload.getOrElse("Payload build Failed"))
      case "2" =>
        val encMsg  = ProcessMessageEcnription.encrypt("""{"command": "getAll"}""")
        val payload = ProcessMessageEcnription.encodeRequestToPayload(encMsg)
        println("Here is your postmen payload:")
        println(payload.getOrElse("Payload build Failed"))
      case "3" =>
        println("Input Account Login:")
        val parameter = StdIn.readLine()
        val encMsg = ProcessMessageEcnription.encrypt(s"""{
             |    "command": "getByLogin",
             |    "parameter": "$parameter"
             |    }""".stripMargin)
        val payload = ProcessMessageEcnription.encodeRequestToPayload(encMsg)
        println("Here is your postmen payload:")
        println(payload.getOrElse("Payload build Failed"))
      case "4" =>
        println("Input Account ID")
        val parameter = StdIn.readLine()
        val encMsg = ProcessMessageEcnription.encrypt(s"""{
             |    "command": "getById",
             |    "parameter": "$parameter"
             |    }""".stripMargin)
        val payload = ProcessMessageEcnription.encodeRequestToPayload(encMsg)
        println("Here is your postmen payload:")
        println(payload.getOrElse("Payload build Failed"))

      case "5" =>
        println("Input Account ID")
        val parameter = StdIn.readLine()
        val encMsg = ProcessMessageEcnription.encrypt(s"""{
             |    "command": "deleteById",
             |    "parameter": "$parameter"
             |    }""".stripMargin)
        val payload = ProcessMessageEcnription.encodeRequestToPayload(encMsg)
        println("Here is your postmen payload:")
        println(payload.getOrElse("Payload build Failed"))
      case "0" => // Stop recursion
      case _ =>
        println("Invalid input. Please enter a number from 0 to 5.")
        processInput()
    }
  }

  processInput()

}
