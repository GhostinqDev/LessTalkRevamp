package me.Ghostinq.lessTalk.Events;

import me.Ghostinq.lessTalk.FileManip.mutedFile;
import me.Ghostinq.lessTalk.FileManip.mutedPlayerFile;
import me.Ghostinq.lessTalk.Util.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class checkMute implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String muted = mutedPlayerFile.getString("player_is_muted");

        if (mutedFile.containsPlayer(p)) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.format(muted));
        }
    }

}
