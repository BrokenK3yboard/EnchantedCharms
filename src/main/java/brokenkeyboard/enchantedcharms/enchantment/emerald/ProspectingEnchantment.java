package brokenkeyboard.enchantedcharms.enchantment.emerald;

import brokenkeyboard.enchantedcharms.EnchantedCharms;
import brokenkeyboard.enchantedcharms.enchantment.CharmEnchantment;
import brokenkeyboard.enchantedcharms.item.CharmItem;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.level.BlockEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;
import java.util.function.Predicate;

public class ProspectingEnchantment extends CharmEnchantment {

    public static final Predicate<ItemStack> EXTRACTING_ENCH = stack -> (EnchantmentHelper.getTagEnchantmentLevel(EnchantedCharms.PROSPECTING.get(), stack) > 0);
    public static final DustParticleOptions COAL = new DustParticleOptions(new Vector3f(0.22F, 0.22F, 0.22F), 1.0F);
    public static final DustParticleOptions COPPER = new DustParticleOptions(new Vector3f(0.89F, 0.51F, 0.42F), 1.0F);
    public static final DustParticleOptions IRON = new DustParticleOptions(new Vector3f(0.85F, 0.69F, 0.58F), 1.0F);
    public static final DustParticleOptions GOLD = new DustParticleOptions(new Vector3f(1F, 0.99F, 0.56F), 1.0F);
    public static final DustParticleOptions DIAMOND = new DustParticleOptions(new Vector3f(0.43F, 0.93F, 0.82F), 1.0F);
    public static final DustParticleOptions REDSTONE = new DustParticleOptions(new Vector3f(0.9F, 0.13F, 0.03F), 1.0F);
    public static final DustParticleOptions LAPIS = new DustParticleOptions(new Vector3f(0.25F, 0.39F, 0.59F), 1.0F);
    public static final DustParticleOptions EMERALD = new DustParticleOptions(new Vector3f(0.51F, 0.96F, 0.68F), 1.0F);
    public static final DustParticleOptions QUARTZ = new DustParticleOptions(new Vector3f(0.95F, 0.94F, 0.93F), 1.0F);
    public static final DustParticleOptions NETHERITE = new DustParticleOptions(new Vector3f(0.35F, 0.34F, 0.35F), 1.0F);
    public static final DustParticleOptions MISC = new DustParticleOptions(new Vector3f(0.8F, 0.8F, 0.8F), 1.0F);

    public ProspectingEnchantment(EnchantmentCategory category) {
        super(category);
        MinecraftForge.EVENT_BUS.addListener(this::blockBreak);
    }

    public void blockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockState state = event.getState();
        Level level = player.getLevel();
        BlockPos pos = event.getPos();

        if (!(player instanceof ServerPlayer) || (state.is(Tags.Blocks.ORES) || !state.getBlock().canHarvestBlock(state, level, pos, player)))
            return;
        Optional<SlotResult> curio = CharmItem.getCurio(player, EXTRACTING_ENCH);
        if (curio.isEmpty()) return;

        HitResult result = player.pick(player.getReachDistance(), 1F, false);
        if (!(result instanceof BlockHitResult blockHit)) return;

        Direction direction = blockHit.getDirection();
        BlockState oreBlock = null;
        double distance = 100;

        for (BlockPos currentPos : getBlocks(pos, direction)) {
            BlockState currentState = level.getBlockState(currentPos);
            double newDistance = pos.distToCenterSqr(new Vec3(currentPos.getX(), currentPos.getY(), currentPos.getZ()));
            if (currentState.is(Tags.Blocks.ORES) && newDistance < distance) {
                oreBlock = currentState;
                distance = newDistance;
            }
        }

        if (oreBlock != null) {
            createParticle(player.getLevel(), event.getPos(), getParticle(oreBlock));
        }
    }

    public void createParticle(Level level, BlockPos pos, DustParticleOptions dust) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        RandomSource random = serverLevel.getRandom();
        for (int i = 0; i < 8; i++) {
            double d0 = (double) pos.getX() + random.nextDouble();
            double d1 = (double) pos.getY() + random.nextDouble();
            double d2 = (double) pos.getZ() + random.nextDouble();
            serverLevel.sendParticles(dust, d0, d1, d2, 4, 0, 0, 0, 1);
        }
    }

    public Iterable<BlockPos> getBlocks(BlockPos pos, Direction direction) {
        return switch (direction) {
            case UP -> BlockPos.betweenClosed(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() - 1), new BlockPos(pos.getX() + 1, pos.getY() - 5, pos.getZ() + 1));
            case DOWN -> BlockPos.betweenClosed(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ() - 1), new BlockPos(pos.getX() + 1, pos.getY() + 5, pos.getZ() + 1));
            case NORTH -> BlockPos.betweenClosed(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ()), new BlockPos(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 5));
            case SOUTH -> BlockPos.betweenClosed(new BlockPos(pos.getX() - 1, pos.getY() - 1, pos.getZ()), new BlockPos(pos.getX() + 1, pos.getY() + 1, pos.getZ() - 5));
            case EAST -> BlockPos.betweenClosed(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() - 1), new BlockPos(pos.getX() - 5, pos.getY() + 1, pos.getZ() + 1));
            case WEST -> BlockPos.betweenClosed(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() - 1), new BlockPos(pos.getX() + 5, pos.getY() + 1, pos.getZ() + 1));
        };
    }

    public DustParticleOptions getParticle(BlockState state) {
        if (state.is(Tags.Blocks.ORES_COAL)) return COAL;
        else if (state.is(Tags.Blocks.ORES_COPPER)) return COPPER;
        else if (state.is(Tags.Blocks.ORES_IRON)) return IRON;
        else if (state.is(Tags.Blocks.ORES_GOLD)) return GOLD;
        else if (state.is(Tags.Blocks.ORES_DIAMOND)) return DIAMOND;
        else if (state.is(Tags.Blocks.ORES_REDSTONE)) return REDSTONE;
        else if (state.is(Tags.Blocks.ORES_LAPIS)) return LAPIS;
        else if (state.is(Tags.Blocks.ORES_EMERALD)) return EMERALD;
        else if (state.is(Blocks.NETHER_QUARTZ_ORE)) return QUARTZ;
        else if (state.is(Blocks.ANCIENT_DEBRIS)) return NETHERITE;
        else return MISC;
    }
}