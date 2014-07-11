package io.github.mats391.spawnsafe;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnSafeListener implements Listener
{

	private final SpawnSafe	plugin;

	public SpawnSafeListener(final SpawnSafe instance) {
		this.plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage( final EntityDamageEvent event ) {
		if ( event.isCancelled() )
			return;

		final double damage = event.getDamage();

		if ( event.getEntity() instanceof Player )
		{
			final Player player = (Player) event.getEntity();
			if ( this.plugin.safetime.containsKey( player ) && this.plugin.safetime.get( player ) > System.currentTimeMillis() )
			{

				event.setDamage( (int) Math.round( damage * this.plugin.dmgToResp ) );

				if ( event instanceof EntityDamageByEntityEvent )
				{
					final EntityDamageByEntityEvent sub = (EntityDamageByEntityEvent) event;

					final Entity damager = sub.getDamager();
					if ( damager instanceof Player )
						( (Player) damager ).sendMessage( "You deal " + this.plugin.dmgToResp * 100 + "% to " + player.getName()
								+ " as he just respawned." );
				}

				return;
			}

			// Remove damaged one from HashMap when nolonger in saftetime
			if ( this.plugin.safetime.containsKey( player ) )
				this.plugin.safetime.remove( player );
		}

		if ( event instanceof EntityDamageByEntityEvent )
		{
			final EntityDamageByEntityEvent sub = (EntityDamageByEntityEvent) event;

			final Entity damager = sub.getDamager();
			if ( damager instanceof Player && this.plugin.safetime.containsKey( damager ) )
			{
				if ( this.plugin.safetime.get( damager ) > System.currentTimeMillis() )
				{
					event.setDamage( (int) Math.round( damage * this.plugin.dmgByResp ) );
					return;
				}

				// Remove damager from HashMap when nolonger in saftetime
				if ( this.plugin.safetime.containsKey( damager ) )
					this.plugin.safetime.remove( damager );
			}
		}

	} // End of onEntityDamage

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn( final PlayerRespawnEvent event ) {
		final Player player = event.getPlayer();
		this.plugin.safetime.put( player, System.currentTimeMillis() + (long) ( this.plugin.proptime * 1000 ) );
	} // End onPlayerRespawn

}
