package cc.carm.plugin.userprefix.folia;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class FoliaScheduler {
    private final Plugin plugin;
    private final boolean folia;

    public FoliaScheduler(Plugin plugin, boolean folia) {
        this.plugin = plugin;
        this.folia = folia;
    }

    public void runAsync(Runnable task) {
        if (this.folia) {
            this.runAsyncFolia(task);
        } else {
            this.runAsyncBukkit(task);
        }
    }

    private void runAsyncBukkit(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, task);
    }

    private void runAsyncFolia(Runnable task) {
        Bukkit.getAsyncScheduler().runNow(this.plugin, t -> task.run());
    }

    /**
     * Folia 上在实体所在的调度器上执行任务，或在 Bukkit 上同步执行任务
     *
     * @param entity 实体
     * @param forceSync 若为 true，Bukkit 下强制同步运行；若为 false，Bukkit 下直接执行
     * @param task 任务
     * @see FoliaScheduler#runBukkit(boolean, Runnable)
     * @see FoliaScheduler#runOnEntityFolia(Entity, Runnable)
     */
    public void runOnEntity(Entity entity, boolean forceSync, Runnable task) {
        if (this.folia) {
            this.runOnEntityFolia(entity, task);
        } else {
            this.runBukkit(forceSync, task);
        }
    }

    private void runOnEntityFolia(Entity entity, Runnable task) {
        entity.getScheduler().run(this.plugin, t -> task.run(), null);
    }

    /**
     * Folia 上在全局调度器上执行任务，或在 Bukkit 上同步执行任务
     *
     * @param forceSync 若为 true，Bukkit 下强制同步运行；若为 false，Bukkit 下直接执行
     * @param task 任务
     * @see FoliaScheduler#runBukkit(boolean, Runnable)
     * @see FoliaScheduler#runGlobalFolia(Runnable)
     */
    public void runGlobal(boolean forceSync, Runnable task) {
        if (this.folia) {
            this.runGlobalFolia(task);
        } else {
            this.runBukkit(forceSync, task);
        }
    }

    private void runGlobalFolia(Runnable task) {
        Bukkit.getGlobalRegionScheduler().execute(this.plugin, task);
    }

    private void runBukkit(boolean forceSync, Runnable task) {
        if (forceSync) {
            Bukkit.getScheduler().runTask(this.plugin, task);
        } else {
            task.run();
        }
    }
}
