package io.github.scamandrill.client

import io.github.scamandrill.MandrillSpec
import io.github.scamandrill.models._

import scala.concurrent.Await

class SendersCallsTest extends MandrillSpec {

  "SendersList" should "work getting a valid List[MSendersListResp]" in {
    val res = Await.result(client.sendersList(MKey()), DefaultConfig.defaultTimeout)
    res.head.getClass shouldBe classOf[MSendersListResp]
    res.head.address shouldBe "scamandrill@test.com"
  }

  "SendersDomains" should "work getting a valid List[MSendersDomainResponses]" in {
    val res = Await.result(client.sendersDomains(MKey()), DefaultConfig.defaultTimeout)
    res.head.getClass shouldBe classOf[MSendersDomainResponses]
    res.head.domain shouldBe "gmail.com"
  }

  "SendersAddDomain" should "work getting a valid MSendersDomainResponses" in {
    val res = Await.result(client.sendersAddDomain(MSenderDomain(domain = "test.com")), DefaultConfig.defaultTimeout)
    res.getClass shouldBe classOf[MSendersDomainResponses]
    res.domain shouldBe "test.com"
  }

  "SendersCheckDomain" should "work getting a valid MSendersDomainResponses" in {
    val res = Await.result(client.sendersCheckDomain(MSenderDomain(domain = "test.com")), DefaultConfig.defaultTimeout)
    res.getClass shouldBe classOf[MSendersDomainResponses]
    res.domain shouldBe "test.com"
  }

  "SendersVerifyDomain" should "work getting a valid MSendersVerifyDomResp" in {
    val res = Await.result(client.sendersVerifyDomain(MSenderVerifyDomain(mailbox = "joypeg.tech", domain = "gmail.com")), DefaultConfig.defaultTimeout)
    res.getClass shouldBe classOf[MSendersVerifyDomResp]
    res.domain shouldBe "gmail.com"
  }

  "SendersInfo" should "work getting a valid MSendersInfoResp" in {
    val res = Await.result(client.sendersInfo(MSenderAddress(address = "scamandrill@test.com")), DefaultConfig.defaultTimeout)
    res.getClass shouldBe classOf[MSendersInfoResp]
    res.address shouldBe "scamandrill@test.com"
  }

  "SendersTimeSeries" should "work getting a valid List[MSenderTSResponse]" in {
    val res: List[MSenderTSResponse] = Await.result(
      client.sendersTimeSeries(MSenderAddress(address = "scamandrill@test.com")), DefaultConfig.defaultTimeout
    )
    res shouldBe Nil
  }
}
