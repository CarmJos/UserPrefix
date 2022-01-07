package cc.carm.plugin.userprefix.event;

import cc.carm.plugin.userprefix.model.ConfiguredPrefix;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UserPrefixExpireEvent extends UserPrefixEvent {

	public static HandlerList handler = new HandlerList();


	public final @NotNull ConfiguredPrefix expiredPrefix;

	public UserPrefixExpireEvent(@NotNull Player who,
								 @NotNull ConfiguredPrefix expiredPrefix) {
		super(who);
		this.expiredPrefix = expiredPrefix;
	}

	public @NotNull ConfiguredPrefix getExpiredPrefix() {
		return expiredPrefix;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handler;
	}
}
