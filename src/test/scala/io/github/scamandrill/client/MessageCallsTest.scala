package io.github.scamandrill.client

import io.github.scamandrill.MandrillSpec
import io.github.scamandrill.models._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Await

class MessageCallsTest extends MandrillSpec with ScalaFutures {

  import scala.concurrent.ExecutionContext.Implicits.global

  "Send" should "work getting a valid List[MSendResponse]" in {
    withClient("/messages/send.json") { ws =>
      val instance = new MandrillClient(ws, new APIKey())
      whenReady(instance.messagesSend(MSendMessage(
        async = false,
        ip_pool = Some("Main Pool"),
        message = new MSendMsg(
          html = "<p>Example HTML content</p>",
          text = "Example text content",
          subject = "example subject",
          from_email = "message.from_email@example.com",
          from_name = "Example Name",
          to = List(MTo(
            email = "recipient.email@example.com",
            name = Some("Recipient Name")
          )),
          headers = Some(List(MHeader("Reply-To", "message.reply@example.com"))),
          important = false,
          bcc_address = Some("message.bcc_address@example.com"),
          merge = true,
          global_merge_vars = List(MVars("merge1", "merge1 content")),
          merge_vars = List(MMergeVars("recipient.email@example.com", List(MVars("merge2", "merge2 content")))),
          tags = List("password-resets"),
          subaccount = Some("customer-123"),
          metadata = List(MMetadata("website", "www.example.com")),
          recipient_metadata = List(MRecipientMetadata("recipient.email@example.com", List(MMetadata("user_id", "123456")))),
          attachments = List(MAttachmetOrImage("text/plain", "myfile.txt", "ZXhhbXBsZSBmaWxl")),
          images = List(MAttachmetOrImage("image/png", "IMAGECID", "ZXhhbXBsZSBmaWxl")),
          google_analytics_campaign = Some("message.from_email@example.com"),
          google_analytics_domains = List("example.com"),
          merge_language = Some("handlebars")
        )
      )), defaultTimeout) {
        case MandrillSuccess(res) =>
          res.size shouldBe 1
          res.head.status shouldBe "sent"
          res.head.email shouldBe "recipient.email@example.com"
          res.head.reject_reason shouldBe Some("hard-bounce")
          res.head._id shouldBe "abc123abc123abc123abc123abc123"
        case x@_ => fail(s"Unsuccessful call: Should be MandrillSuccess, got $x")
      }
    }
  }

  "SendTemplate" should "work getting a valid List[MSendResponse]" in {
    withClient("/messages/send-template.json") { ws =>
      val instance = new MandrillClient(ws, new APIKey())
      whenReady(instance.messagesSendTemplate(MSendTemplateMessage(
        template_name = "example template_name",
        template_content = List(MVars("example name", "example content")),
        async = false,
        ip_pool = Some("Main Pool"),
        message = new MSendMsg(
          html = "<p>Example HTML content</p>",
          text = "Example text content",
          subject = "example subject",
          from_email = "message.from_email@example.com",
          from_name = "Example Name",
          to = List(MTo(
            email = "recipient.email@example.com",
            name = Some("Recipient Name")
          )),
          headers = Some(List(MHeader("Reply-To", "message.reply@example.com"))),
          important = false,
          bcc_address = Some("message.bcc_address@example.com"),
          merge = true,
          global_merge_vars = List(MVars("merge1", "merge1 content")),
          merge_vars = List(MMergeVars("recipient.email@example.com", List(MVars("merge2", "merge2 content")))),
          tags = List("password-resets"),
          subaccount = Some("customer-123"),
          metadata = List(MMetadata("website", "www.example.com")),
          recipient_metadata = List(MRecipientMetadata("recipient.email@example.com", List(MMetadata("user_id", "123456")))),
          attachments = List(MAttachmetOrImage("text/plain", "myfile.txt", "ZXhhbXBsZSBmaWxl")),
          images = List(MAttachmetOrImage("image/png", "IMAGECID", "ZXhhbXBsZSBmaWxl")),
          google_analytics_campaign = Some("message.from_email@example.com"),
          google_analytics_domains = List("example.com"),
          merge_language = Some("handlebars")
        )
      )), defaultTimeout) {
        case MandrillSuccess(res) =>
          res.size shouldBe 1
          res.head.status shouldBe "sent"
          res.head.email shouldBe "recipient.email@example.com"
          res.head.reject_reason shouldBe Some("hard-bounce")
          res.head._id shouldBe "abc123abc123abc123abc123abc123"
        case x@_ => fail(s"Unsuccessful call: Should be MandrillSuccess, got $x")
      }
    }
  }

