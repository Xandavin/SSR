package moze_intel.ssr.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import moze_intel.ssr.gameObjs.ObjHandler;
import moze_intel.ssr.utils.EntityMapper;
import moze_intel.ssr.utils.SSRConfig;
import moze_intel.ssr.utils.SSRLogger;
import moze_intel.ssr.utils.Utils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class PlayerKillEntityEvent {
    @SubscribeEvent
    public void onEntityKill(LivingDeathEvent event) {
        World world = event.entity.worldObj;

        if (world.isRemote || !(event.entity instanceof EntityLiving) || !(event.source.getEntity() instanceof EntityPlayer)) {
            return;
        }

        EntityLiving dead = (EntityLiving) event.entity;

        if (dead.getEntityData().getBoolean("SSR")) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.source.getEntity();

        String entName = EntityList.getEntityString(dead);

        if (entName == null || entName.isEmpty()) {
            SSRLogger.logFatal("Player killed entity with no unlocalized name: " + dead);
            return;
        }

        if (!EntityMapper.isEntityValid(entName)) {
            return;
        }

        if (dead instanceof EntitySkeleton && ((EntitySkeleton) dead).getSkeletonType() == 1) {
            entName = "Wither Skeleton";
        }

        ItemStack shard = Utils.getShardFromInv(player, entName);

        if (shard != null) {
            if (!Utils.isShardBound(shard)) {
                Utils.setShardBoundEnt(shard, entName);
                Utils.writeEntityHeldItem(shard, dead);
            }

            int soulStealer = EnchantmentHelper.getEnchantmentLevel(ObjHandler.SOUL_STEALER.effectId, player.getHeldItem());
            soulStealer *= SSRConfig.ENCHANT_KILL_BONUS;

            Utils.increaseShardKillCount(shard, (short) (1 + soulStealer));
        }
    }
}
