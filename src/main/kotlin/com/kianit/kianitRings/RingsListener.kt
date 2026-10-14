package com.kianit.kianitRings

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

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
    fun onPlayerAttemptPickupItem(event: PlayerAttemptPickupItemEvent) {
        if (isBlocked(event.item.itemStack, event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerItemHeld(event: PlayerItemHeldEvent) {
        if (isBlocked(event.player.inventory.getItem(event.newSlot), event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerSwapHandItems(event: PlayerSwapHandItemsEvent) {
        if (isBlocked(event.mainHandItem, event.player) || isBlocked(event.offHandItem, event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return

        val items = mutableListOf(event.currentItem, event.cursor)
        if (event.hotbarButton >= 0) {
            items.add(player.inventory.getItem(event.hotbarButton))
        }
        if (event.click == ClickType.SWAP_OFFHAND) {
            items.add(player.inventory.itemInOffHand)
        }

        if (items.any { isBlocked(it, player) }) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val player = event.whoClicked as? Player ?: return
        if (isBlocked(event.oldCursor, player)) {
            event.isCancelled = true
        }
    }

    private fun isBlocked(item: ItemStack?, player: Player) : Boolean {
        if (item == null || item.isEmpty) {
            return false
        }
        return ringManager.isRing(item) && !ringManager.isPlayerHasAccess(item, player)
    }
}
