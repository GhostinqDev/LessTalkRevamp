package me.Ghostinq.lessTalk.Commands;

import me.Ghostinq.lessTalk.FileManip.messagesFile;
import me.Ghostinq.lessTalk.Functions.staffChat;
import me.Ghostinq.lessTalk.LessTalk;
import me.Ghostinq.lessTalk.Util.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class staffChatCommand implements CommandExecutor {

    private static boolean getModuleStatus(String module) {
        return LessTalk.getInstance().getConfig().getBoolean("module_toggle." + module);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {

        String prefix = LessTalk.getInstance().getConfig().getString("prefix");
        String disabledCMD = messagesFile.getString("disabled_module");

        Player p = (Player) sender;
        if (p.hasPermission("LessTalk.Staff")) {
            if (getModuleStatus("staff_chat")) {
                staffChat.toggleChat(p);
            } else {
                p.sendMessage(ChatColor.format(prefix + " " + disabledCMD));
                return true;
            }
        }

        return true;
    }
}
