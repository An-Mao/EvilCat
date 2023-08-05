package anmao.idoll.evilcat.Entity.Custom.A;

import anmao.idoll.evilcat.Entity._Entity_Type;
import anmao.idoll.evilcat.EvilCat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EvilCat.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class _EntityA_Client {
    @SubscribeEvent
    public static void registerRender(final EntityRenderersEvent.RegisterRenderers event){//
        event.registerEntityRenderer(_Entity_Type.ENTITYA.get(),_EntityA_Render::new);
    }
}
