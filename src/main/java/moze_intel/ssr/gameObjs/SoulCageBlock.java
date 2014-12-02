package moze_intel.ssr.gameObjs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moze_intel.ssr.utils.SSRLogger;
import moze_intel.ssr.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class SoulCageBlock extends Block implements ITileEntityProvider {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public SoulCageBlock() {
        super(Material.iron);
        this.setBlockName("ssr_cage_block");
        this.setCreativeTab(ObjHandler.CREATIVE_TAB);
        this.blockHardness = 3.0F;
        this.blockResistance = 3.0F;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float f1, float f2, float f3) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(x, y, z);

            if (tile == null) {
                SSRLogger.logFatal("ERROR: no tile entity found at coords: " + x + " " + y + " " + " " + z);
                return false;
            }

            if (player.isSneaking()) {
                if (world.getBlockMetadata(x, y, z) == 0) {
                    return false;
                }

                ForgeDirection dir = ForgeDirection.getOrientation(side);

                world.spawnEntityInWorld(new EntityItem(world, x + (dir.offsetX * 1.75D), y + (dir.offsetY * 1.75D) + 0.5D, z + (dir.offsetZ * 1.75D), ((IInventory) tile).decrStackSize(0, 1)));
            } else {
                if (world.getBlockMetadata(x, y, z) != 0) {
                    return false;
                }

                ItemStack stack = player.getHeldItem();

                if (stack == null || stack.getItem() != ObjHandler.SOUL_SHARD || !Utils.isShardBound(stack) || Utils.getShardTier(stack) == 0) {
                    return false;
                }

                ((IInventory) tile).setInventorySlotContents(0, stack.copy());

                if (!player.capabilities.isCreativeMode) {
                    stack.stackSize--;
                }
            }
        }

        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(x, y, z);

            if (tile instanceof SoulCageTile) {
                ((SoulCageTile) tile).checkRedstone();
            }
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        if (!world.isRemote && world.getBlockMetadata(x, y, z) != 0) {
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);
        }
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        if (!world.isRemote && meta != 0) {
            TileEntity tile = world.getTileEntity(x, y, z);

            if (tile == null) {
                SSRLogger.logFatal("ERROR: no tile entity found at coords: " + x + " " + y + " " + " " + z);
                return;
            }

            ItemStack stack = ((IInventory) tile).decrStackSize(0, 1);

            if (stack != null) {
                world.spawnEntityInWorld(new EntityItem(world, x, y, z, stack));
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new SoulCageTile();
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 1;
    }

    @Override
    public int damageDropped(int par1) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < 3; i++) {
            par3List.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        ForgeDirection dir = ForgeDirection.getOrientation(side);

        if (dir == ForgeDirection.UP || dir == ForgeDirection.DOWN) {
            return icons[0];
        }

        return icons[MathHelper.clamp_int(meta, 0, 2)];
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons = new IIcon[3];

        for (int i = 0; i < 3; i++) {
            icons[i] = iconRegister.registerIcon("ssr:cage_" + i);
        }
    }
}
