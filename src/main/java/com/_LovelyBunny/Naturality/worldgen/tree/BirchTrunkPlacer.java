package com._LovelyBunny.Naturality.worldgen.tree;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BirchTrunkPlacer extends TrunkPlacer {
    public static final Codec<BirchTrunkPlacer> CODEC = RecordCodecBuilder.create(pineTrunkPlacerInstance ->
            trunkPlacerParts(pineTrunkPlacerInstance).apply(pineTrunkPlacerInstance, BirchTrunkPlacer::new));

    public BirchTrunkPlacer(int pBaseHeight, int pHeightRandA, int pHeightRandB) {
        super(pBaseHeight, pHeightRandA, pHeightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return NaturalityTrunkPlacers.BIRCH_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter, RandomSource pRandom, int height, BlockPos pPos, TreeConfiguration pConfig) {

        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom);

        setDirtAt(pLevel, pBlockSetter, pRandom, pPos.below(), pConfig);

        Random random = new Random();

        int branchHeightRange = 5 - 4 + 1;
        int branchHeight =  random.nextInt(branchHeightRange) + 4;

        for(int i = 0; i < height; i++) {
            placeLog(pLevel, pBlockSetter, pRandom, pPos.above(i), pConfig);

        if (height > 7) {
            if(i % 2 == 0 && pRandom.nextBoolean()) {
                if(pRandom.nextFloat() > 0.25f) {
                    for (int x = 1; x < 2; x++) {
                        pBlockSetter.accept(pPos.above(height - branchHeight).relative(direction, x), ((BlockState)
                                Function.identity().apply(pConfig.trunkProvider.getState(pRandom, pPos).setValue(RotatedPillarBlock.AXIS, direction.getAxis()))));
                    }
                }
            }
        }
    }

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(pPos.above(height), 0, false));
    }
}