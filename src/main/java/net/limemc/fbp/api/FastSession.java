package net.limemc.fbp.api;

import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public interface FastSession {

    void addBlock(@NonNull BlockData blockData);

    void addBlock(@NonNull ItemStack item);

    void addBlock(@NonNull Block block);

    void addBlock(@NonNull Material material);

    void setAsync(boolean async);

    void setApplyPhysics(boolean applyPhysics);

    void setRemoveTileEntity(boolean removeTileEntity);

    void apply();

    void thenRun(@NonNull Runnable runnable);

    long flush();
}
