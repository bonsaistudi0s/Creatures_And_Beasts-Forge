package com.cgessinger.creaturesandbeasts.world.gen;

import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.cgessinger.creaturesandbeasts.entities.EndWhaleEntity;
import com.cgessinger.creaturesandbeasts.entities.LilytadEntity;
import com.cgessinger.creaturesandbeasts.entities.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import com.cgessinger.creaturesandbeasts.entities.MinipadEntity;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.cgessinger.creaturesandbeasts.entities.YetiEntity;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public class ModEntitySpawns {

    public static void entitySpawnPlacementRegistry() {
        SpawnPlacements.register(CNBEntityTypes.LITTLE_GREBE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LittleGrebeEntity::checkGrebeSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.LIZARD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LizardEntity::checkLizardSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.CINDERSHELL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CindershellEntity::checkCindershellSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.SPORELING.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SporelingEntity::checkSporelingSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.LILYTAD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LilytadEntity::checkLilytadSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.YETI.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YetiEntity::checkMobSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.MINIPAD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MinipadEntity::checkMinipadSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.END_WHALE.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndWhaleEntity::checkEndWhaleSpawnRules);
    }

}
