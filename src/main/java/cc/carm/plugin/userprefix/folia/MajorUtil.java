package cc.carm.plugin.userprefix.folia;

import cc.carm.plugin.userprefix.Main;
import org.bukkit.enchantments.Enchantment;

public interface MajorUtil {
    static Enchantment getEnchantProtection() {
        Class<Enchantment> enchantmentClass = Enchantment.class;
        try {
            return (Enchantment) enchantmentClass.getField("PROTECTION_ENVIRONMENTAL").get(null);
        } catch (NoSuchFieldException e1) {
            try {
                return (Enchantment) enchantmentClass.getField("PROTECTION").get(null);
            } catch (NoSuchFieldException | IllegalAccessException e2) {
                Main.severe("unexpected exception during reflection (#getEnchantProtection), it should never happen!");
                throw new RuntimeException(e2);
            }
        } catch (IllegalAccessException e) {
            Main.severe("unexpected exception during reflection (#getEnchantProtection), it should never happen!");
            throw new RuntimeException(e);
        }
    }
}
