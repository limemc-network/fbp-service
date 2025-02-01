package net.limemc.fbp.api.region;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.limemc.fbp.api.region.types.CuboidTerritory;
import net.limemc.fbp.api.region.types.CylTerritory;
import net.limemc.fbp.api.region.types.ITerritory;

@Getter
@RequiredArgsConstructor
public enum TerritoryTypes {
    CUBE(new CuboidTerritory()),
    CYL(new CylTerritory());

    private final ITerritory territory;

    public static TerritoryTypes findByName(@NonNull String name) {
        name = name.toUpperCase();
        return valueOf(name);
    }
}