package cc.carm.plugin.userprefix.ui;

import cc.carm.plugin.userprefix.configuration.PrefixConfig;
import cc.carm.plugin.userprefix.util.MessageUtil;
import cc.carm.plugin.userprefix.util.gui.GUIType;
import cc.carm.plugin.userprefix.manager.PrefixManager;
import cc.carm.plugin.userprefix.manager.UserManager;
import cc.carm.plugin.userprefix.model.ConfiguredPrefix;
import cc.carm.plugin.userprefix.util.gui.AutoPagedGUI;
import cc.carm.plugin.userprefix.util.gui.GUIItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PrefixSelectGUI extends AutoPagedGUI {

    public static HashSet<Player> openingUsers = new HashSet<>();

    Player player;

    public PrefixSelectGUI(Player player) {
        super(GUIType.SIXBYNINE, "&f&l我的前缀 &8| 列表", 10, 43);
        this.player = player;

        setPreviousPageSlot(18);
        setNextPageSlot(26);

        loadItems();
    }

    public Player getPlayer() {
        return player;
    }

    public void loadItems() {
        List<ConfiguredPrefix> prefixList = new ArrayList<>();
        prefixList.add(PrefixManager.getDefaultPrefix());
        prefixList.addAll(PrefixManager.getVisiblePrefix());

        ConfiguredPrefix usingPrefix = UserManager.getPrefix(getPlayer());

        for (ConfiguredPrefix prefix : prefixList) {
            if (prefix.getIdentifier().equals(usingPrefix.getIdentifier())) {
                addItem(new GUIItem(prefix.getItemWhenUsing() != null ? prefix.getItemWhenUsing() : prefix.getItemHasPermission()));
            } else if (UserManager.isPrefixUsable(player, prefix)) {
                addItem(new GUIItem(prefix.getItemHasPermission()) {
                    @Override
                    public void onClick(ClickType type) {
                        if (UserManager.isPrefixUsable(player, prefix)) { //再次检查，防止打开GUI后、选择前的时间段内权限消失
                            player.closeInventory();
                            UserManager.setPrefix(player, prefix, true);

                            PrefixConfig.Sounds.PREFIX_CHANGE.play(player);
                            MessageUtil.sendWithPlaceholders(player, PrefixConfig.Messages.SELECTED.get(),
                                    new String[]{"%(name)"},
                                    new Object[]{prefix.getName()});

                        }
                    }
                });
            } else {
                addItem(new GUIItem(prefix.getItemNoPermission()));
            }
        }


    }

    @Override
    public void onClose() {
        openingUsers.remove(player);
    }

    public static void removeOpening(Player player) {
        openingUsers.remove(player);
    }

    public static void closeAll() {
        for (Player player : new HashSet<>(openingUsers)) {
            player.closeInventory();
        }
    }

    public static void open(Player player) {
        PrefixConfig.Sounds.GUI_OPEN.play(player);
        new PrefixSelectGUI(player).openGUI(player);
        openingUsers.add(player);
    }


}
