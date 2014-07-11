package it.github.mats391.spawnsafe;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnSafe extends JavaPlugin
{

	private final SpawnSafeListener	listener	= new SpawnSafeListener( this );

	public HashMap<Player, Long>	safetime	= new HashMap<Player, Long>();
	public double					proptime, dmgToResp, dmgByResp;

	protected FileConfiguration		config;

	Logger							log			= Logger.getLogger( "Minecraft" );

	@Override
	public boolean onCommand( final CommandSender sender, final Command cmd, final String commandLabel, final String[] args ) {
		if ( cmd.getName().equalsIgnoreCase( "ssreload" ) )
		{
			this.safetime.clear();
			this.reloadConfig();
			this.config = this.getConfig();
			this.proptime = this.config.getDouble( "SafeTime", 3.0 );
			this.dmgToResp = this.config.getDouble( "DamageDealtToRespawnee", 0.0 );
			this.dmgByResp = this.config.getDouble( "DamageDealtByRespawnee", 1.0 );
			this.saveConfig();

			if ( sender instanceof Player )
				( (Player) sender ).sendMessage( "Reloaded SpawnSafe config" );

			this.log.info( "Reloaded SpawnSafe config" );
			return true;
		}
		return false;
	} // End onCommand

	@Override
	public void onDisable() {
		this.safetime.clear();
		this.log.info( "SpawnSafe disabled" );
	} // End onDisable

	@Override
	public void onEnable() {

		this.config = this.getConfig();

		this.proptime = this.config.getDouble( "SafeTime", 3.0 );
		this.dmgToResp = this.config.getDouble( "DamageDealtToRespawnee", 0.0 );
		this.dmgByResp = this.config.getDouble( "DamageDealtByRespawnee", 1.0 );

		this.config.set( "SafeTime", this.proptime );
		this.config.set( "DamageDealtToRespawnee", this.dmgToResp );
		this.config.set( "DamageDealtByRespawnee", this.dmgByResp );

		this.saveConfig();

		final PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents( this.listener, this );

		this.log.info( "SpawnSafe v1.0 enabled" );
	} // End onEnable

} // End of Class
