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
import net.minecraft.client.Minecraft
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.tileentity
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.FakePlayerFactory

import com.mojang.authlib.GameProfile

class MyTE extends TileEntity{
	var neighbors: List[Block] = null

  override def updateEntity{
		//val p = FakePlayerFactory.getMinecraft(Minecraft.getMinecraft.getIntegratedServer.worldServerForDimension(0))
		//neighbors.foreach{ n => n.onBlockClicked(worldObj, xCoord, yCoord, zCoord, p)}
		Option(neighbors(0)) match {
			case Some(s) => s.getClass.getMethods.map(_.getName).sorted.foreach{ i => FMLLog.fine(i)}
			case None    => FMLLog.fine("WTF MATE")
		}
  }

	def getNeighbors{
		FMLLog.fine("neigh x: %s, z: %s", xCoord.toString, zCoord.toString)
		neighbors = List(List(xCoord-1,zCoord), List(xCoord+1,zCoord), List(xCoord,zCoord-1), List(xCoord,zCoord+1)).map{ c => worldObj.getBlock(c(0), yCoord, c(1)) }

		FMLLog.fine("Neighbors: %s", neighbors)
	}
}

class ActuatorBlock extends BlockStone with ITileEntityProvider{
	setBlockName("doi")
	setCreativeTab(CreativeTabs.tabBlock)
	var te: MyTE = null

  def createNewTileEntity(w: World, i: Int) : TileEntity = {
    FMLLog.fine("Is something happening?")
		te = new MyTE
    return te
  }

	override def onBlockAdded(w: World, x: Int, y: Int, z: Int){
		te.getNeighbors
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
