package net.limemc.fbp.api;

import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.limemc.fbp.api.region.Territory;
import net.limemc.fbp.api.utility.BlockChanger;
import net.limemc.fbp.api.workload.Workload;
import net.limemc.fbp.api.workload.WorkloadRunnable;
import net.limemc.fbp.api.workload.impl.BlockSetWorkload;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

class FastSessionImpl implements FastSession {

    @Setter
    public boolean async;

    @Setter
    private boolean applyPhysics;

    @Setter
    private boolean removeTileEntity;

    private Location location;
    private Territory territory;

    public FastSessionImpl(final @NonNull Location location, boolean async, boolean applyPhysics, boolean removeTileEntity) {
        this.location = location;
        this.async = async;
        this.applyPhysics = applyPhysics;
        this.removeTileEntity = removeTileEntity;
    }

    public FastSessionImpl(final @NonNull Territory territory, boolean async, boolean applyPhysics, boolean removeTileEntity) {
        this.territory = territory;
        this.async = async;
        this.applyPhysics = applyPhysics;
        this.removeTileEntity = removeTileEntity;
    }

    private BlockData blockData;
    private Long startTimeMillis;

    @Override
    public void addBlock(@NonNull BlockData blockData) {
        this.blockData = blockData;
    }

    @Override
    public void addBlock(@NonNull Material material) {
        addBlock(material.createBlockData());
    }

    @Override
    public void addBlock(@NonNull ItemStack item) {
        addBlock(item.getType());
    }

    @Override
    public void addBlock(@NonNull Block block) {
        addBlock(block.getBlockData());
    }

    @Override
    public void apply() {
        if (startTimeMillis == null) {
            startTimeMillis = System.currentTimeMillis();
        }

        if (location == null && territory == null) {
            throw new NullPointerException("Location or Territory is null!");
        }

        if (async) {
            WorkloadRunnable workloadRunnable = new WorkloadRunnable();

            if (location != null) {
                Workload workload = new BlockSetWorkload(location, blockData, applyPhysics, removeTileEntity);
                workloadRunnable.addWorkload(workload);
            } else {
                for (Block block : territory.getTerritoryBlocks()) {
                    Workload workload = new BlockSetWorkload(block.getLocation(), blockData, applyPhysics, removeTileEntity);
                    workloadRunnable.addWorkload(workload);
                }
            }

        } else {
            if (location != null) {
                BlockChanger.setBlock(location, blockData, applyPhysics, removeTileEntity);
            } else {
                for (Block block : territory.getTerritoryBlocks()) {
                    BlockChanger.setBlock(block.getLocation(), blockData, applyPhysics, removeTileEntity);
                }
            }
        }
    }

    @Override
    public long flush() {
        System.gc();
        return startTimeMillis > 0 ? System.currentTimeMillis() - startTimeMillis : -1L;
    }
}
