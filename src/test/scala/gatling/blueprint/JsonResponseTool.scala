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

import scala.collection.JavaConverters._

object JsonResponseTool {

  private val emptyJson = "[]"
  private var directory = new File("./target/gatling")
  private var isResponseSaved = true

  def init(directory: File, isResponseSaved: Boolean): Unit = {
    this.directory = new File(System.getProperty("response.dir", directory.getAbsolutePath))
    this.isResponseSaved = isResponseSaved
  }

  /**
    * Save a string value into a file when "isResponseSaved" is true.
    */
  def saveToFile(value: String, fileNameParts: String*): Unit = {
    if (isResponseSaved) {
      val file = FileUtil.createFile(directory, "json", seqAsJavaList(fileNameParts))
      FileUtil.writeToFile(file, value)
    }
  }

  /**
    * Extract the JSON object from the session, pretty-print it and save to file
    * when "isResponseSaved" is true.
    *
    * @param session       Gatling session
    * @param key           session key
    * @param fileNameParts parts to build the file name
    */
  def saveToFile(session: Session, key: String, fileNameParts: String*): Unit = {
    if (isResponseSaved) {
      val file = FileUtil.createFile(directory, "json", seqAsJavaList(fileNameParts))
      val prettyPrintedJson = FilteringJsonPrettyPrinter.prettyPrint(session.attributes.getOrElse(key, ""))
      FileUtil.writeToFile(file, prettyPrintedJson)
    }
  }

  /**
    * Extract the JSON object from the session, modify &amp; pretty-print it and save to file
    * when "isResponseSaved" is true.
    *
    * @param session         Gatling session
    * @param key             session key
    * @param skippedJsonKeys JSON keys to remove from the result
    */
  def modify(session: Session, key: String, skippedJsonKeys: String*): String = {
    if (isResponseSaved) {
      val json = session.attributes.getOrElse(key, null)
      FilteringJsonPrettyPrinter.print(json, seqAsJavaList(skippedJsonKeys))
    }
    else {
      emptyJson
    }
  }
}
