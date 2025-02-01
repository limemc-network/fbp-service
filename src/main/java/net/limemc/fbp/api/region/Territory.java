package net.limemc.fbp.api.region;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.List;

@Getter
public class Territory {
    private final TerritoryTypes type;
    private Point[] points;

    public Territory(@NonNull TerritoryTypes type, Point... points) {
        this.type = type;
        this.points = points;
        this.fixPoints();
    }

    public boolean isChunkInside(@NonNull Chunk chunk) {
        return this.type.getTerritory().isChunkInside(this.points, chunk);
    }

    public void setPoints(Point... points) {
        this.points = points;
        this.fixPoints();
    }

    public boolean isLocationInside(@NonNull Location location) {
        return this.type.getTerritory().isLocationInside(this.points, location);
    }

    public int getTerritoryBlocksCount() {
        return this.type.getTerritory().getTerritoryBlocksCount(this.points);
    }

    public List<Block> getTerritoryBlocks() {
        return this.type.getTerritory().getTerritoryBlocks(this.points);
    }

    private void fixPoints() {
        this.type.getTerritory().fixTerritory(this.points);
    }

    public Collection<Entity> getEntitiesInTerritory() {
        return this.type.getTerritory().getEntitiesInTerritory(this.points);
    }

    @Deprecated
    public void setTerritoryBiome(@NonNull Biome biome) {
        this.type.getTerritory().setTerritoryBiome(this.points, biome);
    }

    public List<int[]> getTerritoryChunkCoordinates() {
        return this.type.getTerritory().getTerritoryChunkCoordinates(this.points);
    }

    public World getWorld() {
        return this.points[0].getWorld();
    }
}