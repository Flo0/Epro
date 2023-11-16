package com.gestankbratwurst.epro.holograms.implementations;

import com.gestankbratwurst.epro.holograms.abstraction.Hologram;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class AutoUpdatedHologram implements Hologram, Runnable {

    private final Hologram hologram;
    private final Consumer<Hologram> updateConsumer;
    @Getter
    private final int updateInterval;
    @Getter
    private int ticksAlive = 0;

    public AutoUpdatedHologram(Hologram hologram, Consumer<Hologram> updateConsumer) {
        this(hologram, updateConsumer, 20);
    }

    public AutoUpdatedHologram(Hologram hologram, Consumer<Hologram> updateConsumer, int updateTicks) {
        this.hologram = hologram;
        this.updateConsumer = updateConsumer;
        this.updateInterval = updateTicks;
    }


    @Override
    public String getId() {
        return hologram.getId();
    }

    @Override
    public int size() {
        return hologram.size();
    }

    @Override
    public void addLine(String line) {
        hologram.addLine(line);
    }

    @Override
    public void setLine(int index, String line) {
        hologram.setLine(index, line);
    }

    @Override
    public void removeLine(int index) {
        hologram.removeLine(index);
    }

    @Override
    public void showTo(Player player) {
        hologram.showTo(player);
    }

    @Override
    public void hideFrom(Player player) {
        hologram.hideFrom(player);
    }

    @Override
    public void teleport(Location location) {
        hologram.teleport(location);
    }

    @Override
    public Location getLocation() {
        return hologram.getLocation();
    }

    @Override
    public String getLine(int index) {
        return hologram.getLine(index);
    }

    @Override
    public void run() {
        if (ticksAlive++ % updateInterval == 0) {
            updateConsumer.accept(this);
        }
    }

    @Override
    public int hashCode() {
        return hologram.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Hologram && hologram.equals(obj);
    }
}
