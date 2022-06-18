package cc.carm.plugin.userprefix;

import cc.carm.plugin.userprefix.configuration.prefix.PrefixConfig;
import cc.carm.plugin.userprefix.manager.ConfigManager;
import cc.carm.plugin.userprefix.manager.PrefixManager;
import cc.carm.plugin.userprefix.manager.UserManager;

public class UserPrefixAPI {

    public static PrefixManager getPrefixManager() {
        return Main.getInstance().prefixManager;
    }

    public static ConfigManager getConfigManager() {
        return Main.getInstance().configManager;
    }

    public static UserManager getUserManager() {
        return Main.getInstance().userManager;
    }

    public static PrefixConfig getDefaultPrefix(){
        return getPrefixManager().getDefaultPrefix();
    }

}
