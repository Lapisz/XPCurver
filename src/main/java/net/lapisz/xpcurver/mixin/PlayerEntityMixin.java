package net.lapisz.xpcurver.mixin;

import net.lapisz.xpcurver.XPCurver;
import net.lapisz.xpcurver.util.Interpreter;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Shadow public int experienceLevel;

    @Inject(at = @At("HEAD"), method = "getNextLevelExperience", cancellable = true)
    private void modifiedCurve(CallbackInfoReturnable<Integer> cir) {
        if (this.experienceLevel >= 30) {
            if (XPCurver.ex_tree_31_plus != null) {
                cir.setReturnValue(
                        Math.max(1, (int) Interpreter.evaluate(XPCurver.ex_tree_31_plus, this.experienceLevel))
                );
            }
        } else if (this.experienceLevel >= 15) {
            if (XPCurver.ex_tree_16_30 != null) {
                cir.setReturnValue(
                        Math.max(1, (int) Interpreter.evaluate(XPCurver.ex_tree_16_30, this.experienceLevel))
                );
            }
        } else {
            if (XPCurver.ex_tree_1_15 != null) {
                cir.setReturnValue(
                        Math.max(1, (int) Interpreter.evaluate(XPCurver.ex_tree_1_15, this.experienceLevel))
                );
            }
        }
    }
}
