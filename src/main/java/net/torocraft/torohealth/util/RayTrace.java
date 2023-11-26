package net.torocraft.torohealth.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Predicate;

public class RayTrace {
    private static final Predicate<Entity> isVisible =
            entity -> !entity.isSpectator() && entity.isPickable();
    private static final Minecraft minecraft = Minecraft.getInstance();

    public static LivingEntity getEntityInCrosshair(float partialTicks, double reachDistance) {
        Minecraft client = Minecraft.getInstance();
        Entity viewer = client.getCameraEntity();

        if (viewer == null) return null;

        Vec3 position = viewer.getEyePosition(partialTicks);
        Vec3 look = viewer.getViewVector(1.0F);
        Vec3 max = position.add(look.x * reachDistance, look.y * reachDistance, look.z * reachDistance);
        AABB searchBox = viewer.getBoundingBox().expandTowards(look.scale(reachDistance)).inflate(1.0D, 1.0D, 1.0D);

        EntityHitResult result = ProjectileUtil.getEntityHitResult(viewer, position, max, searchBox, isVisible, reachDistance * reachDistance);

        if (result == null) return null;

        if (result.getEntity() instanceof LivingEntity target) {
            if (client.player == null) return null;
            HitResult blockHit = clip(setupRayTraceContext(client.player, reachDistance, ClipContext.Fluid.NONE));

            if (!blockHit.getType().equals(BlockHitResult.Type.MISS)) {
                double blockDistance = blockHit.getLocation().distanceTo(position);
                if (blockDistance > target.distanceTo(client.player)) return target;
            }
            return target;
        }

        return null;
    }

    private static ClipContext setupRayTraceContext(Player player, double distance, ClipContext.Fluid fluidHandling) {
        float pitch = player.getXRot();
        float yaw = player.getYRot();
        Vec3 fromPos = player.getEyePosition(1.0F);
        float float_3 = Mth.cos(-yaw * 0.017453292F - 3.1415927F);
        float float_4 = Mth.sin(-yaw * 0.017453292F - 3.1415927F);
        float float_5 = -Mth.cos(-pitch * 0.017453292F);
        float xComponent = float_4 * float_5;
        float yComponent = Mth.sin(-pitch * 0.017453292F);
        float zComponent = float_3 * float_5;
        Vec3 toPos = fromPos.add((double) xComponent * distance, (double) yComponent * distance,
                (double) zComponent * distance);
        return new ClipContext(fromPos, toPos, ClipContext.Block.OUTLINE, fluidHandling, player);
    }

    public static BlockHitResult clip(ClipContext context) {
        return BlockGetter.traverseBlocks(context.getFrom(), context.getTo(), context, (c, pos) -> {
            BlockState block = getLevel().getBlockState(pos);
            if (!block.canOcclude()) {
                return null;
            }
            VoxelShape voxelshape = c.getBlockShape(block, getLevel(), pos);
            return getLevel().clipWithInteractionOverride(c.getFrom(), c.getTo(), pos, voxelshape, block);
        }, (c) -> {
            final var to = c.getTo();
            Vec3 vec3 = c.getFrom().subtract(to);
            return BlockHitResult.miss(to, Direction.getNearest(vec3.x, vec3.y, vec3.z),
                    new BlockPos((int) Math.floor(to.x), (int) Math.floor(to.y), (int) Math.floor(to.z)));
        });
    }

    public static Level getLevel() {
        return Minecraft.getInstance().level;
    }
}
