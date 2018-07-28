package elastic.elasticapi.performance

import elastic.elasticapi.ElasticApiChainBuilder
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

  setUp(scenarioBuilder.inject(rampUsers(simulationUsers) over simulationUsersRampup))
    .maxDuration(simulationDuration)
    .protocols(httpConfServer)
    .onFailure(failure => println(failure))
}
