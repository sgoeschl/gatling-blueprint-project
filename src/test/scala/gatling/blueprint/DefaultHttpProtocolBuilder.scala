package gatling.blueprint

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.{HttpProtocolBuilder, ProxyBuilder}

/**
  * Plain vanilla HTTP protocol builder
  * <ul>
  * <li>Configurable proxy support</li>
  * <li>Basic error reporting using the simulation.log</li>
  * </ul>
  */
object DefaultHttpProtocolBuilder {

  /**
    * Create a HttpProtocolBuilder with sensible defaults.
    */
  def create(): HttpProtocolBuilder = {

    var httpProtocolBuilder = http
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
      .acceptEncodingHeader("gzip, deflate")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
      .disableCaching

    if (ConfigurationTool.hasProxy) {
      httpProtocolBuilder = httpProtocolBuilder.proxy(proxyBuilder).noProxyFor("localhost", "127.0.0.1")
    }

    httpProtocolBuilder
  }

  def proxyBuilder: ProxyBuilder = {
    Proxy(ConfigurationTool.proxyHost, ConfigurationTool.proxyPort)
      .httpsPort(ConfigurationTool.proxyPortSecure)
  }
}
