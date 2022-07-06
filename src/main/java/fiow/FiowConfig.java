package fiow;

import net.minecraftforge.common.ForgeConfigSpec;

public final class FiowConfig {

    // world
    private final ForgeConfigSpec.BooleanValue BREAK_LOG_REQUIRES_AXE;
    private final ForgeConfigSpec.BooleanValue BREAK_LOG_HURTS;
    private final ForgeConfigSpec.BooleanValue REPLACE_IN_LOOT;
    // item
    private final ForgeConfigSpec.BooleanValue WOODEN_TOOLS_CANNOT_ATTACK;

    // world
    private boolean breakLogRequiresAxe;
    private boolean breakLogHurts;
    private boolean replaceInLoot;
    // item
    private boolean woodenToolsCannotAttack;

    public FiowConfig(ForgeConfigSpec.Builder builder) {
        builder.push("world");
        BREAK_LOG_REQUIRES_AXE = builder
                .comment("when true, logs can only be broken using an axe")
                .define("break_log_requires_axe", false);
        BREAK_LOG_HURTS = builder
                .comment("when true, breaking a log with a bare hand damages the player")
                .define("break_log_hurts", true);
        REPLACE_IN_LOOT = builder
                .comment("when true, wooden tools in loot tables are replaced with flint tools")
                .define("replace_in_loot", true);
        builder.pop();
        builder.push("item");
        WOODEN_TOOLS_CANNOT_ATTACK = builder
                .comment("when true, wooden tools cannot be used to attack")
                        .define("wooden_tools_cannot_attack", true);
        builder.pop();
    }

    public void bake() {
        // world
        breakLogRequiresAxe = BREAK_LOG_REQUIRES_AXE.get();
        breakLogHurts = BREAK_LOG_HURTS.get();
        replaceInLoot = REPLACE_IN_LOOT.get();
        // item
        woodenToolsCannotAttack = WOODEN_TOOLS_CANNOT_ATTACK.get();
    }

    public boolean breakLogRequiresAxe() {
        return breakLogRequiresAxe;
    }

    public boolean breakLogHurts() {
        return breakLogHurts;
    }

    public boolean replaceInLoot() {
        return replaceInLoot;
    }

    public boolean woodenToolsCannotAttack() {
        return woodenToolsCannotAttack;
    }
}
