package githubapi.github

import gatling.blueprint.{ConfigurationTool, JsonResponseTool}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object GitHubApi {

  val home: ChainBuilder = exec(http("Home")
    .get("/")
    .check(
      bodyBytes.saveAs("lastResponse")))
    .exec(session => {
      JsonResponseTool.saveToFile(session, "lastResponse", "githubapi", "home")
      session
    })
    .pause(ConfigurationTool.getPause)

  val users: ChainBuilder = exec(http("Users")
    .get("/users")
    .queryParam("page", 1)
    .queryParam("per_page", 10)
    .check(
      bodyBytes.saveAs("lastResponse")))
    .exec(session => {
      // remove "received_events_url" from the saved JSON response (just for fun)
      val jsonString = JsonResponseTool.modify(session, "lastResponse", "received_events_url")
      JsonResponseTool.saveToFile(jsonString, "githubapi", "users")
      session
    })
    .pause(ConfigurationTool.getPause)

  val events: ChainBuilder = exec(http("Events")
    .get("/events")
    .queryParam("page", 1)
    .queryParam("per_page", 10)
    .check(
      bodyBytes.saveAs("lastResponse")))
    .exec(session => {
      val jsonString = JsonResponseTool.modify(session, "lastResponse")
      JsonResponseTool.saveToFile(jsonString, "githubapi", "events")
      session
    })
    .pause(ConfigurationTool.getPause)
}
