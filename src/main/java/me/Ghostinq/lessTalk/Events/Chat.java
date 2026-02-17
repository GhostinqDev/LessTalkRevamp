package me.Ghostinq.lessTalk.Events;

import me.Ghostinq.lessTalk.FileManip.lockedMessagesFile;
import me.Ghostinq.lessTalk.Functions.lockChat;
import me.Ghostinq.lessTalk.Util.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (lockChat.getChatStatus()) {
            String lockedMessage = lockedMessagesFile.getString("locked_chat");
            if (!p.hasPermission("LessTalk.Staff")) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.format(lockedMessage));
            }
        }
    }


}