  "Search" should "work getting a valid List[MSearchResponse]" in {
    withClient("/messages/search.json") { ws =>
      val instance = new MandrillClient(ws, new APIKey())
      whenReady(instance.messagesSearch(MSearch(
        query = "email:gmail.com",
        date_from = "2013-01-01",
        date_to = "2013-01-02",
        tags = List("password-reset", "welcome"),
        senders = List("sender@example.com"),
        api_keys = List("PmmzuovUZMPJsa73o3jjCw"),
        limit = 100
      )), defaultTimeout) { res =>
        res shouldBe MandrillSuccess(List(MSearchResponse(
          ts = 1365190000,
          _id = "abc123abc123abc123abc123",
          sender = "sender@example.com",
          template = Some("example-template"),
          subject = "example subject",
          email = "recipient.email@example.com",
          tags = List(
          "password-reset"
          ),
          opens = 42,
          opens_detail = List(MOpenDetail(
            ts = 1365190001,
            ip = "55.55.55.55",
            location = "Georgia, US",
            ua = "Linux/Ubuntu/Chrome/Chrome 28.0.1500.53"
          )),
          clicks = 42,
          clicks_detail = List(MClickDetails(
            ts = 1365190001,
            url = "http://www.example.com",
            ip = "55.55.55.55",
            location = "Georgia, US",
            ua = "Linux/Ubuntu/Chrome/Chrome 28.0.1500.53"
          )),
          state = "sent"
        )))
      }
    }
  }

  "searchTimeSeries" should "handle the example at https://mandrillapp.com/api/docs/messages.JSON.html#method=search-time-series" in {
    withClient("/messages/search-time-series.json"){ wc =>
      val instance = new MandrillClient(wc, new APIKey())
      whenReady(instance.messagesSearchTimeSeries(MSearchTimeSeries(
        query = "email:gmail.com",
        date_from = "2013-01-01",
        date_to = "2013-01-02",
        tags = List(
          "password-reset",
          "welcome"
        ),
        senders = List(
          "sender@example.com"
        )
      )), defaultTimeout)(_ shouldBe MandrillSuccess(List(MTimeSeriesResponse(
        time = "2013-01-01 15:00:00",
        sent = 42,
        hard_bounces = 42,
        soft_bounces = 42,
        rejects = 42,
        complaints = 42,
        unsubs = 42,
        opens = 42,
        unique_opens = 42,
        clicks = 42,
        unique_clicks = 42
      ))))
    }
  }

  "MessageInfo" should "work getting a valid MMessageInfoResponse" in {
    withClient("/messages/info.json"){ wc =>
      val instance = new MandrillClient(wc, new APIKey())
      whenReady(instance.messagesInfo(MMessageInfo(
        id = "abc123abc123abc123abc123"
      )), defaultTimeout)(_ shouldBe MandrillSuccess(MMessageInfoResponse(
        ts = 1365190000,
        _id = "abc123abc123abc123abc123",
        sender = "sender@example.com",
        template = Some("example-template"),
        subject = "example subject",
        email = "recipient.email@example.com",
        tags = List(
          "password-reset"
        ),
        opens = 42,
        opens_detail = List(
          MOpenDetail(
            ts = 1365190001,
            ip = "55.55.55.55",
            location = "Georgia, US",
            ua = "Linux/Ubuntu/Chrome/Chrome 28.0.1500.53"
          )
        ),
        clicks = 42,
        clicks_detail = List(
          MClickDetails(
            ts = 1365190001,
            url = "http://www.example.com",
            ip = "55.55.55.55",
            location = "Georgia, US",
            ua = "Linux/Ubuntu/Chrome/Chrome 28.0.1500.53"
          )
        ),
        state = "sent",
        smtp_events = List(
          MSmtpEvent(
            ts = 1365190001,
            `type` = "sent",
            diag = "250 OK"
          )
        )
      )))
    }
  }

