package computerdatabase.tenant.smoketest

import computerdatabase.tenant.{ComputerDatabaseChainBuilder, ComputerDatabaseHttpProtocolBuilder}
import gatling.blueprint.ConfigurableSimulation
import gatling.blueprint.ConfigurationTool.coordinates
import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.core.structure.ScenarioBuilder

class Test extends ConfigurableSimulation {

  // Resolve to "user-files/data/tenant/local/computerdatabase/smoketest/search.csv""
  val feeder: RecordSeqFeederBuilder[String] = csv(resolveFile("search.csv"))

  // The base URL is taken from "user-files/data/computerdatabase/tenant/environment.properties"
  private val httpProtocol = ComputerDatabaseHttpProtocolBuilder.create(coordinates.getApplication)
    .baseURL(getBaseURL)
    .build

  // 1) Executed test steps are moved into "ComputerDatabaseChainBuilder"
  // 2) Scenario name is derived from the simulation coordinates
  private val users: ScenarioBuilder = scenario(scenarionName)
    .feed(feeder)
    .repeat(simulationLoops) {
      tryMax(simulationTryMax) {
        exec(ComputerDatabaseChainBuilder.create(coordinates))
          .pause(simulationPause)
      }
    }

  setUp(
    users.inject(atOnceUsers(simulationUsersAtOnce), rampUsers(simulationUsers) over simulationUsersRampup))
    .protocols(httpProtocol)
    .pauses(constantPauses)

  before {
    println("Simulation is about to start!")
  }

  after {
    println("Simulation is finished!")
  }
}
