package org.dimaserver.statTracker.containers;

public class DiscordUser {
    private final String minecraftUUID;
    private long discordUserId;

    public DiscordUser(String minecraftUUID, long discordUserId) {
        this.minecraftUUID = minecraftUUID;
        this.discordUserId = discordUserId;
    }

    public String getMinecraftUUID() {
        return minecraftUUID;
    }

    public long getDiscordUserId() {
        return discordUserId;
    }

    public void setDiscordUserId(long discordUserId) {
        this.discordUserId = discordUserId;
    }
}
