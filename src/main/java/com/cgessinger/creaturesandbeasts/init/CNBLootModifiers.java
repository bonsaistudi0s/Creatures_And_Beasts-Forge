package com.cgessinger.creaturesandbeasts.init;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class CNBLootModifiers {
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
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {

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
