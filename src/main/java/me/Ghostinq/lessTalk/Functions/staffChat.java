package me.Ghostinq.lessTalk.Functions;

import me.Ghostinq.lessTalk.FileManip.messagesFile;
import me.Ghostinq.lessTalk.Util.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class staffChat {

    private static ArrayList<Player> staffChat = new ArrayList<>();

    public static boolean containsPlayer(Player p) {
        return staffChat.contains(p);
    }

    public static void toggleChat(Player p) {

        String enabled = messagesFile.getString("staff_chat");
        String disabled = messagesFile.getString("staff_chat_disable");

        if (containsPlayer(p)) {
            staffChat.remove(p);
            p.sendMessage(ChatColor.format(disabled));
        } else {
            staffChat.add(p);
            p.sendMessage(ChatColor.format(enabled));
        }
    }

}
