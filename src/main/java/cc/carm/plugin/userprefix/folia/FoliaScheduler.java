package cc.carm.plugin.userprefix.folia;

import cc.carm.plugin.userprefix.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

public class FoliaScheduler {
    private final Main plugin;
    private final boolean folia;

    public FoliaScheduler(Main plugin, boolean folia) {
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
//        AsyncScheduler asyncScheduler = Bukkit.getAsyncScheduler();
//        asyncScheduler.runNow(this.plugin, t -> task.run());

        try {
            Object asyncScheduler = Bukkit.class.getMethod("getAsyncScheduler")
                    .invoke(null);
            asyncScheduler.getClass().getMethod("runNow", Plugin.class, Consumer.class)
                    .invoke(asyncScheduler, this.plugin, (Consumer<?>) t -> task.run());
        } catch (IllegalAccessException | NoSuchMethodException e) {
            Main.severe("unexpected exception during reflection (#runAsyncFolia), it should never happen!");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw (RuntimeException) e.getTargetException();
        }
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
//        EntityScheduler entityScheduler = entity.getScheduler();
//        entityScheduler.run(this.plugin, t -> task.run(), null);
        try {
            Object entityScheduler = Entity.class.getMethod("getScheduler")
                    .invoke(entity);
            entityScheduler.getClass().getMethod("run", Plugin.class, Consumer.class, Runnable.class)
                    .invoke(entityScheduler, this.plugin, (Consumer<?>) t -> task.run(), null);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            Main.severe("unexpected exception during reflection (#runOnEntityFolia), it should never happen!");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw (RuntimeException) e.getTargetException();
        }
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
//        GlobalRegionScheduler globalRegionScheduler = Bukkit.getGlobalRegionScheduler();
//        globalRegionScheduler.execute(this.plugin, task);
        try {
            Object globalRegionScheduler = Bukkit.class.getMethod("getGlobalRegionScheduler")
                    .invoke(null);
            globalRegionScheduler.getClass().getMethod("execute", Plugin.class, Runnable.class)
                    .invoke(globalRegionScheduler, this.plugin, task);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            Main.severe("unexpected exception during reflection (#runGlobalFolia), it should never happen!");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw (RuntimeException) e.getTargetException();
        }
    }

    private void runBukkit(boolean forceSync, Runnable task) {
        if (forceSync) {
            Bukkit.getScheduler().runTask(this.plugin, task);
        } else {
            task.run();
        }
    }
}
