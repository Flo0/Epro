package com.gestankbratwurst.epro.epro;

import lombok.Data;

import java.util.UUID;

@Data
public class EproNetworkUser {

  public EproNetworkUser(UUID minecraftUid) {
    this.minecraftUid = minecraftUid;
  }

  public EproNetworkUser() {
    this(null);
  }

  private String lastSeenMinecraftName;
  private UUID minecraftUid;
  private Long registeredDiscordId;
  private EproNetworkRank eproNetworkRank = EproNetworkRank.USER;
  private long lastLoginTime = 0L;

}
