package com.cgessinger.creaturesandbeasts.capabilities;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CinderSwordWrapper implements ICinderSwordUpdate, ICapabilityProvider {
    private final LazyOptional<ICinderSwordUpdate> holder = LazyOptional.of(() -> this);

    private int imbuedTicks;
    private boolean imbued;

    public CinderSwordWrapper() {
        this.imbued = false;
        this.imbuedTicks = 0;
    }

    @Override
    public int getImbuedTicks() {
        return this.imbuedTicks;
    }

    @Override
    public int setImbuedTicks(int imbuedTicks) {
        if (imbuedTicks == 0) {
            this.setImbued(false);
        } else {
            this.imbuedTicks = imbuedTicks;
        }

        return imbuedTicks;
    }

    @Override
    public boolean setImbued(boolean isImbued) {
        this.imbuedTicks = isImbued ? 1200 : 0;
        this.imbued = isImbued;
        return isImbued;
    }

    @Override
    public boolean getImbued() {
        return this.imbued;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        return CinderSwordCapability.CINDER_SWORD_CAPABILITY.orEmpty(capability, holder);
    }
}
