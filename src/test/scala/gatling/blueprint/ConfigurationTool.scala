/**
  * Licensed to the Apache Software Foundation (ASF) under one or more
  * contributor license agreements.  See the NOTICE file distributed with
  * this work for additional information regarding copyright ownership.
  * The ASF licenses this file to You under the Apache License, Version 2.0
  * (the "License"); you may not use this file except in compliance with
  * the License.  You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package gatling.blueprint

import java.io.File
import java.util.Properties

import io.gatling.core.config.GatlingConfiguration
import org.github.sgoeschl.gatling.blueprint.extensions.SimulationCoordinates
import org.github.sgoeschl.gatling.blueprint.extensions.file.{ConfigurationFileResolver, EnvironmentPropertiesResolver, URLUtil}

object ConfigurationTool {

  var environmentProperties: Properties = _

  var simulationCoordinates: SimulationCoordinates = _
  private var dataDirectoryName: String = _
  private var resultDirectoryName: String = _
  private var initialized: Boolean = false
  private var proxyHost = ""
  private var proxyPort: Int = 8080
  private var proxyPortSecure: Int = 8080

  def init(configuration: GatlingConfiguration): Unit = {
    dataDirectoryName = configuration.core.directory.data
    resultDirectoryName = configuration.core.directory.results

    val simulationClass = configuration.core.simulationClass.get
    simulationCoordinates = SimulationCoordinates.from(simulationClass, System.getProperties)

    environmentProperties = EnvironmentPropertiesResolver.resolveProperties(dataDirectoryName, simulationCoordinates)

    this.proxyHost = environmentProperties.getProperty("proxy.host")
    this.proxyPort = environmentProperties.getProperty("proxy.port", "8080").toInt
    this.proxyPortSecure = environmentProperties.getProperty("proxy.port.secure", "8080").toInt

    initialized = true
  }

  def coordinates: SimulationCoordinates = simulationCoordinates

  def dataDirectory: File = new File(this.dataDirectoryName)

  def resultDirectory: File = new File(this.resultDirectoryName)

  def resolveFile(fileName: String): String = {
    ConfigurationFileResolver.resolveFile(dataDirectoryName, simulationCoordinates, fileName).getAbsolutePath
  }

  def getProperty(key: String, defaultValue: String = ""): String = {
    environmentProperties.getProperty(key, defaultValue)
  }

  def hasProperty(key: String): Boolean = {
    getProperty(key) != null && !getProperty(key).isEmpty
  }

  def getURL(system: String, endpoint: String = ""): String = {
    URLUtil.getURL(getBaseURL(system), endpoint)
  }

  def hasProxy: Boolean = {
    proxyHost != null
  }

  /**
    * Can we save responses to the file system? When doing this for performance
    * tests this is very likely not a good idea.
    */
  def isResponseSaved: Boolean = {
    val defaultValue = if (isPerformanceTest(simulationCoordinates.getScope)) "false" else "true"
    getProperty("simulation.response.save", defaultValue).equals("true")
  }


  /**
    * Heuristically determine if we run a performance test.
    */
  private def isPerformanceTest(scope: String): Boolean = {
    if (scope.contains("performance") || scope.contains("load") || scope.contains("stress")) true else false
  }

  private def getBaseURL(system: String): String = {
    val key: String = s"$system.base.url"
    if (hasProperty(key)) {
      environmentProperties.getProperty(key)
    }
    else {
      throw new IllegalArgumentException(s"The following key was not found: $key")
    }
  }

  override def toString: String = s"ConfigurationTool(environmentProperties=$environmentProperties, " +
    s"simulationCoordinates=$simulationCoordinates, " +
    s"dataDirectoryName=$dataDirectoryName, " +
    s"resultDirectoryName=$resultDirectoryName, " +
    s"initialized=$initialized, " +
    s"proxyHost=$proxyHost, " +
    s"proxyPort=$proxyPort, " +
    s"proxyPortSecure=$proxyPortSecure, " +
    s"coordinates=$coordinates, " +
    s"dataDirectory=$dataDirectory, " +
    s"resultDirectory=$resultDirectory, " +
    s"hasProxy=$hasProxy)"
}
