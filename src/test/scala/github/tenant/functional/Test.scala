package github.tenant.functional

import computerdatabase.tenant.ComputerDatabaseChainBuilder
import gatling.blueprint.ConfigurationTool.coordinates
import gatling.blueprint.{ConfigurableSimulation, ConfigurationTool}
import github.tenant.GitHubApiChainBuilder
import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
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
    .repeat(getSimulationLoops) {
      tryMax(getSimulationTryMax) {
        exec(GitHubApiChainBuilder.create(coordinates))
          .pause(getSimulationPause)
      }
    }

  setUp(
    users.inject(rampUsers(getSimulationUsers) over getSimulationUsersRampup)
  ).protocols(httpConf)
}
