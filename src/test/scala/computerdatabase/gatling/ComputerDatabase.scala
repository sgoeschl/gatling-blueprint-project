package computerdatabase.gatling

import gatling.blueprint.ConfigurationTool
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object ComputerDatabase {

  val headers_10: Map[String, String] = Map("Content-Type" -> "application/x-www-form-urlencoded")

  val home: ChainBuilder = exec(http("Home")
    .get("/"))
    .pause(ConfigurationTool.getPause)

  object Search {

    val search: ChainBuilder =
      exec(http("Search")
        .get("/computers?f=${searchCriterion}") // 4
        .check(css("a:contains('${searchComputerName}')", "href").saveAs("computerURL")))
        .pause(ConfigurationTool.getPause)
        .exec(http("Select")
          .get("${computerURL}"))
        .pause(ConfigurationTool.getPause)
  }

  object Browse {

    def browse(n: Int): ChainBuilder = repeat(n, "i") {
      exec(http("Page ${i}")
        .get("/computers?p=${i}"))
        .pause(ConfigurationTool.getPause)
    }
  }

}
