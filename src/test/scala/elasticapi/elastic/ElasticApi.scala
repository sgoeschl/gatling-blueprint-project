package elasticapi.elastic

import gatling.blueprint.{ConfigurationTool, JsonResponseTool}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object ElasticApi {

  val search: ChainBuilder =
    exec(_.set("ELASTICBASEURL", ConfigurationTool.getURL("elasticapi")))
      .exec(http("${LABEL}")
        .post("${ELASTICBASEURL}/${URL}")
        // .queryParam("_", "${LINENUMBER}")
        .body(StringBody("${QUERY}")).asJSON
        .check(
          jsonPath("$").ofType[Any].find.saveAs("lastResponse"))
      )
      .exec(session => {
        if (ConfigurationTool.isResponseSaved) {
          val jsonString = JsonResponseTool.modify(session, "lastResponse", "took")
          JsonResponseTool.saveToFile(jsonString, "lastResponse", session.get("LINENUMBER").as[String])
        }
        session
      })
}
