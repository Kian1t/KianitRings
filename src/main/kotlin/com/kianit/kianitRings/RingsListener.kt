package com.kianit.kianitRings

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInputEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import com.kianit.kianitRings.RingManager
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class RingsListener(private var plugin: KianitRings) : Listener {
    @EventHandler
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        if (event.hand == EquipmentSlot.OFF_HAND) {
            return
        }

        if (event.item == null) {
            return
        }

        if (!RingManager(plugin).isRing(event.item!!)) {
            return
        }

        if (event.player.inventory.itemInOffHand == ItemStack(Material.PAPER)) {
            var item = event.player.inventory.itemInMainHand
            var meta = item.itemMeta

            meta.lore?.add(event.player.inventory.itemInOffHand.displayName().toString())
            event.player.inventory.itemInMainHand.setItemMeta(meta)
            event.player.inventory.itemInOffHand.amount -= 1
        }
    }
}