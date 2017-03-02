package computerdatabase.tenant.functional

import computerdatabase.tenant.{ComputerDatabaseChainBuilder, ComputerDatabaseHttpProtocolBuilder}
import gatling.blueprint.ConfigurableSimulation
import gatling.blueprint.ConfigurationTool.coordinates
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

class Test extends ConfigurableSimulation {

  // Resolve to "user-files/data/tenant/local/computerdatabase/functional/search.csv""
  private val feeder = csv(resolveFile("search.csv"))

  // Calculate the simulation users and ramp-up based on the CSV file content
  private val mySimulationUsers = feeder.records.length
  private val mySimulationUsersRampup = new DurationInteger(mySimulationUsers * 10).seconds

  // The base URL is taken from "user-files/data/tenant/local/computerdatabase/environment.properties"
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
      }
    }

  setUp(
    users.inject(rampUsers(mySimulationUsers) over mySimulationUsersRampup)
      .protocols(httpProtocol)
      .pauses(constantPauses))

  before {
    println("Simulation is about to start!")
  }

  after {
    println("Simulation is finished!")
  }
}
