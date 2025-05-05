package cc.carm.plugin.userprefix.manager;

import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.UserPrefixAPI;
import cc.carm.plugin.userprefix.conf.PluginConfig;
import cc.carm.plugin.userprefix.conf.PluginMessages;
import cc.carm.plugin.userprefix.conf.prefix.PrefixConfig;
import cc.carm.plugin.userprefix.event.UserPrefixChangeEvent;
import cc.carm.plugin.userprefix.event.UserPrefixExpireEvent;
import cc.carm.plugin.userprefix.hooker.UserNameTag;
import cc.carm.plugin.userprefix.ui.PrefixSelectGUI;
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

    // LuckPerms竟然会把所有的metaKey全部转换为小写... 那我这里就直接写成小写吧~
    public static final @NotNull String META_KEY = "userprefix";

    protected final HashMap<UUID, UserNameTag> nameTags = new HashMap<>();

    protected final HashSet<UUID> checkingPlayers = new HashSet<>();

    @Nullable
    public UserNameTag getNameTag(Player player) {
        if (this.isNamePrefixEnabled()) {
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
    public UserNameTag createNameTag(Player player) {
        if (nameTags.containsKey(player.getUniqueId())) return nameTags.get(player.getUniqueId());
        UserNameTag nameTag = new UserNameTag(player);
        nameTags.put(player.getUniqueId(), nameTag);
        return nameTag;
    }

    public void initPlayer(Player player) {
        checkPrefix(player, false);
        if (this.isNamePrefixEnabled()) {
            createNameTag(player);
            updatePrefixView(player, true);
        }
    }

    public void unloadPlayer(Player player) {
        PrefixSelectGUI.removeOpening(player);
        unloadNameTag(player.getUniqueId());
        checkingPlayers.remove(player.getUniqueId());
        GUI.removeOpenedGUI(player); // 清空打开过的GUI缓存 (用于记录物品点击的
    }

    /**
     * 更新前缀显示效果
     *
     * @param player     玩家
     * @param loadOthers 是否为玩家更新其他人的前缀(一般用于加入游戏)
     */
    public void updatePrefixView(Player player, boolean loadOthers) {
        if (!this.isNamePrefixEnabled()) return; //未启用的情况下，不需要进行任何操作。
        UserNameTag tag = getNameTag(player);
        if (tag == null) return;  //未启用的情况下，不需要进行任何操作。

        PrefixConfig playerPrefix = getPrefix(player);
        boolean descOrder = PluginConfig.FUNCTIONS.NAME_PREFIX.ORDER_DESC.getNotNull();

        tag.setPrefix(playerPrefix.getContent(player));
        tag.setOrder(descOrder ? 999 - playerPrefix.getWeight() : playerPrefix.getWeight());

        Main.debugging("为玩家 " + player.getName() + " 设置了 " + player.getName() + "的前缀为 #" + playerPrefix.getWeight() + " " + playerPrefix.getName());

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) continue;
            UserNameTag onlinePlayerTag = getNameTag(onlinePlayer);

            if (onlinePlayerTag != null) {
                onlinePlayerTag.setPrefix(player, playerPrefix.getContent(onlinePlayer));
                onlinePlayerTag.setOrder(player, descOrder ? 999 - playerPrefix.getWeight() : playerPrefix.getWeight());
                Main.debugging("为玩家 " + onlinePlayer.getName() + " 设置了 " + player.getName() + "的前缀为 #" + playerPrefix.getWeight() + " " + playerPrefix.getName());

            }

            if (loadOthers) {
                PrefixConfig onlinePlayerPrefix = getPrefix(onlinePlayer);
                tag.setPrefix(onlinePlayer, onlinePlayerPrefix.getContent(player));
                tag.setOrder(onlinePlayer, descOrder ? 999 - onlinePlayerPrefix.getWeight() : onlinePlayerPrefix.getWeight());
                Main.debugging("为玩家 " + player.getName() + " 设置了 " + onlinePlayer.getName() + "的前缀为 #" + onlinePlayerPrefix.getWeight() + " " + onlinePlayerPrefix.getName());
            }
        }
    }

    /**
     * 检查玩家的前缀的使用权
     *
     * @param player     玩家
     * @param updateView 是否更新头顶与TabList中的前缀
     */
    public void checkPrefix(Player player, boolean updateView) {
        if (checkingPlayers.contains(player.getUniqueId())) {
            /*
             * 这里为了避免极短时间内的重复触发导致多次判断且结果相同误导玩家，
             * 故没有采用同步锁，而是采用添加到一个临时Set中，对Set中玩家跳过判断。
             */
            return;
        }
        checkingPlayers.add(player.getUniqueId());
        String currentPrefixData = getPrefixData(player);

        if (!isPrefixUsable(player, currentPrefixData)) {
            PrefixConfig currentPrefix = UserPrefixAPI.getPrefixManager().getPrefix(currentPrefixData);
            PrefixConfig newPrefix = getHighestPrefix(player);

            if (currentPrefix != null) {
                //当前前缀不为空，则代表属于前缀过期的情况
                UserPrefixExpireEvent.call(player, currentPrefix);

                // 发送消息
                PluginMessages.EXPIRED.sendTo(player, currentPrefix.getName(), newPrefix.getName());

                // 播放声音
                PluginConfig.SOUNDS.PREFIX_EXPIRED.playTo(player);
            } else {
                // 当前前缀为空，则代表是旧的前缀不存在了，
                PluginMessages.REMOVED.sendTo(player, newPrefix.getName());
            }

            UserPrefixChangeEvent.call(player, currentPrefix, newPrefix, (after) -> {
                if (after != null) setPrefix(player, after, updateView);
                checkingPlayers.remove(player.getUniqueId());
            });

        } else {
            checkingPlayers.remove(player.getUniqueId());
        }
    }

    public void unloadNameTag(UUID uuid) {
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
    public PrefixConfig getPrefix(Player player) {
        String identifier = getPrefixData(player);
        if (identifier == null || !isPrefixUsable(player, identifier)) {
            return getHighestPrefix(player);
        } else {
            PrefixConfig prefix = UserPrefixAPI.getPrefixManager().getPrefix(identifier);
            return prefix == null ? UserPrefixAPI.getDefaultPrefix() : prefix;
        }
    }

    /**
     * 设定玩家前缀
     *
     * @param player     玩家
     * @param prefix     前缀配置
     * @param updateView 是否更新头顶上、TabList的前缀
     */
    public void setPrefix(Player player, PrefixConfig prefix, boolean updateView) {
        setPrefixData(player, prefix.getIdentifier());
        prefix.executeActions(player);
        if (updateView) updatePrefixView(player, false);
    }

    /**
     * 得到玩家所有可用的前缀
     *
     * @param player 玩家
     * @return 可用前缀列表
     */
    @NotNull
    public List<PrefixConfig> getUsablePrefixes(Player player) {
        return UserPrefixAPI.getPrefixManager().getPrefixes().values().stream()
                .filter(prefix -> prefix.checkPermission(player)) //过滤出玩家可用的前缀
                .sorted(Comparator.comparingInt(PrefixConfig::getWeight)) // 以前缀排序
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
    public PrefixConfig getHighestPrefix(Player player) {
        if (!PluginConfig.FUNCTIONS.AUTO_USE.getNotNull()) {
            // 关闭了自动选择，就直接给默认的前缀，让玩家自己去设置吧~
            return UserPrefixAPI.getDefaultPrefix();
        }
        return getUsablePrefixes(player).stream()
                .max(Comparator.comparingInt(PrefixConfig::getWeight)) // 取权重最大
                .orElseGet(UserPrefixAPI::getDefaultPrefix); // 啥都没有？ 返回默认前缀。
    }

    /**
     * 判断一个前缀对某玩家是否可用
     *
     * @param player           玩家
     * @param prefixIdentifier 前缀标识
     * @return 若前缀标识不存在，则返回false；若前缀为默认前缀，或该前缀无权限，或玩家有该前缀的权限，则返回true。
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isPrefixUsable(Player player, String prefixIdentifier) {
        if (prefixIdentifier == null || prefixIdentifier.equalsIgnoreCase("default")) return true;
        PrefixConfig prefix = UserPrefixAPI.getPrefixManager().getPrefix(prefixIdentifier);
        return prefix != null && prefix.checkPermission(player);
    }


    /**
     * 得到用户当前正在使用的前缀Identifier。
     * 该方法通过LuckPerms的MetaData实现，因此可以通过指令去操作。
     *
     * @param player 玩家
     * @return 正在使用的前缀Identifier(若不存在则返回null, 代表未设置前缀)
     */
    @Nullable
    public String getPrefixData(Player player) {
        return ServiceManager.getAPI().getMetaData(player)
                .getMetaValue(META_KEY, String::valueOf)
                .orElse(null);
    }

    /**
     * 设定用户所使用的的prefix。
     * 该方法通过LuckPerms的MetaData实现，因此可以通过指令去操作。
     *
     * @param player           玩家
     * @param prefixIdentifier 前缀的标识
     */
    public void setPrefixData(Player player, String prefixIdentifier) {
        User user = ServiceManager.getUser(player);
        clearPrefixData(player); // 清除掉旧的数据，LuckPerms不会去覆盖一个Meta，需要手动清除。
        if (prefixIdentifier != null) {
            user.data().add(MetaNode.builder(META_KEY, prefixIdentifier).build());
            ServiceManager.getService().getUserManager().saveUser(user); // 保存数据
        }
    }

    /**
     * 清除玩家所选择的前缀数据
     *
     * @param player 玩家
     */
    public void clearPrefixData(Player player) {
        User user = ServiceManager.getUser(player);
        user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals(META_KEY)));
    }

    private boolean isNamePrefixEnabled() {
        return PluginConfig.FUNCTIONS.NAME_PREFIX.ENABLE.getNotNull() && !Main.getInstance().isOnFolia();
    }
}
