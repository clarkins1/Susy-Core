package supersymmetry.common.entities;

import gregtech.api.GTValues;
import gregtech.api.items.toolitem.ToolClasses;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.ProbeInfo;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import supersymmetry.client.audio.MovingSoundDropPod;
import supersymmetry.client.renderer.particles.SusyParticleFlame;
import supersymmetry.client.renderer.particles.SusyParticleSmoke;

import java.util.Set;

public class EntityDropPod extends EntityLiving implements IAnimatable, IProbeInfoEntityAccessor {

    private static final DataParameter<Boolean> HAS_LANDED = EntityDataManager.createKey(EntityDropPod.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TIME_SINCE_LANDING = EntityDataManager.createKey(EntityDropPod.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TIME_SINCE_SPAWN = EntityDataManager.createKey(EntityDropPod.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> DISABLED = EntityDataManager.createKey(EntityDropPod.class, DataSerializers.BOOLEAN);

    private AnimationFactory factory = new AnimationFactory(this);

    @SideOnly(Side.CLIENT)
    private MovingSoundDropPod soundDropPod;

    public EntityDropPod(World worldIn) {
        super(worldIn);
        this.deathTime = 0;
        this.setSize(1,2);
        this.setEntityInvulnerable(true);
    }

    public EntityDropPod(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setLocationAndAngles(x, y, z, 0.F, 0.F);
    }

    public EntityDropPod(World worldIn, BlockPos pos) {
        this(worldIn, (float) pos.getX() - 0.5F, (float) pos.getY(), (float) pos.getZ() + 0.5);
    }

    public boolean canPlayerDismount() {
        return this.isDead || this.getTimeSinceLanding() >= 30;
    }

    public boolean hasLanded() {
        return this.dataManager.get(HAS_LANDED);
    }

    public void setLanded(boolean landed) {
        this.dataManager.set(HAS_LANDED, landed);
    }

    public int getTimeSinceLanding() {
        return this.dataManager.get(TIME_SINCE_LANDING);
    }

    private void setTimeSinceLanding(int timeSinceLanding) {
        this.dataManager.set(TIME_SINCE_LANDING, timeSinceLanding);
    }

    public boolean hasTakenOff() {
        return this.getTimeSinceLanding() > 200;
    }

    public boolean isDisabled() {
        return this.dataManager.get(DISABLED);
    }

    public void setDisabled(boolean disabled) {
        this.dataManager.set(DISABLED, disabled);
    }

    public void disable() {
        this.setDisabled(true);
    }

    @SideOnly(Side.CLIENT)
    protected void spawnFlightParticles(boolean goingUp) {
        //double offset = goingUp ? 0.0D : 1.5D;
        double offset = goingUp ? 0.2D : 0.5D;
        SusyParticleFlame flame1 = new SusyParticleFlame(
                this.world,
                this.posX + 0.8D,
                this.posY + 0.9D + offset,
                this.posZ + 0.2D,
                1.5 * (GTValues.RNG.nextFloat() + 0.2) * 0.08,
                -1.5,
                1.5 * (GTValues.RNG.nextFloat() - 0.5) * 0.08);
        SusyParticleFlame flame2 = new SusyParticleFlame(
                this.world,
                this.posX + 0.8D,
                this.posY + 0.9D + offset,
                this.posZ - 0.2D,
                1.5 * (GTValues.RNG.nextFloat() + 0.2) * 0.08,
                -1.5,
                1.5 * (GTValues.RNG.nextFloat() - 0.5) * 0.08);
        SusyParticleFlame flame3 = new SusyParticleFlame(
                this.world,
                this.posX - 0.8D,
                this.posY + 0.9D + offset,
                this.posZ + 0.2D,
                1.5 * (GTValues.RNG.nextFloat() - 1.2) * 0.08,
                -1.5,
                1.5 * (GTValues.RNG.nextFloat() - 0.5) * 0.08);
        SusyParticleFlame flame4 = new SusyParticleFlame(
                this.world,
                this.posX - 0.8D,
                this.posY + 0.9D + offset,
                this.posZ - 0.2D,
                1.5 * (GTValues.RNG.nextFloat() - 1.2) * 0.08,
                -1.5,
                1.5 * (GTValues.RNG.nextFloat() - 0.5) * 0.08);

        SusyParticleSmoke smoke1 = new SusyParticleSmoke(
                this.world,
                this.posX + 0.8D,
                this.posY + 0.9D + offset,
                this.posZ + 0.2D,
                1.5 * (GTValues.RNG.nextFloat() + 0.2) * 0.16,
                -1.5,
                1.5 * (GTValues.RNG.nextFloat() - 0.5) * 0.16);
        SusyParticleSmoke smoke2 = new SusyParticleSmoke(
                this.world,
                this.posX + 0.8D,
                this.posY + 0.9D + offset,
                this.posZ - 0.2D,
                1.5 * (GTValues.RNG.nextFloat() + 0.2) * 0.16,
                -1.5,
                1.5 * (GTValues.RNG.nextFloat() - 0.5) * 0.16);
        SusyParticleSmoke smoke3 = new SusyParticleSmoke(
                this.world,
                this.posX - 0.8D,
                this.posY + 0.9D + offset,
                this.posZ + 0.2D,
                1.5 * (GTValues.RNG.nextFloat() - 1.2) * 0.16,
                -1.5,
                1.5 * (GTValues.RNG.nextFloat() - 0.5) * 0.16);
        SusyParticleSmoke smoke4 = new SusyParticleSmoke(
                this.world,
                this.posX - 0.8D,
                this.posY + 0.9D + offset,
                this.posZ - 0.2D,
                1.5 * (GTValues.RNG.nextFloat() - 1.2) * 0.16,
                -1.5,
                1.5 * (GTValues.RNG.nextFloat() - 0.5) * 0.16);

        Minecraft.getMinecraft().effectRenderer.addEffect(smoke1);
        Minecraft.getMinecraft().effectRenderer.addEffect(smoke2);
        Minecraft.getMinecraft().effectRenderer.addEffect(smoke3);
        Minecraft.getMinecraft().effectRenderer.addEffect(smoke4);

        Minecraft.getMinecraft().effectRenderer.addEffect(flame1);
        Minecraft.getMinecraft().effectRenderer.addEffect(flame2);
        Minecraft.getMinecraft().effectRenderer.addEffect(flame3);
        Minecraft.getMinecraft().effectRenderer.addEffect(flame4);
    }

    private void handleCollidedBlocks(boolean above) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                BlockPos pos = new BlockPos(this.posX + i, above ? this.posY + 2 : this.posY - 1, this.posZ + j);
                if (this.world.getBlockState(pos).getBlockHardness(this.world, pos) < 0.3) {
                    this.world.setBlockToAir(pos);
                } else if (above) {
                    this.damageEntity(DamageSource.FLY_INTO_WALL, 1);
                    break;
                }
            }
        }
    }

