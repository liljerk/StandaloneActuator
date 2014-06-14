package com.laodmods.StandaloneActuator

import cpw.mods.fml.common
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.FMLLog
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.registry.GameRegistry

import org.apache.logging.log4j.Logger

import net.minecraft.block.Block
import net.minecraft.block.BlockContainer
import net.minecraft.block.BlockDirt
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.server.MinecraftServer
import net.minecraft.tileentity
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraft.world.WorldServer

import com.laodmods.util.LocalFakePlayer
import com.laodmods.util.LocalFakePlayerFactory

class MyTE extends TileEntity{
  var neighbors: List[(Int, Int, Block, TileEntity)] = null
  var ticks = 0

  override def updateEntity{
    if (ticks % 10 == 0 || neighbors == null) getNeighbors
    if (ticks == 19) ticks = 0
    if (ticks % 5 == 0) {
      val p = LocalFakePlayerFactory.getMinecraft(Minecraft.getMinecraft.getIntegratedServer.worldServerForDimension(0))
			val ds = new ItemStack(Blocks.dirt)
			p.inventory.setInventorySlotContents(0,ds)
      neighbors.foreach{ n => n._3.onBlockActivated(worldObj, n._1, yCoord, n._2, p, 0, 0.1f, 0.0f, 0.0f)}
    }
    ticks += 1
  }

  def getNeighbors{
    FMLLog.fine("neigh x: %s, z: %s", xCoord.toString, zCoord.toString)
    neighbors = List(List(xCoord-1,zCoord), List(xCoord+1,zCoord), List(xCoord,zCoord-1), List(xCoord,zCoord+1))
			.map{ c => ( c(0), c(1), worldObj.getBlock(c(0), yCoord, c(1)), worldObj.getTileEntity(c(0),yCoord,c(1)) ) }

    FMLLog.fine("Neighbors: %s", neighbors)
  }
}

class ActuatorBlock(mat: Material) extends BlockContainer(mat: Material){
  var te: MyTE = null

  def createNewTileEntity(w: World, i: Int) : TileEntity = {
    te = new MyTE
		te
  }

  override def onBlockAdded(w: World, x: Int, y: Int, z: Int){
    te.getNeighbors
  }
}

@Mod(modid = "StandaloneActuator", name = "Standalone Actuator", version = "0.0.1", modLanguage = "scala")
object ExampleMod {
  var log: Logger = null
  var instance = this

  @EventHandler
  def preInit(e: FMLPreInitializationEvent) {
    log = e.getModLog
  }

  @EventHandler
  def init(e: FMLInitializationEvent) {
    val b = new ActuatorBlock(Material.rock)
									.setHardness(1.0f)
									.setStepSound(Block.soundTypeStone)
									.setBlockName("StandaloneActuator")
									.setCreativeTab(CreativeTabs.tabBlock)
		val t = new MyTE
    GameRegistry.registerBlock(b, "StandaloneActuator")
    GameRegistry.registerTileEntity(t.getClass, "actuator")
    log.info("ActuatorBlock ID: " + Block.getIdFromBlock(b).toString)
  }

  @EventHandler
  def postInit(e: FMLPostInitializationEvent) {
  }
}
