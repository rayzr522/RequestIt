/**
 * 
 */
package com.rayzr522.requestit.gui;

import static com.rayzr522.requestit.data.RequestState.COMPLETED;
import static com.rayzr522.requestit.data.RequestState.UNVIEWED;
import static com.rayzr522.requestit.data.RequestState.VIEWED;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.rayzr522.creativelynamedlib.gui.Component;
import com.rayzr522.creativelynamedlib.gui.GUI;
import com.rayzr522.creativelynamedlib.utils.types.Point;
import com.rayzr522.requestit.RequestIt;
import com.rayzr522.requestit.data.Request;
import com.rayzr522.requestit.data.RequestState;

/**
 * @author Rayzr
 *
 */
public class RequestGUI {

    public static void open(RequestIt plugin, Player player) {
        GUI gui = new GUI(6, plugin.trRaw("gui.request.title"));

        boolean admin = player.hasPermission("RequestIt.admin");

        List<Request> requests = admin ? plugin.getRequests() : plugin.getRequests().stream().filter(r -> r.getRequester().equals(player.getUniqueId())).collect(Collectors.toList());

        for (int i = 0; i < Math.min(54, requests.size()); i++) {
            Request request = requests.get(i);

            Component component = Component.create()
                    .type(Material.STAINED_CLAY)
                    .colored(request.getState().getColor())
                    .named(plugin.trRaw("gui.request.item.title", request.getShortDescription()))
                    .withLore(plugin.trRaw("gui.request.item.lore", request.getRequest())).build();

            if (admin) {
                component.setClickHandler(e -> showMenu(plugin, player, request));
            }

            gui.add(component, Point.at(i % 9, i / 9));
        }

        gui.open(player);

    }

    private static void showMenu(RequestIt plugin, Player player, Request request) {
        if (request.getState() == UNVIEWED)
            request.setState(VIEWED);

        String username = Bukkit.getOfflinePlayer(request.getRequester()).getName();

        new GUI(4, plugin.trRaw("gui.control.title"))
                .add(
                        Component.create().colored(DyeColor.BLACK).named(" ").ofSize(9, 3).build(),
                        Point.at(0, 0))
                .add(
                        Component.create().type(Material.BOOK)
                                .named(plugin.trRaw("gui.control.info.title", username))
                                .withLore(plugin.trRaw("gui.control.info.lore", request.getRequest())).build(),
                        Point.at(1, 1))
                .add(statusButton(plugin, request, UNVIEWED),
                        Point.at(3, 1))
                .add(statusButton(plugin, request, VIEWED),
                        Point.at(4, 1))
                .add(statusButton(plugin, request, COMPLETED),
                        Point.at(5, 1))

                .add(
                        Component.create().type(Material.TNT)
                                .named(plugin.trRaw("gui.control.delete.title"))
                                .withLore(plugin.trRaw("gui.control.delete.lore"))
                                .onClick(e -> plugin.getRequests().remove(request)).build(),
                        Point.at(7, 1))
                .add(
                        Component.create()
                                .colored(DyeColor.GRAY)
                                .named(plugin.trRaw("gui.control.back.title"))
                                .withLore(plugin.trRaw("gui.control.back.lore"))
                                .ofSize(9, 1).onClick(e -> open(plugin, player)).build(),
                        Point.at(0, 3))
                .open(player);
    }

    private static Component statusButton(RequestIt plugin, Request request, RequestState state) {
        String display = plugin.trRaw("state." + state.name().toLowerCase());
        return Component.create().type(Material.STAINED_GLASS)
                .colored(state.getColor())
                .named(plugin.trRaw("gui.control.status.title", display))
                .withLore(plugin.trRaw("gui.control.status.lore", display))
                .onClick(e -> {
                    request.setState(state);
                    e.closeOnClick(false);
                }).build();

    }

}
