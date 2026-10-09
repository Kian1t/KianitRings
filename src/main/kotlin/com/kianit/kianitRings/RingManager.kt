package com.kianit.kianitRings

import dev.lone.itemsadder.api.CustomStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.w3c.dom.Text
import java.util.function.Consumer

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
        val key = NamespacedKey(plugin, "privated")
        if (item.persistentDataContainer.has(key)) {
            return true
        } else {
            return false
        }
    }

//    fun privateRing(item: ItemStack) : ItemStack {
//        var result = item.clone()
//        val key = NamespacedKey(plugin, "privated")
//
//        val pdc = item.persistentDataContainer.cop
//        item.editPersistentDataContainer()
//    }

    fun addRingLore(item: ItemStack, com: Component, player: Player) : ItemStack {
        var result = item.clone()
        var meta = item.itemMeta

        var signature = com.color(TextColor.color(0x9D9D97))
        var lore = meta.lore()
        if (lore == null) {
            lore = ArrayList<Component>()
        }

        lore.add(signature)
        meta.lore(lore)
        result.setItemMeta(meta)
        return result

    }


}