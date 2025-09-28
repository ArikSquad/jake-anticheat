package eu.mikart.jake.network

import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.event.PacketSendEvent
import eu.mikart.jake.Jake
import eu.mikart.jake.check.CheckRegistry
import org.bukkit.entity.Player

class PacketListener(private val plugin: Jake) : PacketListenerAbstract() {
    override fun onPacketReceive(event: PacketReceiveEvent) {
        val p = event.getPlayer<Player>()
        val wrap = PacketWrapper.fromReceive(event)
        CheckRegistry.handle(p.uniqueId, wrap)
    }
    override fun onPacketSend(event: PacketSendEvent) {
        val p = event.getPlayer<Player>() ?: return
        val wrap = PacketWrapper.fromSend(event)
        CheckRegistry.handle(p.uniqueId, wrap)
    }
}
