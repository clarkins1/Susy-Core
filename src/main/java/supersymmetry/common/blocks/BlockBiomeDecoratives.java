package supersymmetry.common.blocks;

import gregtech.api.block.IStateHarvestLevel;
import gregtech.api.block.VariantBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public class BlockBiomeDecoratives extends VariantBlock<BlockBiomeDecoratives.BiomeDecorativeType> {

    public BlockBiomeDecoratives() {
        super(Material.CLAY);
        setTranslationKey("biome_decorative");
        setHardness(5.0f);
        setResistance(10.0f);
        setSoundType(SoundType.METAL);
        setHarvestLevel("shovel", 2);
        setDefaultState(getState(BiomeDecorativeType.SULFUR_DEPOSIT));
    }

    public static enum BiomeDecorativeType implements IStringSerializable, IStateHarvestLevel {

        SULFUR_DEPOSIT("sulfur_deposit", 1),
        ORANGE_CLAY("orange_clay", 1);

        private final String name;
        private final int harvestLevel;
        private final String harvestTool;

        private BiomeDecorativeType(String name, int harvestLevel) {
            this.name = name;
            this.harvestLevel = harvestLevel;
            this.harvestTool = "shovel";
        }
        private BiomeDecorativeType(String name, int harvestLevel, String harvestTool) {
            this.name = name;
            this.harvestLevel = harvestLevel;
            this.harvestTool = harvestTool;
        }

        @Nonnull
        public String getName() {
            return this.name;
        }

        public int getHarvestLevel(IBlockState state) {
            return this.harvestLevel;
        }

        public String getHarvestTool(IBlockState state) {
            return this.harvestTool;
        }
    }
}
