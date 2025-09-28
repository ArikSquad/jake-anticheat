package eu.mikart.jake

import com.github.retrooper.packetevents.PacketEvents
import eu.mikart.jake.check.CheckRegistry
import eu.mikart.jake.network.PacketListener
import eu.mikart.jake.service.NetworkService
import eu.mikart.jake.service.PlayerDataService
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import org.bukkit.plugin.java.JavaPlugin

class Jake : JavaPlugin() {
    companion object { var instance: Jake? = null }
    lateinit var players: PlayerDataService
        private set
    lateinit var network: NetworkService
        private set

    override fun onEnable() {
        instance = this
        players = PlayerDataService(this)
        network = NetworkService(this)

        val packetEvents = SpigotPacketEventsBuilder.build(this)
        PacketEvents.setAPI(packetEvents)
        packetEvents.settings.checkForUpdates(false)
        PacketEvents.getAPI().load()
        server.pluginManager.registerEvents(network, this)
        packetEvents.eventManager.registerListener(PacketListener(this))
        packetEvents.init()

        CheckRegistry.init(this)
    }

    override fun onDisable() {
        PacketEvents.getAPI().terminate()
        instance = null
    }
}
