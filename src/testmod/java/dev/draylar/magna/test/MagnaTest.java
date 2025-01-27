package dev.draylar.magna.test;

import dev.draylar.magna.api.BlockFinder;
import dev.draylar.magna.item.ExcavatorItem;
import dev.draylar.magna.item.HammerItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

/**
 * This class is responsible for registering development-environment test items.
 */
public class MagnaTest implements ModInitializer {

    @Override
    public void onInitialize() {
        // Standard Hammer with a tool material of Diamond.
        Registry.register(
                Registries.ITEM,
                Identifier.of("magna", "hammer_test"),
                new HammerItem(ToolMaterials.DIAMOND, new Item.Settings())
        );

        // Standard Hammer with a tool material of Diamond and a modified depth.
        Registry.register(
                Registries.ITEM,
                Identifier.of("magna", "depth_test"),
                new HammerItem(ToolMaterials.DIAMOND, new Item.Settings()) {

                    @Override
                    public int getDepth(ItemStack stack) {
                        return 5;
                    }
                }
        );

        // Standard Excavator with a tool material of Wood.
        Registry.register(
                Registries.ITEM,
                Identifier.of("magna", "excavator_test"),
                new ExcavatorItem(ToolMaterials.WOOD, new Item.Settings())
        );

//        // Hammer with extended reach.
//        Registry.register(
//                Registries.ITEM,
//                new Identifier("magna", "reach_test"),
//                new HammerItem(ToolMaterials.WOOD, 0, 0, new Item.Settings()) {
//                    @Override
//                    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
//                        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
//                        builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Tool modifier", 10, EntityAttributeModifier.Operation.ADDITION));
//                        return builder.build();
//                    }
//                }
//        );

        // Hammer with a huge radius and no outline.
        Registry.register(
                Registries.ITEM,
                Identifier.of("magna", "outline_test"),
                new HammerItem(ToolMaterials.WOOD, new Item.Settings()) {
                    @Override
                    public boolean renderOutline(World world, BlockHitResult ray, PlayerEntity player, ItemStack stack) {
                        return false;
                    }

                    @Override
                    public int getRadius(ItemStack stack) {
                        return 15;
                    }

                    @Override
                    public int getDepth(ItemStack stack) {
                        return 15;
                    }
                }
        );

        // Hammer that only breaks blocks inside its radius with an odd x or (exclusive) z position.
        Registry.register(
                Registries.ITEM,
                Identifier.of("magna", "custom_breaker_test"),
                new HammerItem(ToolMaterials.DIAMOND, new Item.Settings()) {
                    private final BlockFinder TEST_FINDER = new BlockFinder() {
                        @Override
                        public List<BlockPos> findPositions(World world, PlayerEntity playerEntity, int radius, int depth) {
                            return BlockFinder.super.findPositions(world, playerEntity, radius, depth)
                                    .stream()
                                    .filter((pos) -> pos.getX() % 2 == 0 ^ pos.getZ() % 2 == 0)
                                    .toList();
                        }
                    };

                    @Override
                    public BlockFinder getBlockFinder() {
                        return TEST_FINDER;
                    }
                }
        );
    }
}
