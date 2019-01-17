package us.nineworlds.serenity.emby.server

import us.nineworlds.serenity.common.Server
import java.io.Serializable

class EmbyServer : Server, Serializable {

  private var serverName: String? = null
  private var ipAddress: String? = ""
  private var hostName: String? = null
  private var discoveryProtocol: String? = "Emby"
  private var portNumber: String? = null

  override fun setServerName(serverName: String?) {
    this.serverName = serverName
  }

  override fun getIPAddress(): String? {
    return ipAddress
  }

  override fun setIPAddress(ipaddress: String?) {
    this.ipAddress = ipaddress
  }

  override fun getHostName(): String? {
    return hostName
  }

  override fun setHostName(hostName: String?) {
    this.hostName = hostName
  }

  override fun discoveryProtocol(): String? {
    return discoveryProtocol
  }

  override fun setDiscoveryProtocol(protocol: String?) {
    this.discoveryProtocol = protocol
  }

  override fun getServerName(): String? {
    return serverName
  }

  override fun hasMultipleAccounts(): Boolean = true

  override fun getPort(): String? {
    return portNumber
  }

  override fun setPort(portNumber: String?) {
    this.portNumber = portNumber
  }
}