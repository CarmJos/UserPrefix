package cc.carm.plugin.userprefix.manager;

import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.configuration.PluginConfig;
import cc.carm.plugin.userprefix.event.UserPrefixChangeEvent;
import cc.carm.plugin.userprefix.event.UserPrefixExpireEvent;
import cc.carm.plugin.userprefix.model.ConfiguredPrefix;
import cc.carm.plugin.userprefix.nametag.UserNameTag;
import cc.carm.plugin.userprefix.ui.PrefixSelectGUI;
import cc.carm.plugin.userprefix.util.gui.GUI;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class UserManager {

    public static HashMap<UUID, UserNameTag> nameTags = new HashMap<>();

    public static HashSet<UUID> checkingPlayers = new HashSet<>();

    @Nullable
    public static UserNameTag getNameTag(Player player) {
        if (PluginConfig.Functions.NAME_PREFIX.get()) {
            if (nameTags.containsKey(player.getUniqueId())) {
                return nameTags.get(player.getUniqueId());
            } else {
                return createNameTag(player);
            }
        } else {
            return null;
        }
    }

    @NotNull
    public static UserNameTag createNameTag(Player player) {
        if (nameTags.containsKey(player.getUniqueId())) return nameTags.get(player.getUniqueId());
        UserNameTag nameTag = new UserNameTag(player);
        nameTags.put(player.getUniqueId(), nameTag);
        return nameTag;
    }

    public static void initPlayer(Player player) {
        UserManager.checkPrefix(player, false);
        if (PluginConfig.Functions.NAME_PREFIX.get()) {
            UserManager.createNameTag(player);
            UserManager.updatePrefixView(player, true);
        }
    }

    public static void unloadPlayer(Player player) {
        PrefixSelectGUI.removeOpening(player);
        UserManager.unloadNameTag(player.getUniqueId());
        UserManager.checkingPlayers.remove(player.getUniqueId());
        GUI.removeOpenedGUI(player); // 清空打开过的GUI缓存 (用于记录物品点击的
    }

    /**
     * 更新前缀显示效果
     *
     * @param player     玩家
     * @param loadOthers 是否为玩家更新其他人的前缀(一般用于加入游戏)
     */
    public static void updatePrefixView(Player player, boolean loadOthers) {
        if (!PluginConfig.Functions.NAME_PREFIX.get()) return; //未启用的情况下，不需要进行任何操作。
        UserNameTag tag = getNameTag(player);
        if (tag == null) return;  //未启用的情况下，不需要进行任何操作。

        ConfiguredPrefix playerPrefix = UserManager.getPrefix(player);
        boolean descOrder = PluginConfig.Functions.NAME_ORDER_DESC.get();

        tag.setPrefix(playerPrefix.getContent());
        tag.setOrder(descOrder ? 999 - playerPrefix.getWeight() : playerPrefix.getWeight());

        Main.debug("为玩家 " + player.getName() + " 设置了 " + player.getName() + "的前缀为 #" + playerPrefix.getWeight() + " " + playerPrefix.getName());

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) continue;
            UserNameTag onlinePlayerTag = getNameTag(onlinePlayer);

            if (onlinePlayerTag != null) {
                onlinePlayerTag.setPrefix(player, playerPrefix.getContent());
                onlinePlayerTag.setOrder(player, descOrder ? 999 - playerPrefix.getWeight() : playerPrefix.getWeight());
                Main.debug("为玩家 " + onlinePlayer.getName() + " 设置了 " + player.getName() + "的前缀为 #" + playerPrefix.getWeight() + " " + playerPrefix.getName());

            }

            if (loadOthers) {
                ConfiguredPrefix onlinePlayerPrefix = UserManager.getPrefix(onlinePlayer);
                tag.setPrefix(onlinePlayer, onlinePlayerPrefix.getContent());
                tag.setOrder(onlinePlayer, descOrder ? 999 - onlinePlayerPrefix.getWeight() : onlinePlayerPrefix.getWeight());
                Main.debug("为玩家 " + player.getName() + " 设置了 " + onlinePlayer.getName() + "的前缀为 #" + onlinePlayerPrefix.getWeight() + " " + onlinePlayerPrefix.getName());
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
        if (checkingPlayers.contains(player.getUniqueId())) {
            /*
             * 这里为了避免极短时间内的重复触发导致多次判断且结果相同误导玩家，
             * 故没有采用同步锁，而是采用添加到一个临时Set中，对Set中玩家跳过判断。
             */
            return;
        }
        checkingPlayers.add(player.getUniqueId());
        String currentPrefixData = UserManager.getPrefixData(player);

        if (!UserManager.isPrefixUsable(player, currentPrefixData)) {
            ConfiguredPrefix currentPrefix = PrefixManager.getPrefix(currentPrefixData);
            ConfiguredPrefix newPrefix = UserManager.getHighestPrefix(player);

            if (currentPrefix != null) {
                //当前前缀不为空，则代表属于前缀过期的情况
                UserPrefixExpireEvent.call(player, currentPrefix);

                // 发送消息
                PluginConfig.Messages.EXPIRED.sendWithPlaceholders(player,
                        new String[]{"%(newName)", "%(oldName)"},
                        new Object[]{newPrefix.getName(), currentPrefix.getName()}
                );

                // 播放声音
                PluginConfig.Sounds.PREFIX_EXPIRED.play(player);
            } else {
                // 当前前缀为空，则代表是旧的前缀不存在了，
                PluginConfig.Messages.REMOVED.sendWithPlaceholders(player,
                        new String[]{"%(newName)", "%(oldName)"},
                        new Object[]{newPrefix.getName(), currentPrefixData}
                );
            }

            UserPrefixChangeEvent.call(player, currentPrefix, newPrefix, (after) -> {
                if (after != null) {
                    UserManager.setPrefix(player, after, updateView);
                }
                checkingPlayers.remove(player.getUniqueId());
            });

        } else {
            checkingPlayers.remove(player.getUniqueId());
        }
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
    @NotNull
    public static ConfiguredPrefix getPrefix(Player player) {
        String identifier = getPrefixData(player);
        if (identifier == null || !isPrefixUsable(player, identifier)) {
            return getHighestPrefix(player);
        } else {
            ConfiguredPrefix prefix = PrefixManager.getPrefix(identifier);
            return prefix == null ? PrefixManager.getDefaultPrefix() : prefix;
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
    @NotNull
    public static List<ConfiguredPrefix> getUsablePrefixes(Player player) {
        return PrefixManager.getPrefixes().values().stream()
                .filter(configuredPrefix -> isPrefixUsable(player, configuredPrefix)) //过滤出玩家可用的前缀
                .sorted(Comparator.comparingInt(ConfiguredPrefix::getWeight)) // 以前缀排序
                .collect(Collectors.toList()); // 返回集合
    }


    /**
     * 得到玩家可使用的最高权重的权限
     * 注意：若配置文件中关闭了 “autoUsePrefix” ,则会返回默认前缀。
     *
     * @param player 玩家
     * @return 权限内容
     */
    @NotNull
    public static ConfiguredPrefix getHighestPrefix(Player player) {
        if (PluginConfig.Functions.AUTO_USE.get()) {
            // 关闭了自动选择，就直接给默认的前缀，让玩家自己去设置吧~
            return PrefixManager.getDefaultPrefix();
        }
        return getUsablePrefixes(player).stream()
                .max(Comparator.comparingInt(ConfiguredPrefix::getWeight)) // 取权重最大
                .orElseGet(PrefixManager::getDefaultPrefix); // 啥都没有？ 返回默认前缀。
    }

    /**
     * 判断一个前缀对某玩家是否可用
     *
     * @param player           玩家
     * @param prefixIdentifier 前缀标识
     * @return 若前缀标识不存在，则返回false；若前缀为默认前缀，或该前缀无权限，或玩家有该前缀的权限，则返回true。
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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
        return configuredPrefix.isPublic()
                || ServiceManager.hasPermission(ServiceManager.getUser(player), configuredPrefix.getPermission());
    }

    /**
     * 得到用户当前正在使用的前缀Identifier。
     * 该方法通过LuckPerms的MetaData实现，因此可以通过指令去操作。
     *
     * @param player 玩家
     * @return 正在使用的前缀Identifier(若不存在则返回null, 代表未设置前缀)
     */
    @Nullable
    public static String getPrefixData(Player player) {
        return ServiceManager.getAPI().getMetaData(player)
                .getMetaValue("userprefix", String::valueOf)
                .orElse(null);
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
        clearPrefixData(player); // 清除掉旧的数据，LuckPerms不会去覆盖一个Meta，需要手动清除。
        if (prefixIdentifier != null) {
            user.data().add(MetaNode.builder("userprefix", prefixIdentifier).build());
            ServiceManager.getService().getUserManager().saveUser(user); // 保存数据
        }
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
