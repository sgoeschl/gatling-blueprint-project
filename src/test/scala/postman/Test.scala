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

package postman

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Test extends Simulation {

  val httpProtocol = http.baseUrl("https://postman-echo.com")

  val scn = scenario("Postman")
    .exec(http("GET")
      .get("/get?foo1=bar1&foo2=bar2")
      .check(status is 200)
      .check(bodyBytes.transform(_.length > 200).is(true))
    )

  setUp(scn.inject(atOnceUsers(1)).protocols(httpProtocol))
}
