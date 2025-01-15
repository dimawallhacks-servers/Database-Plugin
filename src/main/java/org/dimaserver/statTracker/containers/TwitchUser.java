package org.dimaserver.statTracker.containers;

public class TwitchUser {
    private final String minecraftUUID;
    private String twitchUsername;

    public TwitchUser(String minecraftUUID, String twitchUsername) {
        this.minecraftUUID = minecraftUUID;
        this.twitchUsername = twitchUsername;
    }

    public String getMinecraftUUID() {
        return minecraftUUID;
    }

    public String getTwitchUsername() {
        return twitchUsername;
    }

    public void setTwitchUsername(String twitchUsername) {
        this.twitchUsername = twitchUsername;
    }
}
