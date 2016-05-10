package io.github.scamandrill.client

import io.github.scamandrill.MandrillSpec

import scala.concurrent.Await
import io.github.scamandrill.models._
import io.github.scamandrill.MandrillTestUtils._

import scala.util.Failure
import scala.util.Success

class WhitelistCallsTest extends MandrillSpec {

  "WhitelistAdd" should "work getting a valid MWhitelistAddResponse (async client)" in {
    val res = Await.result(mandrillAsyncClient.whitelistAdd(MWhitelist(email = "whitelist@example.com")), DefaultConfig.defaultTimeout)
    res.getClass shouldBe classOf[MWhitelistAddResponse]
    res shouldBe MWhitelistAddResponse("whitelist@example.com",true)
  }

  "WhitelistList" should "work getting a valid List[MWhitelistDeleteResponse] (async client)" in {
    val res = Await.result(mandrillAsyncClient.whitelistList(MWhitelist(email = "whitelist@example.com")), DefaultConfig.defaultTimeout)
    res.head.getClass shouldBe classOf[MWhitelistListResponse]
    res.head.email shouldBe "whitelist@example.com"
  }

  "WhitelistDelete" should "work getting a valid MWhitelistDeleteResponse (async client)" in {
    val res = Await.result(mandrillAsyncClient.whitelistDelete(MWhitelist(email = "whitelist@example.com")), DefaultConfig.defaultTimeout)
    res.getClass shouldBe classOf[MWhitelistDeleteResponse]
    res shouldBe MWhitelistDeleteResponse("whitelist@example.com",true)
  }
}