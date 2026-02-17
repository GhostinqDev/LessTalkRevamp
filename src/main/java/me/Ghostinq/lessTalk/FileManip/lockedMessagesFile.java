package me.Ghostinq.lessTalk.FileManip;

import me.Ghostinq.lessTalk.LessTalk;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class lockedMessagesFile {

    private static File file;
    private static YamlConfiguration config;

    public static void reloadConfig() throws IOException, InvalidConfigurationException {
        config.load(file);
    }

    public static String getString(String path) {
        return config.getString(path);
    }

    public static void Save() {
        file = new File(LessTalk.getInstance().getDataFolder(), "ChatMessages/LockedMessages.yml");

        try {
            config.save(file);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void Load() throws IOException, InvalidConfigurationException {
        file = new File(LessTalk.getInstance().getDataFolder(), "ChatMessages/LockedMessages.yml");
        config = new YamlConfiguration();
        config.options().copyDefaults(true);
        if (!file.exists()) LessTalk.getInstance().saveResource("ChatMessages/LockedMessages.yml", false);
        config.load(file);
    }

}
