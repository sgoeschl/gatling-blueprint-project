package computerdatabase.tenant.smoketest

import computerdatabase.tenant.ComputerDatabaseChainBuilder
import gatling.blueprint.ConfigurableSimulation
import gatling.blueprint.ConfigurationTool.coordinates
import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class Test extends ConfigurableSimulation {

  // Resolve to "user-files/data/tenant/local/computerdatabase/smoketest/search.csv""
  val feeder: RecordSeqFeederBuilder[String] = csv(resolveFile("search.csv"))

  // The base URL is taken from "user-files/data/computerdatabase/tenant/environment.properties"
  val httpConf: HttpProtocolBuilder = http
    .baseURL("http://computer-database.gatling.io")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // Executed test steps are moved into "ComputerDatabaseChainBuilder"
  // Scenario name is derived from the simulation coordinates
  val users: ScenarioBuilder = scenario(coordinates.toScenarioName)
    .feed(feeder)
    .repeat(simulationLoops) {
      tryMax(simulationTryMax) {
        exec(ComputerDatabaseChainBuilder.create(coordinates))
          .pause(simulationPause)
      }
    }

  setUp(
    users.inject(atOnceUsers(simulationUsersAtOnce), rampUsers(simulationUsers) over simulationUsersRampup))
    .protocols(httpConf)
    .pauses(constantPauses)
}
