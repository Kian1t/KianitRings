package com.kianit.kianitRings

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInputEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class RingsListener(private var plugin: KianitRings, private var ringManager: RingManager) : Listener {
    @EventHandler
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        if (!event.action.isRightClick) {
            return
        }

        if (!event.player.isSneaking) {
            return
        }

        if (event.hand == EquipmentSlot.OFF_HAND) {
            return
        }

        if (event.item == null) {
            return
        }

        if (!ringManager.isRing(event.item!!)) {
            return
        }

        if (event.player.inventory.itemInOffHand.type == Material.PAPER && !ringManager.isRingPrivated(event.item!!)) {
            if (!event.player.inventory.itemInOffHand.itemMeta.hasDisplayName()) {
                return
            }

            var item = event.player.inventory.itemInMainHand
            var meta = item.itemMeta

            meta.lore?.add(event.player.inventory.itemInOffHand.displayName().toString())
            event.player.inventory.itemInMainHand.setItemMeta(meta)
            event.player.inventory.itemInOffHand.amount -= 1
        }
    }
}