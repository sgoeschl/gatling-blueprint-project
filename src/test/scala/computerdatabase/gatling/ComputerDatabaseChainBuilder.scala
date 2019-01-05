package computerdatabase.gatling

import io.gatling.core.structure.ChainBuilder
import org.github.sgoeschl.gatling.blueprint.extensions.SimulationCoordinates

object ComputerDatabaseChainBuilder {

  def create(simulationCoordinates: SimulationCoordinates): List[ChainBuilder] = create(simulationCoordinates.getScope)

  def create(scope: String): List[ChainBuilder] = {
    scope.toLowerCase match {
      case "smoketest" =>
        List(
          ComputerDatabase.home,
          ComputerDatabase.Search.search,
          ComputerDatabase.Browse.browse(1)
        )
      case "functional" =>
        List(
          ComputerDatabase.home,
          ComputerDatabase.Search.search,
          ComputerDatabase.Browse.browse(5)
        )
      case _ =>
        throw new IllegalArgumentException(s"Don't know hot to handle: $scope")
    }
  }
}
