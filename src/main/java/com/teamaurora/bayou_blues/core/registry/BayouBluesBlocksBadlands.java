package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.common.block.badlands.KindlingBlock;
import com.teamaurora.bayou_blues.common.block.badlands.KindlingSlabBlock;
import com.teamaurora.bayou_blues.common.block.badlands.KindlingStairsBlock;
import com.teamaurora.bayou_blues.common.block.badlands.KindlingVerticalSlabBlock;
import com.teamaurora.bayou_blues.common.block.badlands.MagicInvisibleLightBlock;
import com.teamaurora.bayou_blues.common.block.badlands.TerracottaLampBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Better Badlands 内容注册表（陶瓦灯 + 引火物）。
 *
 * <p>内容来源：{@code Better-Badlands-1.16.5-2.0.3.jar} 的
 * {@code com.teamaurora.better_badlands.core.registry.BetterBadlandsBlocks}。
 * 原模组使用 AbnormalsCore 的 {@code BlockSubRegistryHelper}，本项目直接用
 * {@link BayouBluesBlocks} 的 DeferredRegister 注册，并沿用原有注册名，
 * 因此 {@code bayou_blues:kindling}、{@code bayou_blues:terracotta_lamp} 等
 * 与原 {@code better_badlands:*} 一一对应（仅命名空间改为本模组）。
 *
 * <p><b>引火物方块属性</b>：原模组为
 * {@code Properties.of(Material.REPLACEABLE_PLANT, MaterialColor.COLOR_LIGHT_GREEN)
 * .harvestTool(ToolType.HOE).strength(0.5F).sound(SoundType.GRASS).noCollission()}。
 * 1.20.1 已删除 {@code Material} 与 {@code harvestTool}，故改写为
 * {@code Properties.copy(Blocks.DEAD_BUSH)} 再覆盖颜色/硬度/音效/碰撞，
 * 语义等价（枯木丛本身就是 REPLACEABLE_PLANT + 锄头类工具 + 无碰撞）。
 */
public final class BayouBluesBlocksBadlands {

    private BayouBluesBlocksBadlands() {}

    // ── 引火物（Kindling，Dead Bush Thatch）──────────────────────────────────────

    private static BlockBehaviour.Properties kindlingProperties() {
        return BlockBehaviour.Properties.copy(Blocks.DEAD_BUSH)
                .mapColor(MapColor.COLOR_LIGHT_GREEN)
                .strength(0.5F)
                .sound(SoundType.GRASS)
                .noOcclusion()
                .noCollission();
    }

    public static final RegistryObject<Block> KINDLING = createBlock("kindling",
            () -> new KindlingBlock(kindlingProperties()));

    public static final RegistryObject<Block> KINDLING_SLAB = createBlock("kindling_slab",
            () -> new KindlingSlabBlock(kindlingProperties()));

    /**
     * 引火物楼梯。基座方块用延迟解析——静态初始化阶段 {@code KINDLING.get()} 会返回 null，
     * 因此必须在 supplier 求值（注册事件）时才取默认状态。
     */
    public static final RegistryObject<Block> KINDLING_STAIRS = createBlock("kindling_stairs",
            () -> new KindlingStairsBlock(KINDLING.get().defaultBlockState(), kindlingProperties()));

    /**
     * 引火物竖直半砖。<b>联动</b>：原模组通过 AbnormalsCore
     * {@code createCompatBlock("quark", ...)} 注册（Quark vertical_slabs 模块）。
     * 本项目 Quark 为强制依赖，故直接注册，与原模组行为一致。
     */
    public static final RegistryObject<Block> KINDLING_VERTICAL_SLAB = createBlock("kindling_vertical_slab",
            () -> new KindlingVerticalSlabBlock(kindlingProperties()));

    // ── 陶瓦灯（Terracotta Lamp）────────────────────────────────────────────────

