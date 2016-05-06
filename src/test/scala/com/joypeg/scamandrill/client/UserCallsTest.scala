package com.joypeg.scamandrill.client

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll
import scala.concurrent.Await
import com.joypeg.scamandrill.models._
import scala.util.{Try, Failure, Success}
import com.joypeg.scamandrill.utils._
import com.joypeg.scamandrill.MandrillTestUtils._
import com.joypeg.scamandrill.models.MSenderDataResponse
import com.joypeg.scamandrill.models.MInfoResponse
import com.joypeg.scamandrill.models.MKey
import com.joypeg.scamandrill.models.MPingResponse

class UserCallsTest extends FlatSpec with Matchers with SimpleLogger{

  "Ping" should "work getting a valid MPingResponse (async client)" in {
    val res = Await.result(mandrillAsyncClient.usersPing(MKey()), DefaultConfig.defaultTimeout)
    res shouldBe MPingResponse("\"PONG!\"")
  }
  it should "work getting a valid MPingResponse (blocking client)" in {
    mandrillBlockingClient.usersPing(MKey()) match {
      case Success(res) => res shouldBe MPingResponse("\"PONG!\"")
      case Failure(ex) => fail(ex)
    }
  }
  it should "fail if the key passed is invalid, with an 'Invalid_Key' code" in {
    checkFailedBecauseOfInvalidKey(mandrillBlockingClient.usersPing(MKey("invalidkeytest")))
  }

  "Ping (version 2)" should "work getting a valid MPingResponse" in {
    val res1 = Await.result(mandrillAsyncClient.usersPing2(MKey()), DefaultConfig.defaultTimeout)
    res1 shouldBe MPingResponse("PONG!")
  }
  it should "work getting a valid MPingResponse (blocking client)" in {
    mandrillBlockingClient.usersPing2(MKey()) match {
      case Success(res) => res shouldBe MPingResponse("PONG!")
      case Failure(ex) => fail(ex)
    }
  }
  it should "fail if the key passed is invalid, with an 'Invalid_Key' code" in {
    checkFailedBecauseOfInvalidKey(mandrillBlockingClient.usersPing2(MKey("invalidkeytest")))
  }

  "Sender" should "work getting a valid List[MSenderDataResponse] (async client)" in {
    val res = Await.result(mandrillAsyncClient.usersSenders(MKey()), DefaultConfig.defaultTimeout)
    res.head.getClass shouldBe classOf[MSenderDataResponse]
    res.exists(_.address == "scamandrill@test.com") shouldBe true
  }
  it should "work getting a valid List[MSenderDataResponse] (blocking client)" in {
    mandrillBlockingClient.usersSenders(MKey()) match {
      case Success(res) => res.head.getClass shouldBe classOf[MSenderDataResponse]
      case Failure(ex) => fail(ex)
    }
  }
  it should "fail if the key passed is invalid, with an 'Invalid_Key' code" in {
    checkFailedBecauseOfInvalidKey(mandrillBlockingClient.usersSenders(MKey("invalidkeytest")))
  }

  "Info" should "work getting a valid MInfoResponse (async client)" in {
    val res: MInfoResponse = Await.result(mandrillAsyncClient.usersInfo(MKey()), DefaultConfig.defaultTimeout)
    res.username shouldBe "30909130"
    res.created_at shouldBe "2016-05-05 15:25:38.62985"
    res.hourly_quota shouldBe 25
    res.public_id shouldBe "ALLdCj8lhM94O0jL0V3VLQ"
  }
  it should "work getting a valid MInfoResponse (blocking client)" in {
    val tryRes: Try[MInfoResponse] = mandrillBlockingClient.usersInfo(MKey())
    tryRes match {
      case Success(res) =>
        res.username shouldBe "30909130"
        res.created_at shouldBe "2016-05-05 15:25:38.62985"
        res.hourly_quota shouldBe 25
        res.public_id shouldBe "ALLdCj8lhM94O0jL0V3VLQ"
      case Failure(ex) => fail(ex)
    }
  }
  it should "fail if the key passed is invalid, with an 'Invalid_Key' code" in {
    checkFailedBecauseOfInvalidKey(mandrillBlockingClient.usersInfo(MKey("invalidkeytest")))
  }
}
