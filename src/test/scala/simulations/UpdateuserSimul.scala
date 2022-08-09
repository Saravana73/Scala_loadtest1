package  simulations

import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
class UpdateuserSimul extends Simulation{

  val httpConf = http.baseUrl(url = "https://reqres.in")
    .header(name = "Accept", value = "application/json")
    .header(name = "content-type", value = "application/json")

  val scn= scenario(scenarioName = " Update User Scenario ")

  //First call -c check the name of the game
    .exec(http(requestName = "Update specific User Scenario")
    .put(url="/api/users/2")
    .body(RawFileBody(filePath = "./src/test/resources/bodies/UpdateUser.json")).asJson
    .check(status.in(expected = 200 to 201)))

.pause(duration = 3)
  .exec(http(requestName = "delete user")
  .delete(url="/api/users/2")
  .check(status.in(200 to 204)))
  //setup
  setUp(scn.inject(atOnceUsers(users = 1))).protocols(httpConf)

}