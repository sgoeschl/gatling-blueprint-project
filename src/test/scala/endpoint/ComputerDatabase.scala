package endpoint

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object ComputerDatabase {

  val headers_10: Map[String, String] = Map("Content-Type" -> "application/x-www-form-urlencoded")

  val home: ChainBuilder = exec(http("Home")
    .get("/"))
    .pause(1)

  object Search {

    val search: ChainBuilder = exec(http("Home")
      .get("/"))
      .pause(1)
      .exec(http("Search")
        .get("/computers?f=${searchCriterion}") // 4
        .check(css("a:contains('${searchComputerName}')", "href").saveAs("computerURL")))
      .pause(1)
      .exec(http("Select")
        .get("${computerURL}")) // 6
      .pause(1)
  }

  object Browse {

    def browse(n: Int): ChainBuilder = repeat(n, "i") {
      // 1
      exec(http("Page ${i}")
        .get("/computers?p=${i}"))
        .pause(1)
    }
  }

  val edit: ChainBuilder = exec(http("Form")
    .get("/computers/new"))
    .pause(1)
    .exec(http("Post")
      .post("/computers")
      .headers(headers_10)
      .formParam("name", "Beautiful Computer")
      .formParam("introduced", "2012-05-30")
      .formParam("discontinued", "")
      .formParam("company", "37"))
}
