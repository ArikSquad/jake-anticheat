package eu.mikart.jake.service

import java.util.*
import java.util.concurrent.ConcurrentHashMap

data class PlayerData(
    val uuid: UUID,
    var lastGround: Boolean = false,
    var lastDeltaY: Double = 0.0,
    var lastMoveTime: Long = 0L,
    var violations: MutableMap<String, Double> = ConcurrentHashMap()
)

class PlayerDataService(private val plugin: eu.mikart.jake.Jake) {
    private val data = ConcurrentHashMap<UUID, PlayerData>()
    private val alerts = ConcurrentHashMap<UUID, Boolean>()

    fun get(uuid: UUID): PlayerData = data.computeIfAbsent(uuid) { PlayerData(it) }
    fun remove(uuid: UUID) { data.remove(uuid); alerts.remove(uuid) }

    fun toggleAlerts(uuid: UUID): Boolean {
        val newState = !(alerts[uuid] ?: false)
        alerts[uuid] = newState
        return newState
    }

    fun setAlerts(uuid: UUID, enabled: Boolean) {
        alerts[uuid] = enabled
    }

    fun alertsEnabled(uuid: UUID): Boolean = alerts[uuid] ?: true
}
