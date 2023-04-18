package com.peeko32213.unusualprehistory.client.render.dinosaur_renders;


import com.peeko32213.unusualprehistory.client.model.BabyBrachiModel;
import com.peeko32213.unusualprehistory.common.entity.msc.baby.EntityBabyBrachi;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BabyBrachiRenderer extends GeoEntityRenderer<EntityBabyBrachi> {

    public BabyBrachiRenderer(EntityRendererProvider.Context context) {
        super(context, new BabyBrachiModel());
    }
}