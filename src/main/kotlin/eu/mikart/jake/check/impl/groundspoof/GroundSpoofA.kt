package eu.mikart.jake.check.impl.groundspoof

import eu.mikart.jake.Jake
import eu.mikart.jake.check.Check
import eu.mikart.jake.check.CheckCategory
import eu.mikart.jake.check.CheckInfo
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData
import org.bukkit.Bukkit
import org.bukkit.Material
import kotlin.math.abs

// also a bad check
@CheckInfo(name = "GroundSpoof", type = "A", description = "Detects fabricated onGround", category = CheckCategory.MOVEMENT)
class GroundSpoofA(plugin: Jake, data: PlayerData) : Check(plugin, data) {
	override fun onPacket(packet: PacketWrapper) {
		if (!packet.isPosition()) return
		val player = Bukkit.getPlayer(data.uuid) ?: return

	    val clientGround = player.isOnGround()

		val inVehicle = player.isInsideVehicle
		val allowFlight = player.allowFlight
		val isGliding = player.isGliding
		val inLiquid = player.location.block.type == Material.WATER || player.location.block.type == Material.LAVA
		val exempt = inVehicle || allowFlight || isGliding || inLiquid

		if (!exempt) {
            decreaseBuffer()
		} else decreaseBuffer()

		data.lastGround = clientGround
	}
	override fun setback() {
		val p = Bukkit.getPlayer(data.uuid) ?: return
		p.teleport(p.location.add(0.0, -0.25, 0.0))
	}
}