package com.soomo.opendata.service.encryption.token

import com.soomo.opendata.domain.ServiceError.LogicError
import zio.*

import java.util.Base64
final class BouncyCastle extends TokenEncryption {

  import org.bouncycastle.jce.provider.BouncyCastleProvider

  import javax.crypto.Cipher
  import javax.crypto.spec.SecretKeySpec

  /**
   * TODO: Test key MOCK. need to be replaced with The key. Must be 16 bytes for
   * AES key
   */
  val key            = "01234567890abcdef"
  private val provider = new BouncyCastleProvider()
  private val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", provider)

  // Encrypt
  cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), "AES"))
  private val encrypted = cipher.doFinal("Secret message".getBytes("UTF-8"))
  println(new String(encrypted, "UTF-8")) // prints encrypted text

  // Decrypt
  cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), "AES"))
  private val decrypted = cipher.doFinal(encrypted)
  println(new String(decrypted, "UTF-8")) // prints "Secret message"

  // Encrypt
  def encrypt(str: String): IO[LogicError, String] =
    ZIO.from {
      cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), "AES"))
      val encrypted = cipher.doFinal(str.getBytes("UTF-8"))
      Base64.getEncoder.encodeToString(encrypted)
    }.mapError(LogicError)

  // Decrypt
  def decrypt(str: String): IO[LogicError, String] = {
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes("UTF-8"), "AES"))
    for {
      decodedInput <- ZIO.from(Base64.getDecoder.decode(str))
      decrypted = cipher.doFinal(decodedInput)
    } yield new String(decrypted, "UTF-8")
  }.mapError(LogicError)

}
object BouncyCastle {
  val live: URLayer[Any, BouncyCastle] =
    ZLayer.succeed(new BouncyCastle())
}
