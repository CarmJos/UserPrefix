package cc.carm.plugin.userprefix.util.gui;

import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.util.ColorParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUI {

    private static final HashMap<Player, GUI> openedGUIs = new HashMap<>();

    GUIType type;
    String name;
    public GUIItem[] items;
    public Inventory inv;

    boolean setCancelledIfClickOnTarget = true;
    boolean setCancelledIfClickOnSelf = true;
    boolean setCancelledIfClickOnOuter = true;

    Map<String, Object> flags;

    public Listener listener;

    public GUI(GUIType type, String name) {
        this.type = type;
        this.name = ColorParser.parseColor(name);
        switch (type) {
            case ONEBYNINE:
                this.items = new GUIItem[9];
                break;
            case TWOBYNINE:
                this.items = new GUIItem[18];
                break;
            case THREEBYNINE:
                this.items = new GUIItem[27];
                break;
            case FOURBYNINE:
                this.items = new GUIItem[36];
                break;
            case FIVEBYNINE:
                this.items = new GUIItem[45];
                break;
            default:
            case SIXBYNINE:
                this.items = new GUIItem[54];
                break;
            case CANCEL:
                this.items = null;
        }
    }


    public final void setItem(int index, GUIItem item) {
        if (item == null) {
            this.items[index] = new GUIItem(new ItemStack(Material.AIR));
        } else {
            this.items[index] = item;
        }
    }

    /**
     * 批量添加GUI Item
     *
     * @param item
     * @param index
     */
    public void setItem(GUIItem item, int... index) {
        for (int i : index) {
            setItem(i, item);
        }
    }

    public GUIItem getItem(int index) {
        return this.items[index];
    }

    /**
     * 更新玩家箱子的视图
     */
    public void updateView() {
        if (this.inv != null) {
            List<HumanEntity> viewers = this.inv.getViewers();
            for (int index = 0; index < this.items.length; index++) {
                if (items[index] == null) {
                    inv.setItem(index, new ItemStack(Material.AIR));
                } else {
                    inv.setItem(index, items[index].display);
                }
            }
            for (HumanEntity p : viewers) {
                ((Player) p).updateInventory();
            }
        }
    }

    /**
     * 设置是否取消点击GUI内物品的事件
     * 如果不取消，玩家可以从GUI中拿取物品。
     *
     * @param b 是否取消
     */
    public void setCancelledIfClickOnTarget(boolean b) {
        this.setCancelledIfClickOnTarget = b;
    }

    /**
     * 设置是否取消点击自己背包内物品的事件
     * 如果不取消，玩家可以从自己的背包中拿取物品。
     *
     * @param b 是否取消
     */
    public void setCancelledIfClickOnSelf(boolean b) {
        this.setCancelledIfClickOnSelf = b;
    }

    /**
     * 设置是否取消点击GUI外的事件
     * 如果不取消，玩家可以把物品从GUI或背包中丢出去
     *
     * @param b 是否取消
     */
    public void setCancelledIfClickOnOuter(boolean b) {
        this.setCancelledIfClickOnOuter = b;
    }

    public void addFlag(String flag, Object obj) {
        if (this.flags == null) this.flags = new HashMap<>();
        this.flags.put(flag, obj);
    }

    public Object getFlag(String flag) {
        if (this.flags == null) return null;
        else
            return this.flags.get(flag);
    }

    public void setFlag(String flag, Object obj) {
        if (this.flags == null) this.flags = new HashMap<>();
        this.flags.replace(flag, obj);
    }

    public void removeFlag(String flag) {
        if (this.flags == null) this.flags = new HashMap<>();
        this.flags.remove(flag);
    }

    public void rawClickListener(InventoryClickEvent event) {
    }

    public void openGUI(Player player) {
        Inventory inv;
        if (this.type == GUIType.CANCEL) {
            throw new NullPointerException("被取消或不存在的GUI");
        }
        switch (type) {
            default:
            case ONEBYNINE:
            case TWOBYNINE:
            case THREEBYNINE:
            case FOURBYNINE:
            case FIVEBYNINE:
            case SIXBYNINE:
                inv = Bukkit.createInventory(null, this.items.length, this.name);
                break;
        }

        for (int index = 0; index < this.items.length; index++) {
            if (items[index] == null) {
                inv.setItem(index, new ItemStack(Material.AIR));
            } else {
                inv.setItem(index, items[index].display);
            }
        }
        setOpenedGUI(player, this);
        this.inv = inv;
        player.openInventory(inv);

        if (listener == null)
            Bukkit.getPluginManager().registerEvents(listener = new Listener() {
                @EventHandler
                public void onInventoryClickEvent(InventoryClickEvent event) {
                    rawClickListener(event);
                    if (!(event.getWhoClicked() instanceof Player)) {
                        return;
                    }
                    Player p = (Player) event.getWhoClicked();
                    if (event.getSlot() != -999) {
                        try {
                            if (getOpenedGUI(p) == GUI.this && event.getClickedInventory() != null && event.getClickedInventory().equals(GUI.this.inv) && GUI.this.items[event.getSlot()] != null)
                                GUI.this.items[event.getSlot()].realRawClickAction(event);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                            System.err.print("err cause by GUI(" + GUI.this.toString() + "), name=" + name);
                            return;
                        }
                    } else {
                        if (setCancelledIfClickOnOuter) event.setCancelled(true);
                    }
                    if (hasOpenedGUI(p) && /*player.openedGUI.inv.equals(event.getClickedInventory())*/ getOpenedGUI(p) == GUI.this && event.getClickedInventory() != null) {
                        if (event.getClickedInventory().equals(GUI.this.inv)) {
                            if (setCancelledIfClickOnTarget) event.setCancelled(true);

                            if (event.getSlot() != -999 && GUI.this.items[event.getSlot()] != null) {
                                if (GUI.this.items[event.getSlot()].isActionActive()) {
                                    GUI.this.items[event.getSlot()].onClick(event.getClick());
                                    GUI.this.items[event.getSlot()].rawClickAction(event);
                                    if (!GUI.this.items[event.getSlot()].actions.isEmpty()) {
                                        for (GUIItem.GUIClickAction action : GUI.this.items[event.getSlot()].actions) {
                                            action.run(event.getClick(), player);
                                        }
                                    }
                                }
                                if (!GUI.this.items[event.getSlot()].actionsIgnoreActive.isEmpty()) {
                                    for (GUIItem.GUIClickAction action : GUI.this.items[event.getSlot()].actionsIgnoreActive) {
                                        action.run(event.getClick(), player);
                                    }
                                }
                            }
                        } else if (event.getClickedInventory().equals(p.getInventory())) {
                            if (setCancelledIfClickOnSelf) event.setCancelled(true);
                        }
                    }
                }

                @EventHandler
                public void onDrag(InventoryDragEvent e) {
                    if (e.getWhoClicked() instanceof Player) {
                        Player p = (Player) e.getWhoClicked();
                        if (e.getInventory().equals(inv) || e.getInventory().equals(p.getInventory())) {
                            GUI.this.onDrag(e);
                        }
                    }
                }

                @EventHandler
                public void onInventoryCloseEvent(InventoryCloseEvent event) {
                    if (event.getPlayer() instanceof Player && event.getInventory().equals(inv)) {
                        Player p = (Player) event.getPlayer();
                        if (event.getInventory().equals(inv)) {
                            HandlerList.unregisterAll(this);
                            listener = null;
                            onClose();
                        }
                    }
                }
            }, Main.getInstance());

    }

    /**
     * 拖动GUI内物品是执行的代码
     *
     * @param event InventoryDragEvent
     */
    public void onDrag(InventoryDragEvent event) {
    }

    /**
     * 关闭GUI时执行的代码
     */
    public void onClose() {
    }


    public static void setOpenedGUI(Player player, GUI gui) {
        openedGUIs.put(player, gui);
    }

    public static boolean hasOpenedGUI(Player player) {
        return openedGUIs.containsKey(player);
    }

    public static GUI getOpenedGUI(Player player) {
        return openedGUIs.get(player);
    }

    public static void removeOpenedGUI(Player player) {
        openedGUIs.remove(player);
    }

}
