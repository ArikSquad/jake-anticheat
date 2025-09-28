package eu.mikart.jake.check.impl.protocol

import eu.mikart.jake.Jake
import eu.mikart.jake.check.Check
import eu.mikart.jake.check.CheckCategory
import eu.mikart.jake.check.CheckInfo
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData

@CheckInfo(name = "PacketFreq", type = "A", complexType = "Rate", category = CheckCategory.PACKET, description = "Too many movement packets")
class PacketFrequencyA(plugin: Jake, data: PlayerData) : Check(plugin, data) {
    private var lastSecond = 0L
    private var count = 0
    override fun onPacket(packet: PacketWrapper) {
        if (!packet.isPosition()) return
        val now = System.currentTimeMillis() / 1000
        if (lastSecond != now) {
            if (count > 25) if (increaseBuffer() > 2) fail(detail = "c=$count") else decreaseBuffer()
            count = 0
            lastSecond = now
        }
        count++
    }
}