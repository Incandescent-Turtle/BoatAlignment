package mod.icy_turtle.boatalignment.event;

import mod.icy_turtle.boatalignment.BoatAlignment;
import mod.icy_turtle.boatalignment.util.ModUtils;
import mod.icy_turtle.boatalignment.util.RotationUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class KeyInputHandler
{
    public static final String KEY_CATEGORY_BOAT_ALIGNMENT = "key.category.boatalignment.alignment";

    public static final String KEY_ALIGN_PLAYER = "key.boatalignment.align",
            KEY_ORIENTATION_LOCK = "key.boatalignment.lockRotation";

    public static KeyBinding alignKey, orientationLockKey;

    public static void register()
    {
        alignKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ALIGN_PLAYER,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                KEY_CATEGORY_BOAT_ALIGNMENT
        ));

        orientationLockKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ORIENTATION_LOCK,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                KEY_CATEGORY_BOAT_ALIGNMENT
        ));

        ClientTickEvents.END_CLIENT_TICK.register(KeyInputHandler::registerKeyInputs);
    }

    private static void registerKeyInputs(MinecraftClient client)
    {
        if (client.player == null) return;
        var player = client.player;

        if (alignKey.wasPressed())
        {
            if (player.getVehicle() instanceof BoatEntity boat)
            {
                RotationUtils.snapBoat(boat);
            }
        }

        if (orientationLockKey.wasPressed())
        {
            BoatAlignment.isOrientationLocked = !BoatAlignment.isOrientationLocked;
            Text state = !BoatAlignment.isOrientationLocked
                    ? getPositiveMessage("UNLOCKED")
                    : getNegativeMessage("LOCKED");
            player.sendMessage(Texts.join(List.of(Text.of("Orientation is"), state), Text.of(" ")));
            if (player.getVehicle() instanceof BoatEntity boat)
            {
                RotationUtils.snapBoat(boat);
            }
        }
    }

    private static Text getPositiveMessage(String msg)
    {
        return ModUtils.getBoldAndColored(msg, Formatting.GREEN).get(0);
    }

    private static Text getNegativeMessage(String msg)
    {
        return ModUtils.getBoldAndColored(msg, Formatting.RED).get(0);
    }
}