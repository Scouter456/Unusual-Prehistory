package com.peeko32213.unusualprehistory.common.entity.msc.util;

import com.peeko32213.unusualprehistory.common.data.LootFruitCodec;
import com.peeko32213.unusualprehistory.common.data.LootFruitJsonManager;
import com.peeko32213.unusualprehistory.common.entity.msc.util.dino.EntityBaseDinosaurAnimal;
import com.peeko32213.unusualprehistory.core.registry.UPBlocks;
import com.peeko32213.unusualprehistory.core.registry.UPItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class TradeGoal extends Goal {
    private static final TargetingConditions TRADE_TARGETING = TargetingConditions.forNonCombat().range(10.0D).ignoreLineOfSight();
    private final TargetingConditions targetingConditions;
    protected final EntityBaseDinosaurAnimal mob;
    private final Ingredient items;
    @Nullable
    protected Player player;


    public TradeGoal(EntityBaseDinosaurAnimal pMob, Ingredient pItems) {
        this.mob = pMob;
        this.items = pItems;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.targetingConditions = TRADE_TARGETING.copy().selector(this::shouldTrade);
    }

    private boolean shouldTrade(LivingEntity p_148139_) {
        return this.items.test(p_148139_.getMainHandItem()) || this.items.test(p_148139_.getOffhandItem());
    }


    @Override
    public boolean canUse() {
        this.player = this.mob.level.getNearestPlayer(this.targetingConditions, this.mob);
        return this.player != null;
    }


    @Override
    public boolean canContinueToUse() {
        this.player = this.mob.level.getNearestPlayer(this.targetingConditions, this.mob);
        return this.player != null;
    }


    @Override
    public void tick() {
        super.tick();
        Item item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        List<LootFruitCodec> lootFruits = LootFruitJsonManager.getLoot(item, null);
        if (lootFruits == null) return;
        if (!lootFruits.isEmpty()) {
            ItemStack lootFruit = new ItemStack(UPBlocks.FRUIT_LOOT_BOX.get());
            CompoundTag lootFruitTag = lootFruit.getOrCreateTag();
            int color = lootFruits.get(0).getColor().getValue();
            lootFruitTag.putInt("color", color);
            lootFruitTag.put("tradeItem", item.getDefaultInstance().serializeNBT());
            lootFruit.setTag(lootFruitTag);
            this.mob.setItemInHand(InteractionHand.MAIN_HAND, lootFruit);
            if (this.mob.isTrading()) {
                ItemEntity lootFruitEntity = new ItemEntity(this.mob.level, this.mob.getX(), this.mob.getY(), this.mob.getZ(), lootFruit);
                this.player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                this.mob.level.addFreshEntity(lootFruitEntity);
                this.mob.setIsTrading(false);
                this.stop();
            }
        }
    }

    @Override
    public void stop() {
        this.mob.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        super.stop();
    }
}
