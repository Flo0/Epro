package com.gestankbratwurst.epro.model;

import com.gestankbratwurst.epro.bridge.RemoteEvent;
import com.gestankbratwurst.epro.utils.SerializedUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoSyncUpdateEvent<K, V> implements RemoteEvent {

  private ContextKey<K, V> contextKey;
  private SerializedUnit<K> updatedKey;

}
