package com.kianit.kianitRings

import org.bukkit.plugin.java.JavaPlugin

class KianitRings : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()
        getCommand("rings")?.setExecutor(CommandManager(this))
        ringsConfig = RingsConfig(this)
        ringsConfig.load()

        server.pluginManager.registerEvents(RingsListener(this, RingManager(this)), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    lateinit var ringsConfig: RingsConfig
            private set
}
