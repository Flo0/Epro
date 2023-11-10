package com.gestankbratwurst.epro.epro.proxy;

import com.gestankbratwurst.epro.bridge.RemoteEvent;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserTransferRequest implements RemoteEvent {

  public static final String CHANNEL = "_USER_TRANSFER_";

  private UUID userId;
  private String server;
  private boolean indicated;

}
