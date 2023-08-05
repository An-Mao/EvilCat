package anmao.idoll.evilcat.Entity.Custom.A;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class _EntityA_Render extends GeoEntityRenderer<_EntityA> {
    public _EntityA_Render(EntityRendererProvider.Context renderManager) {
        super(renderManager, new _EntityA_Models());
    }
}
