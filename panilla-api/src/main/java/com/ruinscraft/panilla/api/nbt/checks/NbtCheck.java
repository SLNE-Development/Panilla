package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public abstract class NbtCheck {

    private final String name;
    private final PStrictness tolerance;
    private final String[] aliases;

    public NbtCheck(String name, PStrictness tolerance, String... aliases) {
        this.name = name;
        this.tolerance = tolerance;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public PStrictness getTolerance() {
        return tolerance;
    }

    public String[] getAliases() {
        return aliases;
    }

    public abstract NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla);

    public enum NbtCheckResult {
        PASS,
        FAIL,
        CRITICAL
    }

}
