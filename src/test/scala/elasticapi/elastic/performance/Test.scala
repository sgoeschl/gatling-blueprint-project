package elasticapi.elastic.performance

import elasticapi.elastic.ElasticApiChainBuilder
import gatling.blueprint.{ConfigurableSimulation, DefaultHttpProtocolBuilder}
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

class Test extends ConfigurableSimulation {

  private val elasticTsv = tsv(resolveFile("elastic.tsv"))
  private val httpConfServer = DefaultHttpProtocolBuilder.create()

  val scenarioBuilder: ScenarioBuilder = scenario("Elastic Performance")
    .tryMax(simulationTryMax) {
      repeat(simulationLoops) {
        feed(elasticTsv.circular)
          .exec(ElasticApiChainBuilder.create("performance"))
          .pause(simulationPause)
      }
    }

  setUp(scenarioBuilder.inject(rampUsers(simulationUsers) during simulationUsersRampup))
    .maxDuration(simulationDuration)
    .protocols(httpConfServer)
}
