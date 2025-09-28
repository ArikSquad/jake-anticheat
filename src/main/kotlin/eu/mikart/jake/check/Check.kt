package eu.mikart.jake.check

import eu.mikart.jake.Jake
import eu.mikart.jake.check.impl.aura.KillAuraB
import eu.mikart.jake.check.impl.aura.KillAuraA
import eu.mikart.jake.check.impl.groundspoof.GroundSpoofA
import eu.mikart.jake.check.impl.movement.FlyingRateA
import eu.mikart.jake.check.impl.protocol.PitchLimitA
import eu.mikart.jake.check.impl.movement.SpeedA
import eu.mikart.jake.check.impl.movement.RotationRatioA
import eu.mikart.jake.check.impl.protocol.FastUseA
import eu.mikart.jake.check.impl.protocol.KeepAliveOrderA
import eu.mikart.jake.check.impl.protocol.PacketFrequencyA
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData
import net.kyori.adventure.text.Component
import java.util.*
import kotlin.math.max

abstract class Check(val plugin: Jake, val data: PlayerData) {
	protected val info = this::class.annotations.filterIsInstance<CheckInfo>().first()
	private var vl = 0.0
	private var buffer = 0.0
	protected fun increase(amount: Double = 1.0) { vl += amount }
	protected fun decrease(amount: Double = 0.01) { vl = max(0.0, vl - amount) }
	protected fun increaseBuffer(by: Double = 1.0): Double { buffer += by; return buffer }
	protected fun decreaseBuffer(by: Double = 0.25) { buffer = max(0.0, buffer - by) }
	fun handle(packet: PacketWrapper) { onPacket(packet) }
	protected abstract fun onPacket(packet: PacketWrapper)
	protected fun fail(reduction: Double = .5, detail: String = "") {
		increase()
		data.violations[info.name] = vl
		if (reduction > 0) buffer *= reduction
		val msg = Component.text("${info.name} ${info.type}${if (info.complexType.isNotEmpty()) "-${info.complexType}" else ""} VL ${"%.1f".format(vl)} $detail")
		plugin.network.broadcastAlert(msg)
		if (info.setback) setback()
	}
	open fun setback() {}
}

object CheckRegistry {
	private val checks = mutableMapOf<UUID, MutableList<Check>>()
	private lateinit var plugin: Jake
	fun init(p: Jake) { plugin = p }
	fun handle(uuid: UUID, packet: PacketWrapper) {
		val list = checks.computeIfAbsent(uuid) { mutableListOf() }
		if (list.isEmpty()) list.addAll(buildChecks(uuid))
		list.forEach { it.handle(packet) }
	}
	private fun buildChecks(uuid: UUID): List<Check> {
		val data = plugin.players.get(uuid)
		return listOf(
            GroundSpoofA(plugin, data),
            SpeedA(plugin, data),
            PitchLimitA(plugin, data),
            RotationRatioA(plugin, data),
            PacketFrequencyA(plugin, data),
            KeepAliveOrderA(plugin, data),
            FlyingRateA(plugin, data),
            KillAuraA(plugin, data),
            KillAuraB(plugin, data),
            FastUseA(plugin, data)
		)
	}
	fun remove(uuid: UUID) { checks.remove(uuid) }
}




