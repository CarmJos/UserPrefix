package cc.carm.plugin.userprefix.nametag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UserNameTag {

    private final Player viewer;
    private Team team;
    private final Scoreboard sb;
    private int order = 99999;
    private final Map<UUID, Integer> targetOrders = new HashMap<>();
    private final Map<UUID, String> previousTeamNames = new HashMap<>();

    public UserNameTag(Player viewer) {
        this.viewer = viewer;
        Scoreboard sb = viewer.getScoreboard();
        if (sb == Bukkit.getServer().getScoreboardManager().getMainScoreboard()) {
            sb = Bukkit.getScoreboardManager().getNewScoreboard();
        }
        team = sb.registerNewTeam(order + viewer.getUniqueId().toString().substring(0, 10));
        team.setCanSeeFriendlyInvisibles(true);
        team.addEntry(viewer.getName());
        this.sb = sb;
        viewer.setScoreboard(sb);
    }

    /**
     * 设置自己的前缀
     *
     * @param prefix 前缀内容
     */
    public void setPrefix(String prefix) {
        team.setPrefix(prefix);
        update(viewer);
    }

    /**
     * 设置某个玩家的前缀
     *
     * @param target 目标
     * @param prefix 前缀内容
     */
    public void setPrefix(Player target, String prefix) {
        if (target == viewer) {
            setPrefix(prefix);
        } else {
            Team targetTeam = checkTeam(target);
            targetTeam.setPrefix(prefix);
        }
        update(viewer);
        if (viewer != target)
            update(target);
    }

    /**
     * 设置名字在TabList中的顺序
     *
     * @param order 顺序 (0~99999)
     */
    public void setOrder(int order) {
        if (order < 0 || order > 99999)
            throw new IllegalArgumentException("order must be in 0~99999");
        this.order = order;
        targetOrders.put(viewer.getUniqueId(), order);
        update(viewer);
    }

    /**
     * 设置名字在TabList中的顺序
     *
     * @param order 顺序
     */
    public void setOrder(Player target, int order) {
        if (order < 0 || order > 99999) throw new IllegalArgumentException("order must be in 0~99999");

        Team targetTeam = checkTeam(target);
        String teamName = order + UUID.randomUUID().toString().substring(0, 10);
        targetTeam.setDisplayName(teamName);
        targetOrders.put(target.getUniqueId(), order);
        update(viewer);
        if (viewer != target) update(target);
    }

    public void update(Player target) {
        if (target == viewer) {
            Set<String> entries = team.getEntries();
            String name = order + viewer.getUniqueId().toString().substring(0, 10);
            if (sb.getTeam(name) == null) {
                Team newTeam = sb.registerNewTeam(name);
                newTeam.setDisplayName(name);
                entries.forEach(newTeam::addEntry);
                team.getPrefix();
                if (!team.getPrefix().isEmpty())
                    newTeam.setPrefix(team.getPrefix());
                team.getSuffix();
                if (!team.getSuffix().isEmpty())
                    newTeam.setSuffix(team.getSuffix());
                newTeam.setNameTagVisibility(team.getNameTagVisibility());
                newTeam.setCanSeeFriendlyInvisibles(true);
                team.unregister();
                team = newTeam;
            }
        } else {
            int order = targetOrders.getOrDefault(target.getUniqueId(), 99999);
            String previousTeamName = previousTeamNames.get(target.getUniqueId());
            if (previousTeamName == null) {
                return;
            }
            String name = order + target.getUniqueId().toString().substring(0, 10);
            Team targetTeam = this.sb.getTeam(previousTeamName);
            if (targetTeam == null) {
                return;
            }
            if (sb.getTeam(name) == null) {
                Team newTeam = sb.registerNewTeam(name);
                newTeam.setDisplayName(name);
                newTeam.addEntry(target.getName());
                newTeam.setNameTagVisibility(targetTeam.getNameTagVisibility());
                newTeam.setCanSeeFriendlyInvisibles(true);
                targetTeam.getPrefix();
                if (!targetTeam.getPrefix().isEmpty()) newTeam.setPrefix(targetTeam.getPrefix());
                targetTeam.getSuffix();
                if (!targetTeam.getSuffix().isEmpty()) newTeam.setSuffix(targetTeam.getSuffix());
                targetTeam.unregister();
                previousTeamNames.put(target.getUniqueId(), name);
            }
        }

    }

    private Team checkTeam(Player target) {
        int order = targetOrders.getOrDefault(target.getUniqueId(), 99999);
        String name = order + target.getUniqueId().toString().substring(0, 10);
        Team targetTeam = this.sb.getTeam(name);
        if (targetTeam == null) {
            targetTeam = this.sb.registerNewTeam(name);
            targetTeam.addEntry(target.getName());
            previousTeamNames.put(target.getUniqueId(), name);
        }
        return targetTeam;
    }


}
