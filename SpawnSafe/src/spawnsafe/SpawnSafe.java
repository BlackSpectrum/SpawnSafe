package spawnsafe;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;






public class SpawnSafe extends JavaPlugin{
	
	private final SpawnSafeListener listener = new SpawnSafeListener(this);

	
	public HashMap<Player, Long> safetime = new HashMap<Player, Long>();
	public double proptime, dmgToResp, dmgByResp;
	
	protected FileConfiguration config;
	
	Logger log = Logger.getLogger("Minecraft");
	@Override
	public void onDisable() {	
		safetime.clear();
		log.info("SpawnSafe disabled");
	}	//End onDisable

	@Override
	public void onEnable() {
		

				config = this.getConfig();
		
				proptime = config.getDouble("SafeTime", 3.0);
				dmgToResp = config.getDouble("DamageDealtToRespawnee", 0.0);
				dmgByResp = config.getDouble("DamageDealtByRespawnee", 1.0);
				
				config.set("SafeTime",  proptime);
				config.set("DamageDealtToRespawnee",dmgToResp);
				config.set("DamageDealtByRespawnee", dmgByResp);
				
				saveConfig();
								
				
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(listener, this);
		
		log.info("SpawnSafe v1.0 enabled");		
	}	//End onEnable
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("ssreload"))
		{
			safetime.clear();
			reloadConfig();
			config = this.getConfig();
			proptime = config.getDouble("SafeTime", 3.0);
			dmgToResp = config.getDouble("DamageDealtToRespawnee", 0.0);
			dmgByResp = config.getDouble("DamageDealtByRespawnee", 1.0);
			this.saveConfig();
			
			if(sender instanceof Player)
				((Player)sender).sendMessage("Reloaded SpawnSafe config");
			
			log.info("Reloaded SpawnSafe config");	
			return true;
		} 
		return false; 
	}	//End onCommand
	

	

}	//End of Class
