package org.dimaserver.statTracker;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.dimaserver.statTracker.db.Database;
import org.dimaserver.statTracker.listeners.Listeners;
import org.dimaserver.statTracker.commands.CommandHandler;

import java.sql.Connection;
import java.sql.SQLException;

public final class StatTracker extends JavaPlugin implements Listener {
    Connection connection = null;
    private Database database;
    private final FileConfiguration config = getConfig();

    public Database getDatabase() {
        return database;
    }

    public FileConfiguration getConfiguration() {
        return config;
    }
    
    @Override
    public void onEnable() {
        config.addDefault("mysql.url", "jdbc:mysql://localhost/minecraft");
        config.addDefault("mysql.username", "root");
        config.addDefault("mysql.password", "password");

        config.addDefault("ipinfo.token", "YOUR_TOKEN");
        config.options().copyDefaults(true);
        saveConfig();

        try {
            database = new Database();
            database.initDatabase();
        } catch (SQLException e) {
            getLogger().info("Could not initialize database");

            throw new RuntimeException(e);
        }

        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getCommand("link").setExecutor(new CommandHandler(this));
        getCommand("unlink").setExecutor(new CommandHandler(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
