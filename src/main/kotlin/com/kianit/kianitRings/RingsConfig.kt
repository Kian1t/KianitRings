package com.kianit.kianitRings

class RingsConfig(private val plugin: KianitRings) {
    var ringsIds: Set<String> = emptySet()
        private set

    fun load() {
        ringsIds = plugin.config.getStringList("rings").toSet()

    }



}