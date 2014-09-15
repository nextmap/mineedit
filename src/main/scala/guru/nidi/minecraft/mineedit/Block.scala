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
  val COBBLESTONE = Cobblestone
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
  val WATER = Water(0)
  val FALLING_WATER = Water(8)
  val WATER_LEVEL1 = Water(7)
  val WATER_LEVEL2 = Water(6)
  val WATER_LEVEL3 = Water(5)
  val WATER_LEVEL4 = Water(4)
  val WATER_LEVEL5 = Water(3)
  val WATER_LEVEL6 = Water(2)
  val WATER_LEVEL7 = Water(1)
  val FLOWING_WATER = FlowingWater(0)
  val FLOWING_FALLING_WATER = FlowingWater(8)
  val FLOWING_WATER_LEVEL1 = FlowingWater(7)
  val FLOWING_WATER_LEVEL2 = FlowingWater(6)
  val FLOWING_WATER_LEVEL3 = FlowingWater(5)
  val FLOWING_WATER_LEVEL4 = FlowingWater(4)
  val FLOWING_WATER_LEVEL5 = FlowingWater(3)
  val FLOWING_WATER_LEVEL6 = FlowingWater(2)
  val FLOWING_WATER_LEVEL7 = FlowingWater(1)
  val LAVA = Lava(0)
  val FALLING_LAVA = Lava(8)
  val LAVA_LEVEL2 = Lava(6)
  val LAVA_LEVEL4 = Lava(4)
  val LAVA_LEVEL6 = Lava(2)
  val FLOWING_LAVA = FlowingLava(0)
  val FLOWING_FALLING_LAVA = FlowingLava(8)
  val FLOWING_LAVA_LEVEL2 = FlowingLava(6)
  val FLOWING_LAVA_LEVEL4 = FlowingLava(4)
  val FLOWING_LAVA_LEVEL6 = FlowingLava(2)
  val SAND = Sand(0)
  val RED_SAND = Sand(1)
  val GRAVEL = Gravel()
  val GOLD_ORE = GoldOre()
  val IRON_ORE = IronOre()
  val COAL_ORE = CoalOre()
  val OAK_WOOD_UP_DOWN = Wood(0)
  val SPRUCE_WOOD_UP_DOWN = Wood(1)
  val BIRCH_WOOD_UP_DOWN = Wood(2)
  val JUNGLE_WOOD_UP_DOWN = Wood(3)
  val OAK_WOOD_EAST_WEST = Wood(4)
  val SPRUCE_WOOD_EAST_WEST = Wood(5)
  val BIRCH_WOOD_EAST_WEST = Wood(6)
  val JUNGLE_WOOD_EAST_WEST = Wood(7)
  val OAK_WOOD_NORTH_SOUTH = Wood(8)
  val SPRUCE_WOOD_NORTH_SOUTH = Wood(9)
  val BIRCH_WOOD_NORTH_SOUTH = Wood(10)
  val JUNGLE_WOOD_NORTH_SOUTH = Wood(11)
  val OAK_WOOD_BARK = Wood(12)
  val SPRUCE_WOOD_BARK = Wood(13)
  val BIRCH_WOOD_BARK = Wood(14)
  val JUNGLE_WOOD_BARK = Wood(15)

  def hash(id: Int, data: Int) = (id << 16) + data

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

case class FlowingWater(override val data: Int) extends Block(8, data)

case class Water(override val data: Int) extends Block(9, data)

case class FlowingLava(override val data: Int) extends Block(10, data)

case class Lava(override val data: Int) extends Block(11, data)

case class Sand(override val data: Int) extends Block(12, data)

case class Gravel() extends Block(13)

case class GoldOre() extends Block(14)

case class IronOre() extends Block(15)

case class CoalOre() extends Block(16)

case class Wood(override val data: Int) extends Block(17, data)

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

case class StonePressurePlate() extends Block(70)

case class IronDoor() extends Block(71)

case class WoodenPressurePlate() extends Block(72)

case class RedstoneOre() extends Block(73)

case class LitRedstoneOre() extends Block(74)

case class UnlitRedstoneTorch() extends Block(75)

case class RedstoneTorch() extends Block(76)

case class StoneButton() extends Block(77)

case class SnowLayer() extends Block(78)

case class ice() extends Block(79)

case class snow() extends Block(80)

case class cactus() extends Block(81)

case class clay() extends Block(82)

case class reeds() extends Block(83)

case class jukebox() extends Block(84)

case class fence() extends Block(85)

case class pumpkin() extends Block(86)

case class netherrack() extends Block(87)

case class SoulSand() extends Block(88)

case class glowstone() extends Block(89)

case class portal() extends Block(90)

