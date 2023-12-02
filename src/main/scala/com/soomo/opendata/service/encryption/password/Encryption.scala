package com.soomo.opendata.service.encryption.password

trait Encryption {
  def encrypt(str: String): String
  def check(candidate: String, hashedPassword: String): Boolean
}