  "Parse" should "work getting a valid MParseResponse" in {
    withClient("/messages/parse.json"){ wc =>
      val instance = new MandrillClient(wc, new APIKey())
      whenReady(instance.messagesParse(MParse(
        raw_message = "From: sender@example.com\nTo: recipient.email@example.com\nSubject: Some Subject\n\nSome content."
      )), defaultTimeout)(_ shouldBe MandrillSuccess(MParseResponse(
        subject = Some("Some Subject"),
        from_email = Some("sender@example.com"),
        from_name = Some("Sender Name"),
        to = Some(List(
          MToResponse(
            email = "recipient.email@example.com",
            name = "Recipient Name"
          )
        )),
        text = Some("Some text content"),
        html = Some("<p>Some HTML content</p>"),
        attachments = Some(List(
          MAttachmetOrImage(
            name = "example.txt",
            `type`= "text/plain",
            content = "example non-binary content"
          )
        )),
        images = Some(List(
          MAttachmetOrImage(
            name = "IMAGEID",
            `type` = "image/png",
            content = "ZXhhbXBsZSBmaWxl"
          )
        ))
      )))
    }
  }

  "SendRaw" should "handle the example at https://mandrillapp.com/api/docs/messages.JSON.html#method=send-raw" in {
    withClient("/messages/send-raw.json"){ wc =>
      val instance = new MandrillClient(wc, new APIKey())
      whenReady(instance.messagesSendRaw(MSendRaw(
        raw_message = "From: sender@example.com\nTo: recipient.email@example.com\nSubject: Some Subject\n\nSome content.",
        from_email = Some("sender@example.com"),
        from_name = Some("From Name"),
        to = List("recipient.email@example.com"),
        async = false,
        ip_pool = Some("Main Pool"),
        send_at = Some("example send_at"),
        return_path_domain = Some("mail.com")
      )), defaultTimeout)(_ shouldBe MandrillSuccess(List(MSendResponse(
        email = "recipient.email@example.com",
        status = "sent",
        reject_reason = Some("hard-bounce"),
        _id = "abc123abc123abc123abc123"
      ))))
    }
  }

  "ListSchedule" should "work getting a valid List[MScheduleResponse]" in {
    withClient("/messages/list-scheduled.json"){ wc =>
      val instance = new MandrillClient(wc, new APIKey())
      whenReady(instance.messagesListSchedule(MListSchedule(
        to = "test.recipient@example.com"
      )), defaultTimeout)(_ shouldBe MandrillSuccess(List(MScheduleResponse(
        _id = "I_dtFt2ZNPW5QD9-FaDU1A",
        created_at = "2013-01-20 12:13:01",
        send_at = "2021-01-05 12:42:01",
        from_email = "sender@example.com",
        to = "test.recipient@example.com",
        subject = "This is a scheduled email"
      ))))
    }
  }

  "CancelScheduled" should "handle the example at https://mandrillapp.com/api/docs/messages.JSON.html#method=cancel-scheduled" in {
    withClient("/messages/cancel-scheduled.json"){ wc =>
      val instance = new MandrillClient(wc, new APIKey())
      whenReady(instance.messagesCancelSchedule(MCancelSchedule(
        id = "I_dtFt2ZNPW5QD9-FaDU1A"
      )), defaultTimeout)(_ shouldBe MandrillSuccess(MScheduleResponse(
        _id = "I_dtFt2ZNPW5QD9-FaDU1A",
        created_at = "2013-01-20 12:13:01",
        send_at = "2021-01-05 12:42:01",
        from_email = "sender@example.com",
        to = "test.recipient@example.com",
        subject = "This is a scheduled email"
      )))
    }
  }

  "Reschedule" should "handle the example at https://mandrillapp.com/api/docs/messages.JSON.html#method=reschedule" in {
    withClient("/messages/reschedule.json"){ wc =>
      val instance = new MandrillClient(wc, new APIKey())
      whenReady(instance.messagesReschedule(MReSchedule(
        id = "I_dtFt2ZNPW5QD9-FaDU1A",
        send_at = "20120-06-01 08:15:01"
      )), defaultTimeout)(_ shouldBe MandrillSuccess(MScheduleResponse(
        _id = "I_dtFt2ZNPW5QD9-FaDU1A",
        created_at = "2013-01-20 12:13:01",
        send_at = "2021-01-05 12:42:01",
        from_email = "sender@example.com",
        to = "test.recipient@example.com",
        subject = "This is a scheduled email"
      )))
    }
  }
}
