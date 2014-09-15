package guru.nidi.minecraft.mineedit

/**
 *
 */
class Block(val id: Int, val data: Int) {
  def this(id: Int) = this(id, 0)

  Block.register(this)
}

object Block {
  val values = collection.mutable.Map[Int, Block]()

  val AIR = Air()
  val GRASS = Grass()
  val STONE = Stone(0)
  val GRANITE = Stone(1)
  val POLISHED_GRANTIE = Stone(2)
  val DIORITE = Stone(3)
  val POLISHED_DIORITE = Stone(4)
  val ANDESITE = Stone(5)
  val POLISHED_ANDESITE = Stone(6)
  val DIRT = Dirt(0)
  val COARSE_DIRT = Dirt(1)
  val PODZOL = Dirt(2)
  val COBBLESTONE = Cobblestone()
  val OAK_WOOD_PLANKS = Planks(0)
  val SPRUCE_WOOD_PLANKS = Planks(1)
  val BIRCH_WOOD_PLANKS = Planks(2)
  val JUNGLE_WOOD_PLANKS = Planks(3)
  val ACACIA_WOOD_PLANKS = Planks(4)
  val DARK_OAK_WOOD_PLANKS = Planks(5)
  val OAK_SAPLING = Sapling(0)
  val SPRUCE_WOOD_SAPLING = Sapling(1)
  val BIRCH_WOOD_SAPLING = Sapling(2)
  val JUNGLE_WOOD_SAPLING = Sapling(3)
  val ACACIA_WOOD_SAPLING = Sapling(4)
  val DARK_OAK_SAPLING = Sapling(5)
  val BEDROCK = Bedrock()


  def hash(id: Int, data: Int) = id << 16 + data

  def register(b: Block) = values.put(hash(b.id, b.data), b)

  def apply(id: Int, data: Int): Block = {
    values.getOrElse(hash(id, data), id match {
      case _ => throw new IllegalArgumentException(id + " " + data + " unknown block id/data")
    })
  }
}

case class Air() extends Block(0)

case class Stone(override val data: Int) extends Block(1, data)

case class Grass() extends Block(2)

case class Dirt(override val data: Int) extends Block(3, data)

case class Cobblestone() extends Block(4)

case class Planks(override val data: Int) extends Block(5, data)

case class Sapling(override val data: Int) extends Block(6, data)

case class Bedrock() extends Block(7)

case class FlowingWater() extends Block(8)

case class Water() extends Block(9)

case class FlowingLava() extends Block(10)

case class Lava() extends Block(11)

case class Sand() extends Block(12)

case class Gravel() extends Block(13)

case class GoldOre() extends Block(14)

case class IronOre() extends Block(15)

case class CoalOre() extends Block(16)

case class Log() extends Block(17)

case class Leaves() extends Block(18)

case class Sponge() extends Block(19)

case class Glass() extends Block(20)

case class LapisOre() extends Block(21)

case class LapisBlock() extends Block(22)

case class Dispenser() extends Block(23)

case class Sandstone() extends Block(24)

case class Noteblock() extends Block(25)

case class Bed() extends Block(26)

case class GoldenRail() extends Block(27)

case class DetectorRail() extends Block(28)

case class StickyPiston() extends Block(29)

case class Web() extends Block(30)

case class Tallgrass() extends Block(31)

case class Deadbush() extends Block(32)

case class Piston() extends Block(33)

case class PistonHead() extends Block(34)

case class Wool() extends Block(35)

case class PistonExtension() extends Block(36)

case class YellowFlower() extends Block(37)

case class RedFlower() extends Block(38)

case class BrownMushroom() extends Block(39)

case class RedMushroom() extends Block(40)

case class GoldBlock() extends Block(41)

case class IronBlock() extends Block(42)

case class DoubleStoneSlab() extends Block(43)

case class StoneSlab() extends Block(44)

case class BrickBlock() extends Block(45)

case class Tnt() extends Block(46)

case class Bookshelf() extends Block(47)

case class MossyCobblestone() extends Block(48)

case class Obsidian() extends Block(49)

case class Torch() extends Block(50)

case class Fire() extends Block(51)

case class MobSpawner() extends Block(52)

case class OakStairs() extends Block(53)

case class Chest() extends Block(54)

case class RedstoneWire() extends Block(55)

case class DiamondOre() extends Block(56)

case class DiamondBlock() extends Block(57)

case class CraftingTable() extends Block(58)

case class Wheat() extends Block(59)

case class Farmland() extends Block(60)

case class Furnace() extends Block(61)

case class LitFurnace() extends Block(62)

case class StandingSign() extends Block(63)

case class WoodenDoor() extends Block(64)

case class Ladder() extends Block(65)

case class Rail() extends Block(66)

case class StoneStairs() extends Block(67)

case class WallSign() extends Block(68)

case class Lever() extends Block(69)

case class stone_pressure_plate() extends Block(70)

case class iron_door() extends Block(71)

case class wooden_pressure_plate() extends Block(72)

case class redstone_ore() extends Block(73)

case class lit_redstone_ore() extends Block(74)

case class unlit_redstone_torch() extends Block(75)

