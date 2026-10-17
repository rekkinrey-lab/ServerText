package com.example.servertext;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

public class ServerText extends JavaPlugin {

    private final MiniMessage mm = MiniMessage.miniMessage();
    private BukkitTask task;
    private boolean papi;
    private String text;
    private Set<String> worlds = new HashSet<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        papi = getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        start();
    }

    @Override
    public void onDisable() {
        if (task != null) task.cancel();
    }

    private void start() {
        if (task != null) task.cancel();
        reloadConfig();
        text = getConfig().getString("text", "<gray>naw.regionpvp.com | 25907");
        long interval = Math.max(1, getConfig().getLong("update-ticks", 20));
        worlds = new HashSet<>(getConfig().getStringList("enabled-worlds"));

        task = getServer().getScheduler().runTaskTimer(this, () -> {
            for (Player p : getServer().getOnlinePlayers()) {
                if (!worlds.isEmpty() && !worlds.contains(p.getWorld().getName())) continue;
                p.sendActionBar(mm.deserialize(render(p)));
            }
        }, 20L, interval);
    }

    private String render(Player p) {
        String out = text
                .replace("%player%", p.getName())
                .replace("%online%", String.valueOf(getServer().getOnlinePlayers().size()))
                .replace("%max%", String.valueOf(getServer().getMaxPlayers()))
                .replace("%ping%", String.valueOf(p.getPing()))
                .replace("%world%", p.getWorld().getName());
        if (papi) out = PlaceholderAPI.setPlaceholders(p, out);
        return out;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("servertext.admin")) {
            sender.sendMessage("You don't have permission.");
            return true;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            start();
            sender.sendMessage("ServerText reloaded.");
            return true;
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("set")) {
            String newText = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
            getConfig().set("text", newText);
            saveConfig();
            start();
            sender.sendMessage("Text updated.");
            return true;
        }
        sender.sendMessage("Usage: /" + label + " reload | set <text>");
        return true;
    }
}
