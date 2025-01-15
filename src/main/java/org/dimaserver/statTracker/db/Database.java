package org.dimaserver.statTracker.db;

import org.bukkit.configuration.file.FileConfiguration;
import org.dimaserver.statTracker.StatTracker;
import org.dimaserver.statTracker.containers.PlayerStats;

import java.sql.*;

import static org.bukkit.Bukkit.getLogger;

public class Database {
    private Connection connection;

    public Connection getConnection() throws SQLException{
        FileConfiguration config = StatTracker.getPlugin(StatTracker.class).getConfiguration();

        if (connection != null) {
            return connection;
        }

        String url = config.getString("mysql.url");
        String username = config.getString("mysql.username");
        String password = config.getString("mysql.password");

        assert url != null;
        this.connection = DriverManager.getConnection(url, username, password);

        System.out.println("Connected to database");

        return this.connection;
    }

    public void initDatabase() throws SQLException {
        Statement statement = getConnection().createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS player_stats (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "username VARCHAR(16)," +
                "last_login DATETIME," +
                "last_logout DATETIME," +
                "times_joined INT," +
                "discord_user_id BIGINT," +
                "twitch_username VARCHAR(16)," +
                "ip_address VARCHAR(16)," +
                "geo_location VARCHAR(16))" +
                "playtime BIGINT," +
                "last_seen_world VARCHAR(16)," +
                "kills INT," +
                "deaths INT," +
                "mob_kills INT";
                
        statement.execute(sql);

        statement.close();

        getLogger().info("Table created");
    }

    public PlayerStats findPlayerStatsByUUID(String uuid) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM player_stats WHERE uuid = ?");
        statement.setString(1, uuid);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            PlayerStats playerStats = new PlayerStats(uuid);
            playerStats.setUsername(results.getString("username"));
            playerStats.setLastLogin(results.getDate("last_login"));
            playerStats.setLastLogout(results.getDate("last_logout"));
            playerStats.setTimesJoined(results.getInt("times_joined"));
            playerStats.setDiscordUserId(results.getInt("discord_user_id"));
            playerStats.setTwitchUsername(results.getString("twitch_username"));
            playerStats.setIpAddress(results.getString("ip_address"));
            playerStats.setGeoLocation(results.getString("geo_location"));

            statement.close();

            getLogger().info("Player stats found for " + playerStats.getUsername());

            return playerStats;
        }

        statement.close();

        getLogger().info("Player stats not found for " + uuid);

        return null;
    }

    public void createPlayerStats(PlayerStats stats) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("INSERT INTO player_stats (" +
                        "uuid, " +
                        "username, " +
                        "last_login, " +
                        "last_logout, " +
                        "times_joined, " +
                        "discord_user_id," +
                        "twitch_username," +
                        "ip_address," +
                        "geo_location) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

        statement.setString(1, stats.getUuid());
        statement.setString(2, stats.getUsername());
        statement.setDate(3, new Date(stats.getLastLogin().getTime()));
        statement.setDate(4, new Date(stats.getLastLogout().getTime()));
        statement.setInt(5, stats.getTimesJoined());
        statement.setLong(6, stats.getDiscordUserId());
        statement.setString(7, stats.getTwitchUsername());
        statement.setString(8, stats.getIpAddress());
        statement.setString(9, stats.getGeoLocation());

        statement.executeUpdate();

        statement.close();

        getLogger().info("Player stats created for " + stats.getUsername());
    }

    public void updatePlayerStats(PlayerStats stats) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE player_stats SET " +
                        "username = ?, " +
                        "last_login = ?, " +
                        "last_logout = ?, " +
                        "times_joined = ?," +
                        "discord_user_id = ?," +
                        "twitch_username = ?," +
                        "ip_address = ?," +
                        "geo_location = ? WHERE uuid = ?");
        statement.setString(1, stats.getUsername());
        statement.setDate(2, new Date(stats.getLastLogin().getTime()));
        statement.setDate(3, new Date(stats.getLastLogout().getTime()));
        statement.setInt(4, stats.getTimesJoined());
        statement.setLong(5, stats.getDiscordUserId());
        statement.setString(6, stats.getTwitchUsername());
        statement.setString(7, stats.getIpAddress());
        statement.setString(8, stats.getGeoLocation());
        statement.setString(9, stats.getUuid());

        statement.executeUpdate();

        statement.close();
    }

    public void linkDiscordAccount(String uuid, long discordUserId) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE player_stats SET discord_user_id = ? WHERE uuid = ?");
        statement.setLong(1, discordUserId);
        statement.setString(2, uuid);
        statement.executeUpdate();
        statement.close();
    }

    public void unlinkDiscordAccount(String uuid) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE player_stats SET discord_user_id = NULL WHERE uuid = ?");
        statement.setString(1, uuid);
        statement.executeUpdate();
        statement.close();
    }

    public void linkTwitchAccount(String uuid, String twitchUsername) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE player_stats SET twitch_username = ? WHERE uuid = ?");
        statement.setString(1, twitchUsername);
        statement.setString(2, uuid);
        statement.executeUpdate();
        statement.close();
    }

    public void unlinkTwitchAccount(String uuid) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE player_stats SET twitch_username = NULL WHERE uuid = ?");
        statement.setString(1, uuid);
        statement.executeUpdate();
        statement.close();
    }
}