case class redstone_torch() extends Block(76)

case class stone_button() extends Block(77)

case class snow_layer() extends Block(78)

case class ice() extends Block(79)

case class snow() extends Block(80)

case class cactus() extends Block(81)

case class clay() extends Block(82)

case class reeds() extends Block(83)

case class jukebox() extends Block(84)

case class fence() extends Block(85)

case class pumpkin() extends Block(86)

case class netherrack() extends Block(87)

case class soul_sand() extends Block(88)

case class glowstone() extends Block(89)

case class portal() extends Block(90)

case class lit_pumpkin() extends Block(91)

case class cake() extends Block(92)

case class unpowered_repeater() extends Block(93)

case class powered_repeater() extends Block(94)

case class stained_glass() extends Block(95)

case class trapdoor() extends Block(96)

case class monster_egg() extends Block(97)

case class stonebrick() extends Block(98)

case class brown_mushroom_block() extends Block(99)

case class red_mushroom_block() extends Block(100)

case class iron_bars() extends Block(101)

case class glass_pane() extends Block(102)

case class melon_block() extends Block(103)

case class pumpkin_stem() extends Block(104)

case class melon_stem() extends Block(105)

case class vine() extends Block(106)

case class fence_gate() extends Block(107)

case class brick_stairs() extends Block(108)

case class stone_brick_stairs() extends Block(109)

case class mycelium() extends Block(110)

case class waterlily() extends Block(111)

case class nether_brick() extends Block(112)

case class nether_brick_fence() extends Block(113)

case class nether_brick_stairs() extends Block(114)

case class nether_wart() extends Block(115)

case class enchanting_table() extends Block(116)

case class brewing_stand() extends Block(117)

case class cauldron() extends Block(118)

case class end_portal() extends Block(119)

case class end_portal_frame() extends Block(120)

case class end_stone() extends Block(121)

case class dragon_egg() extends Block(122)

case class redstone_lamp() extends Block(123)

case class lit_redstone_lamp() extends Block(124)

case class double_wooden_slab() extends Block(125)

case class wooden_slab() extends Block(126)

case class cocoa() extends Block(127)

case class sandstone_stairs() extends Block(128)

case class emerald_ore() extends Block(129)

case class ender_chest() extends Block(130)

case class tripwire_hook() extends Block(131)

case class tripwire() extends Block(132)

case class emerald_block() extends Block(133)

case class spruce_stairs() extends Block(134)

case class birch_stairs() extends Block(135)

case class jungle_stairs() extends Block(136)

case class command_block() extends Block(137)

case class beacon() extends Block(138)

case class cobblestone_wall() extends Block(139)

case class flower_pot() extends Block(140)

case class carrots() extends Block(141)

case class potatoes() extends Block(142)

case class wooden_button() extends Block(143)

case class skull() extends Block(144)

case class anvil() extends Block(145)

case class trapped_chest() extends Block(146)

case class light_weighted_pressure_plate() extends Block(147)

case class heavy_weighted_pressure_plate() extends Block(148)

case class unpowered_comparator() extends Block(149)

case class powered_comparator() extends Block(150)

case class daylight_detector() extends Block(151)

case class redstone_block() extends Block(152)

case class quartz_ore() extends Block(153)

case class hopper() extends Block(154)

case class quartz_block() extends Block(155)

case class quartz_stairs() extends Block(156)

case class activator_rail() extends Block(157)

case class dropper() extends Block(158)

case class stained_hardened_clay() extends Block(159)

case class stained_glass_pane() extends Block(160)

case class leaves2() extends Block(161)

case class log2() extends Block(162)

case class acacia_stairs() extends Block(163)

case class dark_oak_stairs() extends Block(164)

case class slime() extends Block(165)

case class barrier() extends Block(166)

case class iron_trapdoor() extends Block(167)

case class prismarine() extends Block(168)

case class sea_lantern() extends Block(169)

case class hay_block() extends Block(170)

case class carpet() extends Block(171)

case class hardened_clay() extends Block(172)

case class coal_block() extends Block(173)

case class packed_ice() extends Block(174)

case class large_flowers() extends Block(175)

case class standing_banner() extends Block(176)

case class wall_banner() extends Block(177)

case class daylight_detector_inverted() extends Block(178)

case class red_sandstone() extends Block(179)

case class red_sandstone_stairs() extends Block(180)

case class double_stone_slab2() extends Block(181)

case class stone_slab2() extends Block(182)

case class spruce_fence_gate() extends Block(183)

case class birch_fence_gate() extends Block(184)

case class jungle_fence_gate() extends Block(185)

case class dark_oak_fence_gate() extends Block(186)

case class acacia_fence_gate() extends Block(187)

case class spruce_fence() extends Block(188)

case class birch_fence() extends Block(189)

case class jungle_fence() extends Block(190)

case class dark_oak_fence() extends Block(191)

case class acacia_fence() extends Block(192)

case class spruce_door() extends Block(193)

case class birch_door() extends Block(194)

case class jungle_door() extends Block(195)

case class acacia_door() extends Block(196)

case class dark_oak_door() extends Block(197)
