package com.soomo.opendata.service.encryption.token

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object SignatureKey {
  // val secretKey = sys.env.getOrElse("SECRET_KEY", throw new Exception("SECRET_KEY not set"))

  /**
   * TODO: env key MOCK. need to be replaced with sys.env key
   */
  private val secretKey = "TEST_SECRET_KEY"

  def createSignature(data: String): String = {
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"))
    Base64.getEncoder.encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)))
  }
}
