package github.tenant.functional

import gatling.blueprint.ConfigurableSimulation
import gatling.blueprint.ConfigurationTool.coordinates
import github.tenant.GitHubApiChainBuilder
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class Test extends ConfigurableSimulation {

  // The base URL is taken from "user-files/data/tenant/local/github/environment.properties"
  val httpConf: HttpProtocolBuilder = http
    .baseURL(getBaseURL)
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("curl/7.52.0")

  val users: ScenarioBuilder = scenario(coordinates.toScenarioName)
    .repeat(simulationLoops) {
      tryMax(simulationTryMax) {
        exec(GitHubApiChainBuilder.create(coordinates))
          .pause(simulationPause)
      }
    }

  setUp(
    users.inject(atOnceUsers(simulationUsersAtOnce), rampUsers(simulationUsers) over simulationUsersRampup))
    .protocols(httpConf)
    .pauses(constantPauses)
}
