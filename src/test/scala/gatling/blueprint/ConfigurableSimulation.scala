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

import io.gatling.core.Predef.DurationInteger
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.scenario.Simulation

import scala.concurrent.duration.FiniteDuration

abstract class ConfigurableSimulation(implicit configuration: GatlingConfiguration) extends Simulation {

  ConfigurationTool.init(configuration)
  JsonResponseTool.init(ConfigurationTool.resultDirectory, ConfigurationTool.isResponseSaved)

  val simulationUsersAtOnce: Int = getProperty("simulation.users.atonce", "1").toInt
  val simulationUsers: Int = getProperty("simulation.users", "0").toInt
  val simulationUsersRampup: FiniteDuration = new DurationInteger(getProperty("simulation.users.rampup", "0").toInt).seconds
  val simulationDuration: FiniteDuration = new DurationInteger(getProperty("simulation.duration", "300").toInt).seconds
  val simulationLoops: Int = getProperty("simulation.loops", "1").toInt
  val simulationTryMax: Int = getProperty("simulation.try.max", "1").toInt
  val simulationPause: FiniteDuration = ConfigurationTool.getPause

  print("Coordinates: " + ConfigurationTool.coordinates)
  print("Environment: " + ConfigurationTool.environmentProperties)
  print("Simulation: " + this.toString)

  if (ConfigurationTool.environmentProperties.isEmpty) {
    print("No environment properties are found - please check your configuration")
  }

  def resolveFile(fileName: String): String = {
    val resolvedFileName = ConfigurationTool.resolveFile(fileName)
    print(s"Resolve file '$fileName' to '$resolvedFileName'")
    return resolvedFileName
  }

  def getBaseURL: String = ConfigurationTool.getURL(ConfigurationTool.coordinates.getApplication)

  def getProperty(key: String): String = ConfigurationTool.getProperty(key)

  def getProperty(key: String, defaultValue: String): String = ConfigurationTool.getProperty(key, defaultValue)

  def hasProxy: Boolean = ConfigurationTool.hasProxy

  override def toString: String = s"(" +
    s"usersAtOnce=$simulationUsersAtOnce, " +
    s"users=$simulationUsers, " +
    s"usersRampup=$simulationUsersRampup, " +
    s"duration=$simulationDuration, " +
    s"loops=$simulationLoops, " +
    s"tryMax=$simulationTryMax, " +
    s"pause=$simulationPause)"

  private def print(line: String): Unit = System.out.println(line)
}
