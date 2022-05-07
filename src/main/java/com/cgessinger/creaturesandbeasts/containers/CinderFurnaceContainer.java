package com.cgessinger.creaturesandbeasts.containers;

import com.cgessinger.creaturesandbeasts.init.CNBContainerTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CinderFurnaceContainer extends RecipeBookMenu<Container> {
    public static final int INGREDIENT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    public static final int SLOT_COUNT = 2;
    private static final int INV_SLOT_START = 2;
    private static final int INV_SLOT_END = 29;
    private static final int USE_ROW_SLOT_START = 29;
    private static final int USE_ROW_SLOT_END = 38;
    private final Container container;
    protected final Level level;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final RecipeBookType recipeBookType;

    public CinderFurnaceContainer(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(2));
    }

    public CinderFurnaceContainer(int id, Inventory inventory, Container container) {
        super(CNBContainerTypes.CINDER_FURNACE_CONTAINER.get(), id);
        this.recipeBookType = RecipeBookType.FURNACE;
        this.recipeType = RecipeType.SMELTING;
        checkContainerSize(container, SLOT_COUNT);
        this.container = container;
        this.level = inventory.player.level;
        this.addSlot(new Slot(container, INGREDIENT_SLOT, 56, 17));
        this.addSlot(new FurnaceResultSlot(inventory.player, container, RESULT_SLOT, 116, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }

    public void fillCraftSlotsStackedContents(StackedContents contents) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible)this.container).fillStackedContents(contents);
        }

    }

    public void clearCraftingContent() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            this.getSlot(i).set(ItemStack.EMPTY);
        }
    }

    public boolean recipeMatches(Recipe<? super Container> recipe) {
        return recipe.matches(this.container, this.level);
    }

    public int getResultSlotIndex() {
        return RESULT_SLOT;
    }

    public int getGridWidth() {
        return 1;
    }

    public int getGridHeight() {
        return 1;
    }

    public int getSize() {
        return SLOT_COUNT;
    }

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (slotIndex == RESULT_SLOT) {
                if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (slotIndex != INGREDIENT_SLOT) {
                if (this.canSmelt(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, INGREDIENT_SLOT, RESULT_SLOT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= INV_SLOT_START && slotIndex < INV_SLOT_END) {
                    if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= USE_ROW_SLOT_START && slotIndex < USE_ROW_SLOT_END && !this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    protected boolean canSmelt(ItemStack stack) {
        return this.level.getRecipeManager().getRecipeFor(this.recipeType, new SimpleContainer(stack), this.level).isPresent();
    }

    public RecipeBookType getRecipeBookType() {
        return this.recipeBookType;
    }

    public boolean shouldMoveToInventory(int slotIndex) {
        return true;
    }
}
