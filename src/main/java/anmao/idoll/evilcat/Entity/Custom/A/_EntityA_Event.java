package anmao.idoll.evilcat.Entity.Custom.A;

import anmao.idoll.evilcat.Entity._Entity_Type;
import anmao.idoll.evilcat.EvilCat;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EvilCat.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class _EntityA_Event {
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {

        event.put(_Entity_Type.ENTITYA.get(), _EntityA.setAttributes());
    }
}
