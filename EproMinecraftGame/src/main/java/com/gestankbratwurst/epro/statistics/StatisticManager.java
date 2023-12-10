package com.gestankbratwurst.epro.statistics;

import com.gestankbratwurst.epro.EproCore;
import com.gestankbratwurst.epro.holograms.abstraction.Hologram;
import com.gestankbratwurst.epro.statistics.command.StatisticHologramCommand;
import com.gestankbratwurst.epro.tasks.TaskManager;
import org.bukkit.*;

import java.util.*;

public class StatisticManager {

    public StatisticManager() {
        EproCore.getPaperCommandManager().registerCommand(new StatisticHologramCommand());
    }

    public StatisticResult getCombinedStatistic(EproStatistic statistic) {
        int total = 0;
        List<StatisticEntry> topPlayers = new ArrayList<>();

        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            int playerStat = statistic.getValueOf(player);
            topPlayers.add(new StatisticEntry(player, playerStat));
            total += playerStat;
        }
        topPlayers.sort(Comparator.comparingInt(StatisticEntry::value).reversed());
        return new StatisticResult(total, topPlayers);
    }

    public void createHologram(Location location, EproStatistic statistic, int updateInterval, String id) {
        var hologramManger = EproCore.getHologramManager();
        hologramManger.createUpdatedHologram(location, id, hologram -> updateHologram(hologram, statistic), updateInterval);
    }

    private void updateHologram(Hologram hologram, EproStatistic statistic) {
        TaskManager.runTaskAsync(() -> {
            StatisticResult statResult = getCombinedStatistic(statistic);
            TaskManager.runTask(() -> {
                if (hologram.size() < 8) {
                    for (int i = 0; i < 8; i++) {
                        hologram.addLine("§e");
                    }
                }
                hologram.setLine(0, "§4" + statistic.getSpelling());
                hologram.setLine(1, "§eGesamt: " + statistic.getFormattedString(statResult.total));
                hologram.setLine(2, "§e - - - - - -");
                for (int i = 0; i < 5; i++) {
                    if (statResult.topPlayers.size() <= i) {
                        hologram.setLine(3 + i, "§e" + (i+1) + ". ~ ~ ~ ~ ~");
                        continue;
                    }
                    StatisticEntry entry = statResult.topPlayers.get(i);
                    hologram.setLine(3 + i, "§e" + (i+1) + ". " + entry.player.getName() + ": " + statistic.getFormattedString(entry.value()));
                }
            });
        });
    }


    private record StatisticEntry(OfflinePlayer player, int value) {}

    public record StatisticResult(int total, List<StatisticEntry> topPlayers) {}
}