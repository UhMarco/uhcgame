package me.marco.uhcgame.manager;

import me.marco.uhcgame.UHCGame;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

public class ConfigManager {
    private UHCGame plugin = UHCGame.getInstance();

    public ConfigManager() {
        File dataFolder = new File(plugin.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                plugin.getLogger().log(Level.SEVERE, "Error creating data directory.");
            }
        }

        File deathmatchData = new File(dataFolder, "deathmatch.yml");
        if (!deathmatchData.exists()) {
            FileConfiguration data = YamlConfiguration.loadConfiguration(deathmatchData);
            // data.set("spawns", );

            try {
                data.save(deathmatchData);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error creating data file: ", e);
            }
        }
    }

    public FileConfiguration fetch(String file) {
        File configFile = new File(plugin.getDataFolder() + "/data", file + ".yml");

        YamlConfiguration customConfig = new YamlConfiguration();

        try {
            customConfig.load(configFile);
            return customConfig;
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error retrieving data file: ", e);
            return null;
        }
    }

    public void save(String file, FileConfiguration data) {
        File configFile = new File(plugin.getDataFolder() + "/data", file + ".yml");

        try {
            data.save(configFile);
        } catch(Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error updating data file: ", e);
        }
    }
}

/*
```java
package me.marco.skills.utils;

import lombok.experimental.UtilityClass;
import me.marco.skills.Skills;
import me.marco.skills.utils.Util.Skill;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;
import java.util.logging.Level;

@UtilityClass
public class PlayerData {

    public void create(Player player) {
        UUID uuid = player.getUniqueId();

        Skills plugin = Skills.getInstance();

        File dataFolder = new File(plugin.getDataFolder(), "PlayerData");
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                plugin.getLogger().log(Level.SEVERE, "Error creating PlayerData directory.");
            }
        }

        File playerData = new File(dataFolder, uuid + ".yml");

        if (!playerData.exists()) {
            FileConfiguration data = YamlConfiguration.loadConfiguration(playerData);

            // This for loop doesn't work:
            for (Skill skill : Skill.values()) {
                String skillName = skill.toString().toLowerCase();
                data.set(skillName + "-exp", 0);
                data.set(skillName + "-level", 0);
            }

            try {
                data.save(playerData);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error creating data file: ", e);
            }
        }
    }

    public FileConfiguration fetch(Player player) {
        UUID uuid = player.getUniqueId();

        Skills plugin = Skills.getInstance();

        File configFile = new File(plugin.getDataFolder() + "/PlayerData", uuid + ".yml");

        YamlConfiguration customConfig = new YamlConfiguration();

        try {
            customConfig.load(configFile);
            return customConfig;
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error retrieving data file: ", e);
            return null;
        }
    }

    public void save(Player player, FileConfiguration data) {
        Skills plugin = Skills.getInstance();

        File configFile = new File(plugin.getDataFolder() + "/PlayerData", player.getUniqueId() + ".yml");

        try {
            data.save(configFile);
        } catch(Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error updating data file: ", e);
        }
    }
}
```
 */