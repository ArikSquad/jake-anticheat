package eu.mikart.jake.check.impl.aura

import eu.mikart.jake.Jake
import eu.mikart.jake.check.*
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData

// horrible check
@CheckInfo(name = "KillAura", type = "A", complexType = "Swing", category = CheckCategory.COMBAT, description = "Swing pattern abnormal")
class KillAuraA(plugin: Jake, data: PlayerData) : Check(plugin, data) {
    private var lastSwing = 0L
    override fun onPacket(packet: PacketWrapper) {
        if (!packet.isArmAnimation()) return

        val now = System.currentTimeMillis()
        if (lastSwing != 0L) {
            val diff = now - lastSwing
            if (diff < 35) {
                if (increaseBuffer() > 6) fail(detail = "fast=${diff}ms")
            } else decreaseBuffer(0.5)
        }
        lastSwing = now
    }
}
