package com.posterous.finatra 

import org.junit.Test
import com.codahale.simplespec.Spec
import org.jboss.netty.handler.codec.http.{DefaultHttpRequest, HttpMethod, HttpVersion}
import com.twitter.finagle.http.{Http, RichHttp, Request, Response}
import com.twitter.finagle.http.Status._


object FakeApp extends FinatraApp {
  get("/") { "resp" }

  get("/other") { "otherresp" }
  head("/other") { "specialresp" }
  
  get("/jsonstuff") { toJson(Map("foo" -> "bar")) }
  
  get("/redirectme") { redirect("/gohere") }
}
 
class RouterSpec extends Spec {

  FakeApp

  class `GET '/'` {

    var request = Request(HttpMethod.GET, "/")
    var response = Router.dispatch(request)

    @Test def `returns 200` = {
      response.statusCode.must(be(200))
    }
    
    @Test def `returns a response` = {
      response.must(beA[Response])
    }

    @Test def `responds with 'resp'` = {
      response.content.toString("UTF8").must(be("resp"))
    }

  }

  class `HEAD '/' (curl -I)` {

    var request = Request(HttpMethod.HEAD, "/")
    var response = Router.dispatch(request)

    @Test def `returns 200` = {
      response.statusCode.must(be(200))
    }
    
    @Test def `returns a response` = {
      response.must(beA[Response])
    }

    @Test def `responds with 'resp'` = {
      response.content.toString("UTF8").must(be("resp"))
    }

  }

  class `HEAD '/other' (curl -I)` {

    var request = Request(HttpMethod.HEAD, "/other")
    var response = Router.dispatch(request)

    @Test def `returns 200` = {
      response.statusCode.must(be(200))
    }
    
    @Test def `returns a response` = {
      response.must(beA[Response])
    }

    @Test def `responds with 'resp'` = {
      response.content.toString("UTF8").must(be("specialresp"))
    }

  }

  class `GET '/other'` {

    var request = Request(HttpMethod.GET, "/other")
    var response = Router.dispatch(request)

    @Test def `returns 200` = {
      response.statusCode.must(be(200))
    }
    
    @Test def `returns a response` = {
      response.must(beA[Response])
    }

    @Test def `responds with 'resp'` = {
      response.content.toString("UTF8").must(be("otherresp"))
    }

  }
  
  class `GET '/jsonstuff'` {

    var request = Request(HttpMethod.GET, "/jsonstuff")
    var response = Router.dispatch(request)

    @Test def `returns 200` = {
      response.statusCode.must(be(200))
    }
    
    @Test def `returns a response` = {
      response.must(beA[Response])
    }

    @Test def `responds with 'json'` = {
      response.content.toString("UTF8").must(be("{\"foo\":\"bar\"}"))
    }

  }
  
  class `GET '/redirect'` {

    var request = Request(HttpMethod.GET, "/redirectme")
    var response = Router.dispatch(request)

    @Test def `returns 301` = {
      response.statusCode.must(be(301))
    }
    
    @Test def `returns a response` = {
      response.must(beA[Response])
    }

    @Test def `redirect with Location: /gohere'` = {
      response.headers.get("Location").getOrElse(null).toString.must(be("/gohere"))
      //response.content.toString("UTF8").must(be("{\"foo\":\"bar\"}"))
    }

  }
}

