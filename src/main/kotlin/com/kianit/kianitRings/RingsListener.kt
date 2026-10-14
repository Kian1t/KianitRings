package com.kianit.kianitRings

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class RingsListener(private val plugin: KianitRings, private val ringManager: RingManager) : Listener {
    @EventHandler
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        if (!event.action.isRightClick) {
            return
        }

        if (event.hand != EquipmentSlot.HAND) {
            return
        }

        val ring = event.item ?: return
        if (!ringManager.isRing(ring)) {
            return
        }

        val player = event.player
        val offHand = player.inventory.itemInOffHand
        if (offHand.type != Material.PAPER && offHand.type != Material.DIAMOND) {
            return
        }

        event.isCancelled = true

        if (ringManager.isRingPrivated(ring)) {
            player.sendMessage("Это кольцо уже подписано")
            return
        }

        val text = offHand.itemMeta.displayName()
        if (text == null) {
            player.sendMessage("Предмет в левой руке должен быть переименован")
            return
        }

        if (offHand.type == Material.PAPER) {
            player.inventory.setItemInMainHand(ringManager.addRingLore(ring, text))
            player.sendMessage("Описание добавлено")
        } else {
            player.inventory.setItemInMainHand(ringManager.privateRing(ring, text, player))
            player.sendMessage("Кольцо подписано")
        }
        offHand.amount -= 1
    }
}
