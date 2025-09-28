package eu.mikart.jake

import com.mojang.brigadier.Command
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.text.Component

class JakeBootstrap : PluginBootstrap {
    override fun bootstrap(context: BootstrapContext) {
        context.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val alertsNode = Commands.literal("alerts")
                .executes { ctx ->
                    val sender = ctx.source.sender
                    val plugin = Jake.instance ?: return@executes Command.SINGLE_SUCCESS
                    if (sender.hasPermission("jake.alerts") && sender is org.bukkit.entity.Player) {
                        val toggled = plugin.players.toggleAlerts(sender.uniqueId)
                        sender.sendMessage(plugin.network.alertPrefix().append(if (toggled) Component.text(" Now receiving alerts.") else Component.text(" Alerts disabled.")))
                    }
                    Command.SINGLE_SUCCESS
                }
                .build()
            event.registrar().register(alertsNode)
        }
    }
}
