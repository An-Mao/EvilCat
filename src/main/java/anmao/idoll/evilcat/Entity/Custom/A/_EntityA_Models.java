package anmao.idoll.evilcat.Entity.Custom.A;

import anmao.idoll.evilcat.EvilCat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class _EntityA_Models extends DefaultedEntityGeoModel<_EntityA> {
    public _EntityA_Models() {
        super(new ResourceLocation(EvilCat.MOD_ID,"entitya"));
    }

    @Override
    public RenderType getRenderType(_EntityA animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
