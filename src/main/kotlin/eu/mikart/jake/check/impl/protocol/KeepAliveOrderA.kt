package eu.mikart.jake.check.impl.protocol

import eu.mikart.jake.Jake
import eu.mikart.jake.check.Check
import eu.mikart.jake.check.CheckCategory
import eu.mikart.jake.check.CheckInfo
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData

@CheckInfo(name = "KeepAlive", type = "A", complexType = "Order", category = CheckCategory.PACKET, description = "Out of order keepalive")
class KeepAliveOrderA(plugin: Jake, data: PlayerData) : Check(plugin, data) {
    private var lastId: Int = -1
    override fun onPacket(packet: PacketWrapper) {
        if (!packet.isKeepAlive()) return
        val id = packet.packetId
        if (lastId != -1 && id < lastId) if (increaseBuffer() > 2) fail(detail = "id=$id last=$lastId") else decreaseBuffer(.5)
        lastId = id
    }
}