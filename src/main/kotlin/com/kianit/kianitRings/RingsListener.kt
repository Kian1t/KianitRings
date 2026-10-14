package com.kianit.kianitRings

import io.papermc.paper.command.brigadier.argument.ArgumentTypes.player
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerPickupItemEvent
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

        if (!ringManager.isPlayerHasAccess(ring, event.player)) {
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

    @EventHandler
    fun onPlayerPickupItem(event: PlayerPickupItemEvent) {
        if (!ringManager.isRing(event.item.itemStack)) {
            return
        }

        val player = event.player
        val item = event.item.itemStack

        if (!ringManager.isPlayerHasAccess(item, player)) {
            event.isCancelled = true
            return
        }
    }

    @EventHandler
    fun onPlayerItemHeldEvent(event: PlayerItemHeldEvent) {
        val player = event.player
        val item = event.player.inventory.itemInMainHand
        if (!ringManager.isRing(item)) {
            return
        }

        if (!ringManager.isPlayerHasAccess(item, player)) {
            event.isCancelled = true
            return
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val item = event.currentItem ?: return

        if (!ringManager.isRing(item)) {
            return
        }
        if (!ringManager.isPlayerHasAccess(item, player)) {
            event.isCancelled = true
            return
        }
    }


}
