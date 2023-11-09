package supersymmetry.common;

import com.codetaylor.mc.pyrotech.modules.core.item.ItemMaterial;
import com.codetaylor.mc.pyrotech.modules.tech.basic.event.RecipeRepeat;
import gregtech.api.util.GTTeleporter;
import gregtech.api.util.TeleportHandler;
import gregtech.common.items.MetaItems;
import gregtechfoodoption.item.GTFOMetaItem;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.jetbrains.annotations.Nullable;
import supersymmetry.Supersymmetry;
import supersymmetry.client.audio.SusyCoreSounds;
import supersymmetry.common.entities.EntityDropPod;

@Mod.EventBusSubscriber(modid = Supersymmetry.MODID)
public class EventHandlers {

    private static final String FIRST_SPAWN = Supersymmetry.MODID + ".first_spawn";

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {

        NBTTagCompound playerData = event.player.getEntityData();
        NBTTagCompound data = playerData.hasKey(EntityPlayer.PERSISTED_NBT_TAG) ? playerData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG) : new NBTTagCompound();

        if(!event.player.getEntityWorld().isRemote && !data.getBoolean(FIRST_SPAWN)) {

            data.setBoolean(FIRST_SPAWN, true);
            playerData.setTag(EntityPlayer.PERSISTED_NBT_TAG, data);

            EntityDropPod dropPod = new EntityDropPod(event.player.getEntityWorld(), event.player.posX, event.player.posY + 256, event.player.posZ);

            GTTeleporter teleporter = new GTTeleporter((WorldServer) event.player.world, event.player.posX, event.player.posY + 256, event.player.posZ);
            TeleportHandler.teleport(event.player, event.player.dimension, teleporter, event.player.posX, event.player.posY + 256, event.player.posZ);

            event.player.getEntityWorld().spawnEntity(dropPod);
            event.player.startRiding(dropPod);

            event.player.addItemStackToInventory(GTFOMetaItem.EMERGENCY_RATIONS.getStackForm(10));
            event.player.addItemStackToInventory(MetaItems.PROSPECTOR_LV.getChargedStack(100000));
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        EnumFacing face = event.getFace();
        EntityPlayer player = event.getEntityPlayer();
        final IBlockState state = event.getWorld().getBlockState(event.getPos());
        ItemStack stack = player.getHeldItemMainhand();
        if (!world.isRemote && state.getMaterial() == Material.ROCK) {
            checkKnapping(world, stack, player, pos, face, Items.FLINT, ItemMaterial.EnumType.FLINT_SHARD.asStack(2));
            checkKnapping(world, stack, player, pos, face, Items.BONE, ItemMaterial.EnumType.BONE_SHARD.asStack(2));
        }
    }

    private static void checkKnapping(World world, ItemStack stack, EntityPlayer player, BlockPos pos, EnumFacing face, Item input, ItemStack output) {
        if (stack.getItem().equals(input)) {
                if (world.rand.nextFloat() < 0.33) {
                    if (world.rand.nextFloat() < 0.9) {
                        world.spawnEntity(new EntityItem(world, pos.getX() + 0.5 + 0.5 * face.getXOffset(),
                                pos.getY() + 0.5 + 0.5 * face.getYOffset(),
                                pos.getZ() + 0.5 + 0.5 * face.getZOffset(), output));
                    }
                    stack.shrink(1);
                    player.setHeldItem(player.getActiveHand(), stack);
                }
                world.playSound(null, pos, SusyCoreSounds.KNAPPING, SoundCategory.BLOCKS, (float) Math.random() + 0.5F, (float) Math.random() + 0.8F);
        }
    }

    @SubscribeEvent
    public static void onTrySpawnPortal(BlockEvent.PortalSpawnEvent event) {
        event.setCanceled(true);
    }

}