    /**
     * 不可见光源：亮度 13、无掉落、无碰撞、无遮挡、不可见（由陶瓦灯自动放置）。
     *
     * <p>{@code noCollission()} 与 {@link com.teamaurora.bayou_blues.common.block.badlands.MagicInvisibleLightBlock}
     * 里把 {@code getShape/getCollisionShape} 置空是<b>两件不同的事</b>，都必须做：
     * {@code noCollission()} 只改 {@code Properties} 上的碰撞开关，而玩家准星命中判定走的是
     * {@code BlockState#getShape}（默认回退到碰撞箱）。少了类里的覆写，就会出现
     * 「准星选中了看不见的方块、下面那块永远挖不动」。
     */
    public static final RegistryObject<Block> TERRACOTTA_LAMP_LIGHT_SOURCE = createBlockNoItem("light_source",
            () -> new MagicInvisibleLightBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.NONE)
                    .lightLevel(state -> 13)
                    .noLootTable()
                    .noOcclusion()
                    .noCollission()
                    .instabreak()
                    .replaceable()));

    public static final RegistryObject<Block> TERRACOTTA_LAMP = createBlock("terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));

    public static final RegistryObject<Block> BLACK_TERRACOTTA_LAMP = createBlock("black_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> BLUE_TERRACOTTA_LAMP = createBlock("blue_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> BROWN_TERRACOTTA_LAMP = createBlock("brown_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> CYAN_TERRACOTTA_LAMP = createBlock("cyan_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> GRAY_TERRACOTTA_LAMP = createBlock("gray_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> GREEN_TERRACOTTA_LAMP = createBlock("green_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> LIGHT_BLUE_TERRACOTTA_LAMP = createBlock("light_blue_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> LIGHT_GRAY_TERRACOTTA_LAMP = createBlock("light_gray_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> LIME_TERRACOTTA_LAMP = createBlock("lime_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> MAGENTA_TERRACOTTA_LAMP = createBlock("magenta_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> ORANGE_TERRACOTTA_LAMP = createBlock("orange_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> PINK_TERRACOTTA_LAMP = createBlock("pink_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> PURPLE_TERRACOTTA_LAMP = createBlock("purple_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> RED_TERRACOTTA_LAMP = createBlock("red_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> WHITE_TERRACOTTA_LAMP = createBlock("white_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));
    public static final RegistryObject<Block> YELLOW_TERRACOTTA_LAMP = createBlock("yellow_terracotta_lamp",
            () -> new TerracottaLampBlock(lampProperties()));

    /** 陶瓦灯属性：原模组为 {@code Properties.of(Material.CLAY)}，1.20.1 用陶瓦本体作模板。 */
    private static BlockBehaviour.Properties lampProperties() {
        return BlockBehaviour.Properties.copy(Blocks.TERRACOTTA);
    }

    /**
     * 强制触发本类的静态初始化，使全部 DeferredRegister 条目在 {@code RegisterEvent}
     * 触发之前完成登记。
     *
     * <p><b>为什么必须有这个方法</b>：Java 只在首次访问类的静态成员时才执行 {@code <clinit>}。
     * 如果只靠客户端 {@code ClientRegister.setupRenderLayers()} 去读
     * {@code BayouBluesBlocksBadlands.KINDLING}，那一刻早已过了注册窗口，
     * DeferredRegister 会抛
     * {@code Cannot register new entries to DeferredRegister after RegisterEvent has been fired}。
     * 因此必须在模组构造器里显式调用一次。
     */
    public static void bootstrap() {
        // 仅用于触发 <clinit>，无需返回值
    }

    // ── 注册辅助 ───────────────────────────────────────────────────────────────

    private static RegistryObject<Block> createBlock(String name, Supplier<Block> block) {
        RegistryObject<Block> blockObj = BayouBluesBlocks.BLOCKS.register(name, block);
        BayouBluesBlocks.ITEMS.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties()));
        return blockObj;
    }

    private static RegistryObject<Block> createBlockNoItem(String name, Supplier<Block> block) {
        return BayouBluesBlocks.BLOCKS.register(name, block);
    }
}
