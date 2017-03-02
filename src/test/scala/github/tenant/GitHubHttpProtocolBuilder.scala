package github.tenant

import java.text.SimpleDateFormat
import java.util.Date

import gatling.blueprint.ConfigurationTool
import io.gatling.commons.stats.Status
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.{HttpProtocolBuilder, HttpProxyBuilder}
import io.gatling.http.request.ExtraInfo

object GitHubHttpProtocolBuilder {

  private val dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

  /**
    * Create a HttpProtocolBuilder with all the sensible defaults you might need.
    *
    * @param application the application to simulate
    */
  def create(application: String): HttpProtocolBuilder = {

    var httpProtocolBuilder: HttpProtocolBuilder = null

    application match {
      case "github" =>
        httpProtocolBuilder = http
          .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
          .doNotTrackHeader("1")
          .acceptLanguageHeader("en-US,en;q=0.5")
          .acceptEncodingHeader("gzip, deflate")
          .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
          .extraInfoExtractor { extraInfo => extractExtraInfo(extraInfo) }
          .disableCaching
      case _ =>
        throw new IllegalArgumentException(s"Don't know hot to handle: $application")

    }

    if (ConfigurationTool.hasProxy) {
      val httpProxy: HttpProxyBuilder = Proxy(ConfigurationTool.proxyHost, ConfigurationTool.proxyPort).httpsPort(ConfigurationTool.proxyPortSecure)
      httpProtocolBuilder = httpProtocolBuilder.proxy(httpProxy).noProxyFor("localhost", "127.0.0.1")
    }

    httpProtocolBuilder
  }

  /**
    * Add custom-data to the "simulation.log" - see <a href="http://gatling.io/docs/2.2.3/http/http_protocol.html">HTTP Protocol</a>.
    *
    * <ul>
    * <li>The format of the "simulation.log" is un-documented but seems to be a tab-seperated file</li>
    * </ul>
    */
  private def extractExtraInfo(extraInfo: ExtraInfo): List[String] = {
    val statusCode = extraInfo.response.statusCode.getOrElse(0)
    if (statusCode >= 300 || extraInfo.status.eq(Status.apply("KO"))) {
      val url = extraInfo.request.getUrl
      val timestamp = dateFormatter.format(new Date())
      val responseBody = extractResponseBody(extraInfo)
      List(s"\tURL=$url;Status=$statusCode;Timestamp=$timestamp;responseBody=$responseBody")
    } else {
      List("")
    }
  }

  /**
    * Create a manageable response body.
    * <li>
    * <ul>Limit the length</ul>
    * <ul>Replace line breaks and tabs</ul>
    * </li>
    */
  private def extractResponseBody(extraInfo: ExtraInfo): String = {
    val rawResponseBody = extraInfo.response.body.string
    if (rawResponseBody == null) return "<null>"
    val length = Math.min(rawResponseBody.length, 512)
    rawResponseBody.substring(0, length).replace("\n\r", " ").replace("\t", " ") + "..."
  }
}
