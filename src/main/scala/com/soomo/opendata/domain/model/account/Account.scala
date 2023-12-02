package com.soomo.opendata.domain.model.account

import zio.json.*

/**
 * Account - This class represents an account within the system. Each account
 * has a unique login, a hashed password, an optional info string and a unique
 * ID.
 *
 * @param login:
 *   This is the unique login identifier for the account.
 *
 * @param hashedPassword:
 *   This is the hashed representation of the account's password.
 *
 * @param info:
 *   This is a string field that contains additional information or details
 *   about the account. The information is typically stored as a JSON string to
 *   allow structured data to be included. It defaults to an empty string if no
 *   information is provided during the creation of the account.
 *
 * @param accessLevel:
 *   This is an integer field indicating the access level or privileges of the
 *   account. It can be used to control which operations an account can perform.
 *
 * @param id:
 *   This is the unique ID of the account. It defaults to 0 and will typically
 *   be assigned when the account is saved to the database.
 */
case class Account(
    login: String,
    hashedPassword: String,
    info: String = "",
    accessLevel: Int = 1,
    id: Int = 0
)

object Account:
  given JsonCodec[Account] = DeriveJsonCodec.gen[Account]
