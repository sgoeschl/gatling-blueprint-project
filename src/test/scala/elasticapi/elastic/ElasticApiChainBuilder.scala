package elasticapi.elastic

import githubapi.github.GitHubApi
import io.gatling.core.structure.ChainBuilder
import org.github.sgoeschl.gatling.blueprint.extensions.SimulationCoordinates

object ElasticApiChainBuilder {

  def create(simulationCoordinates: SimulationCoordinates): List[ChainBuilder] = create(simulationCoordinates.getScope)

  def create(scope: String): List[ChainBuilder] = {
    scope.toLowerCase match {
      case "smoketest" | "functional" | "performance" =>
        List(
          ElasticApi.search
        )
      case _ =>
        throw new IllegalArgumentException(s"Don't know hot to handle: $scope")
    }
  }

}
