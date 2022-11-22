package com.peeko32213.unusualprehistory.client.model.tool;


import com.peeko32213.unusualprehistory.UnusualPrehistory;
import com.peeko32213.unusualprehistory.common.item.tool.ItemTrikeShield;
import com.peeko32213.unusualprehistory.common.item.tool.ItemVelociShield;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TrikeShieldModel extends AnimatedGeoModel<ItemTrikeShield> {
    @Override
    public ResourceLocation getModelLocation(ItemTrikeShield object) {
        return new ResourceLocation(UnusualPrehistory.MODID, "geo/trike_shield.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemTrikeShield object) {
        return new ResourceLocation(UnusualPrehistory.MODID, "textures/item/trike_shield.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemTrikeShield animatable) {
        return new ResourceLocation(UnusualPrehistory.MODID, "animations/armor.animation.json");
    }
}
