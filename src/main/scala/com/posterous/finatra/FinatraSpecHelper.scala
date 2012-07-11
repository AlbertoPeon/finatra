package com.posterous.finatra.test

import com.posterous.finatra._
import com.twitter.finatra_core.{AbstractFinatraSpec}
import org.jboss.netty.handler.codec.http.DefaultHttpResponse
import com.twitter.util.Future
import scala.collection.mutable.Map
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.util.CharsetUtil.UTF_8

abstract class FinatraSpecHelper extends AbstractFinatraSpec[Request, Future[HttpResponse]] {

  def response  = lastResponse.get.getContent.toString(UTF_8)
  def request   = new Request

  var lastResponse:Future[HttpResponse] = null

  def buildRequest(method:String, path:String, params:Map[String,String]=Map(), headers:Map[String,String]=Map()) {
    val req = request
    req.method(method)
    req.path(path)
    req.params(params)
    lastResponse = app.dispatch(req).asInstanceOf[Option[Future[HttpResponse]]].get
  }
}
