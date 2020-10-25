package com.cgessinger.creaturesandbeasts.common.items;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public ActionResultType onItemUse (ItemUseContext context)
	{
		if(this.variant != -1)
		{
			ItemStack stack = context.getItem();
			stack.getOrCreateTag().putInt("variant", this.variant);
		}
		return super.onItemUse(context);
	}

	public static void initSpawnEggs ()
	{
		final Map<EntityType<?>, SpawnEggItem> EGGS = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, null, "field_195987_b");
		DefaultDispenseItemBehavior dispenseItemBehavior = new DefaultDispenseItemBehavior()
		{
			@Override
			protected ItemStack dispenseStack (IBlockSource source, ItemStack stack)
			{
				Direction direction = source.getBlockState().get(DispenserBlock.FACING);
				EntityType<?> type = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
				type.spawn(source.getWorld(), stack, null, source.getBlockPos(), SpawnReason.DISPENSER, direction != Direction.UP, false);
				stack.shrink(1);
				return stack;
			}
		};

		for (final SpawnEggItem spawnEggItem : UNADDED_EGGS)
		{
			EGGS.put(spawnEggItem.getType(null), spawnEggItem);
			DispenserBlock.registerDispenseBehavior(spawnEggItem, dispenseItemBehavior);
		}
		UNADDED_EGGS.clear();
	}

	@Override
	public EntityType<?> getType (@Nullable CompoundNBT p_208076_1_)
	{
		return this.entityTypeSupplier.get();
	}

	public ModSpawnEggItem spawnsVariant(int variant)
	{
		this.variant = variant;
		return this;
	}
}
