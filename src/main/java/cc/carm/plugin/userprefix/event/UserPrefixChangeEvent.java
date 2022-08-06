package cc.carm.plugin.userprefix.event;

import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.conf.prefix.PrefixConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class UserPrefixChangeEvent extends UserPrefixEvent implements Cancellable {

    public static HandlerList handler = new HandlerList();

    private boolean cancelled;

    private final @Nullable PrefixConfig before;
    private @NotNull PrefixConfig after;

    public UserPrefixChangeEvent(@NotNull Player who,
                                 @Nullable PrefixConfig before,
                                 @NotNull PrefixConfig after) {
        super(who);
        this.before = before;
        this.after = after;
    }

    public @Nullable PrefixConfig getBefore() {
        return before;
    }

    public @NotNull PrefixConfig getAfter() {
        return after;
    }

    public void setAfter(@NotNull PrefixConfig after) {
        this.after = after;
    }

    @Override
    public boolean isCancelled() {
        if (before == null) return false; //Could not be cancelled when prefix is null.
        else return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handler;
    }

    public static void call(@NotNull Player who,
                            @Nullable PrefixConfig before,
                            @NotNull PrefixConfig after,
                            @Nullable Consumer<@Nullable PrefixConfig> finish) {
        Main.getInstance().callSync(new UserPrefixChangeEvent(who, before, after))
                .thenAccept((e) -> Optional.ofNullable(finish).ifPresent(f -> f.accept(e.getAfter())));
    }

}
