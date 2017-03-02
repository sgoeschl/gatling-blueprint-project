package github.tenant.functional

import gatling.blueprint.ConfigurableSimulation
import gatling.blueprint.ConfigurationTool.coordinates
import github.tenant.{GitHubApiChainBuilder, GitHubHttpProtocolBuilder}
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

class Test extends ConfigurableSimulation {

  // The base URL is taken from "user-files/data/tenant/local/github/environment.properties"
  private val httpProtocol = GitHubHttpProtocolBuilder.create(coordinates.getApplication)
    .baseURL(getBaseURL)
    .build

  // 1) Executed test steps are moved into "GitHubApiChainBuilder"
  // 2) Scenario name is derived from the simulation coordinates
  private val users: ScenarioBuilder = scenario(coordinates.toScenarioName)
    .repeat(simulationLoops) {
      tryMax(simulationTryMax) {
        exec(GitHubApiChainBuilder.create(coordinates))
          .pause(simulationPause)
      }
    }

  setUp(
    users.inject(rampUsers(simulationUsers) over simulationUsersRampup))
    .protocols(httpProtocol)
    .pauses(constantPauses)

  before {
    println("Simulation is about to start!")
  }

  after {
    println("Simulation is finished!")
  }
}
