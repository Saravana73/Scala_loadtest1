package  simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class CheckAndPause extends Simulation{
  val httpConf = http.baseUrl(url = "https://reqres.in")
    .header(name = "Accept", value = "application/json")

  val scn= scenario(scenarioName="user api calls")

    .exec(http(requestName = "list all users")
    .get("/api/users?page=2")
    check(status. is(200)))
    .pause(duration = 5)
    .exec(http(requestName = "single user api")
    .get("/api/users/2")
    .check(status.in(200 to 210)))
    .pause(1,10)

    .exec(http(requestName = "single use not found api")
    .get("/api/users/23")
    .check(status.not(expected=400),status.not(500)))
    .pause(3000.milliseconds)

  setUp(scn.inject(atOnceUsers(users=5))).protocols(httpConf)
}