package com.peeko32213.unusualprehistory.core.registry;

import com.google.common.collect.ImmutableList;
import com.peeko32213.unusualprehistory.UnusualPrehistory;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UPPlacedFeatures {
    public static List<String> placedFeatureList = new ArrayList<>();

    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, UnusualPrehistory.MODID);

    private static final String METEOR_FEATURE_NAME_PLACED = "petrified_wood_forest_feature_placed";


    public static final RegistryObject<PlacedFeature> GINKGO_CHECKED = registerPlacedFeature("ginkgo_checked", () -> new PlacedFeature(UPConfiguredFeatures.GINKGO_TREE.getHolder().orElseThrow(), ImmutableList.of(
            PlacementUtils.filteredByBlockSurvival(UPBlocks.GINKGO_SAPLING.get()))));

    public static final RegistryObject<PlacedFeature> STONE_FOSSIL_ORE = registerPlacedFeature("stone_fossil_ore_placed", () -> new PlacedFeature(UPConfiguredFeatures.STONE_FOSSIL_ORE.getHolder().orElseThrow(),
            commonOrePlacement(10,
            HeightRangePlacement.triangle(VerticalAnchor.absolute(-48), VerticalAnchor.absolute(58)))));
    public static final RegistryObject<PlacedFeature> AMBER_FOSSIL_ORE = registerPlacedFeature("amber_fossil_ore_placed", () -> new PlacedFeature(UPConfiguredFeatures.AMBER_FOSSIL_ORE.getHolder().orElseThrow(), commonOrePlacement(4,
            HeightRangePlacement.triangle(VerticalAnchor.absolute(-48), VerticalAnchor.absolute(58)))));

    public static final RegistryObject<PlacedFeature> JUNGLE_AMBER_FOSSIL_ORE = registerPlacedFeature("jungle_amber_fossil_ore_placed", () -> new PlacedFeature(UPConfiguredFeatures.AMBER_FOSSIL_ORE.getHolder().orElseThrow(), commonOrePlacement(12,
            HeightRangePlacement.uniform(VerticalAnchor.absolute(11), VerticalAnchor.absolute(256)))));
    public static final RegistryObject<PlacedFeature> DEEPSLATE_FOSSIL_ORE = registerPlacedFeature("deepslate_fossil_ore_placed", () -> new PlacedFeature(UPConfiguredFeatures.DEEPSLATE_FOSSIL_ORE.getHolder().orElseThrow(),  commonOrePlacement(17, // VeinsPerChunk
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(1)))));
    public static final RegistryObject<PlacedFeature> PLANT_FOSSIL_ORE = registerPlacedFeature("plant_fossil_ore_placed", () -> new PlacedFeature(UPConfiguredFeatures.PLANT_FOSSIL_ORE.getHolder().orElseThrow(), commonOrePlacement(10,
            HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(48)))));
    public static final RegistryObject<PlacedFeature> DEEPSLATE_PLANT_FOSSIL_ORE = registerPlacedFeature("deepslate_plant_fossil_ore_placed", () -> new PlacedFeature(UPConfiguredFeatures.DEEPSLATE_PLANT_FOSSIL_ORE.getHolder().orElseThrow(),  commonOrePlacement(17, // VeinsPerChunk
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(1)))));

    public static final RegistryObject<PlacedFeature> PETRIFIED_WOOD_FOREST_PLACED = registerPlacedFeature(METEOR_FEATURE_NAME_PLACED, () -> new PlacedFeature(UPConfiguredFeatures.CONFIGURED_PETRIFIED_WOOD_FOREST.getHolder().orElseThrow(), ImmutableList.of(
            RarityFilter.onAverageOnceEvery(10),
            //InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP_TOP_SOLID,
            //CountPlacement.of(80),
            BiomeFilter.biome())));


    private static List<PlacementModifier> orePlacement(PlacementModifier modifier, PlacementModifier modifier2) {
        return List.of(modifier, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
        return orePlacement(CountPlacement.of(count), modifier);
    }


    private static RegistryObject<PlacedFeature> registerPlacedFeature(String name, Supplier<PlacedFeature> feature) {
        placedFeatureList.add(name);
        return PLACED_FEATURES.register(name, feature);
    }

}
