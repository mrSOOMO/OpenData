package com.soomo.opendata.service.encryption.api_request_encryption

import org.bouncycastle.jce.provider.BouncyCastleProvider

import java.io.{File, PrintWriter}
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import java.security.*
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}
import java.util.Base64
import javax.crypto.Cipher

/**
 * The ProcessMessageEncryption object in Scala implements RSA encryption and
 * decryption for messages using the Bouncy Castle Provider. The keys for the
 * encryption process are read from files on the disk.
 *
 * This class includes methods for:
 *
 *   1. Reading and converting the RSA keys from files.
 *
 * 2. Storing RSA keys to files.
 *
 * 3. Encrypting and decrypting messages using the RSA keys and Cipher.
 *
 * 4. Encoding requests to payloads and decoding payloads to requests using
 * Base64 encoding and decoding.
 *
 * The RSA keys are used with the Cipher instance initialized with the
 * RSA/None/OAEPWithSHA1AndMGF1Padding transformation. The public key is used
 * for encryption and the private key is used for decryption.
 *
 * The encrypt function accepts a string message, encrypts it using the public
 * key, and returns it as an array of bytes.
 *
 * The decrypt function accepts an array of bytes, decrypts it using the private
 * key, and returns the original message string.
 *
 * The encodeRequestToPayload and decodePayloadToRequest methods use Base64
 * encoding and decoding to encode requests into payloads and decode payloads
 * back into requests.
 *
 * Please note, the RSA keys are currently stored in plaintext files which can
 * be a security risk. A safe way to store them, like using a Java KeyStore,
 * should be considered.
 */

object ProcessMessageEcnription {

  Security.addProvider(new BouncyCastleProvider)

  // ToDo: Handle secure key storage (Java KeyStore (JKS) is a repository of security certificates)

  val privateKey: PrivateKey = retrievePrivateKeyFromFile()
  val publicKey: PublicKey   = retrievePublicKeyFromFile()
  //  Convert the PrivateKey to a string (for storing):
  val privateKeyBytes  = privateKey.getEncoded
  val privateKeyString = Base64.getEncoder.encodeToString(privateKeyBytes)
  //  Convert the PublicKey to a string (for storing):
  val publicKeyBytes  = publicKey.getEncoded
  val publicKeyString = Base64.getEncoder.encodeToString(publicKeyBytes)
  // The cipherText action
  private val cipher = Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC")

  def retrievePublicKeyFromFile(): PublicKey = {
    val pwd             = new java.io.File(".").getCanonicalPath
    val path            = Paths.get(s"$pwd/src/main/resources/security/asymmetric_encryption/keys/publicKey.txt")
    val keyBytes        = Files.readAllBytes(path)
    val publicKeyString = new String(keyBytes)
    val decodedKeyBytes = Base64.getDecoder.decode(publicKeyString)
    val spec            = new X509EncodedKeySpec(decodedKeyBytes)
    val keyFactory      = KeyFactory.getInstance("RSA")
    keyFactory.generatePublic(spec)
  }

  def retrievePrivateKeyFromFile(): PrivateKey = {
    val pwd              = new java.io.File(".").getCanonicalPath
    val path             = Paths.get(s"$pwd/src/main/resources/security/asymmetric_encryption/keys/privateKey.txt")
    val keyBytes         = Files.readAllBytes(path)
    val privateKeyString = new String(keyBytes)
    val decodedKeyBytes  = Base64.getDecoder.decode(privateKeyString)
    val spec             = new PKCS8EncodedKeySpec(decodedKeyBytes)
    val keyFactory       = KeyFactory.getInstance("RSA")
    keyFactory.generatePrivate(spec)
  }

  // Write the keys to files
  def saveKeysToFile() =
    val pwd           = new java.io.File(".").getCanonicalPath
    val publicKeyPath = Paths.get(s"$pwd/src/main/resources/security/asymmetric_encryption/keys/publicKey.txt")
    Files.write(publicKeyPath, publicKeyString.getBytes(StandardCharsets.UTF_8))

    val privateKeyPath = Paths.get(s"$pwd/src/main/resources/security/asymmetric_encryption/keys/privateKey.txt")
    Files.write(privateKeyPath, privateKeyString.getBytes(StandardCharsets.UTF_8))

  def encrypt(msg: String): Array[Byte] = {
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8))
  }

  def decrypt(cipherText: Array[Byte]): Option[String] =
    try {
      cipher.init(Cipher.DECRYPT_MODE, privateKey)
      Some(new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8))
    } catch {
      case e: Exception =>
        e.printStackTrace()
        None
    }

  def encodeRequestToPayload(req: Array[Byte]): Option[String] =
    try
      Some(Base64.getEncoder.encodeToString(req))
    catch {
      case e: IllegalArgumentException =>
        e.printStackTrace()
        None
    }

  def decodePayloadToRequest(payload: String): Option[Array[Byte]] =
    try
      Some(Base64.getDecoder.decode(payload))
    catch {
      case e: IllegalArgumentException =>
        e.printStackTrace()
        None
    }
}
