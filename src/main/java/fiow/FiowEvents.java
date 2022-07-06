package fiow;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import fiow.item.KnifeItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Set;

public final class FiowEvents {

    public static final class ForgeHandler {

        private static final DamageSource LOG = new DamageSource("log");
        private static final ResourceLocation WOODEN_TOOLS = new ResourceLocation("forge", "tools/wooden");
        private static final Set<ToolAction> ANY_TOOL = ImmutableSet.copyOf(Iterables.concat(
                ToolActions.DEFAULT_AXE_ACTIONS, ToolActions.DEFAULT_SWORD_ACTIONS,
                ToolActions.DEFAULT_PICKAXE_ACTIONS, ToolActions.DEFAULT_HOE_ACTIONS,
                ToolActions.DEFAULT_SHOVEL_ACTIONS));

        @SubscribeEvent
        public static void onBreakSpeed(final PlayerEvent.BreakSpeed event) {
            // check if player is breaking log
            if(!event.getState().is(BlockTags.LOGS)) {
                return;
            }
            // check held item to determine result
            ItemStack itemStack = event.getPlayer().getMainHandItem();
            if(Fiow.CONFIG.breakLogRequiresAxe()) {
                // check if axe is required but player is not holding axe
                if(!itemStack.canPerformAction(ToolActions.AXE_DIG)) {
                    event.setCanceled(true);
                }
            } else if(Fiow.CONFIG.breakLogHurts() && event.getPlayer().hurtTime == 0) {
                // check if breaking can hurt and player is not holding a tool
                boolean hasAnyTool = false;
                for(ToolAction action : ANY_TOOL) {
                    if(itemStack.canPerformAction(action)) {
                        hasAnyTool = true;
                        break;
                    }
                }
                if(!hasAnyTool) {
                    // determine damage amount
                    // 1 health per 7 hits per log with no tool
                    float amount = 0.1428571428571F;
                    // multiply damage when player has high health (to get the player's attention)
                    if(event.getPlayer().getHealth() / event.getPlayer().getMaxHealth() > 0.75F) {
                        amount *= 4;
                    }
                    event.getPlayer().hurt(LOG, amount);
                }
            }
        }

        @SubscribeEvent
        public static void onAttackTarget(final AttackEntityEvent event) {
            ItemStack itemStack = event.getPlayer().getMainHandItem();
            // cancel when using wooden tools
            if(Fiow.CONFIG.woodenToolsCannotAttack() && itemStack.is(ItemTags.create(WOODEN_TOOLS))) {
                event.setCanceled(true);
                return;
            }
            // cancel when using knife and target is out of range
            if(itemStack.getItem() instanceof KnifeItem) {
                // determine if target is within range
                final AttributeInstance reachDistance = event.getPlayer().getAttribute(ForgeMod.REACH_DISTANCE.get());
                if(reachDistance != null) {
                    // raytrace along the reach distance to check for the hit entity
                    final double length = reachDistance.getValue() - 1.0D;
                    final Vec3 eyeVec = event.getPlayer().getEyePosition(1.0F);
                    final Vec3 lookVec = event.getPlayer().getLookAngle();
                    Vec3 stepVec;
                    Vec3 startVec;
                    Vec3 endVec;
                    AABB box;
                    boolean hitEntity = false;
                    // step over the look vector until an entity is found
                    for (float step = 0, delta = 0.25F, expand = delta * 0.5F; step < length; step += delta) {
                        stepVec = eyeVec.add(lookVec.scale(step));
                        startVec = stepVec.subtract(expand, expand, expand);
                        endVec = stepVec.add(expand, expand, expand);
                        box = new AABB(startVec, endVec);
                        if (!event.getPlayer().level.getEntities(event.getPlayer(), box).isEmpty()) {
                            hitEntity = true;
                            break;
                        }
                    }
                    // cancel event when no entity is within range
                    if(!hitEntity) {
                        event.setCanceled(true);
                        return;
                    }
                }
            }
        }
    }

    public static final class ModHandler {

    }
}
