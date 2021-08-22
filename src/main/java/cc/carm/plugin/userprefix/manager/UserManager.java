package cc.carm.plugin.userprefix.manager;

import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.configuration.PrefixConfig;
import cc.carm.plugin.userprefix.model.ConfiguredPrefix;
import cc.carm.plugin.userprefix.nametag.UserNameTag;
import cc.carm.plugin.userprefix.util.MessageUtil;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class UserManager {

    public static HashMap<UUID, UserNameTag> nameTags = new HashMap<>();

    public static HashSet<UUID> checkingPlayers = new HashSet<>();

    public static UserNameTag getNameTag(Player player) {
        return nameTags.get(player.getUniqueId());
    }

    public static UserNameTag createNameTag(Player player) {
        UserNameTag nameTag = new UserNameTag(player);
        nameTags.put(player.getUniqueId(), nameTag);
        return nameTag;
    }

    /**
     * 更新前缀显示效果
     *
     * @param player     玩家
     * @param loadOthers 是否为玩家更新其他人的前缀(一般用于加入游戏)
     */
    public static void updatePrefixView(Player player, boolean loadOthers) {
        ConfiguredPrefix playerPrefix = UserManager.getPrefix(player);

        UserNameTag tag = getNameTag(player);

        tag.setPrefix(playerPrefix.getContent());
        tag.setOrder(playerPrefix.getWeight());

        Main.debug("为玩家 " + player.getName() + " 设置了 " + player.getName() + "的前缀为 #" + playerPrefix.getWeight() + " " + playerPrefix.getName());

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) continue;
            UserNameTag onlinePlayerTag = getNameTag(onlinePlayer);
            if (onlinePlayerTag != null) {
                onlinePlayerTag.setPrefix(player, playerPrefix.getContent());
                onlinePlayerTag.setOrder(player, playerPrefix.getWeight());
                Main.debug("为玩家 " + onlinePlayer.getName() + " 设置了 " + player.getName() + "的前缀为 #" + playerPrefix.getWeight() + " " + playerPrefix.getName());

            }

            if (loadOthers) {
                ConfiguredPrefix onlinePlayerPrefix = UserManager.getPrefix(onlinePlayer);
                if (onlinePlayerPrefix != null) {
                    tag.setPrefix(onlinePlayer, onlinePlayerPrefix.getContent());
                    tag.setOrder(onlinePlayer, onlinePlayerPrefix.getWeight());
                    Main.debug("为玩家 " + player.getName() + " 设置了 " + player.getName() + "的前缀为 #" + onlinePlayerPrefix.getWeight() + " " + onlinePlayerPrefix.getName());
                }
            }
        }
    }

    /**
     * 检查玩家的前缀的使用权
     *
     * @param player     玩家
     * @param updateView 是否更新头顶与TabList中的前缀
     */
    public static void checkPrefix(Player player, boolean updateView) {
        if (checkingPlayers.contains(player.getUniqueId())) return;
        checkingPlayers.add(player.getUniqueId());
        String currentPrefixIdentifier = UserManager.getPrefixData(player);
        ConfiguredPrefix currentPrefix = PrefixManager.getPrefix(currentPrefixIdentifier);
        if (!UserManager.isPrefixUsable(player, currentPrefixIdentifier)) {
            ConfiguredPrefix newPrefix = UserManager.getHighestPrefix(player);
            // 更新前缀
            UserManager.setPrefix(player, newPrefix, updateView);
            // 发送消息
            MessageUtil.sendWithPlaceholders(player, PrefixConfig.Messages.EXPIRED.get(),
                    new String[]{"%(newName)", "%(oldName)"},
                    new Object[]{newPrefix.getName(), currentPrefix != null ? currentPrefix.getName() : currentPrefixIdentifier}
            );
            // 播放声音
            PrefixConfig.Sounds.PREFIX_EXPIRED.play(player);
        }
        checkingPlayers.remove(player.getUniqueId());
    }

    public static void unloadNameTag(UUID uuid) {
        nameTags.remove(uuid);
    }

    /**
     * 得到玩家的前缀。
     * 该方法会自动判断玩家当前的前缀是否可用，并返回最终可用的前缀。
     *
     * @param player 玩家
     * @return 前缀配置
     */
    public static ConfiguredPrefix getPrefix(Player player) {
        String identifier = getPrefixData(player);
        if (identifier == null || !isPrefixUsable(player, identifier)) {
            return getHighestPrefix(player);
        } else {
            return PrefixManager.getPrefix(identifier);
        }
    }

    /**
     * 设定玩家前缀
     *
     * @param player     玩家
     * @param prefix     前缀配置
     * @param updateView 是否更新头顶上、TabList的前缀
     */
    public static void setPrefix(Player player, ConfiguredPrefix prefix, boolean updateView) {
        setPrefixData(player, prefix.getIdentifier());
        if (updateView) updatePrefixView(player, false);
    }

    /**
     * 得到玩家所有可用的前缀
     *
     * @param player 玩家
     * @return 可用前缀列表
     */
    public static List<ConfiguredPrefix> getUsablePrefixes(Player player) {
        return PrefixManager.getPrefixes().values().stream()
                .filter(configuredPrefix -> isPrefixUsable(player, configuredPrefix))
                .sorted(Comparator.comparingInt(ConfiguredPrefix::getWeight))
                .collect(Collectors.toList());
    }


    /**
     * 得到玩家可使用的最高权重的权限
     * 注意：若配置文件中关闭了 “autoUsePrefix” ,则会返回默认前缀。
     *
     * @param player 玩家
     * @return 权限内容
     */
    public static ConfiguredPrefix getHighestPrefix(Player player) {
        if (PrefixConfig.Functions.AUTO_USE.get()) {
            // 关闭了自动选择，就直接给默认的前缀，让玩家自己去设置吧
            return PrefixManager.getDefaultPrefix();
        }
        List<ConfiguredPrefix> prefixes = getUsablePrefixes(player);
        return prefixes.stream().max(Comparator.comparingInt(ConfiguredPrefix::getWeight)).orElseGet(PrefixManager::getDefaultPrefix);
    }

    /**
     * 判断一个前缀对某玩家是否可用
     *
     * @param player           玩家
     * @param prefixIdentifier 前缀标识
     * @return 若前缀标识不存在，则返回false；若前缀为默认前缀，或该前缀无权限，或玩家有该前缀的权限，则返回true。
     */
    public static boolean isPrefixUsable(Player player, String prefixIdentifier) {
        if (prefixIdentifier == null || prefixIdentifier.equalsIgnoreCase("default")) return true;
        ConfiguredPrefix prefix = PrefixManager.getPrefix(prefixIdentifier);
        if (prefix == null) return false;
        return isPrefixUsable(player, prefix);
    }

    /**
     * 判断一个前缀对某玩家是否可用
     *
     * @param player           玩家
     * @param configuredPrefix 前缀配置
     * @return 若前缀标识不存在，则返回false；若前缀为默认前缀，或该前缀无权限，或玩家有该前缀的权限，则返回true。
     */
    public static boolean isPrefixUsable(Player player, ConfiguredPrefix configuredPrefix) {
        return configuredPrefix.getPermission() == null || ServiceManager.hasPermission(ServiceManager.getUser(player), configuredPrefix.getPermission());
    }

    /**
     * 得到用户当前正在使用的前缀Identifier。
     * 该方法通过LuckPerms的MetaData实现，因此可以通过指令去操作。
     *
     * @param player 玩家
     * @return 正在使用的前缀Identifier(若不存在则返回null)
     */
    public static String getPrefixData(Player player) {
        return ServiceManager.getAPI().getMetaData(player).getMetaValue("userprefix", String::valueOf).orElse(null);
    }

    /**
     * 设定用户所使用的的prefix。
     * 该方法通过LuckPerms的MetaData实现，因此可以通过指令去操作。
     *
     * @param player           玩家
     * @param prefixIdentifier 前缀的标识
     */
    public static void setPrefixData(Player player, String prefixIdentifier) {
        User user = ServiceManager.getUser(player);
        clearPrefixData(player);
        // LuckPerms竟然会把所有的metaKey全部转换为小写... 那我这里就直接写成小写吧~
        MetaNode node = MetaNode.builder("userprefix", prefixIdentifier).build();
        user.data().add(node);
        ServiceManager.getService().getUserManager().saveUser(user);
    }

    /**
     * 清除玩家所选择的前缀数据
     *
     * @param player 玩家
     */
    public static void clearPrefixData(Player player) {
        User user = ServiceManager.getUser(player);
        // LuckPerms竟然会把所有的metaKey全部转换为小写... 那我这里就直接写成小写吧~
        user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("userprefix")));
    }


}
