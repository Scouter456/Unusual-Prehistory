package com.peeko32213.unusualprehistory.common.entity;

import com.google.common.collect.Lists;
import com.peeko32213.unusualprehistory.common.entity.msc.util.EatLeavesGoal;
import com.peeko32213.unusualprehistory.common.entity.msc.util.dino.EntityBaseDinosaurAnimal;
import com.peeko32213.unusualprehistory.common.entity.msc.util.dino.EntityTameableBaseDinosaurAnimal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class EntityMegatherium extends EntityTameableBaseDinosaurAnimal {
    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(EntityMegatherium.class, EntityDataSerializers.BOOLEAN);
    private Ingredient temptationItems;
    private int eatingTime;


    public EntityMegatherium(EntityType<? extends EntityTameableBaseDinosaurAnimal> entityType, Level level) {
        super(entityType, level);
    }
    public static final int ATTACK_COOLDOWN = 30;
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.16D)
                .add(Attributes.ARMOR, 3.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 3.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3.5D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(1, new EatLeavesGoal(this));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, getTemptationItems(), false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.targetSelector.addGoal(8, (new HurtByTargetGoal(this)));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    private Ingredient getTemptationItems() {
        if(temptationItems == null)
            temptationItems = Ingredient.merge(Lists.newArrayList(
                    Ingredient.of(ItemTags.LEAVES)
            ));

        return temptationItems;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EATING, Boolean.valueOf(false));
    }

    public boolean isEating() {
        return this.entityData.get(EATING).booleanValue();
    }

    public void setEating(boolean eating) {
        this.entityData.set(EATING, Boolean.valueOf(eating));
    }

    public void tick() {
        super.tick();
        if (!this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            this.setEating(true);
        }

        if (isEating()) {
            eatingTime++;
            if (!this.getMainHandItem().is(ItemTags.LEAVES)) {
                for (int i = 0; i < 3; i++) {
                    double d2 = this.random.nextGaussian() * 0.02D;
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItemInHand(InteractionHand.MAIN_HAND)), this.getX() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() * 0.5F, this.getY() + this.getBbHeight() * 0.5F + (double) (this.random.nextFloat() * this.getBbHeight() * 0.5F), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth()) - (double) this.getBbWidth() * 0.5F, d0, d1, d2);
                }
            }
            if (eatingTime % 5 == 0) {
                this.gameEvent(GameEvent.EAT);
                this.playSound(SoundEvents.PANDA_EAT, this.getSoundVolume(), this.getVoicePitch());
            }
            if (eatingTime > 100) {
                this.setEating(false);
                eatingTime = 0;
            }
        }
    }


    //TODO add positionRider to base class so we dont have to do all this again we can just use a method to fetch the offset
    public void positionRider(Entity passenger) {

        float ySin = Mth.sin(this.yBodyRot * ((float) Math.PI / 180F));
        float yCos = Mth.cos(this.yBodyRot * ((float) Math.PI / 180F));
        if(!this.isInSittingPose()) {
            passenger.setPos(this.getX() + (double) (0.5F * ySin), this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset() + 0.4F, this.getZ() - (double) (0.5F * yCos));
            return;
        }
        passenger.setPos(this.getX() + (double) (0.5F * ySin), this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset() - 1.0, this.getZ() - (double) (0.5F * yCos));


    }
    //TODO add getPassengersRidingOffset to base class so we dont have to do all this again
    public double getPassengersRidingOffset() {
        if (this.isInWater()) {
            return -0.5;
        }
        else {
            return 2.0D;
        }
    }
    //TODO add mobinteract to base class so we dont have to do all this again
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!isTame() && itemstack.is( ItemTags.LEAVES)) {
            //int size = itemstack.getCount();
            if(random.nextBoolean()) {
                this.tame(player);
            }
            itemstack.shrink(1);
            this.spawnAtLocation(Items.BOWL);
            return InteractionResult.SUCCESS;
        } else{
            player.startRiding(this);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void performAttack() {
        if(!this.level.isClientSide){
            ServerLevel serverLevel = (ServerLevel) this.level;
            float angle = (0.01745329251F * this.yBodyRot);
            double radius = this.getBbWidth();
            double extraX = radius * Mth.sin((float) (Math.PI + angle));
            double extraZ = radius * Mth.cos(angle);
            BlockPos targetPos = new BlockPos(this.getX() + extraX, this.getY(), this.getZ() + extraZ);
            if(((Player)this.getControllingPassenger()).getItemInHand(InteractionHand.MAIN_HAND).is(Items.WOODEN_SHOVEL)){
                targetPos = targetPos.offset(0,-1,0);
            }
            if(((Player)this.getControllingPassenger()).getItemInHand(InteractionHand.MAIN_HAND).is(Items.IRON_SHOVEL)){
                targetPos = targetPos.offset(0,1,0);
            }

            for(int x = -2; x < 2; x++){
                for(int z = -2; z < 2; z++){
                    for(int y = 0; y < 3; y++) {
                        if(serverLevel.getBlockState(targetPos.offset(x,y,z)).is(BlockTags.DIRT))
                        serverLevel.destroyBlock(targetPos.offset(x, y, z), true);
                    }
                }
            }
        }
    }

    @Override
    public float getStepHeight() {
        return 1.2F;
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (this.isTame()) {
            LivingEntity livingentity = this.getOwner();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.isAlliedTo(entityIn);
            }
        }

        return super.isAlliedTo(entityIn);
    }

    @Override
    public void travel(Vec3 pos) {
        if (this.isAlive()) {
            LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();

            if (this.isVehicle() && livingentity != null) {
                double d0 = 0.08D;
                this.setYRot(livingentity.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(livingentity.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float f = livingentity.xxa * 0.5F;
                float f1 = livingentity.zza;
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                }

                this.setSpeed(0.3F);
                super.travel(new Vec3((double) f, pos.y, (double) f1));

            } else {
                super.travel(pos);
            }
        }
    }

    @Override
    protected SoundEvent getAttackSound() {
        return null;
    }

    @Override
    protected int getKillHealAmount() {
        return 0;
    }

    @Override
    protected boolean canGetHungry() {
        return false;
    }

    @Override
    protected boolean hasTargets() {
        return false;
    }

    @Override
    protected boolean hasAvoidEntity() {
        return false;
    }

    @Override
    protected boolean hasCustomNavigation() {
        return false;
    }

    @Override
    protected boolean hasMakeStuckInBlock() {
        return false;
    }

    @Override
    protected boolean customMakeStuckInBlockCheck(BlockState blockState) {
        return false;
    }

    @Override
    protected TagKey<EntityType<?>> getTargetTag() {
        return null;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            event.getController().setAnimation(new AnimationBuilder().loop("animation.megatherium.move"));
            event.getController().setAnimationSpeed(1.5D);
            return PlayState.CONTINUE;
        }
        else {
            event.getController().setAnimation(new AnimationBuilder().loop("animation.megatherium.idle"));
            event.getController().setAnimationSpeed(1.0D);
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState eatPredicate(AnimationEvent<E> event) {
        if (this.isEating()) {
            event.getController().setAnimation(new AnimationBuilder().loop("animation.megatherium.eating"));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(5);
        AnimationController<EntityMegatherium> controller = new AnimationController<>(this, "controller", 5, this::predicate);
        data.addAnimationController(new AnimationController<>(this, "eatController", 5, this::eatPredicate));
        data.addAnimationController(controller);
    }
}