package elasticapi.elastic

import gatling.blueprint.{ConfigurationTool, JsonResponseTool}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

object ElasticApi {

  val HTTP_OK = 200

  val search: ChainBuilder =
    exec(_.set("ELASTICBASEURL", ConfigurationTool.getURL("elasticapi")))
      .exec(http("${LABEL}")
        .post("${ELASTICBASEURL}/${URL}")
        // enable if you want to pass the line number of the current request
        // .queryParam("_", "${LINENUMBER}")
        .body(StringBody("${QUERY}")).asJson
        .check(status.is(HTTP_OK)))

  val searchAndSaveResponse: ChainBuilder =
    exec(_.set("ELASTICBASEURL", ConfigurationTool.getURL("elasticapi")))
      .exec(http("${LABEL}")
        .post("${ELASTICBASEURL}/${URL}")
        // enable if you want to pass the line number of the current request
        // .queryParam("_", "${LINENUMBER}")
        .body(StringBody("${QUERY}")).asJson
        .check(status.is(HTTP_OK))
        .check(status.saveAs("lastResponseStatus"))
        .check(checkIf((response: Response, session: Session) =>
          session("lastResponseStatus").as[Int] == HTTP_OK)(jsonPath("$").ofType[Any].find.saveAs("lastResponse"))))
      .exec(session => {
        if (ConfigurationTool.isResponseSaved && session.contains("lastResponse")) {
          val jsonString = JsonResponseTool.modify(session, "lastResponse", "took")
          JsonResponseTool.saveToFile(jsonString, "lastResponse", session.attributes.getOrElse("LINENUMBER", "").toString)
        }
        session
      })
}
