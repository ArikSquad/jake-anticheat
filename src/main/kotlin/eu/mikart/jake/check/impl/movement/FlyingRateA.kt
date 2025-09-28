package eu.mikart.jake.check.impl.movement

import eu.mikart.jake.Jake
import eu.mikart.jake.check.Check
import eu.mikart.jake.check.CheckCategory
import eu.mikart.jake.check.CheckInfo
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData
import org.bukkit.Bukkit
import kotlin.math.abs

@CheckInfo(name = "Flying", type = "A", complexType = "Rate", category = CheckCategory.MOVEMENT, description = "Irregular flying packet rate")
class FlyingRateA(plugin: Jake, data: PlayerData) : Check(plugin, data) {
    private var windowStart = System.currentTimeMillis()
    private var count = 0
    private var lastX = Double.NaN
    private var lastY = Double.NaN
    private var lastZ = Double.NaN
    override fun onPacket(packet: PacketWrapper) {
        if (!packet.isPosition()) return
        val p = Bukkit.getPlayer(data.uuid) ?: return

        val loc = p.location
        if (lastX.isNaN()) {
            lastX = loc.x
            lastY = loc.y
            lastZ = loc.z
            windowStart = System.currentTimeMillis()
            return
        }

        val dx = loc.x - lastX
        val dy = loc.y - lastY
        val dz = loc.z - lastZ
        val moved = abs(dx) + abs(dy) + abs(dz) > 1.0E-4
        if (moved) count++

        val now = System.currentTimeMillis()
        val elapsed = now - windowStart
        if (elapsed >= 1000) {
            if (count == 0) {
                windowStart = now
                lastX = loc.x
                lastY = loc.y
                lastZ = loc.z
                return
            }
            val rate = count * 1000.0 / elapsed.coerceAtLeast(1)
            val ok = rate in 10.0..30.0
            if (!ok) {
                if (increaseBuffer() > 4) fail(detail = "rate=${"%.1f".format(rate)}")
            } else {
                decreaseBuffer()
            }
            count = 0
            windowStart = now
        }

        lastX = loc.x
        lastY = loc.y
        lastZ = loc.z
    }
}