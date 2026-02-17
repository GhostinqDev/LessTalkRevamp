package me.Ghostinq.lessTalk.Functions;

import me.Ghostinq.lessTalk.FileManip.broadcastFile;
import me.Ghostinq.lessTalk.Util.ChatColor;
import me.Ghostinq.lessTalk.Util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class lockChat {

    static boolean locked = false;
    public static boolean getChatStatus() {
        return locked;
    }

    public static void setChatLocked(Player p) {

        String broadcastLock = broadcastFile.getString("server_chat_muted");
        String broadcastUnlock = broadcastFile.getString("server_chat_unmuted");

        if (locked) {
            locked = false;
            Bukkit.getServer().broadcastMessage(ChatColor.format(StringUtil.replaceChar(broadcastUnlock, p)));
        } else {
            locked = true;
            Bukkit.getServer().broadcastMessage(ChatColor.format(StringUtil.replaceChar(broadcastLock, p)));
        }
    }

    public static boolean isChatLocked(Player p) {
        return getChatStatus();
    }

}
