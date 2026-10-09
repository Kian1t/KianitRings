package com.kianit.kianitRings

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CommandManager(private val plugin: KianitRings): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name != "rings") {
            return false
        }
        if (args.isEmpty()) {
            return false
        }
        if (args[0] != "reload") {
            return false
        }

        plugin.saveDefaultConfig()
        plugin.reloadConfig()
        sender.sendMessage("Конфиг перезагружен")
        plugin.ringsConfig.load()
        return true
    }
}