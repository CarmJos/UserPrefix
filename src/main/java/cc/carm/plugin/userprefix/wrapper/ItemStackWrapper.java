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
        // 用于判断是否支持unsafe
        try {
            Class.forName("org.bukkit.UnsafeValues");
            int dataVersion = Bukkit.getServer().getUnsafe().getDataVersion();
            unsafeAvailable = true;
        } catch (Exception e) {
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
                String itemName = args.get("type").toString();
                Material legacyMaterial = Material.matchMaterial(itemName, true);
                if (legacyMaterial == null) {
                    Material material = Material.matchMaterial(args.get("type").toString());
                    if (material == null) {
                        throw new IllegalArgumentException("物品 " + args.get("type") + " 不存在");
                    }
                    args.put("v", Bukkit.getServer().getUnsafe().getDataVersion());
                }
            }
        }
        return ItemStack.deserialize(args);
    }
}
