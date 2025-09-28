package eu.mikart.jake.check.impl.protocol

import eu.mikart.jake.Jake
import eu.mikart.jake.check.Check
import eu.mikart.jake.check.CheckCategory
import eu.mikart.jake.check.CheckInfo
import eu.mikart.jake.network.PacketWrapper
import eu.mikart.jake.service.PlayerData
import org.bukkit.Bukkit
import org.bukkit.Material

@CheckInfo(name = "FastUse", type = "A", complexType = "Delay", category = CheckCategory.PACKET, description = "Too fast item use")
class FastUseA(plugin: Jake, data: PlayerData) : Check(plugin, data) {
    private var lastUse = 0L
    override fun onPacket(packet: PacketWrapper) {
        val p = Bukkit.getPlayer(data.uuid) ?: return
        if (!packet.isItemUse() || !packet.isBlockDig()) return

        val item = p.inventory.itemInMainHand.type
        val rapidUse = when (item) {
            Material.ENDER_PEARL,
            Material.SNOWBALL,
            Material.EGG,
            Material.SPLASH_POTION,
            Material.LINGERING_POTION,
            Material.EXPERIENCE_BOTTLE,
            Material.TRIDENT,
            Material.FIREWORK_ROCKET -> true
            else -> false
        }
        if (!rapidUse) return

        if (p.getCooldown(item) > 0) return

        val now = System.currentTimeMillis()
        val diff = now - lastUse
        if (lastUse != 0L) {
            if (diff < 95) {
                if (increaseBuffer() > 3) fail(detail = "diff=${diff}ms item=$item")
            } else decreaseBuffer(0.25)
        }
        lastUse = now
    }
}