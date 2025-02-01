package net.limemc.fbp.api.region;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

@Getter
@AllArgsConstructor
@Accessors(chain = true)
public class Point implements Cloneable {
    private World world;

    @Setter
    private int x;
    
    @Setter 
    private int y;
    
    @Setter 
    private int z;

    /**
     * Создает новую точку на основе названия мира и координат.
     *
     * @param world Название мира, в котором находится точка.
     * @param x     Координата X.
     * @param y     Координата Y.
     * @param z     Координата Z.
     * @throws IllegalArgumentException если мир с указанным именем не найден.
     */
    public Point(@NonNull String world, int x, int y, int z) {
        this(Bukkit.getWorld(world), x, y, z);
    }

    /**
     * Создает новую точку на основе объекта {@link Location}.
     *
     * @param loc Объект {@link Location}, координаты и мир которого будут использованы.
     */
    public Point(@NonNull Location loc) {
        this(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @Override
    public Point clone() {
        try {
            return (Point) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported for Point class", e);
        }
    }

    public @NonNull Location toLocation() {
        return new Location(this.world, this.x, this.y, this.z);
    }

    public @NonNull Chunk getChunk() {
        return this.world.getChunkAt(this.toLocation());
    }
}