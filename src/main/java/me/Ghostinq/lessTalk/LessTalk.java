package me.Ghostinq.lessTalk;

import me.Ghostinq.lessTalk.Commands.generalCommands;
import me.Ghostinq.lessTalk.Commands.staffChatCommand;
import me.Ghostinq.lessTalk.Events.*;
import me.Ghostinq.lessTalk.FileManip.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public final class LessTalk extends JavaPlugin {

    private static LessTalk instance;

    @Override
    public void onEnable() {

        instance = this;



        getConfig().options().copyDefaults();
        saveDefaultConfig();

        try {
            // After restart, all config files load.
            broadcastFile.Load();
            lockedMessagesFile.Load();
            mutedPlayerFile.Load();
            messagesFile.Load();
            mutedFile.Load();
            warnsFile.Load();
            discordConfigFile.Load();
            banScreenFile.Load();

            Bukkit.getServer().getLogger().log(Level.INFO, "Configuration Files loaded.");

        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        // Register Plugin Events.
        getServer().getPluginManager().registerEvents(new Chat(), this);
        getServer().getPluginManager().registerEvents(new staffChatEvent(), this);
        getServer().getPluginManager().registerEvents(new duplicateMessage(), this);
        getServer().getPluginManager().registerEvents(new checkMessage(), this);
        getServer().getPluginManager().registerEvents(new slowMode(), this);
        getServer().getPluginManager().registerEvents(new checkMute(), this);

        getCommand("lesstalk").setExecutor(new generalCommands());
        getCommand("staffchat").setExecutor(new staffChatCommand());

        Bukkit.getServer().getLogger().log(Level.INFO, "LessTalk has loaded successfully! Enjoy!");
        Bukkit.getServer().getLogger().log(Level.INFO, "If there are any bugs, please report them in our Discord Server!");

    }

    @Override
    public void onDisable() {
        broadcastFile.Save();
        lockedMessagesFile.Save();
        mutedPlayerFile.Save();
        messagesFile.Save();
        mutedFile.Save();
        discordConfigFile.Save();
        banScreenFile.Save();
    }

    public static LessTalk getInstance() {
        return instance;
    }
}
