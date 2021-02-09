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

import io.gatling.core.Predef.DurationInteger
import io.gatling.core.config.GatlingConfiguration
import org.github.sgoeschl.gatling.blueprint.extensions.file.URLUtil
import org.github.sgoeschl.gatling.blueprint.extensions.{DataFileResolver, EnvironmentPropertiesResolver, SimulationCoordinates}

import scala.concurrent.duration.FiniteDuration

object ConfigurationTool {

  var initialized: Boolean = false
  var environmentProperties: Properties = _
  var dataDirectory: File = _
  var resultDirectory: File = _
  var hasProxy: Boolean = _
  var proxyHost: String = _
  var proxyPort: Int = 8080
  var proxyPortSecure: Int = 8080
  var coordinates: SimulationCoordinates = _

  def init(configuration: GatlingConfiguration): Unit = {
    coordinates = SimulationCoordinates.from(configuration.core.simulationClass.get, System.getProperties)
    dataDirectory = configuration.core.directory.resources.toFile
    resultDirectory = configuration.core.directory.results.toFile
    environmentProperties = EnvironmentPropertiesResolver.resolveProperties(dataDirectory, coordinates)
    proxyHost = environmentProperties.getProperty("proxy.host")
    proxyPort = environmentProperties.getProperty("proxy.port", "8080").toInt
    proxyPortSecure = environmentProperties.getProperty("proxy.port.secure", "8080").toInt
    hasProxy = proxyHost != null && proxyHost.trim.nonEmpty
    initialized = true
  }

  def resolveFile(fileName: String): String = {
    val file = DataFileResolver.resolveFile(dataDirectory, coordinates, fileName)
    dataDirectory.toPath.relativize(file.toPath).toString
  }

  def getProperty(key: String): String = {
    val value = environmentProperties.getProperty(key)
    if (value != null) value else throw new IllegalArgumentException(s"Property $key not found")
  }

  def getProperty(key: String, defaultValue: String): String = {
    environmentProperties.getProperty(key, defaultValue)
  }

  def hasBaseURL(system: String): Boolean = {
    val baseURL = createBaseURL(system)
    baseURL != null && baseURL.nonEmpty
  }

  /**
    * Lookup a "${system}.base.url" and add the relative URL.
    */
  def getURL(system: String, relativeURL: String = ""): String = {
    URLUtil.getURL(createBaseURL(system), relativeURL)
  }

  def getPause: FiniteDuration = {
    new DurationInteger(getProperty("simulation.pause.ms", "0").toInt).milliseconds
  }

  /**
    * Can we save responses to the file system? When doing this for performance
    * tests this is very likely not a good idea due to performance and disk
    * space issues.
    */
  def isResponseSaved: Boolean = {
    val defaultValue = if (isPerformanceTest(coordinates.getScope)) "false" else "true"
    getProperty("simulation.response.save", defaultValue).equals("true")
  }

  /**
    * Heuristically determine if we run a performance test.
    */
  private def isPerformanceTest(scope: String): Boolean = {
    if (scope.contains("performance")
      || scope.contains("endurance")
      || scope.contains("load")
      || scope.contains("stress")) true
    else false
  }

  private def createBaseURL(system: String): String = getProperty(s"$system.base.url")

  override def toString: String = s"ConfigurationTool(" +
    s"initialized=$initialized, " +
    s"coordinates=$coordinates, " +
    s"environmentProperties=$environmentProperties, " +
    s"dataDirectory=$dataDirectory, " +
    s"resultDirectory=$resultDirectory, " +
    s"initialized=$initialized, " +
    s"proxyHost=$proxyHost, " +
    s"proxyPort=$proxyPort, " +
    s"proxyPortSecure=$proxyPortSecure, " +
    s"coordinates=$coordinates, " +
    s"dataDirectory=$dataDirectory, " +
    s"resultDirectory=$resultDirectory, " +
    s"hasProxy=$hasProxy)"
}
