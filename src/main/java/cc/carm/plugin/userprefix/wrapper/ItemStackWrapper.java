package cc.carm.plugin.userprefix.wrapper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ItemStackWrapper implements ConfigurationSerializable {
    private static boolean unsafeAvailable;

    static {
        try {
            Class.forName("org.bukkit.UnsafeValues");
            unsafeAvailable = true;
        } catch (ClassNotFoundException e) {
            unsafeAvailable = false;
        }
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        throw new UnsupportedOperationException("Use ConfigurationUtil#dong to save configuration");
    }

    @NotNull
    public static ItemStack deserialize(@NotNull Map<String, Object> args) {
        // static define will cause problem, lazy load it
        if (unsafeAvailable) {
            if (!args.containsKey("v")) {
                Material material = Material.matchMaterial(args.get("type").toString());
                if (material == null) {
                    throw new IllegalArgumentException("物品 "+args.get("type")+" 不存在");
                }
                args.put("v", Bukkit.getServer().getUnsafe().getDataVersion());
            }
        }
        return ItemStack.deserialize(args);
    }
}
