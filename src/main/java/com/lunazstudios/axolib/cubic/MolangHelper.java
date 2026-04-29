package com.lunazstudios.axolib.cubic;

import com.lunazstudios.axolib.math.molang.MolangParser;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class MolangHelper {

    public static void registerVars(MolangParser parser) {
        parser.register("query.anim_time");
        parser.register("query.life_time");
        parser.register("query.ground_speed");
        parser.register("query.yaw_speed");
        parser.register("query.head_yaw");
        parser.register("query.head_pitch");
        parser.register("query.velocity");
        parser.register("query.age");
        parser.register("query.limb_swing");
        parser.register("query.limb_swing_amount");
    }

    public static void setVariables(MolangParser parser, LivingEntity entity, float frame, float partialTick) {
        if (entity == null) {
            parser.setValue("query.anim_time",         frame / 20.0);
            parser.setValue("query.life_time",         frame / 20.0);
            parser.setValue("query.ground_speed",      0);
            parser.setValue("query.yaw_speed",         0);
            parser.setValue("query.head_yaw",          0);
            parser.setValue("query.head_pitch",        0);
            parser.setValue("query.velocity",          0);
            parser.setValue("query.age",               0);
            parser.setValue("query.limb_swing",        0);
            parser.setValue("query.limb_swing_amount", 0);
            return;
        }

        float bodyYaw   = Mth.lerp(partialTick, entity.yBodyRotO,  entity.yBodyRot);
        float headYaw   = Mth.lerp(partialTick, entity.yHeadRotO,  entity.yHeadRot);
        float headPitch = Mth.lerp(partialTick, entity.xRotO,      entity.getXRot());

        double dx = entity.getDeltaMovement().x;
        double dy = entity.getDeltaMovement().y;
        double dz = entity.getDeltaMovement().z;
        double groundSpeed = Math.sqrt(dx * dx + dz * dz);
        double velocity    = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (entity.onGround() && dy < 0 && groundSpeed < 0.001) velocity = 0;

        parser.setValue("query.anim_time",         frame / 20.0);
        parser.setValue("query.life_time",         (entity.tickCount + partialTick) / 20.0);
        parser.setValue("query.ground_speed",      groundSpeed);
        parser.setValue("query.yaw_speed",         Math.toRadians(entity.yBodyRot - entity.yBodyRotO));
        parser.setValue("query.head_yaw",          headYaw - bodyYaw);
        parser.setValue("query.head_pitch",        headPitch);
        parser.setValue("query.velocity",          velocity);
        parser.setValue("query.age",               entity.tickCount + partialTick);
        parser.setValue("query.limb_swing",        entity.walkAnimation.position(partialTick));
        parser.setValue("query.limb_swing_amount", entity.walkAnimation.speed(partialTick));
    }
}
