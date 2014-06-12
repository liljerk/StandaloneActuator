package example

import cpw.mods.fml.common
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.FMLLog
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.registry.GameRegistry

import org.apache.logging.log4j.Logger

import net.minecraft.block.BlockStone
import net.minecraft.block.Block
import net.minecraft.block.ITileEntityProvider
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.tileentity
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.FakePlayerFactory

class MyTE extends TileEntity{

  override def updateEntity{
  }
}

class ActuatorBlock extends BlockStone with ITileEntityProvider{
	setBlockName("doi")
	setCreativeTab(CreativeTabs.tabBlock)

  def createNewTileEntity(w: World, i: Int) : TileEntity = {
    FMLLog.fine("Is something happening?")
    return new MyTE
  }
}

@Mod(modid = "ExampleMod", name = "ExampleMod", version = "0.0.0", modLanguage = "scala")
object ExampleMod {
	var log: Logger = null
  var instance = this

	@EventHandler
	def preInit(e: FMLPreInitializationEvent) {
    FMLLog.fine("Is something happening?")
		log = e.getModLog
	}

	@EventHandler
	def init(e: FMLInitializationEvent) {
    FMLLog.fine("Is something happening?")
		log.info("testing one two")
		val b = new ActuatorBlock
		GameRegistry.registerBlock(b, "doi")
		log.info("ActuatorBlock ID: " + Block.getIdFromBlock(b).toString)
	}

	@EventHandler
	def postInit(e: FMLPostInitializationEvent) {
    FMLLog.fine("Is something happening?")
	}
}
