package com.teamaurora.bayou_blues.common.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 丝柏箱子物品：原版对 builtin/entity 物品模型的 BEWLR 分发只认原版三种箱子方块，
 * 自定义箱子必须自己提供 IClientItemExtensions。渲染器复用 Zeta 的
 * SimpleWithoutLevelRenderer（伪造方块实体 → Quark 的 VariantChestRenderer，
 * 贴图取 quark:quark_variant_chests/cypress/*）。
 */
public class BBChestItem extends BlockItem {

    private final Supplier<BlockEntityType<?>> beType;

    public BBChestItem(Block block, Properties properties, Supplier<BlockEntityType<?>> beType) {
        super(block, properties);
        this.beType = beType;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        // DistExecutor 保证匿名类（引用客户端类）只在客户端加载
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new org.violetmoon.zeta.client.SimpleWithoutLevelRenderer(
                        BBChestItem.this.beType.get(), BBChestItem.this.getBlock().defaultBlockState());
            }
        }));
    }
}
