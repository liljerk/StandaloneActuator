package example

import cpw.mods.fml.common
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.registry.GameRegistry

import org.apache.logging.log4j.Logger

import net.minecraft.block.BlockStone
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack

class ActuatorBlock extends BlockStone{
	setBlockName("doi")
	setCreativeTab(CreativeTabs.tabBlock)
}

@Mod(modid = "ExampleMod", name = "ExampleMod", version = "0.0.0", modLanguage = "scala")
object ExampleMod {
	var log: Logger = null

	@EventHandler
	def preInit(e: FMLPreInitializationEvent) {
		log = e.getModLog
	}

	@EventHandler
	def init(e: FMLInitializationEvent) {
		log.info("testing one two")
		val b = new ActuatorBlock
		GameRegistry.registerBlock(b, "doi")
		log.info("ActuatorBlock ID: " + Block.getIdFromBlock(b).toString)
	}

	@EventHandler
	def postInit(e: FMLPostInitializationEvent) {
	}
}
