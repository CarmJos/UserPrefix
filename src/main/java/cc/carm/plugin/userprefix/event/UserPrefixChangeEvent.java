package cc.carm.plugin.userprefix.event;

import cc.carm.plugin.userprefix.model.ConfiguredPrefix;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UserPrefixChangeEvent extends UserPrefixEvent implements Cancellable {

	public static HandlerList handler = new HandlerList();

	private boolean cancelled;

	private final @Nullable ConfiguredPrefix before;
	private @NotNull ConfiguredPrefix after;

	public UserPrefixChangeEvent(@NotNull Player who,
								 @Nullable ConfiguredPrefix before,
								 @NotNull ConfiguredPrefix after) {
		super(who);
		this.before = before;
		this.after = after;
	}

	public @Nullable ConfiguredPrefix getBefore() {
		return before;
	}

	public @NotNull ConfiguredPrefix getAfter() {
		return after;
	}

	public void setAfter(@NotNull ConfiguredPrefix after) {
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
}
