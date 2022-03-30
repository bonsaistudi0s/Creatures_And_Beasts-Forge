package com.cgessinger.creaturesandbeasts.common.items;

import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.BlockSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModSpawnEggItem extends SpawnEggItem
{
	protected static final List<ModSpawnEggItem> UNADDED_EGGS = new ArrayList<>();
	private final Lazy<? extends EntityType<?>> entityTypeSupplier;
	private int variant = -1;

	public ModSpawnEggItem (final RegistryObject<? extends EntityType<?>> entityTypeSupplier, int primaryColorIn, int secondaryColorIn, Properties builder)
	{
		super(null, primaryColorIn, secondaryColorIn, builder);
		this.entityTypeSupplier = Lazy.of(entityTypeSupplier::get);
		UNADDED_EGGS.add(this);
	}

	@Override
	public void inventoryTick (ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (this.variant != -1 && !stack.getOrCreateTag().contains("variant"))
		{
			stack.getOrCreateTag().putInt("variant", this.variant);
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	public static void initSpawnEggs ()
	{
		final Map<EntityType<?>, SpawnEggItem> EGGS = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, null, "BY_ID");
		DefaultDispenseItemBehavior dispenseItemBehavior = new DefaultDispenseItemBehavior()
		{
			@Override
			protected ItemStack execute (BlockSource source, ItemStack stack)
			{
				Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
				EntityType<?> type = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
				type.spawn(source.getLevel(), stack, null, source.getPos(), MobSpawnType.DISPENSER, direction != Direction.UP, false);
				stack.shrink(1);
				return stack;
			}
		};

		for (final SpawnEggItem spawnEggItem : UNADDED_EGGS)
		{
			EGGS.put(spawnEggItem.getType(null), spawnEggItem);
			DispenserBlock.registerBehavior(spawnEggItem, dispenseItemBehavior);
		}
		UNADDED_EGGS.clear();
	}

	@Override
	public EntityType<?> getType (@Nullable CompoundTag p_208076_1_)
	{
		return this.entityTypeSupplier.get();
	}

	public ModSpawnEggItem spawnsVariant (int variant)
	{
		this.variant = variant;
		return this;
	}

	@Override
	public void appendHoverText (ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
	{
		if(stack.hasTag())
		{
			CompoundTag nbt = stack.getTag();
			if(nbt.contains("name"))
			{
				tooltip.add(new TextComponent("Name: " + nbt.getString("name")));
			}
			if(nbt.contains("health"))
			{
				tooltip.add(new TextComponent("Health: " + Math.round(nbt.getFloat("health"))));
			}
		}
	}
}
