package eu.mikart.jake.check.impl.movement

import eu.mikart.jake.Jake
import eu.mikart.jake.check.Check
import eu.mikart.jake.check.CheckCategory
import eu.mikart.jake.check.CheckInfo
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.potion.PotionEffectType
import kotlin.math.sqrt

@CheckInfo(name = "Speed", type = "A", description = "Horizontal move too fast", category = CheckCategory.MOVEMENT)
class SpeedA(plugin: Jake, data: PlayerData) : Check(plugin, data) {
	private var lastX = 0.0
	private var lastZ = 0.0
	private var lastTime = System.currentTimeMillis()
	override fun onPacket(packet: PacketWrapper) {
		if (!packet.isPosition()) return
		val p = Bukkit.getPlayer(data.uuid) ?: return
		val loc = p.location
		val now = System.currentTimeMillis()

		val dx = loc.x - lastX
		val dz = loc.z - lastZ
		val horizontal = sqrt(dx * dx + dz * dz)

		val ms = (now - lastTime).coerceAtLeast(1)
		val perTick = horizontal * 50.0 / ms
		var limit = 0.28

		if (p.isSprinting) limit *= 1.3

		val speed = p.getPotionEffect(PotionEffectType.SPEED)?.amplifier ?: -1
		if (speed >= 0) limit *= (1.0 + 0.2 * (speed + 1))

		val slow = p.getPotionEffect(PotionEffectType.SLOWNESS)?.amplifier ?: -1
		if (slow >= 0) limit *= 1.0 / (1.0 + 0.15 * (slow + 1))

        val below = loc.clone().subtract(0.0, 0.01, 0.0).block
        when (below.type) {
            Material.ICE, Material.FROSTED_ICE, Material.PACKED_ICE, Material.BLUE_ICE -> limit *= 1.7
            Material.SLIME_BLOCK -> limit *= 1.4
            Material.SOUL_SAND, Material.SOUL_SOIL -> limit *= 1.2 // lenient
            else -> {}
        }

		if (!p.isOnGround) limit *= 1.1

		limit *= 1.15
		if (perTick > limit) {
			if (increaseBuffer() > 8) fail(detail = "hpt=${"%.3f".format(perTick)} lim=${"%.3f".format(limit)} ms=$ms")
		} else decreaseBuffer()

		lastX = loc.x
		lastZ = loc.z
		lastTime = now
	}
}