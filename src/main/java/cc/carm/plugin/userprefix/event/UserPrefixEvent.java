package cc.carm.plugin.userprefix.event;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public abstract class UserPrefixEvent extends PlayerEvent {

	public UserPrefixEvent(@NotNull Player who) {
		super(who);
	}

}
