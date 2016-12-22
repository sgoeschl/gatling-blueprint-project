package github.tenant

import endpoint.GitHubApi
import io.gatling.core.structure.ChainBuilder
import org.github.sgoeschl.gatling.blueprint.extensions.SimulationCoordinates

object GitHubApiChainBuilder {

  def create(simulationCoordinates: SimulationCoordinates): List[ChainBuilder] = create(simulationCoordinates.getScope)

  def create(scope: String): List[ChainBuilder] = {
    scope.toLowerCase match {
      case "functional" =>
        List(
          GitHubApi.home,
          GitHubApi.users
        )
      case _ =>
        throw new IllegalArgumentException(s"Don't know hot to handle: $scope")
    }
  }
}
