package eu.mikart.jake.check.impl.protocol

import eu.mikart.jake.Jake
import eu.mikart.jake.check.Check
import eu.mikart.jake.check.CheckCategory
import eu.mikart.jake.check.CheckInfo
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData
import org.bukkit.Bukkit

@CheckInfo(name = "Pitch", type = "A", complexType = "Limit", category = CheckCategory.ROTATION, description = "Pitch out of bounds")
class PitchLimitA(plugin: Jake, data: PlayerData) : Check(plugin, data) {
    override fun onPacket(packet: PacketWrapper) {
        if (!packet.isRotation()) return
        val p = Bukkit.getPlayer(data.uuid) ?: return
        val pitch = p.location.pitch
        if (pitch > 90f || pitch < -90f) if (increaseBuffer() > 1) fail(detail = "pitch=$pitch") else decreaseBuffer(.25)
    }
}