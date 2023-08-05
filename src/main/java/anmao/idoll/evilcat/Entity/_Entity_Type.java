package anmao.idoll.evilcat.Entity;

import anmao.idoll.evilcat.Entity.Custom.A._EntityA;
import anmao.idoll.evilcat.EvilCat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class _Entity_Type {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EvilCat.MOD_ID);
    public static final RegistryObject<EntityType<_EntityA>> ENTITYA = ENTITY_TYPES.register("entitya",()->EntityType.Builder.of(_EntityA::new, MobCategory.MONSTER).sized(1.0f,1.0f).build(new ResourceLocation(EvilCat.MOD_ID,"entitya").toString()));
}
