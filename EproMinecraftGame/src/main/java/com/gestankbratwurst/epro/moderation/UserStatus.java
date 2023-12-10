package com.gestankbratwurst.epro.moderation;

public enum UserStatus {

  NOT_REGISTERED,
  REGISTERED,
  BANNED,
  ERROR;

  public static UserStatus fromId(int id) {
    return values()[id];
  }

}