case class LitPumpkin() extends Block(91)

case class cake() extends Block(92)

case class UnpoweredRepeater() extends Block(93)

case class PoweredRepeater() extends Block(94)

case class StainedGlass() extends Block(95)

case class trapdoor() extends Block(96)

case class MonsterEgg() extends Block(97)

case class stonebrick() extends Block(98)

case class BrownMushroomBlock() extends Block(99)

case class RedMushroomBlock() extends Block(100)

case class IronBars() extends Block(101)

case class GlassPane() extends Block(102)

case class MelonBlock() extends Block(103)

case class PumpkinStem() extends Block(104)

case class MelonStem() extends Block(105)

case class vine() extends Block(106)

case class FenceGate() extends Block(107)

case class BrickStairs() extends Block(108)

case class StoneBrickStairs() extends Block(109)

case class mycelium() extends Block(110)

case class waterlily() extends Block(111)

case class NetherBrick() extends Block(112)

case class NetherBrickFence() extends Block(113)

case class NetherBrickStairs() extends Block(114)

case class NetherWart() extends Block(115)

case class EnchantingTable() extends Block(116)

case class BrewingStand() extends Block(117)

case class cauldron() extends Block(118)

case class EndPortal() extends Block(119)

case class EndPortalFrame() extends Block(120)

case class EndStone() extends Block(121)

case class DragonEgg() extends Block(122)

case class RedstoneLamp() extends Block(123)

case class LitRedstoneLamp() extends Block(124)

case class DoubleWoodenSlab() extends Block(125)

case class WoodenSlab() extends Block(126)

case class cocoa() extends Block(127)

case class SandstoneStairs() extends Block(128)

case class EmeraldOre() extends Block(129)

case class EnderChest() extends Block(130)

case class TripwireHook() extends Block(131)

case class tripwire() extends Block(132)

case class EmeraldBlock() extends Block(133)

case class SpruceStairs() extends Block(134)

case class BirchStairs() extends Block(135)

case class JungleStairs() extends Block(136)

case class CommandBlock() extends Block(137)

case class beacon() extends Block(138)

case class CobblestoneWall() extends Block(139)

case class FlowerPot() extends Block(140)

case class carrots() extends Block(141)

case class potatoes() extends Block(142)

case class WoodenButton() extends Block(143)

case class skull() extends Block(144)

case class anvil() extends Block(145)

case class TrappedChest() extends Block(146)

case class LightWeightedPressurePlate() extends Block(147)

case class HeavyWeightedPressurePlate() extends Block(148)

case class UnpoweredComparator() extends Block(149)

case class PoweredComparator() extends Block(150)

case class daylight_detector() extends Block(151)

case class redstone_block() extends Block(152)

case class QuartzOre() extends Block(153)

case class hopper() extends Block(154)

case class QuartzBlock() extends Block(155)

case class QuartzStairs() extends Block(156)

case class ActivatorRail() extends Block(157)

case class dropper() extends Block(158)

case class stained_hardened_clay() extends Block(159)

case class stained_glass_pane() extends Block(160)

case class leaves2() extends Block(161)

case class log2() extends Block(162)

case class acacia_stairs() extends Block(163)

case class dark_oak_stairs() extends Block(164)

case class slime() extends Block(165)

case class barrier() extends Block(166)

case class IronTrapdoor() extends Block(167)

case class prismarine() extends Block(168)

case class SeaLantern() extends Block(169)

case class HayBlock() extends Block(170)

case class carpet() extends Block(171)

case class HardenedClay() extends Block(172)

case class CoalBlock() extends Block(173)

case class PackedIce() extends Block(174)

case class LargeFlowers() extends Block(175)

case class StandingBanner() extends Block(176)

case class WallBanner() extends Block(177)

case class DaylightDetectorInverted() extends Block(178)

case class RedSandstone() extends Block(179)

case class RedSandstoneStairs() extends Block(180)

case class DoubleStoneSlab2() extends Block(181)

case class StoneSlab2() extends Block(182)

case class SpruceFenceGate() extends Block(183)

case class BirchFenceGate() extends Block(184)

case class JungleFenceGate() extends Block(185)

case class DarkOakFenceGate() extends Block(186)

case class AcaciaFenceGate() extends Block(187)

case class SpruceFence() extends Block(188)

case class BirchFence() extends Block(189)

case class JungleFence() extends Block(190)

case class DarkOakFence() extends Block(191)

case class AcaciaFence() extends Block(192)

case class SpruceDoor() extends Block(193)

case class BirchDoor() extends Block(194)

case class JungleDoor() extends Block(195)

case class AcaciaDoor() extends Block(196)

case class DarkOakDoor() extends Block(197)
