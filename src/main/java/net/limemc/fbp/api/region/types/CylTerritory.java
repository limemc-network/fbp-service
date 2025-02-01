package net.limemc.fbp.api.region.types;

import lombok.NonNull;
import net.limemc.fbp.api.region.Point;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.*;

/**
 * Реализация интерфейса {@link ITerritory}
 * Вспомогательный класс для управления цилиндрическими территориями. Территория определяется двумя точками:
 * точкой центра-верха и точкой низа-слева. Радиус цилиндра определяется горизонтальным расстоянием между этими
 * точками. Высота - это вертикальное расстояние.
 *
 * @author Dwyur
 */
public class CylTerritory implements ITerritory {

    /**
     * Проверяет, находится ли заданная позиция в пределах цилиндрической территории, определенной точками.
     * @param points Массив из двух точек: [0] = центр-верх, [1] = низ-слева.
     * @param pos Позиция для проверки.
     * @return True, если позиция находится в пределах территории, false в противном случае.
     */
    @Override
    public boolean isLocationInside(Point[] points, @NonNull Location pos) {
        if (points == null || points.length != 2) {
            return false;
        }

        return isContains(points, pos.getWorld(), pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
    }

    /**
     * Проверяет, находится ли заданный чанк в пределах цилиндрической территории. Эта реализация проверяет,
     * находится ли какой-либо блок в границах чанка внутри цилиндра. Более эффективные алгоритмы могут существовать
     * в зависимости от конкретных потребностей.
     * @param points Массив из двух точек: [0] = центр-верх, [1] = низ-слева.
     * @param chunk Чанк для проверки.
     * @return True, если какая-либо часть чанка находится в пределах территории, false в противном случае.
     */
    @Override
    public boolean isChunkInside(Point[] points, @NonNull Chunk chunk) {
        if (points == null || points.length != 2) {
            return false;
        }

        World world = chunk.getWorld();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();
        int minX = chunkX * 16;
        int minZ = chunkZ * 16;
        int maxX = minX + 15;
        int maxZ = minZ + 15;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = 0; y < world.getMaxHeight(); y++) { // Проверка всех возможных значений y
                    if (isContains(points, world, x, y, z)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Проверяет, находится ли заданная мировоая координата в пределах цилиндрической территории.
     * @param points Массив из двух точек: [0] = центр-верх, [1] = низ-слева.
     * @param world Мир, в котором находится координата.
     * @param x Координата x.
     * @param y Координата y.
     * @param z Координата z.
     * @return True, если координата находится в пределах территории, false в противном случае.
     */
    private boolean isContains(Point[] points, @NonNull World world, int x, int y, int z) {
        if (points == null || points.length != 2) {
            return false;
        }

        Point centerTop = points[0];
        Point leftDown = points[1];
        int radius = centerTop.getX() - leftDown.getX();
        if (world.equals(centerTop.getWorld()) &&
                x >= leftDown.getX() && x <= centerTop.getX() * 2 - leftDown.getX() &&
                z >= leftDown.getZ() && z <= centerTop.getZ() * 2 - leftDown.getZ() &&
                y >= leftDown.getY() && y <= centerTop.getY()) {
            int vecX = centerTop.getX() - x;
            int vecZ = centerTop.getZ() - z;
            return vecX * vecX + vecZ * vecZ <= radius * radius; // Эффективнее, чем Math.sqrt
        }
        return false;
    }

    /**
     * Исправляет массив точек, чтобы гарантировать, что points[0] - это центр-верх, а points[1] - это низ-слева.
     * Также вычисляет правильную нижнюю левую точку на основе радиуса.
     * @param points Массив из двух точек.
     */
    @Override
    public void fixTerritory(Point[] points) {
        if (points == null || points.length != 2) {
            return;
        }

        Point centerTop = points[0];
        Point leftDown = points[1];
        if (centerTop.getY() < leftDown.getY()) {
            Point temp = centerTop;
            centerTop = points[0] = leftDown;
            leftDown = points[1] = temp;
        }

        int radius = centerTop.getX() - leftDown.getX();
        leftDown.setX(centerTop.getX() - radius);
        leftDown.setZ(centerTop.getZ() - radius); // Исправлено для использования радиуса и для Z.
    }

    /**
     * Получает все сущности в пределах цилиндрической территории.
     * @param points Массив из двух точек: [0] = центр-верх, [1] = низ-слева.
     * @return Коллекция сущностей в пределах территории.
     */
    @Override
    public Collection<Entity> getEntitiesInTerritory(Point[] points) {
        if (points == null || points.length != 2) {
            return Collections.emptyList();
        }

        Point centerTop = points[0];
        Point leftDown = points[1];

        int radiusHorizontally = centerTop.getX() - leftDown.getX();
        int radiusVertical = centerTop.getY() - leftDown.getY();

        int x = centerTop.getX();
        int y = leftDown.getY() + radiusVertical / 2;
        int z = centerTop.getZ();

        List<Entity> result = new LinkedList<>();

        for (Entity entity : centerTop.getWorld().getNearbyEntities(new Location(centerTop.getWorld(), x, y, z), radiusHorizontally + 1, radiusVertical, radiusHorizontally)) {
            Location pos = entity.getLocation();

            int vecX = centerTop.getX() - pos.getBlockX();
            int vecZ = centerTop.getZ() - pos.getBlockZ();

            if (vecX * vecX + vecZ * vecZ <= radiusHorizontally * radiusHorizontally) { // Эффективнее
                result.add(entity);
            }
        }
        return result;
    }


    /**
     * Устанавливает биом для всех блоков в пределах цилиндрической территории.
     * @param points Массив из двух точек: [0] = центр-верх, [1] = низ-слева.
     * @param biome Биом для установки.
     */
    @Override
    @Deprecated
    public void setTerritoryBiome(Point[] points, @NonNull Biome biome) {
        if (points == null || points.length != 2) {
            return;
        }

        Point centerTop = points[0];
        Point leftDown = points[1];

        int minX = leftDown.getX();
        int minZ = leftDown.getZ();

        int maxX = centerTop.getX() * 2 - leftDown.getX();
        int maxZ = centerTop.getZ() * 2 - leftDown.getZ();

        World world = centerTop.getWorld();

        int minY = leftDown.getY();
        int maxY = centerTop.getY();

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                for (int y = minY; y <= maxY; y++) {
                    if (isContains(points, world, x, y, z)) {
                        world.setBiome(x, z, biome);
                    }
                }
            }
        }
    }

    /**
     * Получает список координат чанков, которые пересекаются с цилиндрической территорией.
     * @param points Массив из двух точек: [0] = центр-верх, [1] = низ-слева.
     * @return Список координат чанков в виде массивов int [chunkX, chunkZ].
     */
    @Override
    public List<int[]> getTerritoryChunkCoordinates(Point[] points) {
        if (points == null || points.length != 2) {
            return Collections.emptyList();
        }

        Point centerTop = points[0];
        Point leftDown = points[1];

        int radius = centerTop.getX() - leftDown.getX();
        int centerX = centerTop.getX();
        int centerZ = centerTop.getZ();

        List<int[]> chunks = new ArrayList<>();

        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int z = centerZ - radius; z <= centerZ + radius; z++) {

                int chunkX = x >> 4;
                int chunkZ = z >> 4;

                if (!containsChunk(chunks, chunkX, chunkZ)) {
                    chunks.add(new int[] {chunkX, chunkZ});
                }
            }
        }
        return chunks;
    }

    private boolean containsChunk(@NonNull List<int[]> chunks, int chunkX, int chunkZ){
        for (int[] chunk : chunks) {
            if (chunk[0] == chunkX && chunk[1] == chunkZ) {
                return true;
            }
        }

        return false;
    }

    /**
     * Вычисляет приблизительное количество блоков внутри цилиндрической территории. Использует формулу объема цилиндра.
     * @param points Массив из двух точек: [0] = центр-верх, [1] = низ-слева.
     * @return Приблизительное количество блоков.
     */
    @Override
    public int getTerritoryBlocksCount(Point[] points) {
        if (points == null || points.length != 2) {
            return 0;
        }

        Point centerTop = points[0];
        Point leftDown = points[1];

        int radius = centerTop.getX() - leftDown.getX();
        int height = centerTop.getY() - leftDown.getY();

        return (int) (Math.PI * radius * radius * height);
    }

    /**
     * Получает все блоки внутри цилиндрической территории.
     * @param points Массив из двух точек: [0] = центр-верх, [1] = низ-слева.
     * @return Список блоков внутри территории.
     */
    @Override
    public List<Block> getTerritoryBlocks(Point[] points) {
        if (points == null || points.length != 2) {
            return Collections.emptyList();
        }

        List<Block> list = new ArrayList<>(getTerritoryBlocksCount(points));

        Point min = points[1];
        Point max = points[0];

        World world = points[0].getWorld();

        for (int y = min.getY(); y <= max.getY(); ++y) {
            for (int x = min.getX(); x <= max.getX() * 2 - min.getX(); ++x) {
                for (int z = min.getZ(); z <= max.getZ() * 2 - min.getZ(); ++z) {
                    if (isContains(points, world, x, y, z)) {
                        list.add(world.getBlockAt(x, y, z));
                    }
                }
            }
        }
        return list;
    }
}
