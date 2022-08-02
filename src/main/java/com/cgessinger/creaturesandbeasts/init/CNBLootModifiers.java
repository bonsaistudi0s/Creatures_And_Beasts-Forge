package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Random;

public class CNBLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, CreaturesAndBeasts.MOD_ID);

    public static final RegistryObject<Codec<NetherBridgeLootModifier>> NETHER_BRIDGE_LOOT_MODIFIER = LOOT_MODIFIERS.register("nether_bridge_loot_modifier", () -> NetherBridgeLootModifier.CODEC);

    protected static class NetherBridgeLootModifier extends LootModifier {
        public static final Codec<NetherBridgeLootModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, NetherBridgeLootModifier::new));

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

        @Override
        public Codec<? extends IGlobalLootModifier> codec() {
            return CODEC;
        }
    }
}
