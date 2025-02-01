package net.limemc.fbp.api.workload.impl;

import lombok.RequiredArgsConstructor;
import net.limemc.fbp.api.utility.BlockChanger;
import net.limemc.fbp.api.workload.Workload;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

@RequiredArgsConstructor
public class BlockSetWorkload implements Workload {

    private final Location location;
    private final BlockData blockData;
    private final boolean applyPhysics;
    private final boolean removeTileEntity;

    @Override
    public void compute() {
        BlockChanger.setBlock(location, blockData, applyPhysics, removeTileEntity);
    }
}
