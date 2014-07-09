package spawnsafe;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnSafeListener implements Listener{
	
	private final SpawnSafe plugin;
	
	public SpawnSafeListener(SpawnSafe instance) {
		plugin = instance;
    }
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) 
	{
		if(event.isCancelled())
			return;
		
		double damage = event.getDamage();
		
		if(event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			if(plugin.safetime.containsKey(player)
					&& plugin.safetime.get(player) > System.currentTimeMillis())
			{
				
				event.setDamage((int) Math.round((damage*plugin.dmgToResp)));
	
				if (event instanceof EntityDamageByEntityEvent)
				{
					EntityDamageByEntityEvent sub = (EntityDamageByEntityEvent)event;
					
					Entity damager = sub.getDamager();
					if(damager instanceof Player)
					{				
						((Player) damager).sendMessage("You deal " + plugin.dmgToResp*100 + "% to " +player.getName() + 
									" as he just respawned.");
					}
				}
			
				return;
		}
			
			//Remove damaged one from HashMap when nolonger in saftetime
			if(plugin.safetime.containsKey(player))
				plugin.safetime.remove(player);
		}
			
			if (event instanceof EntityDamageByEntityEvent)
			{
				EntityDamageByEntityEvent sub = (EntityDamageByEntityEvent)event;
				
				Entity damager = sub.getDamager();
				if(damager instanceof Player && plugin.safetime.containsKey((Player) damager))
				{
					if(plugin.safetime.get((Player) damager) > System.currentTimeMillis())
					{
						event.setDamage((int) Math.round((damage*plugin.dmgByResp)));
						return;
					}
				
				//Remove damager from HashMap when nolonger in saftetime
				if(plugin.safetime.containsKey((Player) damager))
					plugin.safetime.remove((Player) damager);
				}
			}
				
			 
		
			
	}	//End of onEntityDamage 
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
			Player player = event.getPlayer();
			plugin.safetime.put(player,  (System.currentTimeMillis() + (long) (plugin.proptime*1000)));
	}	//End onPlayerRespawn

}
