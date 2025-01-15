package org.dimaserver.statTracker.containers;

import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Objects;

public class PlayerStats {
    private final String uuid;
    private String username;
    private Date lastLogin;
    private Date lastLogout;
    private int timesJoined;
    private long discordUserId;
    private String twitchUsername;
    private String ipAddress;
    private String geoLocation;
    private long playtime; // Total time spent on the server in milliseconds
    private String lastSeenWorld; // Last world the player was in
    private int kills; // Number of kills
    private int deaths; // Number of deaths
    private int mobKills; // Number of mobs killed

    public PlayerStats(String uuid) {
        this.uuid = uuid;
        this.username = "";
        this.lastLogin = new Date();
        this.lastLogout = new Date();
        this.timesJoined = 0;
        this.discordUserId = 0;
        this.twitchUsername = "";
        this.ipAddress = "";
        this.geoLocation = "";
    }

    public PlayerStats(String uuid, String username) {
        this.uuid = uuid;
        this.username = username;
        this.lastLogin = new Date();
        this.lastLogout = new Date();
        this.timesJoined = 0;
        this.discordUserId = 0;
        this.twitchUsername = "";
        this.ipAddress = "";
        this.geoLocation = "";
    }

    public PlayerStats(String uuid, String username, String ipAddress) {
        this.uuid = uuid;
        this.username = username;
        this.lastLogin = new Date();
        this.lastLogout = new Date();
        this.timesJoined = 0;
        this.discordUserId = 0;
        this.twitchUsername = "";
        this.ipAddress = ipAddress;
        this.geoLocation = "";
    }

    //#region Getters and Setters
    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getLastLogout() {
        return lastLogout;
    }

    public void setLastLogout(Date lastLogout) {
        this.lastLogout = lastLogout;
    }

    public int getTimesJoined() {
        return timesJoined;
    }

    public void setTimesJoined(int timesJoined) {
        this.timesJoined = timesJoined;
    }

    public long getDiscordUserId() {
        return discordUserId;
    }

    public void setDiscordUserId(long discordUserId) {
        this.discordUserId = discordUserId;
    }

    public String getTwitchUsername() {
        return twitchUsername;
    }

    public void setTwitchUsername(String twitchUsername) {
        this.twitchUsername = twitchUsername;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    public String getLastSeenWorld() {
        return lastSeenWorld;
    }

    public void setLastSeenWorld(String lastSeenWorld) {
        this.lastSeenWorld = lastSeenWorld;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getMobKills() {
        return mobKills;
    }

    public void setMobKills(int mobKills) {
        this.mobKills = mobKills;
    }
    //#endregion

    public void addJoin() {
        this.timesJoined++;
    }

    public void updateIP(Player p) {
        try {
            this.ipAddress = Objects.requireNonNull(p.getAddress()).getAddress().getHostAddress();
        } catch (NullPointerException e) {
            this.ipAddress = "";
        }
    }
}
