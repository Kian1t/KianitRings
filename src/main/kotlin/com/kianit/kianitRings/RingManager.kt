package com.kianit.kianitRings

import dev.lone.itemsadder.api.CustomStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class RingManager(private val plugin: KianitRings) {
    val ownerKey = NamespacedKey(plugin, "owner")
    val partnerKey = NamespacedKey(plugin, "partner")

    fun isRing(item: ItemStack) : Boolean {
        val customItem = CustomStack.byItemStack(item) ?: return false
        return customItem.namespacedID in plugin.ringsConfig.ringsIds
    }

    fun isRingPrivated(item: ItemStack) : Boolean {
        return isRing(item) && item.persistentDataContainer.has(ownerKey)
    }

    fun isPlayerHasAccess(item: ItemStack, player: Player) : Boolean {
        val meta = item.itemMeta ?: return false

        if (!isRingPrivated(item)) {
            return true
        }

        if (meta.persistentDataContainer.get(ownerKey, PersistentDataType.STRING) == player.uniqueId.toString()) {
            return true
        }

        return meta.persistentDataContainer.get(partnerKey, PersistentDataType.STRING).equals(player.name, ignoreCase = true)
    }

    fun privateRing(item: ItemStack, com: Component, owner: Player) : ItemStack {
        val result = item.clone()
        val meta = result.itemMeta

        val partnerName = PlainTextComponentSerializer.plainText().serialize(com).trim()
        meta.persistentDataContainer.set(ownerKey, PersistentDataType.STRING, owner.uniqueId.toString())
        meta.persistentDataContainer.set(partnerKey, PersistentDataType.STRING, partnerName)

        val name = (meta.displayName() ?: result.effectiveName()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        meta.displayName(name.append(Component.text(" ")).append(com))

        val lore = meta.lore() ?: ArrayList<Component>()
        lore.add(Component.empty())
        val sign = Component.text("Партнер: ").append(com).color(TextColor.color(0x3AB3DA)).decoration(TextDecoration.ITALIC, false)
        lore.add(sign)
        meta.lore(lore)

        result.itemMeta = meta
        return result
    }

    fun addRingLore(item: ItemStack, com: Component) : ItemStack {
        val result = item.clone()
        val meta = result.itemMeta

        val text = com.color(TextColor.color(0x9D9D97)).decoration(TextDecoration.ITALIC, false)
        val lore = meta.lore() ?: ArrayList<Component>()
        lore.add(text)
        meta.lore(lore)

        result.itemMeta = meta
        return result
    }
}
