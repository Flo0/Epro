package com.gestankbratwurst.epro.statistics;

import org.bukkit.OfflinePlayer;

public interface EproStatistic {

    String getSpelling();
    int getValueOf(OfflinePlayer player);
    String getFormattedString(int value);
}
