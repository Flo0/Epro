package com.gestankbratwurst.epro.statistics.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import com.gestankbratwurst.epro.EproCore;
import com.gestankbratwurst.epro.EproMinecraftGame;
import com.gestankbratwurst.epro.statistics.impl.VanillaStatistic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

@CommandAlias("holostats")
public class StatisticHologramCommand extends BaseCommand {

    @Subcommand("create material")
    public void onSpawnMaterialStat(Player sender, String id, Statistic statistic, Material material, float x, float y, float z, String... spelling) {
        Location location = new Location(sender.getWorld(), x, y, z);
        EproMinecraftGame.getStatisticManager().createHologram(location, new VanillaStatistic(String.join(" ", spelling), statistic, material), 30, id);
    }

    @Subcommand("create")
    public void onSpawnStat(Player sender, String id, Statistic statistic, float x, float y, float z, String... spelling) {
        Location location = new Location(sender.getWorld(), x, y, z);
        EproMinecraftGame.getStatisticManager().createHologram(location, new VanillaStatistic(String.join(" ", spelling), statistic), 30, id);
    }

    @Subcommand("remove")
    public void onRemove(Player sender, String id) {
        EproCore.getHologramManager().deleteHologram(id);
    }

}
