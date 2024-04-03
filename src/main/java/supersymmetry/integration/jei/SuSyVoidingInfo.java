package supersymmetry.integration.jei;

import gregtech.api.unification.material.Material;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import supersymmetry.common.metatileentities.multi.VoidingMultiblockBase;

import java.util.ArrayList;
import java.util.List;

public class SuSyVoidingInfo implements IRecipeWrapper {

    public static List<SuSyVoidingInfo> voidingInfoList = new ArrayList<>();
    FluidStack inputFluidStack;
    ItemStack voidingMultiStack;

    public SuSyVoidingInfo(FluidStack fluidStack, VoidingMultiblockBase voidingMulti) {
        this.inputFluidStack = fluidStack;
        this.voidingMultiStack = voidingMulti.getStackForm(1);

        voidingInfoList.add(this);
    }

    public static void voidingInfosFromMaterial(Material material) {

    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.FLUID, this.inputFluidStack);
    }


}
