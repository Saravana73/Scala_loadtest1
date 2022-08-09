package simulations

import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

class LoopCalls extends Simulation{
  val httpConf = http.baseUrl(url = "https://reqres.in")
    .header(name = "Accept", value = "application/json")
    .header(name = "content-type", value = "application/json")

  def getAllUserRequest(): ChainBuilder={
    repeat(times = 2){
      exec(http(requestName = "get all user request")
      .get("/api/users?/page=2")
      .check(status.in(200 to 201)))
    }
  }

  def getaUserRequest(): ChainBuilder={
    repeat(times = 2){
      exec(http(requestName = "get a single user request")
      .get("/api/users/2")
      .check(status.is(expected = 201)))
    }
  }

  def addAUser(): ChainBuilder = {
    repeat(times = 2) {
      exec(http(requestName = "add a user request")
        .post(url = "/api/users")
        .body(RawFileBody(filePath = "./src/test/resources/bodies/AddUser.json")).asJson
        .check(status.is(200)))

    }
  }
  val scn = scenario(scenarioName = "user request scenario")
    .exec(getAllUserRequest())
    .pause(duration = 2)
    .exec(getaUserRequest())
    .pause(duration=2)
    .exec(addAUser())

  setUp(scn.inject(atOnceUsers(users = 1))).protocols(httpConf)


}