    private void handleCollidedBlocks() {
        handleCollidedBlocks(false);
    }

    @Override
    public void onDeath(@NotNull DamageSource source) {
        super.onDeath(source);
        this.explode();
    }

    private void explode() {
        this.world.newExplosion(this, this.posX, this.posY, this.posZ, 6, true, true);
        this.setDead();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HAS_LANDED, false);
        this.dataManager.register(TIME_SINCE_LANDING, 0);
        this.dataManager.register(TIME_SINCE_SPAWN, 0);
        this.dataManager.register(DISABLED, false);
    }

    @Override
    public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("landed", this.hasLanded());
        compound.setBoolean("disabled", this.isDisabled());
        compound.setInteger("time_since_landing", this.getTimeSinceLanding());
    }

    @Override
    public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setLanded(compound.getBoolean("landed"));
        this.setDisabled(compound.getBoolean("disabled"));
        this.setTimeSinceLanding(compound.getInteger("time_since_landing"));
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (this.canPlayerDismount()) {
            for (Entity rider : this.getRecursivePassengers()) {
                rider.dismountRidingEntity();
            }
        }

        if (!world.isRemote) {
            if (!this.onGround && this.motionY < 0.0D && !this.isDisabled()) {
                this.motionY *= 0.9D;
            }

            if (!this.hasLanded()) {
                this.handleCollidedBlocks();
                this.getPassengers().forEach(e -> e.fallDistance = 0);
            }

            this.setLanded(this.hasLanded() || this.onGround);

            if (this.hasLanded()) {
                if (this.getTimeSinceLanding() == 0) {
                    int posXRounded = MathHelper.floor(this.posX);
                    int posYBeneath = MathHelper.floor(this.posY - 1.20000000298023224D);
                    int posZRounded = MathHelper.floor(this.posZ);
                    IBlockState blockBeneath = this.world.getBlockState(new BlockPos(posXRounded, posYBeneath, posZRounded));

                    if (blockBeneath.getMaterial() != Material.AIR)
                    {
                        SoundType soundType = blockBeneath.getBlock().getSoundType(blockBeneath, world, new BlockPos(posXRounded, posYBeneath, posZRounded), this);
                        this.playSound(soundType.getBreakSound(), soundType.getVolume() * 3.0F, soundType.getPitch() * 0.2F);
                    }
                }
                this.setTimeSinceLanding(this.getTimeSinceLanding() + 1);
            }

            if (this.hasTakenOff()) {
                if (!this.isDisabled()) {
                    if (this.motionY < 10.D) {
                        if (this.motionY < 1.D) {
                            this.motionY += 0.1;
                        }
                        this.motionY *= 1.1D;
                    }
                    if (this.motionY < 0.1D) {
                        this.handleCollidedBlocks(true);
                    }
                    this.isDead = this.posY > 300;
                }
            }
        } else {
            if (!this.hasLanded() && !this.isDisabled()) {
                this.spawnFlightParticles(false);
            }
            if (this.hasTakenOff() && !this.isDisabled()) {
                this.spawnFlightParticles(true);
            }
        }

        this.dataManager.set(TIME_SINCE_SPAWN, this.dataManager.get(TIME_SINCE_SPAWN) + 1);


        if (world.isRemote && this.soundDropPod != null) {
            if (!this.hasLanded() || this.hasTakenOff()) {
                soundDropPod.startPlaying();
            } else {
                soundDropPod.stopPlaying();
            }
        }

    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        Set<String> toolClasses = itemstack.getItem().getToolClasses(itemstack);
        if (toolClasses.contains(ToolClasses.CROWBAR)) {
            player.swingArm(hand);

            if (!this.world.isRemote)
            {
                this.disable();
                itemstack.damageItem(1, player);
            }
        }
        else if(itemstack.getItem() == Items.FLINT_AND_STEEL) {
            player.swingArm(hand);

            if (!this.world.isRemote)
            {
                this.explode();
                itemstack.damageItem(1, player);
            }
        }
        else if (toolClasses.contains(ToolClasses.WRENCH)) {

        }

        return true;
    }

    @Override
    protected void removePassenger(@NotNull Entity passenger) {
        if (this.canPlayerDismount()) {
            super.removePassenger(passenger);
        }
    }

    @Override
    public void updatePassenger(@NotNull Entity passenger) {
        super.updatePassenger(passenger);
        float xOffset = MathHelper.sin(this.renderYawOffset * 0.1F);
        float zOffset = MathHelper.cos(this.renderYawOffset * 0.1F);
        passenger.setPosition(this.posX + (double)(0.1F * xOffset), this.posY + (double)(this.height * 0.2F) + passenger.getYOffset() + 0.0D, this.posZ - (double)(0.1F * zOffset));

        if (passenger instanceof EntityLivingBase) {
            ((EntityLivingBase)passenger).renderYawOffset = this.renderYawOffset;
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if(this.motionY < -1) {
            this.onDeath(DamageSource.FLY_INTO_WALL);
        }
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return super.isEntityInvulnerable(source) && !source.isExplosion();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void setFire(int seconds) {
        explode();
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {

    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean canBeLeashedTo(@NotNull EntityPlayer player) {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeHitWithPotion() {
        return false;
    }

    @Override
    public void knockBack(@NotNull Entity entityIn, float strength, double xRatio, double zRatio) {

    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        return true;
    }

    @Override
    public void setAir(int air) {
        super.setAir(300);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getTimeSinceLanding() > 0 && this.getTimeSinceLanding() < 140) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.drop_pod.complete", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        if (this.getPassengers().isEmpty())
            super.addPassenger(passenger);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.world.isRemote) {
            setupDropPodSound();
        }
    }

    @SideOnly(Side.CLIENT)
    public void setupDropPodSound() {
        this.soundDropPod = new MovingSoundDropPod(this);
        Minecraft.getMinecraft().getSoundHandler().playSound(this.soundDropPod);
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {
        IProbeConfig config = Config.getRealConfig();

        EntityLivingBase livingBase = (EntityLivingBase) entity;

        // Hack to remove the default progress widget thingy
        if (probeInfo instanceof ProbeInfo info) {
            info.removeElement(info.getElements().get(1));
        }

        if (Tools.show(mode, config.getShowMobHealth())) {
            int health = (int) livingBase.getHealth();
            int maxHealth = (int) livingBase.getMaxHealth();
            int armor = livingBase.getTotalArmorValue();

            probeInfo.progress(health, maxHealth, probeInfo.defaultProgressStyle().showText(false).width(100).height(10).borderColor(0xff5b5b5b).backgroundColor(0xff7d7d7d).filledColor(0xff2f4f4f).alternateFilledColor(0xff7d7d7d));
            // Offset so that the text and the progress bar align properly
            probeInfo.text(String.format("§7Hull Points: %d / %d", health, maxHealth));

            if (armor > 0) {
                probeInfo.progress(armor, armor, probeInfo.defaultProgressStyle().armorBar(true).showText(false).width(80).height(10));
            }
        }
    }
}
