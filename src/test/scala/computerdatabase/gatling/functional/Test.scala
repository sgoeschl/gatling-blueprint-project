package computerdatabase.gatling.functional

import computerdatabase.gatling.ComputerDatabaseChainBuilder
import gatling.blueprint.ConfigurationTool.coordinates
import gatling.blueprint.{ConfigurableSimulation, DefaultHttpProtocolBuilder}
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

class Test extends ConfigurableSimulation {

  // Resolve to "user-files/data/tenant/local/computerdatabase/functional/search.csv""
  private val feeder = csv(resolveFile("search.csv"))

  // The base URL is taken from "user-files/data/tenant/local/computerdatabase/environment.properties"
  private val httpProtocol = DefaultHttpProtocolBuilder.create()
    .baseUrl(getBaseURL)
    .build

  // 1) Executed test steps are moved into "ComputerDatabaseChainBuilder"
  // 2) Scenario name is derived from the simulation coordinates
  private val users: ScenarioBuilder = scenario(scenarioName)
    .feed(feeder)
    .repeat(simulationLoops) {
      tryMax(simulationTryMax) {
        exec(ComputerDatabaseChainBuilder.create(coordinates))
      }
    }

  setUp(
    users.inject(atOnceUsers(simulationUsersAtOnce))
      .protocols(httpProtocol)
      .pauses(constantPauses))
      .assertions(global.failedRequests.count.is(0))

  before {
    println("Simulation is about to start!")
  }

  after {
    println("Simulation is finished!")
  }
}
