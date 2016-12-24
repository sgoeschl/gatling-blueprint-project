package computerdatabase.tenant.functional

import computerdatabase.tenant.ComputerDatabaseChainBuilder
import gatling.blueprint.ConfigurableSimulation
import gatling.blueprint.ConfigurationTool.coordinates
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class Test extends ConfigurableSimulation {

  // Resolve to "user-files/data/tenant/local/computerdatabase/functional/search.csv""
  private val feeder = csv(resolveFile("search.csv"))

  // Calculate the simulation users and ramp-up based on the CSV file content
  private val mySimulationUsers = feeder.records.length
  private val mySimulationUsersRampup = new DurationInteger(mySimulationUsers * 10).seconds

  // The base URL is taken from "user-files/data/tenant/local/computerdatabase/environment.properties"
  val httpConf: HttpProtocolBuilder = http
    .baseURL(getBaseURL)
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
      }
    }

  setUp(
    users.inject(rampUsers(mySimulationUsers) over mySimulationUsersRampup)
      .protocols(httpConf)
      .pauses(constantPauses))
}
