package eu.mikart.jake.network

import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon
import org.bukkit.entity.Player

class PacketWrapper private constructor(
    val player: Player?,
    val packetType: PacketTypeCommon?,
    val packetId: Int,
    val receiving: Boolean
) {
    companion object {
        fun fromReceive(e: PacketReceiveEvent) = PacketWrapper(e.getPlayer(), e.packetType, e.packetId,true)
        fun fromSend(e: PacketSendEvent) = PacketWrapper(e.getPlayer(), e.packetType, e.packetId, false)
    }
    fun isPosition(): Boolean = receiving && (packetType == PacketType.Play.Client.PLAYER_POSITION || packetType == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION)
    fun isRotation(): Boolean = receiving && (packetType == PacketType.Play.Client.PLAYER_ROTATION || packetType == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION)
    fun isArmAnimation(): Boolean = receiving && (packetType == PacketType.Play.Client.ANIMATION)
    fun isBlockDig(): Boolean = receiving && (packetType == PacketType.Play.Client.PLAYER_DIGGING)
    fun isFlying(): Boolean = receiving && (packetType == PacketType.Play.Client.PLAYER_FLYING || packetType == PacketType.Play.Client.PLAYER_POSITION || packetType == PacketType.Play.Client.PLAYER_ROTATION || packetType == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION)
    fun isKeepAlive(): Boolean = receiving && (packetType == PacketType.Play.Client.KEEP_ALIVE)
}
