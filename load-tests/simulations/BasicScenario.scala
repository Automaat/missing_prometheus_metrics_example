package benchamark_load_tests

import java.util.UUID

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.language.postfixOps

class BasicScenario extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("http://proxy:20001") // Here is the root for all relative URLs
    .acceptHeader("application/json") // Here are the common headers
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn: ScenarioBuilder = scenario("Simple io benchmark")
    .exec(_.set("resourceId", UUID.randomUUID().toString))
    .exec(
      http("create order")
        .post("/orders")
        .body(StringBody(s"""{ "items":  ["12345", "432"], "value": "${scala.util.Random.nextInt(100).toString}" }"""))
        .header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)
        .check(jsonPath("$.id").saveAs("orderId"))
    )
    .pause(10)
    .exec(
      http("get order")
        .get("/orders/${orderId}")
    )
    .pause(100)
    .exec(
      http("pay for order")
        .post("/orders/${orderId}/paid")
    )
    .pause(10)
    .exec(
      http("get order")
        .get("/orders/${orderId}")
    )
    .pause(100)
    .exec(
      http("ship order")
        .post("/orders/${orderId}/shipped")
    )
    .pause(10)
    .exec(
      http("get order")
        .get("/orders/${orderId}")
    )

  setUp(scn.inject(constantUsersPerSec(20) during (1800 seconds)).protocols(httpProtocol))
}