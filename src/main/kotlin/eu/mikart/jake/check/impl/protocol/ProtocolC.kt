package eu.mikart.jake.check.impl.protocol

import eu.mikart.jake.Jake
import eu.mikart.jake.check.Check
import eu.mikart.jake.check.CheckCategory
import eu.mikart.jake.check.CheckInfo
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData
import org.bukkit.Bukkit

@CheckInfo(name = "Protocol", type = "C", complexType = "Abilities", category = CheckCategory.PACKET, description = "Spoofed Abilities packets")
class ProtocolC(plugin: Jake, data: PlayerData) : Check(plugin, data) {
    override fun onPacket(packet: PacketWrapper) {
        val p = Bukkit.getPlayer(data.uuid) ?: return
        if(!packet.isAbilities()) return

        if (p.allowFlight) return

        // Above 1.9, we generally shouldn't see clients toggling abilities like flight unless allowed.
        // Since we don't have a version util here, treat all as suspicious if not allowed.
        if (increaseBuffer() > 2) fail(detail = "abilities while allowFlight=false") else decreaseBuffer(0.25)
    }
}
