/**
 * 
 */
package com.rayzr522.requestit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.rayzr522.creativelynamedlib.utils.text.TextUtils;
import com.rayzr522.requestit.data.Request;
import com.rayzr522.requestit.gui.RequestGUI;

/**
 * @author Rayzr
 *
 */
public class ComamndRequest implements CommandExecutor {

    private Map<UUID, Long> cooldowns = new HashMap<>();

    private RequestIt plugin;

    public ComamndRequest(RequestIt plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.tr("command.only-players"));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("RequestIt.use")) {
            player.sendMessage(plugin.tr("no-permission", "RequestIt.use"));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(plugin.tr("command.request.usage"));
            return true;
        }

        String sub = args[0].toLowerCase();

        if (sub.equals("view")) {
            // View requests...
            RequestGUI.open(plugin, player);
        } else {
            // Request a feature
            long now = System.currentTimeMillis();
            long elapsed = now - cooldowns.getOrDefault(player.getUniqueId(), 0L);
            long cooldown = plugin.getConfig().getLong("cooldown");

            if (elapsed < cooldown) {
                player.sendMessage(plugin.tr("command.request.cooldown", TextUtils.format((cooldown - elapsed) / 1000L)));
                return true;
            }

            cooldowns.put(player.getUniqueId(), now);

            String message = Arrays.stream(args).collect(Collectors.joining(" "));

            if (plugin.getRequests().stream().filter(r -> r.getRequest().equalsIgnoreCase(message)).findFirst().isPresent()) {
                // You, or someone else, has already made that (almost) exact request
                player.sendMessage(plugin.tr("command.request.already-exists"));
                return true;
            }

            Request request = new Request(message, player.getUniqueId());
            plugin.getRequests().add(request);

            player.sendMessage(plugin.tr("command.request.accepted"));
        }

        return true;
    }

}
