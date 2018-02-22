import java.util.UUID

import com.typesafe.config.{Config, ConfigFactory}
import io.gatling.core.Predef.{jumpToRps, _}
import io.gatling.http.Predef._

import scala.concurrent.duration._

class LoadBalancerSimulation extends Simulation {

  val config: Config = ConfigFactory.load()

  val httpProtocol = http
    .baseURL(config.getString("performance.host"))

  val scn = scenario("Load balancing scenario")
    .exec(http("Get group for user")
      .get("/route/" + UUID.randomUUID().toString)
         .check(status.is(200)))

  setUp(scn.inject(rampUsers(80000) over(3 seconds)).protocols(httpProtocol))
}