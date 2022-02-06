package cc.carm.plugin.userprefix.hooker;

import cc.carm.lib.githubreleases4j.GithubReleases4J;
import cc.carm.plugin.userprefix.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateChecker {

    public static void checkUpdate(String currentVersion) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String downloadURL = GithubReleases4J.getReleasesURL("CarmJos", "UserPrefix");
                Integer behindVersions = GithubReleases4J.getVersionBehind(
                        "CarmJos", "UserPrefix", currentVersion
                );

                if (behindVersions == null) {
                    Main.error("检查更新失败，请您定期查看插件是否更新，避免安全问题。");
                    Main.error("下载地址 " + downloadURL);
                } else if (behindVersions == 0) {
                    Main.log("检查完成，当前已是最新版本。");
                } else if (behindVersions > 0) {
                    Main.log("发现新版本! 目前已落后 " + behindVersions + " 个版本。");
                    Main.log("最新版下载地址 " + downloadURL);
                } else {
                    Main.error("检查更新失败! 当前版本未知，请您使用原生版本以避免安全问题。");
                    Main.error("最新版下载地址 " + downloadURL);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }


}
