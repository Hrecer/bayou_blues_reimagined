# 移植决策与差异记录（PORTING-NOTES）

本模组是 Bayou Blues（Teamauroramods，1.16.5）+ Better Badlands（同作者，1.16.5）+ Bayou Delight
的非官方 1.20.1 重制。本文件汇总**所有刻意的偏差、对上游 bug 的修复、以及现代版本的机制适配**，
供后续维护者（包括未来的我们）不再需要对照 1.16.5 源码即可理解"为什么是这样写的"。

范围：上游完整对照审计于 2026-10-14 完成（Better Badlands master 全部 Java 文件与 219 个资源文件逐项核对），
之后不再以"像不像 1.16.5"为标准评估改动——本模组按自己的独立版本演进，只需要与本文件的既有决策保持一致。

## 一、刻意的依赖与实现差异

| 项 | 上游做法 | 本模组做法 | 理由 |
|---|---|---|---|
| 群系生成 | Blueprint + BiomeManager | **TerraBlender 区域**（替换原版红树林沼泽的气候槽位 + 海滨滩涂） | 1.20.1 的现代方案；用户已确认群系表现可接受 |
| Quark 兼容 | 可选联动（AbnormalsCore 反射注册） | **强制依赖**（mods.toml mandatory） | 内容完整性优先；避免反射注册出紫黑方块（见 build.gradle 顶部全记录） |
| Farmer's Delight | 可选（仅影响曲奇是否快食） | **强制依赖** | 切菜板配方是内容本体；数据包条件因此可省略 |
| Blueprint | 强依赖（abnormals_core） | **不依赖** | 所需类（Thatch*、TreeUtil）本地等价实现，见 mods.toml 注释 |
| Woodworks | （不存在，锯木台在 Environmental） | 可选依赖 + 16 条 `woodworks:sawmill` 配方 | 锯木台在 1.18+ 移交 Woodworks，配方按现代生态数值 |
| 引火物 `IS_BURNED` 属性名 | `burned` | `is_burned` | 端口早期命名，属于对外可见的存档/命令接口，不再更名 |
| 药水酿造 | AT 开放 `PotionBrewing.addMix` | `PotionMixingBrewingRecipe`（Forge BrewingRecipeRegistry） | 1.20.1 的 addMix 私有化；自实现还能保留普通/喷溅/滞留容器 |
| 单个深色橡木树苗长小树 | `SaplingGrowTreeEvent` 处理器 | 启动时替换原版深色橡木树苗的 **TreeGrower** | 1.20.1 的事件在 grower 内部触发，单个树苗在 `getConfiguredFeature` 返回 null 就退出、事件不触发，照抄无效（详见 SmallDarkOakTreeGrower 注释） |
| `cactus_paste` 配方 | `quark:cactus_paste` | `quark:moss_paste` | 现代 Quark 已更名，条件同步改为 `quark:flag: midori` |
| 苔藓（BeardMoss）骨粉/生长 | — | 行为一致；叶子循环的判定修复了上游笔误（上游重复检查 `pos.down()` 而非递增深度） | 行为不变、逻辑修正 |
| 树苗/树叶等 | — | 群系颜色按原版数值（草 0x6CC147 / 叶 0x69AA2F）；曾出现互换已修复 | — |

## 二、对上游 bug 的修复（行为按"修复后的上游意图"）

1. **引火物点燃排程**：上游只在放置时安排 30t 计时器，放置超时后再点燃会永久卡在 `burn_distance=1`。
   本模组在所有点燃路径补排程；蔓延节奏（30t/格）、邻岩浆无掉落销毁、烧毁放火、TNT 引爆、粒子音效均与上游一致。
2. **大蕨/树枝/水藻/蜂箱战利品**：按上游数据原意修正（丝触、剪刀、25% 种子、蜂箱自掉等），详见提交历史。
3. **potted_saguaro_sprout**：上游缺 blockstate 与战利品表（显示紫黑块、无掉落），已补齐。
4. **柏树断树补全**：区块装饰中断残留的柏树原木不再阻止重新放树（`TreeUtil.isValidTrunkPosition`，详见注释）。
5. **小深色橡木落地判定**：上游"能种深色橡木苗"的判定在 1.20.1 恶地地表（粗泥/陶瓦）恒为 false，
   已改为恶地真实地表方块清单，否则树永远不生成。

## 三、1.20.1 机制适配（与上游等价但形态不同）

- 方块实体：原版蜂箱/告示牌类型自持注册（1.20.1 校验 `getType().isValid(state)`）。
- 树装饰器：`MutableBoundingBox` → `TreeDecorator.Context`；本模组自有的 4 个特征逐行翻译。
- 蜂箱 `copy_state` 指向 `minecraft:beehive`（仅用于解析期校验属性名；无 Buzzier Bees 时战利品表仍会记 2 行
  `Expected name to be an item` 报错，这与 1.16.5 上游同款、不影响功能）。
- Quark 4.x/Zeta：书架/树篱不传 module 以抑制 `quark:` 重复物品；渲染层/可燃性/颜色自注册。
- TerraBlender 群系防成环：bayou 使用的 placed feature 做独立副本 + `copy_features` 修饰器（见 build.gradle 顶部全记录）。

## 四、已知的群系差异（有意保留，用户已确认不必强对齐）

- 只有 `bayou` 单群系（上游另有 `bayou_hills`）。
- 巨型高草使用 `marsh` 密集变体（上游用 savanna 稀有变体）。
- 无原版水湖（`lake_water`）；含 1.19+ 现代补充：青蛙生成、紫水晶洞、发光鱿鱼、glow_lichen。
- `bayou_weight` 语义为 TerraBlender 区域权重（默认 10 ≈ 一半区域划给本群系），与上游 Blueprint 权重不可直接比较。

## 五、验证方式

- 资源一致性：blockstate/模型/贴图/战利品/语言/数据引用全量脚本核查通过。
- 大型膝根：存档实证 251 块全部距原木 ≤2 格、90 个下段 79 个泡水（跨水面形态）。
- 游戏内建议回归点：`/place feature bayou_blues:saguaro_cactus`、单个深色橡木树苗骨粉、
  引火物引线、衰亡药水（受伤触发剧毒）、Environmental slabfish 类型/成就。
