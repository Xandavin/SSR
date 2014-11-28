package moze_intel.ssr.gameObjs;

import cpw.mods.fml.common.registry.GameRegistry;
import moze_intel.ssr.utils.SSRConfig;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ObjHandler {
	public static Enchantment SOUL_STEALER;
	public static final SSRCreativeTab CREATIVE_TAB = new SSRCreativeTab();
	public static final Item SOUL_SHARD = new SoulShardItem();
	public static final Block SOUL_CAGE = new SoulCageBlock();

	public static void registerObjs() {
		int counter = 0;
		boolean found = false;

		while (counter <= 256 && !found) {
			if (Enchantment.enchantmentsList[counter] == null) {
				Enchantment SOUL_STEALER = new SoulStealerEnchant(counter,
						SSRConfig.ENCHANT_WEIGHT);
				found = true;
			}
		}
		GameRegistry.registerItem(SOUL_SHARD, "ssr_soul_shard");
		GameRegistry.registerBlock(SOUL_CAGE, SoulCageItem.class,
				"ssr_soul_cage");
		GameRegistry.registerTileEntity(SoulCageTile.class,
				"ssr_soul_cage_tile");

		GameRegistry.addShapedRecipe(new ItemStack(ObjHandler.SOUL_CAGE),
				"III", "IXI", "III", 'I', Blocks.iron_bars);
	}
}
