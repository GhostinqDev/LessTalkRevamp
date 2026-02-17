package me.Ghostinq.lessTalk.FileManip;

import me.Ghostinq.lessTalk.LessTalk;
import me.Ghostinq.lessTalk.Util.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class mutedFile {

    private static File file;
    private static YamlConfiguration config;

    public static void removeMutedPlayer(Player p, Player staff) {
        String prefix = LessTalk.getInstance().getConfig().getString("prefix");
        if (!config.contains(p.getName())) {
            staff.sendMessage(ChatColor.format(prefix + " That player is not muted!"));
        }
        config.set(p.getName(), null);
        Save();
    }

    public static void addMutedPlayer(Player p, Player staff) {
        UUID pUUID = p.getUniqueId();
        config.set(p.getName(), null);
        config.set(p.getName() + ".UUID", pUUID.toString());
        config.set(p.getName() + ".Muted By", staff.getName());
        Save();
    }

    public static boolean containsPlayer(Player p) {
        return config.contains(p.getName());
    }

    public static void Save() {
        file = new File(LessTalk.getInstance().getDataFolder(), "Players/Muted.yml");

        try {
            config.save(file);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Load() throws IOException, InvalidConfigurationException {
        file = new File(LessTalk.getInstance().getDataFolder(), "Players/Muted.yml");
        config = new YamlConfiguration();
        config.options().copyDefaults(true);
        if (!file.exists()) LessTalk.getInstance().saveResource("Players/Muted.yml", false);
        config.load(file);
    }
}
