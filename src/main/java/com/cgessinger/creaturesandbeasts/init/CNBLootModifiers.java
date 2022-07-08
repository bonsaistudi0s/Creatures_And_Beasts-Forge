package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Random;

public class CNBLootModifiers {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, CreaturesAndBeasts.MOD_ID);

    public static final RegistryObject<GlobalLootModifierSerializer<NetherBridgeLootModifier>> NETHER_BRIDGE_LOOT_MODIFIER = LOOT_MODIFIERS.register("nether_bridge_loot_modifier", NetherBridgeLootSerializer::new);

    protected static class NetherBridgeLootModifier extends LootModifier {
        private final Random rand = new Random();

        /**
         * Constructs a LootModifier.
         *
         * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
         */
        protected NetherBridgeLootModifier(LootItemCondition[] conditionsIn) {
            super(conditionsIn);
        }

        @Nonnull
        @Override
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {

            if (rand.nextInt(73) < 5) {
                generatedLoot.add(new ItemStack(CNBItems.CINDERSHELL_SHELL_SHARD.get(), rand.nextInt(3) + 1));
            }

            return generatedLoot;
        }
    }

    public static class NetherBridgeLootSerializer extends GlobalLootModifierSerializer<NetherBridgeLootModifier> {

        @Override
        public NetherBridgeLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditionsIn) {
            return new NetherBridgeLootModifier(conditionsIn);
        }

        @Override
        public JsonObject write(NetherBridgeLootModifier instance) {
            return null;
        }
    }
}
