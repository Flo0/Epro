package com.gestankbratwurst.epro.statistics.impl;

import com.gestankbratwurst.epro.statistics.EproStatistic;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

public class VanillaStatistic implements EproStatistic {

    private final String spelling;
    private final boolean timeStatistic;
    private final Material material;
    private final Statistic statistic;

    public VanillaStatistic(Statistic statistic) {
        this(statistic.name(), statistic);
    }

    public VanillaStatistic(String spelling, Statistic statistic) {
        this(spelling, statistic, Material.BARRIER);
    }

    public VanillaStatistic(String spelling, Statistic statistic, Material material) {
        this.spelling = spelling;
        this.material = material;
        this.statistic = statistic;
        this.timeStatistic = isTimeStatistic();
    }

    @Override
    public String getSpelling() {
        return spelling;
    }

    @Override
    public int getValueOf(OfflinePlayer player) {
        return material.equals(Material.BARRIER) ? player.getStatistic(statistic) : player.getStatistic(statistic, material);
    }

    @Override
    public String getFormattedString(int value) {
        return timeStatistic ? (value / 20 / 60) + "m" : String.valueOf(value);
    }

    private boolean isTimeStatistic() {
        return statistic == Statistic.PLAY_ONE_MINUTE || statistic == Statistic.TIME_SINCE_REST
                || statistic == Statistic.TIME_SINCE_DEATH || statistic == Statistic.SNEAK_TIME;
    }

}
