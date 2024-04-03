package supersymmetry.integration.jei;

import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.PropertyKey;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import supersymmetry.SuSyValues;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class SuSyJustEnoughItemsIntegration implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        for (Material material : GregTechAPI.materialManager.getRegisteredMaterials()) {
            if (material.hasProperty(PropertyKey.FLUID)) {
                SuSyVoidingInfo.
            }
        }
        registry.addRecipes(SuSyVoidingInfo.voidingInfoList, SuSyValues.MODID + ":" + "voiding_info");
    }
}
