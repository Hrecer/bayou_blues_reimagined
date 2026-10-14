package com.teamaurora.bayou_blues.core.registry;

import com.teamaurora.bayou_blues.common.item.*;
import com.teamaurora.bayou_blues.core.BayouBlues;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BayouBluesItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BayouBlues.MOD_ID);

    public static final RegistryObject<Item> CYPRESS_BOAT = ITEMS.register("cypress_boat", () -> new CypressBoatItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CYPRESS_SIGN = createItem("cypress_sign", () -> new SignItem(new Item.Properties().stacksTo(16), BayouBluesBlocks.CYPRESS_SIGN.get(), BayouBluesBlocks.CYPRESS_WALL_SIGN.get()));

    public static final RegistryObject<Item> GOOSEBERRIES = createItem("gooseberries", () -> new Item(new Item.Properties().food(Foods.GOOSEBERRIES)));
    public static final RegistryObject<Item> GOOSEBERRY_JUICE = createItem("gooseberry_juice", () -> new DrinkItem(new Item.Properties().food(Foods.GOOSEBERRY_JUICE).stacksTo(16)));
    public static final RegistryObject<Item> GOOSEBERRY_PIE = createItem("gooseberry_pie", () -> new Item(new Item.Properties().food(Foods.GOOSEBERRY_PIE)));
    public static final RegistryObject<Item> HONEY_GLAZED_GOOSEBERRIES = createItem("honey_glazed_gooseberries", () -> new Item(new Item.Properties().food(Foods.HONEY_GLAZED_GOOSEBERRIES)));
    public static final RegistryObject<Item> GOOSEBERRY_JAM = createItem("gooseberry_jam", () -> new JamItem(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).food(Foods.GOOSEBERRY_JAM).stacksTo(16)));
    public static final RegistryObject<Item> GOOSEBERRY_JAM_COOKIE = createItem("gooseberry_jam_cookie", () -> new Item(new Item.Properties().food(Foods.GOOSEBERRY_JAM_COOKIE)));
    public static final RegistryObject<Item> ALGAE_SANDWICH = createItem("algae_sandwich", () -> new Item(new Item.Properties().food(Foods.ALGAE_SANDWICH).stacksTo(16)));
    public static final RegistryObject<Item> ALGAE_SOUP = createItem("algae_soup", () -> new AlgaeSoupItem(new Item.Properties().food(Foods.ALGAE_SOUP).stacksTo(1), () -> Items.BOWL));
    public static final RegistryObject<Item> BAYOU_FLOWER_TEA = createItem("bayou_flower_tea", () -> new ContainerFoodItem(new Item.Properties().food(Foods.BAYOU_FLOWER_TEA).stacksTo(1), () -> Items.GLASS_BOTTLE));
    public static final RegistryObject<Item> BAYOU_PIE = createItem("bayou_pie", () -> new Item(new Item.Properties().food(Foods.BAYOU_PIE).stacksTo(1)));
    public static final RegistryObject<Item> BAYOU_SALAD = createItem("bayou_salad", () -> new ContainerFoodItem(new Item.Properties().food(Foods.BAYOU_SALAD).stacksTo(1), () -> Items.BOWL));
    public static final RegistryObject<Item> BEARD_MOSS_JUICE = createItem("beard_moss_juice", () -> new ContainerFoodItem(new Item.Properties().food(Foods.BEARD_MOSS_JUICE).stacksTo(1), () -> Items.GLASS_BOTTLE));
    public static final RegistryObject<Item> COOKED_ALGAE = createItem("cooked_algae", () -> new Item(new Item.Properties().food(Foods.COOKED_ALGAE)));
    public static final RegistryObject<Item> COOKED_GOOSEBERRIES = createItem("cooked_gooseberries", () -> new Item(new Item.Properties().food(Foods.COOKED_GOOSEBERRIES)));
    public static final RegistryObject<Item> CYPRESS_SOUP = createItem("cypress_soup", () -> new ContainerFoodItem(new Item.Properties().food(Foods.CYPRESS_SOUP).stacksTo(1), () -> Items.BOWL));
    public static final RegistryObject<Item> FERN_SALAD = createItem("fern_salad", () -> new ContainerFoodItem(new Item.Properties().food(Foods.FERN_SALAD).stacksTo(1), () -> Items.BOWL));
    public static final RegistryObject<Item> GOOSEBERRIES_CYPRESS_TEA = createItem("gooseberries_cypress_tea", () -> new ContainerFoodItem(new Item.Properties().food(Foods.GOOSEBERRIES_CYPRESS_TEA).stacksTo(1), () -> Items.GLASS_BOTTLE));
    public static final RegistryObject<Item> GOOSEBERRIES_MILK_TEA = createItem("gooseberries_milk_tea", () -> new ContainerFoodItem(new Item.Properties().food(Foods.GOOSEBERRIES_MILK_TEA).stacksTo(1), () -> Items.GLASS_BOTTLE));
    public static final RegistryObject<Item> GOOSEBERRIES_POPSICLE = createItem("gooseberries_popsicle", () -> new Item(new Item.Properties().food(Foods.GOOSEBERRIES_POPSICLE)));
    public static final RegistryObject<Item> GOOSEBERRIES_TOMATO_JAM = createItem("gooseberries_tomato_jam", () -> new ContainerFoodItem(new Item.Properties().food(Foods.GOOSEBERRIES_TOMATO_JAM).stacksTo(1), () -> Items.GLASS_BOTTLE));
    public static final RegistryObject<Item> HONEY_GOOSEBERRIES_COOKIE = createItem("honey_gooseberries_cookie", () -> new Item(new Item.Properties().food(Foods.HONEY_GOOSEBERRIES_COOKIE)));
    public static final RegistryObject<Item> HONEY_LILY_TEA = createItem("honey_lily_tea", () -> new ContainerFoodItem(new Item.Properties().food(Foods.HONEY_LILY_TEA).stacksTo(1), () -> Items.GLASS_BOTTLE));
    public static final RegistryObject<Item> LILY_LILY_PAD_SOUP = createItem("lily_lily_pad_soup", () -> new ContainerFoodItem(new Item.Properties().food(Foods.LILY_LILY_PAD_SOUP).stacksTo(1), () -> Items.BOWL));

    public static final RegistryObject<Item> ALGAE = createItem("algae", () -> new AlgaeItem(BayouBluesBlocks.ALGAE.get(), new Item.Properties()));
    public static final RegistryObject<Item> SAGUARO_FLOWER = createItem("saguaro_flower", () -> new SaguaroFlowerItem(new Item.Properties()));
    public static final RegistryObject<Item> BLUE_LILY = createItem("blue_lily", () -> new LilyItem(BayouBluesBlocks.BLUE_LILY, BayouBluesBlocks.POTTED_BLUE_LILY, new Item.Properties()));
    public static final RegistryObject<Item> LIGHT_GRAY_LILY = createItem("light_gray_lily", () -> new LilyItem(BayouBluesBlocks.LIGHT_GRAY_LILY, BayouBluesBlocks.POTTED_LIGHT_GRAY_LILY, new Item.Properties()));
    public static final RegistryObject<Item> CYAN_LILY = createItem("cyan_lily", () -> new LilyItem(BayouBluesBlocks.CYAN_LILY, BayouBluesBlocks.POTTED_CYAN_LILY, new Item.Properties()));
    public static final RegistryObject<Item> LIGHT_BLUE_LILY = createItem("light_blue_lily", () -> new LilyItem(BayouBluesBlocks.LIGHT_BLUE_LILY, BayouBluesBlocks.POTTED_LIGHT_BLUE_LILY, new Item.Properties()));
    public static final RegistryObject<Item> MAGENTA_LILY = createItem("magenta_lily", () -> new LilyItem(BayouBluesBlocks.MAGENTA_LILY, BayouBluesBlocks.POTTED_MAGENTA_LILY, new Item.Properties()));
    public static final RegistryObject<Item> PINK_LILY = createItem("pink_lily", () -> new LilyItem(BayouBluesBlocks.PINK_LILY, BayouBluesBlocks.POTTED_PINK_LILY, new Item.Properties()));
    public static final RegistryObject<Item> PURPLE_LILY = createItem("purple_lily", () -> new LilyItem(BayouBluesBlocks.PURPLE_LILY, BayouBluesBlocks.POTTED_PURPLE_LILY, new Item.Properties()));
    public static final RegistryObject<Item> WHITE_LILY = createItem("white_lily", () -> new LilyItem(BayouBluesBlocks.WHITE_LILY, BayouBluesBlocks.POTTED_WHITE_LILY, new Item.Properties()));

    private static RegistryObject<Item> createItem(String name, Supplier<Item> supplier) {
        return ITEMS.register(name, supplier);
    }

    public static final class Foods {
        public static final FoodProperties GOOSEBERRIES = new FoodProperties.Builder().nutrition(2).saturationMod(0.2F).build();
        public static final FoodProperties GOOSEBERRY_JUICE = new FoodProperties.Builder().nutrition(1).saturationMod(0.1F).build();
        public static final FoodProperties GOOSEBERRY_PIE = new FoodProperties.Builder().nutrition(6).saturationMod(0.5F).build();
        public static final FoodProperties HONEY_GLAZED_GOOSEBERRIES = new FoodProperties.Builder().nutrition(7).saturationMod(0.3F).build();
        public static final FoodProperties GOOSEBERRY_JAM = new FoodProperties.Builder().nutrition(2).saturationMod(0.25F).build();
        public static final FoodProperties GOOSEBERRY_JAM_COOKIE = new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).fast().build();
        public static final FoodProperties ALGAE_SANDWICH = new FoodProperties.Builder().nutrition(13).saturationMod(1.2F).build();
        public static final FoodProperties ALGAE_SOUP = new FoodProperties.Builder().nutrition(4).saturationMod(0.8F).build();
        public static final FoodProperties BAYOU_FLOWER_TEA = new FoodProperties.Builder().nutrition(6).saturationMod(0.3F).build();
        public static final FoodProperties BAYOU_PIE = new FoodProperties.Builder().nutrition(12).saturationMod(0.4F).build();
        public static final FoodProperties BAYOU_SALAD = new FoodProperties.Builder().nutrition(10).saturationMod(0.6F).build();
        public static final FoodProperties BEARD_MOSS_JUICE = new FoodProperties.Builder().nutrition(4).saturationMod(0.5F).build();
        public static final FoodProperties COOKED_ALGAE = new FoodProperties.Builder().nutrition(4).saturationMod(0.5F).build();
        public static final FoodProperties COOKED_GOOSEBERRIES = new FoodProperties.Builder().nutrition(4).saturationMod(0.5F).build();
        public static final FoodProperties CYPRESS_SOUP = new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build();
        public static final FoodProperties FERN_SALAD = new FoodProperties.Builder().nutrition(6).saturationMod(0.4F).build();
        public static final FoodProperties GOOSEBERRIES_CYPRESS_TEA = new FoodProperties.Builder().nutrition(8).saturationMod(0.7F).build();
        public static final FoodProperties GOOSEBERRIES_MILK_TEA = new FoodProperties.Builder().nutrition(7).saturationMod(0.7F).build();
        public static final FoodProperties GOOSEBERRIES_POPSICLE = new FoodProperties.Builder().nutrition(6).saturationMod(1.2F).build();
        public static final FoodProperties GOOSEBERRIES_TOMATO_JAM = new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build();
        public static final FoodProperties HONEY_GOOSEBERRIES_COOKIE = new FoodProperties.Builder().nutrition(3).saturationMod(0.6F).build();
        public static final FoodProperties HONEY_LILY_TEA = new FoodProperties.Builder().nutrition(8).saturationMod(1.0F).build();
        public static final FoodProperties LILY_LILY_PAD_SOUP = new FoodProperties.Builder().nutrition(9).saturationMod(0.7F).build();
    }
}
