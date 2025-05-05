package cc.carm.plugin.userprefix.conf.gui;

import cc.carm.lib.configuration.source.section.ConfigureSection;
import cc.carm.lib.easyplugin.gui.configuration.GUIActionConfiguration;
import cc.carm.lib.easyplugin.gui.configuration.GUIActionType;
import cc.carm.lib.easyplugin.gui.configuration.GUIConfiguration;
import cc.carm.lib.easyplugin.gui.configuration.GUIItemConfiguration;
import cc.carm.lib.mineconfiguration.bukkit.source.BukkitSection;
import cc.carm.plugin.userprefix.Main;
import org.bukkit.Material;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GUIItems {

    protected final Map<String, GUIItemConfiguration> items;

    public GUIItems(Map<String, GUIItemConfiguration> items) {
        this.items = items;
    }

    public Map<String, GUIItemConfiguration> getItems() {
        return items;
    }

    public Map<String, Object> serialize() {
        return items.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().serialize(), (a, b) -> b,
                LinkedHashMap::new
        ));
    }

    public static GUIItems parse(ConfigureSection section) {
        if (!(section instanceof BukkitSection)) {
            Main.severe("Unknown configure source, it should never happened!");
            Main.severe("Required: " + BukkitSection.class.getSimpleName());
            Main.severe("Found: " + section.getClass().getSimpleName());
            return new GUIItems(new LinkedHashMap<>());
        }
        BukkitSection craft = (BukkitSection) section;
        return new GUIItems(GUIConfiguration.readItems(craft.data()));
    }

    public static GUIItems defaults() {
        LinkedHashMap<String, GUIItemConfiguration> map = new LinkedHashMap<>();
        map.put("back", new GUIItemConfiguration(
                Material.BARRIER, 1, 0, "&c&l返回",
                Collections.singletonList("&f点击即可返回上一菜单"),
                Collections.singletonList(GUIActionConfiguration.of(GUIActionType.CHAT, "/menu")),
                Collections.singletonList(49)
        ));
        return new GUIItems(map);
    }

}
