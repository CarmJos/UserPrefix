package cc.carm.plugin.userprefix.conf.reader;

import cc.carm.lib.configuration.core.source.ConfigurationWrapper;
import cc.carm.lib.easyplugin.gui.configuration.GUIActionConfiguration;
import cc.carm.lib.easyplugin.gui.configuration.GUIItemConfiguration;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GUIItemReader {

    public static @Nullable GUIItemConfiguration readFrom(@Nullable ConfigurationWrapper section) {
        if (section == null) return null;
        String material = Optional.ofNullable(section.getString("type")).orElse("STONE");
        Material type = Optional.ofNullable(Material.matchMaterial(material)).orElse(Material.STONE);
        int data = section.getInt("data", 0);
        int amount = section.getInt("amount", 1);
        String name = section.getString("name");
        List<String> lore = section.getStringList("lore");

        List<Integer> slots = section.getIntegerList("slots");
        int slot = section.getInt("slot", 0);

        List<String> actionsString = section.getStringList("actions");
        List<GUIActionConfiguration> actions = new ArrayList<>();
        for (String actionString : actionsString) {
            GUIActionConfiguration action = GUIActionConfiguration.deserialize(actionString);
            if (action == null) continue;
            actions.add(action);
        }

        return new GUIItemConfiguration(
                type, amount, data, name, lore, actions,
                slots.size() > 0 ? slots : Collections.singletonList(slot)
        );
    }

}
