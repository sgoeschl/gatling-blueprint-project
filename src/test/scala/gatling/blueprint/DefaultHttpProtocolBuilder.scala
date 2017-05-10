package gatling.blueprint

import java.text.SimpleDateFormat
import java.util.Date

import io.gatling.commons.stats.Status
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.{HttpProtocolBuilder, HttpProxyBuilder}
import io.gatling.http.request.ExtraInfo

/**
  * Plain vanilla HTTP protocol builder
  * <ul>
  * <li>Configurable proxy support</li>
  * <li>Basic error reporting using the simulation.log</li>
  * </ul>
  */
object DefaultHttpProtocolBuilder {

  private val responseBodyLengthToBePrinted = 512
  private val dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

  /**
    * Create a HttpProtocolBuilder with sensible defaults.
    */
  def create(): HttpProtocolBuilder = {

    var httpProtocolBuilder = http
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
      .acceptEncodingHeader("gzip, deflate")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
      .extraInfoExtractor { extraInfo => extractExtraInfo(extraInfo) }
      .disableCaching

    if (ConfigurationTool.hasProxy) {
      httpProtocolBuilder = httpProtocolBuilder.proxy(httpProxyBuilder).noProxyFor("localhost", "127.0.0.1")
    }

    httpProtocolBuilder
  }

  def httpProxyBuilder: HttpProxyBuilder = {
    Proxy(ConfigurationTool.proxyHost, ConfigurationTool.proxyPort)
      .httpsPort(ConfigurationTool.proxyPortSecure)
  }

  /**
    * Add custom-data to the "simulation.log" - see <a href="http://gatling.io/docs/2.2.3/http/http_protocol.html">HTTP Protocol</a>.
    *
    * <ul>
    * <li>The format of the "simulation.log" is un-documented but seems to be a tab-separated file</li>
    * </ul>
    */
  private def extractExtraInfo(extraInfo: ExtraInfo): List[String] = {
    val statusCode = extraInfo.response.statusCode.getOrElse(0)
    if (statusCode >= 300 || extraInfo.status.eq(Status.apply("KO"))) {
      val url = extraInfo.request.getUrl
      val duration = extraInfo.response.timings.responseTime
      val timestamp = dateFormatter.format(new Date())
      val bodyLength = extraInfo.response.bodyLength
      val responseBody = extractResponseBody(extraInfo)
      List(s"\tURL=$url|Status=$statusCode|Timestamp=$timestamp|Length=$bodyLength|Duration=$duration|Response=$responseBody")
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
    val length = Math.min(rawResponseBody.length, responseBodyLengthToBePrinted)
    rawResponseBody.substring(0, length).replace("\n\r", " ").replace("\t", " ") + "..."
  }
}
