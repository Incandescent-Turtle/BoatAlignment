package mod.icy_turtle.boatalignment.util;

import mod.icy_turtle.boatalignment.BoatAlignment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;

import java.lang.reflect.Field;

public class RotationUtils
{
    private RotationUtils(){}

    public static void snapBoat(BoatEntity boat)
    {
        lockToClosestLockAngle(boat);
        try {
            Field yawVel = BoatEntity.class.getDeclaredField("yawVelocity");
            yawVel.setAccessible(true);
            yawVel.set(boat, 0);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static float roundToClosestLockAngle(float input)
    {
        return Math.round(input/ BoatAlignment.ANGLE_TO_LOCK_AT)* BoatAlignment.ANGLE_TO_LOCK_AT;
    }

    public static void lockToClosestLockAngle(Entity entity)
    {
        entity.setYaw(roundToClosestLockAngle(entity.getYaw()));
    }
}