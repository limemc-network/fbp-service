package net.limemc.fbp.api;

import lombok.NonNull;
import net.limemc.fbp.api.region.Territory;
import org.bukkit.Location;

public class FastSessionBuilder {

    private final FastSession session;

    public static FastSessionBuilder builder(@NonNull Territory territory) {
        return new FastSessionBuilder(territory);
    }

    public static FastSessionBuilder builder(@NonNull Location location) {
        return new FastSessionBuilder(location);
    }

    public FastSessionBuilder(@NonNull Territory territory) {
        this.session = new FastSessionImpl(territory, false, true, true);
    }

    public FastSessionBuilder(@NonNull Location location) {
        this.session = new FastSessionImpl(location, false, true, true);
    }

    public FastSessionBuilder async(boolean async) {
        session.setAsync(async);
        return this;
    }

    public FastSessionBuilder applyPhysics(boolean physics) {
        session.setApplyPhysics(physics);
        return this;
    }

    public FastSessionBuilder removeTileEntity(boolean removeTileEntity) {
        session.setRemoveTileEntity(removeTileEntity);
        return this;
    }

    public FastSessionBuilder thenRun(@NonNull Runnable runnable) {
        session.thenRun(runnable);
        return this;
    }

    public FastSession build() {
        return session;
    }
}
