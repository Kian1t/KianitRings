package com.kianit.kianitRings

import dev.lone.itemsadder.api.CustomStack
import org.bukkit.inventory.ItemStack

class RingManager(private var plugin: KianitRings) {
    fun isRing(item: ItemStack) : Boolean {
        if (CustomStack.byItemStack(item) == null) {
            return false
        }

        var customItem = CustomStack.byItemStack(item)
        if (customItem?.namespacedID in plugin.ringsConfig.ringsIds) {
            return true
        } else {
            return false
        }
    }

    fun isRingPrivated(item: ItemStack) : Boolean {
        if (!isRing(item)) {
            return false
        }
        if ("Партнер" in item.lore.toString()) {
            return true
        } else {
            return false
        }
    }


}