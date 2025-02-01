package net.limemc.fbp.api.region.types;

import net.limemc.fbp.api.region.Point;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.Collection;
import java.util.List;

/**
 * @author Dwyur
 */
public interface ITerritory {
    /**
     * Проверяет, находится ли локация внутри территории, определенной массивом точек.
     *
     * @param points Массив точек, определяющих территорию.
     * @param location Локация для проверки.
     * @return {@code true}, если локация находится внутри территории, иначе {@code false}.
     */
    boolean isLocationInside(Point[] points, Location location);


    /**
     * Проверяет, находится ли чанк внутри территории, определенной массивом точек.
     *
     * @param points Массив точек, определяющих территорию.
     * @param chunk Чанк для проверки.
     * @return {@code true}, если чанк находится внутри территории, иначе {@code false}.
     */
    boolean isChunkInside(Point[] points, Chunk chunk);

    /**
     * Исправляет территорию, определенную массивом точек, если это необходимо.
     *
     * @param points Массив точек, определяющих территорию.
     */
    void fixTerritory(Point[] points);

    /**
     * Возвращает коллекцию всех сущностей, находящихся в территории, определенной массивом точек.
     *
     * @param points Массив точек, определяющих территорию.
     * @return Коллекция сущностей.
     */
    Collection<Entity> getEntitiesInTerritory(Point[] points);

    /**
     * Устанавливает биом для территории, определенной массивом точек.
     *
     * @param points Массив точек, определяющих территорию.
     * @param biome Биом для установки.
     */
    @Deprecated
    void setTerritoryBiome(Point[] points, Biome biome);

    /**
     * Возвращает список координат чанков, которые попадают в территорию, определенную массивом точек.
     *
     * @param points Массив точек, определяющих территорию.
     * @return Список массивов координат чанков (x, z).
     */
    List<int[]> getTerritoryChunkCoordinates(Point[] points);


    /**
     * Возвращает общее количество блоков в территории, определенной массивом точек.
     *
     * @param points Массив точек, определяющих территорию.
     * @return Количество блоков в территории.
     */
    int getTerritoryBlocksCount(Point[] points);

    /**
     * Возвращает список блоков в территории, определенной массивом точек.
     *
     * @param points Массив точек, определяющих территорию.
     * @return Список блоков в территории.
     */
    List<Block> getTerritoryBlocks(Point[] points);
}
