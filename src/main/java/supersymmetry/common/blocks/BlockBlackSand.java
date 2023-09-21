package supersymmetry.common.blocks;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockBlackSand extends BlockFalling {

    public BlockBlackSand() {
        setHardness(0.6F);
        setTranslationKey("black_sand");
        setSoundType(SoundType.SAND);
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return MapColor.BLACK;
    }
}
