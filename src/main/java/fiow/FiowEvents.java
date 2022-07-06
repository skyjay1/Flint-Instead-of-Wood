package fiow;

import fiow.item.KnifeItem;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class FiowEvents {

    public static final class ForgeHandler {

        private static final DamageSource LOG = new DamageSource("log");
        private static final ResourceLocation WOODEN_TOOLS = new ResourceLocation("forge", "tools/wooden");

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
                if(!itemStack.getToolTypes().contains(ToolType.AXE)) {
                    event.setCanceled(true);
                }
            } else if(Fiow.CONFIG.breakLogHurts()) {
                // check if breaking hurts and player is not holding a tool, and player can be hurt
                if(itemStack.getToolTypes().isEmpty() && !(itemStack.getItem() instanceof SwordItem)
                    && event.getPlayer().hurtTime == 0) {
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
            if(Fiow.CONFIG.woodenToolsCannotAttack() && itemStack.getItem().is(ItemTags.createOptional(WOODEN_TOOLS))) {
                event.setCanceled(true);
                return;
            }
            // cancel when using knife and target is out of range
            if(itemStack.getItem() instanceof KnifeItem) {
                // determine if target is within range
                final ModifiableAttributeInstance reachDistance = event.getPlayer().getAttribute(ForgeMod.REACH_DISTANCE.get());
                if(reachDistance != null) {
                    // raytrace along the reach distance to check for the hit entity
                    final double length = reachDistance.getValue() - 1.0D;
                    final Vector3d eyeVec = event.getPlayer().getEyePosition(1.0F);
                    final Vector3d lookVec = event.getPlayer().getLookAngle();
                    Vector3d stepVec;
                    Vector3d startVec;
                    Vector3d endVec;
                    AxisAlignedBB box;
                    boolean hitEntity = false;
                    // step over the look vector until an entity is found
                    for (float step = 0, delta = 0.25F, expand = delta * 0.5F; step < length; step += delta) {
                        stepVec = eyeVec.add(lookVec.scale(step));
                        startVec = stepVec.subtract(expand, expand, expand);
                        endVec = stepVec.add(expand, expand, expand);
                        box = new AxisAlignedBB(startVec, endVec);
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
