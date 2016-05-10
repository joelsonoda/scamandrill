package io.github.scamandrill.client

import io.github.scamandrill.MandrillSpec

import scala.concurrent.Await
import io.github.scamandrill.models._

import scala.util.{Failure, Success}
import io.github.scamandrill.MandrillTestUtils._
import io.github.scamandrill.models.MRejectAdd
import io.github.scamandrill.models.MRejectAddResponse
import org.scalatest.tagobjects.Retryable

class RejectCallsTest extends MandrillSpec {

  "RejectAdd" should "work getting a valid MRejectAdd (async client)" in {
    val res = Await.result(mandrillAsyncClient.rejectAdd(MRejectAdd(email = "add@example.com")), DefaultConfig.defaultTimeout)
    res.getClass shouldBe classOf[MRejectAddResponse]
    res shouldBe MRejectAddResponse("add@example.com",true)
  }

  "RejectList" should "work getting a valid MRejectListResponse (async client)" taggedAs(Retryable) in {
    val res = Await.result(mandrillAsyncClient.rejectList(MRejectList(email = "add@example.com")), DefaultConfig.defaultTimeout)
    res.head.getClass shouldBe classOf[MRejectListResponse]
    res.head.email shouldBe "add@example.com"
  }

  "RejecDelete" should "work getting a valid MRejectDeleteResponse (async client)" in {
    val res = Await.result(mandrillAsyncClient.rejectDelete(MRejectDelete(email = "add@example.com")), DefaultConfig.defaultTimeout)
    res.getClass shouldBe classOf[MRejectDeleteResponse]
    res shouldBe MRejectDeleteResponse("add@example.com", true, None)
  }

}
