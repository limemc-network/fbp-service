package net.limemc.fbp.api.utility;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;

import java.util.Objects;

/**
 * Адаптировано под 1.20.4
 *
 * @author Dwyur
 */
@UtilityClass
public class BlockChanger {

    public void setBlock(@NonNull Location location, @NonNull BlockData blockData, boolean applyPhysics, boolean removeTileEntity) {
        World world = Objects.requireNonNull(location.getWorld());

        setBlock(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                blockData, applyPhysics, removeTileEntity);
    }

    private void setBlock(@NonNull World world, int x, int y, int z, BlockData blockData, boolean applyPhysics, boolean removeTileEntity) {
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        Chunk chunk = worldServer.d(x >> 4, z >> 4);
        BlockPosition position = new BlockPosition(x, y, z);
        IBlockData block = ((CraftBlockData) blockData).getState();
        ChunkSection section = chunk.d()[y >> 4];

        if (removeTileEntity)
            removeTileEntityIfExists(worldServer, position);

        if (section == chunk.d()[chunk.a()]) {
            chunk.d()[y >> 4] = section;
        }

        if (applyPhysics)
            section.h().a(x & 15, y & 15, z & 15, block);
        else
            section.h().b(x & 15, y & 15, z & 15, block);

        refreshChunk(position, block);
    }

    private void refreshChunk(@NonNull BlockPosition blockPosition, @NonNull IBlockData blockData) {
        Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().c.a(
                new PacketPlayOutBlockChange(blockPosition, blockData)
        ));
    }

    private void removeTileEntityIfExists(@NonNull WorldServer worldServer, @NonNull BlockPosition position) {
        if (worldServer.capturedTileEntities.containsKey(position))
            worldServer.o(position);
    }
}
