package me.Ghostinq.lessTalk.FileManip;

import me.Ghostinq.lessTalk.LessTalk;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class warnsFile {

    private static File file;
    private static YamlConfiguration config;

    public static void addWarn(Player p, String reason) {

        ConfigurationSection players = config.getConfigurationSection("players");
        if (players == null) {
            players = config.createSection("players");
        }

        ConfigurationSection player = players.getConfigurationSection(p.getName());
        if (player == null) {
            player = players.createSection(p.getName());
            player.set("UUID", p.getUniqueId().toString());
            player.set("warn_amount", 0);
        }

        int warnAmount = player.getInt("warn_amount") + 1;

        player.set("warn_amount", warnAmount);
        player.set("warn_reasons.Warn_" + warnAmount, reason);

        Save();
    }


    public static Integer getWarnAmount(Player p) {
        ConfigurationSection playerSection = config.getConfigurationSection("players");
        if (playerSection == null) {
            Bukkit.getServer().getLogger().log(Level.SEVERE, "Failed to load Warns.yml, please make sure you have the most recent update!");
        }
        if (playerSection.contains(p.getName())) {
            ConfigurationSection targetSect = config.getConfigurationSection("players." + p.getName());
            if (targetSect == null) return 0;
            return (targetSect.getInt("warn_amount"));
        }
        return 0;
    }

    public static void Save() {
        file = new File(LessTalk.getInstance().getDataFolder(), "Players/Warns.yml");

        try {
            config.save(file);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Load() throws IOException, InvalidConfigurationException {
        file = new File(LessTalk.getInstance().getDataFolder(), "Players/Warns.yml");
        config = new YamlConfiguration();
        config.options().copyDefaults(true);
        if (!file.exists()) LessTalk.getInstance().saveResource("Players/Warns.yml", false);
        config.load(file);
    }

}
