package org.dimaserver.statTracker.listeners;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.dimaserver.statTracker.StatTracker;
import org.dimaserver.statTracker.containers.PlayerStats;

import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;

public class Listeners implements Listener {
    private final StatTracker plugin;

    public Listeners(StatTracker plugin) {
        this.plugin = plugin;
    }

    private String GetGeolocationByIP(String ip) {
        IPinfo ipinfo = new IPinfo.Builder()
                .setToken(this.plugin.getConfiguration().getString("ipinfo.token"))
                .build();

        try {
            IPResponse response = ipinfo.lookupIP(ip);

            getLogger().info("Country: " + response.getCountryName());
            return response.getCountryName();
        } catch (RateLimitedException e) {
            getLogger().info("Rate limited");
            throw new RuntimeException(e);
        }
    }

    private PlayerStats getPlayerStatsFromDB(Player p) throws SQLException {
        PlayerStats stats = this.plugin.getDatabase().findPlayerStatsByUUID(p.getUniqueId().toString());

        if (stats == null) {
            stats = new PlayerStats(p.getUniqueId().toString(), p.getName());
            this.plugin.getDatabase().createPlayerStats(stats);

            return stats;
        }

        return stats;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        try {
            PlayerStats stats = getPlayerStatsFromDB(p);
            stats.setLastLogin(new Date());
            stats.addJoin();
            String ipAddress = Objects.requireNonNull(p.getAddress()).getHostString();
            stats.setIpAddress(ipAddress);
            stats.setGeoLocation(GetGeolocationByIP(ipAddress));

            if (!p.getName().equals(stats.getUsername())) {
                stats.setUsername(p.getName());

                getLogger().info("Player " + p.getName() + " has changed their username (was " + stats.getUsername() + ")");
            }

            this.plugin.getDatabase().updatePlayerStats(stats);

            getLogger().info("Player " + p.getName() + " has joined for the " + stats.getTimesJoined() + " time at " + new Date());
        } catch (SQLException ex) {
            getLogger().info("Could not update player stats");

            throw new RuntimeException(ex);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        try {
            PlayerStats stats = getPlayerStatsFromDB(p);
            stats.setLastLogout(new Date());

            this.plugin.getDatabase().updatePlayerStats(stats);

            getLogger().info("Player " + p.getName() + " has quit at " + new Date());
        } catch (SQLException ex) {
            getLogger().info("Could not update player stats");

            throw new RuntimeException(ex);
        }
    }
}
