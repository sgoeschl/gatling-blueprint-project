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

import io.gatling.core.Predef._
import org.github.sgoeschl.gatling.blueprint.extensions.boon.FilteringJsonPrettyPrinter
import org.github.sgoeschl.gatling.blueprint.extensions.file.FileUtil

import scala.collection.JavaConversions._

object JsonResponseTool {

  private var directory = new File("./target/gatling")
  private var isResponseSaved = true

  def init(directory: File, isResponseSaved: Boolean): Unit = {
    this.directory = directory
    this.isResponseSaved = isResponseSaved
  }

  /**
    * Save a pretty-printed JSON string as file.
    *
    * @param jsonString JSON response to save
    * @param nameParts  parts to build the file name
    * @return
    */
  def saveToFile(jsonString: String, nameParts: String*): Unit = {
    if (isResponseSaved) {
      val file = FileUtil.createFile(directory, "json", nameParts)
      val prettyPrintedJson = FilteringJsonPrettyPrinter.prettyPrint(jsonString)
      FileUtil.writeToFile(file, prettyPrintedJson)
    }
  }

  /**
    * Save the JSON object found in the session as file.
    *
    * @param session   the Gatling session
    * @param key       key to access the session
    * @param nameParts parts to build the file name
    * @return
    */
  def saveToFile(session: Session, key: String, nameParts: String*): Unit = {
    if (isResponseSaved) {
      val file = FileUtil.createFile(directory, "json", nameParts)
      val prettyPrintedJson = FilteringJsonPrettyPrinter.prettyPrint((session get key).as[Any])
      FileUtil.writeToFile(file, FilteringJsonPrettyPrinter.prettyPrint(prettyPrintedJson))
    }
  }
}
