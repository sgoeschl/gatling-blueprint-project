package github.tenant

import gatling.blueprint.JsonResponseTool
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object GitHubApi {

  val home: ChainBuilder = exec(http("Home")
    .get("/")
    .check(
      jsonPath("$").ofType[Any].find.saveAs("lastResponse")))
    .exec(session => {
      JsonResponseTool.saveToFile(session, "lastResponse", "github", "home")
      session
    })

  val users: ChainBuilder = exec(http("Users")
    .get("/users")
    .queryParam("page", 1)
    .queryParam("per_page", 10)
    .check(
      jsonPath("$").ofType[Any].find.saveAs("lastResponse")))
    .exec(session => {
      // remove "received_events_url" from the saved JSON response (just for fun)
      val jsonString = JsonResponseTool.modify(session, "lastResponse", "received_events_url")
      JsonResponseTool.saveToFile(jsonString, "github", "users")
      session
    })

  val events: ChainBuilder = exec(http("Events")
    .get("/events")
    .queryParam("page", 1)
    .queryParam("per_page", 10)
    .check(
      jsonPath("$").ofType[Any].find.saveAs("lastResponse")))
    .exec(session => {
      val jsonString = JsonResponseTool.modify(session, "lastResponse")
      JsonResponseTool.saveToFile(jsonString, "github", "events")
      session
    })
}
