package supersymmetry.client.renderer.handler;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import supersymmetry.SuSyValues;
import supersymmetry.common.entities.EntityDrone;

public class DroneModel extends AnimatedGeoModel<EntityDrone> {

    private static final ResourceLocation modelResource = new ResourceLocation(SuSyValues.MODID, "geo/gatherer_drone.geo.json");
    private static final ResourceLocation textureResource = new ResourceLocation(SuSyValues.MODID, "textures/entities/drone.png");
    private static final ResourceLocation animationResource = new ResourceLocation(SuSyValues.MODID, "animations/drone.animation.json");

    @Override
    public ResourceLocation getModelLocation(EntityDrone entityDrone) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDrone entityDrone) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDrone entityDrone) {
        return animationResource;
    }

}
