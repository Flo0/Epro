package com.gestankbratwurst.epro.epro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum EproNetworkRank {

  USER(),
  ADMIN(USER);

  private final Set<EproNetworkRank> inheritedRanks;

  EproNetworkRank(EproNetworkRank... inherited) {
    this.inheritedRanks = new HashSet<>();

    List<EproNetworkRank> rankList = new ArrayList<>();
    for (EproNetworkRank rank : inherited) {
      rank.addInherits(rankList);
    }

    inheritedRanks.addAll(rankList);
    this.inheritedRanks.add(this);
  }

  private void addInherits(List<EproNetworkRank> rankList) {
    if (rankList.contains(this)) {
      return;
    }
    rankList.add(this);
    rankList.addAll(inheritedRanks);
    inheritedRanks.forEach(rank -> rank.addInherits(rankList));
  }

  public boolean hasClearanceOf(EproNetworkRank rank) {
    return this.inheritedRanks.contains(rank);
  }

}
