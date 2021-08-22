package cc.carm.plugin.userprefix.util.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class GUIItem {

    ItemStack display;
    boolean actionActive = true;

    public Set<GUIClickAction> actions = new HashSet<>();
    public Set<GUIClickAction> actionsIgnoreActive = new HashSet<>();

    public GUIItem(ItemStack display) {
        this.display = display;
    }

    public final ItemStack getDisplay() {
        return this.display;
    }

    public final void setDisplay(ItemStack display) {
        this.display = display;
    }

    public final boolean isActionActive() {
        return this.actionActive;
    }

    public final void setActionActive(boolean b) {
        actionActive = b;
    }

    /**
     * 玩家点击GUI后执行的代码
     *
     * @param type   点击的类型
     * @param player 点击GUI的玩家
     */
    @Deprecated
    public void ClickAction(ClickType type, Player player) {

    }

    /**
     * 玩家点击GUI后执行的代码
     *
     * @param type 点击的类型
     */
    public void onClick(ClickType type) {

    }

    public void addClickAction(GUIClickAction action) {
        actions.add(action);
    }

    public void addActionIgnoreActive(GUIClickAction action) {
        actionsIgnoreActive.add(action);
    }

    public void customAction() {

    }

    public void rawClickAction(InventoryClickEvent event) {

    }

    public void realRawClickAction(InventoryClickEvent event) {

    }

    public void customAction(Object obj) {

    }

    /**
     * 玩家点击GUI后执行的代码
     *
     * @param player 点击GUI的玩家
     */
    public void customAction(Player player) {

    }

    public abstract static class GUIClickAction {
        public abstract void run(ClickType type, Player player);
    }

}
