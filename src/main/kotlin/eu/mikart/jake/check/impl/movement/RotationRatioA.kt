package eu.mikart.jake.check.impl.movement

import eu.mikart.jake.Jake
import eu.mikart.jake.check.Check
import eu.mikart.jake.check.CheckCategory
import eu.mikart.jake.check.CheckInfo
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData
import org.bukkit.Bukkit
import kotlin.math.abs

@CheckInfo(name = "Aim", type = "A", complexType = "Ratio", category = CheckCategory.COMBAT, description = "Invalid rotation ratio")
class RotationRatioA(plugin: Jake, data: PlayerData) : Check(plugin, data) {
    private var lastYaw = 0f
    private var lastPitch = 0f
    override fun onPacket(packet: PacketWrapper) {
        if (!packet.isRotation()) return
        val p = Bukkit.getPlayer(data.uuid) ?: return
        val loc = p.location
        val dy = abs(loc.yaw - lastYaw)
        val dp = abs(loc.pitch - lastPitch)
        if (dy > .5f && dp < 0.0001f && dp > 0f) {
            if (increaseBuffer() > 4) fail(detail = "dy=$dy dp=$dp")
        } else decreaseBuffer(.25)
        lastYaw = loc.yaw
        lastPitch = loc.pitch
    }
}