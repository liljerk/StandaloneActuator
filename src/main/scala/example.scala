package com.laodmods.StandaloneActuator

import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.FMLLog
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.network.{IGuiHandler, NetworkRegistry}
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.SideOnly
import cpw.mods.fml.relauncher.Side

import org.apache.logging.log4j.Logger

import net.minecraft.block.Block
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.{Container, IInventory, Slot}
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import com.laodmods.util.LocalFakePlayerFactory

class MyGH extends IGuiHandler{
  NetworkRegistry.INSTANCE.registerGuiHandler(StandaloneActuator.instance, this)

  def getServerGuiElement(id: Int, p: EntityPlayer, w: World, x: Int, y: Int, z: Int): Object = {
    new ActuatorContainer(p,w.getTileEntity(x,y,z).asInstanceOf[MyTE])
  }
  def getClientGuiElement(id: Int, p: EntityPlayer, w: World, x: Int, y: Int, z: Int): Object = {
    new ActuatorGUI(new ActuatorContainer(p,w.getTileEntity(x,y,z).asInstanceOf[MyTE]))
  }
}

class ActuatorContainer(p: EntityPlayer, te: MyTE) extends Container{

  for (yi <- 0 to 3; xi <- 0 to 8) addSlotToContainer(new Slot(p.inventory, yi * 9 + xi, xi * 18 + 10, yi * 18 + 38))
  addSlotToContainer(new Slot(te, 0, 10, 10))

  def canInteractWith(p: EntityPlayer): Boolean = true
}

@SideOnly(Side.CLIENT)
class ActuatorGUI(c: Container) extends GuiContainer(c){
  val texture: ResourceLocation = new ResourceLocation("textures/gui/actuator.png")
  def drawGuiContainerBackgroundLayer(x: Float, y: Int, z: Int): Unit = {
    mc.getTextureManager.bindTexture(texture)
    val xo = (width - xSize)/2
    val yo = (height - ySize)/2
    drawTexturedModalRect(xo, yo, 0, 0, xSize, ySize)
  }
}

case class Neighbor(xCoord: Int, zCoord: Int, block: Block)

class MyTE extends TileEntity with IInventory{
  var ticks: Int = 0
  var inventory: Array[ItemStack] = new Array[ItemStack](1)

  override def updateEntity(): Unit = {
    if (ticks == 19) ticks = 0
    if (ticks % 5 == 0) {
      val p = LocalFakePlayerFactory.getMinecraft(Minecraft.getMinecraft.getIntegratedServer.worldServerForDimension(0))
      getNeighbors().foreach { n =>
        if(worldObj.blockExists(n.xCoord,yCoord,n.zCoord)) {
          if( p.inventory.getStackInSlot(0) != null && p.inventory.getStackInSlot(0).stackSize <= 0 ) p.inventory.setInventorySlotContents(0, null)
          if( p.inventory.getStackInSlot(0) == null && inventory(0) != null) p.inventory.setInventorySlotContents(0,decrStackSize(0,1))
          // FIXME: npe possible here if you break the block this thing is molesting
          n.block.onBlockActivated(worldObj, n.xCoord, yCoord, n.zCoord, p, 0, 0.1f, 0.0f, 0.0f)
        } else {
          if(inventory(0) != null && inventory(0).stackSize > 0){
            val ib = new ItemBlock(Block.getBlockFromItem(inventory(0).getItem))
            if(ib != null) ib.placeBlockAt(decrStackSize(0,1), p, worldObj, n.xCoord, yCoord, n.zCoord, 0, 0.1f, 0.0f, 0.0f, 0)
          }
        }
      }
    }
    ticks += 1
  }

  def getNeighbors(): List[Neighbor] = {
    val coords = List((xCoord-1,zCoord), (xCoord+1,zCoord), (xCoord,zCoord-1), (xCoord,zCoord+1))
    coords.map { case(x,z) =>
      Neighbor(x, z, worldObj.getBlock(x, yCoord, z))
    }
  }

  def closeInventory(): Unit = {
  }

  def decrStackSize(slot: Int, i: Int): ItemStack = {
    if (inventory(0) == null) return null
    val oldSize = inventory(0).stackSize
    inventory(0).stackSize -= i
    val newSize = oldSize - i
    var newStack: ItemStack = null
    if (oldSize > 0) newStack = inventory(0).copy
    if (newSize >= 0) newStack.stackSize = i
    if (newSize <= 0) inventory(0) = null
    if (newSize < 0) newStack.stackSize = oldSize
    newStack
  }

  val getInventoryName: String = "Standalone Actuator"

  val getInventoryStackLimit: Int = 64

  val getSizeInventory: Int = 1

  def getStackInSlot(slot: Int): ItemStack = slot match {
      case 0 => inventory(0)
      case _ => null
  }

  def getStackInSlotOnClosing(x$1: Int): ItemStack = {
    null
  }

  val hasCustomInventoryName: Boolean = true

  def isItemValidForSlot(x$1: Int,x$2: ItemStack): Boolean = true

  def isUseableByPlayer(x$1: EntityPlayer): Boolean = true

  def openInventory(){}

  def setInventorySlotContents(x$1: Int,x$2: ItemStack){
    inventory(0) = x$2
    FMLLog.info("%s",inventory)
  }
}

class ActuatorBlock(mat: Material) extends BlockContainer(mat: Material){
  var te: MyTE = null

  def createNewTileEntity(w: World, i: Int) : TileEntity = {
    te = new MyTE
    te
  }

  override def onBlockAdded(w: World, x: Int, y: Int, z: Int){
    //te.getNeighbors
  }

  override def onBlockActivated(w: World, x: Int, y: Int, z: Int, p: EntityPlayer, side: Int, xo: Float, yo: Float, zo: Float): Boolean = {
    StandaloneActuator.instance.log.info("Activated")
    if (!w.isRemote) p.openGui(StandaloneActuator.instance, 1, w, x, y, z)
    true
  }
}

@Mod(modid = "StandaloneActuator", name = "Standalone Actuator", version = "0.0.1", modLanguage = "scala")
object StandaloneActuator {
  var log: Logger = null
  var instance = this

  @EventHandler
  def preInit(e: FMLPreInitializationEvent) {
    log = e.getModLog
  }

  @EventHandler
  def init(e: FMLInitializationEvent) {
    new MyGH()
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
