package moze_intel.ssr.utils;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EntityMapper {
    private static final List<String> VALID_ENTITIES = new ArrayList<String>();

    public static void init() {
        for (Map.Entry<Class, String> entry : ((HashMap<Class, String>) EntityList.classToStringMapping).entrySet()) {
            if (VALID_ENTITIES.contains(entry.getValue())) {
                SSRLogger.logInfo("Skipping mapping for " + entry.getValue() + ": already mapped.");
                continue;
            }

            if (IBossDisplayData.class.isAssignableFrom(entry.getKey())) {
                SSRLogger.logInfo("Skipped mapping for " + entry.getValue() + ": detected as boss.");
                continue;
            }

            if (EntityLiving.class.isAssignableFrom(entry.getKey())) {
                VALID_ENTITIES.add(entry.getValue());
            }
        }

        VALID_ENTITIES.add("Wither Skeleton");

        SSRLogger.logInfo("Finished entity mapping (" + VALID_ENTITIES.size() + " entries).");
    }

    public static boolean isEntityValid(String entName) {
        return VALID_ENTITIES.contains(entName);
    }

    public static EntityLiving getNewEntityInstance(World world, String ent) {
        if (ent.equals("Wither Skeleton")) {
            EntitySkeleton skele = new EntitySkeleton(world);
            skele.setSkeletonType(1);
            return skele;
        }

        return (EntityLiving) EntityList.createEntityByName(ent, world);
    }
}
