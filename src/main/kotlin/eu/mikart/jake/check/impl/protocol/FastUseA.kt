package eu.mikart.jake.check.impl.protocol

import eu.mikart.jake.Jake
import eu.mikart.jake.check.Check
import eu.mikart.jake.check.CheckCategory
import eu.mikart.jake.check.CheckInfo
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData

@CheckInfo(name = "FastUse", type = "A", complexType = "Delay", category = CheckCategory.PACKET, description = "Too fast item use")
class FastUseA(plugin: Jake, data: PlayerData) : Check(plugin, data) {
    private var lastUse = 0L
    override fun onPacket(packet: PacketWrapper) {
        if (!packet.isBlockDig()) return
        val now = System.currentTimeMillis()
        val diff = now - lastUse
        if (lastUse != 0L && diff < 90) if (increaseBuffer() > 2) fail(detail = "diff=$diff") else decreaseBuffer(.25)
        lastUse = now
    }
}