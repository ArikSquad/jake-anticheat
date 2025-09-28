package eu.mikart.jake.service

import eu.mikart.jake.Jake
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

class NetworkService(private val plugin: Jake) : Listener {
    private val mm = MiniMessage.miniMessage()

    fun alertPrefix(): Component = mm.deserialize("<gradient:#FC835C:#FCD05C>ᴀʟᴇʀᴛ</gradient>")

    fun broadcastAlert(message: Component) {
        val permission = "jake.alerts"
        for (p in plugin.server.onlinePlayers) {
            if (p.hasPermission(permission) && plugin.players.alertsEnabled(p.uniqueId)) {
                p.sendMessage(alertPrefix().append(Component.space()).append(message))
            }
        }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val uuid: UUID = e.player.uniqueId
        plugin.players.get(uuid)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        plugin.players.remove(e.player.uniqueId)
    }
}
