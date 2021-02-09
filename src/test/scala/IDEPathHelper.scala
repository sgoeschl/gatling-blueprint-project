import io.gatling.commons.shared.unstable.util.PathHelper.RichPath

import java.nio.file.{Path, Paths}

object IDEPathHelper {

  val projectRootDir: Path = Paths.get(getClass.getClassLoader.getResource("gatling.conf").toURI).getParent.getParent.getParent

  val mavenSourcesDirectory: Path = projectRootDir / "src" / "test" / "scala"
  val mavenResourcesDirectory: Path = projectRootDir / "src" / "test" / "resources"
  val mavenTargetDirectory: Path = projectRootDir / "target"
  val mavenBinariesDirectory: Path = mavenTargetDirectory / "test-classes"

  val resourcesDirectory: Path = mavenResourcesDirectory
  val recorderSimulationsDirectory: Path = mavenSourcesDirectory
  val resultsDirectory: Path = mavenTargetDirectory / "gatling"
  val recorderConfigFile: Path = projectRootDir / "conf" / "recorder.conf"
}
