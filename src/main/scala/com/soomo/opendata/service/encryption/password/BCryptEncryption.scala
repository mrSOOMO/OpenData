package com.soomo.opendata.service.encryption.password

import zio.*
final class BCryptEncryption extends Encryption {

  import org.mindrot.jbcrypt.BCrypt

  // these standards would guide towards best practices such as:
  // Storing passwords in a secure, hashed and salted format.
  // Implementing secure password policies (e.g., minimum password length, SPECIFY complexity requirements)
  // Using secure methods for password recovery or reset.
  // it is generally not a good idea to transmit passwords back and forth. Consider using tokens or session IDs for authentication.
  // It's also a good practice to hash passwords on the client side before sending them to the server, and then hash them again on the server side before storing them in the database.

  // hash a password:
  def encrypt(str: String): String = {
    val salt = BCrypt.gensalt()
    BCrypt.hashpw(str, salt)
  }

  // check a password:
  def check(candidate: String, hashedPassword: String): Boolean =
    BCrypt.checkpw(candidate, hashedPassword)
}

object BCryptEncryption {
  val live: URLayer[Any, BCryptEncryption] =
    ZLayer.succeed(new BCryptEncryption())
}
