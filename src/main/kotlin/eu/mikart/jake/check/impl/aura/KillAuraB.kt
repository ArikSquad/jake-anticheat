package eu.mikart.jake.check.impl.aura

import eu.mikart.jake.Jake
import eu.mikart.jake.check.*
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData

@CheckInfo(name = "KillAura", type = "B", complexType = "Delay", category = CheckCategory.COMBAT, description = "Constant attack delay")
class KillAuraB(plugin: Jake, data: PlayerData) : Check(plugin, data) {
    private var last = 0L
    private var streak = 0
    override fun onPacket(packet: PacketWrapper) {
    if (!packet.isArmAnimation()) return

        val now = System.currentTimeMillis()
        val diff = now - last
        if (last != 0L) {
            if (diff in 46..57) {
                streak++
                if (streak > 8 && increaseBuffer() > 6) fail(detail = "const=${diff}ms streak=$streak")
            } else streak = 0
        }
        last = now
    }
}
