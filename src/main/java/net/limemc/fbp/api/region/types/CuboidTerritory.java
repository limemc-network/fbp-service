package net.limemc.fbp.api.region.types;

import lombok.NonNull;
import net.limemc.fbp.api.region.Point;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.NumberConversions;

import java.util.*;

/**
 * Реализация интерфейса {@link ITerritory} для работы с кубическими областями.
 * Предоставляет методы для проверки принадлежности точки или чанка к области,
 * изменения биома, получения сущностей и блоков в области.
 *
 * @author Dwyur
 */
public class CuboidTerritory implements ITerritory {
    /**
     * Проверяет, находится ли указанная позиция {@link Location} внутри кубической области,
     * заданной двумя точками {@link Point}.
     *
     * @param points Массив из двух точек, представляющих противоположные углы куба.
     * @param pos    Позиция {@link Location} для проверки.
     * @return true, если позиция находится внутри куба, иначе false.
     */
    @Override
    public boolean isLocationInside(Point[] points, @NonNull Location pos) {
        if (points == null || points.length != 2) {
            return false;
        }

        try {
            Point min = points[0];
            Point max = points[1];

            return min.getWorld().equals(pos.getWorld()) &&
                    pos.getBlockX() >= min.getX() &&
                    pos.getBlockY() >= min.getY() &&
                    pos.getBlockZ() >= min.getZ() &&
                    pos.getBlockX() <= max.getX() &&
                    pos.getBlockY() <= max.getY() &&
                    pos.getBlockZ() <= max.getZ();
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Проверяет, находится ли указанный чанк {@link Chunk} внутри кубической области,
     * заданной двумя точками {@link Point}.
     *
     * @param points Массив из двух точек, представляющих противоположные углы куба.
     * @param chunk  Чанк {@link Chunk} для проверки.
     * @return true, если чанк находится внутри куба, иначе false.
     */
    @Override
    public boolean isChunkInside(Point[] points, @NonNull Chunk chunk) {
        if (points == null || points.length != 2) {
            return false;
        }

        try {
            Point min = points[0];
            Point max = points[1];

            return min.getWorld().equals(chunk.getWorld()) &&
                    chunk.getX() >= NumberConversions.floor(min.getX() >> 4) &&
                    chunk.getZ() >= NumberConversions.floor(min.getZ() >> 4) &&
                    chunk.getX() <= NumberConversions.floor(max.getX() >> 4) &&
                    chunk.getZ() <= NumberConversions.floor(max.getZ() >> 4);
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Упорядочивает координаты двух точек {@link Point} так, чтобы первая точка
     * представляла минимальные значения координат, а вторая - максимальные.
     *
     * @param points Массив из двух точек, которые нужно упорядочить.
     */
    @Override
    public void fixTerritory(Point[] points) {
        if (points == null || points.length != 2) {
            return;
        }

        Point min = points[0];
        Point max = points[1];
        int x1 = min.getX();
        int x2 = max.getX();
        int y1 = min.getY();
        int y2 = max.getY();
        int z1 = min.getZ();
        int z2 = max.getZ();

        min.setX(Math.min(x1, x2));
        min.setY(Math.min(y1, y2));
        min.setZ(Math.min(z1, z2));

        max.setX(Math.max(x1, x2));
        max.setY(Math.max(y1, y2));
        max.setZ(Math.max(z1, z2));
    }

    /**
     * Возвращает коллекцию сущностей {@link Entity}, находящихся внутри кубической области,
     * заданной двумя точками {@link Point}.
     *
     * @param points Массив из двух точек, представляющих противоположные углы куба.
     * @return Коллекция сущностей {@link Entity}, находящихся в области.
     */
    @Override
    public Collection<Entity> getEntitiesInTerritory(Point[] points) {
        if (points == null || points.length != 2) {
            return Collections.emptyList();
        }

        Point min = points[0], max = points[1];

        double worldX = (min.getX() + max.getX()) * 0.5;
        double worldY = (min.getY() + max.getY()) * 0.5;
        double worldZ = (min.getZ() + max.getZ()) * 0.5;

        double distanceX = (max.getX() - min.getX()) * 0.5;
        double distanceY = (max.getY() - min.getY()) * 0.5;
        double distanceZ = (max.getZ() - min.getZ()) * 0.5;

        return min.getWorld().getNearbyEntities(new Location(min.getWorld(), worldX, worldY, worldZ), distanceX, distanceY, distanceZ);
    }

    /**
     * Устанавливает указанный биом {@link Biome} для всех блоков внутри кубической области,
     * заданной двумя точками {@link Point}.
     *
     * @param points Массив из двух точек, представляющих противоположные углы куба.
     * @param biome  Биом {@link Biome}, который нужно установить.
     */
    @Override
    @Deprecated
    public void setTerritoryBiome(Point[] points, @NonNull Biome biome) {
        if (points == null || points.length != 2) {
            return;
        }

        Point min = points[0];
        Point max = points[1];

        int minX = min.getX();
        int minZ = min.getZ();
        int maxX = max.getX();
        int maxZ = max.getZ();
        World world = min.getWorld();

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                world.setBiome(x, z, biome);
            }
        }
    }

    /**
     * Возвращает список координат чанков, которые полностью или частично находятся
     * внутри кубической области, заданной двумя точками {@link Point}.
     *
     * @param points Массив из двух точек, представляющих противоположные углы куба.
     * @return Список массивов int[2], где int[0] - координата X чанка, int[1] - координата Z чанка.
     */
    @Override
    public List<int[]> getTerritoryChunkCoordinates(Point[] points) {
        if (points == null || points.length != 2) {
            return Collections.emptyList();
        }

        List<int[]> list = new LinkedList<>();

        Point min = points[0];
        Point max = points[1];

        int minX = min.getX();
        int minZ = min.getZ();
        int maxX = max.getX();
        int maxZ = max.getZ();

        int maxChunkX = maxX >> 4;
        int maxChunkZ = maxZ >> 4;

        for (int chunkX = minX >> 4; chunkX <= maxChunkX; ++chunkX) {
            for (int chunkZ = minZ >> 4; chunkZ <= maxChunkZ; ++chunkZ) {
                list.add(new int[]{chunkX, chunkZ});
            }
        }

        return list;
    }

    /**
     * Возвращает общее количество блоков внутри кубической области,
     * заданной двумя точками {@link Point}.
     *
     * @param points Массив из двух точек, представляющих противоположные углы куба.
     * @return Общее количество блоков в области.
     */
    @Override
    public int getTerritoryBlocksCount(Point[] points) {
        if (points == null || points.length != 2) {
            return 0;
        }

        Point min = points[0];
        Point max = points[1];

        int x = Math.abs(max.getX() - min.getX()) + 1;
        int y = Math.abs(max.getY() - min.getY()) + 1;
        int z = Math.abs(max.getZ() - min.getZ()) + 1;

        return x * y * z;
    }

    /**
     * Возвращает список блоков {@link Block}, находящихся внутри кубической области,
     * заданной двумя точками {@link Point}.
     *
     * @param points Массив из двух точек, представляющих противоположные углы куба.
     * @return Список блоков {@link Block} в области.
     */
    @Override
    public List<Block> getTerritoryBlocks(Point[] points) {
        if (points == null || points.length != 2) {
            return Collections.emptyList();
        }

        List<Block> list = new ArrayList<>(this.getTerritoryBlocksCount(points));

        Point min = points[0];
        Point max = points[1];

        World world = min.getWorld();

        for (int y = min.getY(); y <= max.getY(); ++y) {
            for (int x = min.getX(); x <= max.getX(); ++x) {
                for (int z = min.getZ(); z <= max.getZ(); ++z) {
                    list.add(world.getBlockAt(x, y, z));
                }
            }
        }

        return list;
    }
}
