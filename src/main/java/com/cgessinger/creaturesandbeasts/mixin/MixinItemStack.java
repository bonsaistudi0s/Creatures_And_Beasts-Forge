package com.cgessinger.creaturesandbeasts.mixin;

import com.cgessinger.creaturesandbeasts.capabilities.CinderSwordCapability;
import com.cgessinger.creaturesandbeasts.capabilities.ICinderSwordUpdate;
import com.cgessinger.creaturesandbeasts.items.CinderSwordItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @Shadow
    @Nullable
    private CompoundTag tag;

    @Inject(method = "copy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setPopTime(I)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void CNB_updateStackCopyCapabilities(CallbackInfoReturnable<ItemStack> cir, ItemStack itemStack) {
        ItemStack stackThis = (ItemStack) (Object) this;

        if (!stackThis.isEmpty() && stackThis.getItem() instanceof CinderSwordItem) {
            LazyOptional<ICinderSwordUpdate> newCapability = itemStack.getCapability(CinderSwordCapability.CINDER_SWORD_CAPABILITY);
            LazyOptional<ICinderSwordUpdate> oldCapability = stackThis.getCapability(CinderSwordCapability.CINDER_SWORD_CAPABILITY);

            newCapability.map(handler -> handler.setImbuedTicks(oldCapability.map(ICinderSwordUpdate::getImbuedTicks).get()));

            if (this.tag != null) {
                itemStack.setTag(this.tag.copy());
            }

            cir.setReturnValue(itemStack);
        }
    }

}
