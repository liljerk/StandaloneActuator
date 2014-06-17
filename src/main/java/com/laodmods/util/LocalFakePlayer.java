package com.laodmods.util;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/*
This is a slight modification of net/minecraftforge/common/util/FakePlayerFactory.java.
All credit to the forge team.
*/

//Preliminary, simple Fake Player class 
public class LocalFakePlayer extends EntityPlayerMP
{
    public LocalFakePlayer(WorldServer world, GameProfile name)
    {
        super(FMLCommonHandler.instance().getMinecraftServerInstance(), world, name, new ItemInWorldManager(world));
    }

    @Override public boolean canCommandSenderUseCommand(int i, String s){ return false; }
    @Override public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(0,0,0);
    }

    @Override public void addChatComponentMessage(IChatComponent chatmessagecomponent){}
    @Override public void addStat(StatBase par1StatBase, int par2){}
    @Override public void openGui(Object mod, int modGuiId, World world, int x, int y, int z){}
    @Override public boolean isEntityInvulnerable(){ return true; }
    @Override public boolean canAttackPlayer(EntityPlayer player){ return false; }
    @Override public void onDeath(DamageSource source){ return; }
    @Override public void onUpdate(){ return; }
    @Override public void travelToDimension(int dim){ return; }
    @Override public void func_147100_a(C15PacketClientSettings pkt){ return; }

    // We don't respond to any gui invocations
    @Override public void displayGUIChest(IInventory par1IInventory) { return; }
    @Override public void displayGUIAnvil(int par1, int par2, int par3) { return; }
    @Override public void displayGUIEnchantment(int par1, int par2, int par3, java.lang.String par4Str) { return; }
    @Override public void displayGUIWorkbench(int par1, int par2, int par3) { return; }
    @Override public void func_146093_a(TileEntityHopper p_146093_1_) { return; }
    @Override public void func_146098_a(TileEntityBrewingStand p_146098_1_) { return; }
    @Override public void func_146101_a(TileEntityFurnace p_146101_1_) { return; }
    @Override public void func_146102_a(TileEntityDispenser p_146102_1_) { return; }
    @Override public void func_146104_a(TileEntityBeacon p_146104_1_) { return; }
}
