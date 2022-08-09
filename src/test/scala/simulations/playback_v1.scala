package playback
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
class PlaybackSimulation extends Simulation {
   val httpProtocol = http
   // Here is the root for all relative URLs
  .baseUrl(System.getenv("PLAYBACK_BASE_URL"))
   // Here are the common headers
   .acceptHeader("application/json")
   .doNotTrackHeader("1")
   .acceptEncodingHeader("gzip, deflate")
   .header("x-vdp-access-key", System.getenv("PLAYBACK_ACCESS_KEY"))
   .userAgentHeader("Gatling 3.5.1")
   var filename = ""
   // set the assets csv file based on env selected
   def selectEnvFunc():ChainBuilder={
   if(System.getenv("ENV")=="PROD") {
   filename = "playback-assets-prod.csv"
   exec{session => println(filename); session}
   } else{
   filename = "playback-assets-stage.csv"
   exec{session => println(filename) ; session}
   }}
   var scn = scenario("Olympic load").exec(selectEnvFunc())
   .feed(csv(filename).circular)
   .repeat(10) {
     println("/v1/streams/${asset_uuid}/urls?realm=${realm}&contentSubsetId=${contentSubsetId}&countryCode=${countryCode}")
     exec(http("${scenarioName}").get("/v1/streams/${asset_uuid}/urls?realm=${realm}&contentSubsetId=${contentSubsetId}&countryCode=${countryCode}").check(status.is(200)))
     }

   val totalUsers = sys.env.getOrElse("LOAD_TEST_TOTAL_USERS", "10")
   val totalSeconds = sys.env.getOrElse("LOAD_TEST_TOTAL_SECONDS", "10")
   println(s"users: $totalUsers; seconds: $totalSeconds")
   setUp(scn.inject(rampUsers(totalUsers.toInt).during(totalSeconds.toInt.seconds)).protocols(httpProtocol))
}

