package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class TestApisimul extends Simulation{

  //http conf
  val httpConf: HttpProtocolBuilder = http.baseUrl(url="https://reqres.in")
    .header(name="Accept", value="application/json")
    .header(name="content-type", value="application/json")

  //scenario
  val scn = scenario(scenarioName = "get user")
  .exec(http(requestName = "get user request")
    .get("/api/users/")
    .check(status is 200))


  //setup
  setUp(scn.inject(atOnceUsers(users=5))).protocols(httpConf)


}



