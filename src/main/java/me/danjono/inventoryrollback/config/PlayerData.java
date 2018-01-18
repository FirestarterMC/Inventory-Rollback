package me.danjono.inventoryrollback.config;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.danjono.inventoryrollback.InventoryRollback;

public class PlayerData {
	
	private UUID uuid;
	private String logType;
	private File folderLocation;
	
	private File playerFile;
	private FileConfiguration playerData;
	
	public PlayerData(Player player, String logType) {
		this.logType = logType;
		this.uuid = player.getUniqueId();		
		this.folderLocation = new File(ConfigFile.folderLocation, "saves/");
		
		findPlayerFile();
		findPlayerData();
	}
	
	public PlayerData(OfflinePlayer player, String logType) {
		this.logType = logType;
		this.uuid = player.getUniqueId();	
		this.folderLocation = new File(ConfigFile.folderLocation, "saves/");
		
		findPlayerFile();
		findPlayerData();
	}
	
	public PlayerData(UUID uuid, String logType) {
		this.logType = logType;
		this.uuid = uuid;	
		this.folderLocation = new File(ConfigFile.folderLocation, "saves/");
		
		findPlayerFile();
		findPlayerData();
	}
		
	private boolean findPlayerFile() {			
		if (logType.equalsIgnoreCase("JOIN")) {
			this.playerFile = new File(folderLocation, "joins/" + uuid + ".yml");
		} else if (logType.equalsIgnoreCase("QUIT")) {
			this.playerFile = new File(folderLocation, "quits/" + uuid + ".yml");
		} else if (logType.equalsIgnoreCase("DEATH")) {
			this.playerFile = new File(folderLocation, "deaths/" + uuid + ".yml");
		} else if (logType.equalsIgnoreCase("WORLDCHANGE")) {
			this.playerFile = new File(folderLocation, "worldChanges/" + uuid + ".yml");
		} else if (logType.equalsIgnoreCase("FORCE")) {
			this.playerFile = new File(folderLocation, "force/" + uuid + ".yml");
		}
				
		if (this.playerFile == null)
			return false;
			
		return true;
	}

	private boolean findPlayerData() {
		this.playerData = YamlConfiguration.loadConfiguration(playerFile);
		
		if (this.playerData == null)
			return false;
		
		return true;
	}
	
	public File getFile() {
		return this.playerFile;
	}
	
	public FileConfiguration getData() {
		return this.playerData;
	}
	
	public void saveData() {
		InventoryRollback.instance.getServer().getScheduler().runTaskAsynchronously(InventoryRollback.instance, new Runnable() {
			@Override
			public void run() {
				try {
					playerData.save(playerFile);
				} catch (IOException e) {
					e.printStackTrace();
					
				}
			}
		});
	}
	
	public int getMaxSaves() {
		if (logType.equalsIgnoreCase("JOIN")) {
			return ConfigFile.maxSavesJoin;
		} else if (logType.equalsIgnoreCase("QUIT")) {
			return ConfigFile.maxSavesQuit;
		} else if (logType.equalsIgnoreCase("DEATH")) {
			return ConfigFile.maxSavesDeath;
		} else if (logType.equalsIgnoreCase("WORLDCHANGE")) {
			return ConfigFile.maxSavesWorldChange;
		} else if (logType.equalsIgnoreCase("FORCE")) {
			return ConfigFile.maxSavesForce;
		}  else {
			return 0;
		}
	}

}
