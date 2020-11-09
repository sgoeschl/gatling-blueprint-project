package helloworld

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class HelloWorldSimulation extends Simulation {
  val httpProtocol = http.baseUrl("https://postman-echo.com")
  val scn = scenario("Hello World")
    .exec(http("GET").get("/get?msg=Hello%20World"))
  setUp(scn.inject(atOnceUsers(1)).protocols(httpProtocol))
}
