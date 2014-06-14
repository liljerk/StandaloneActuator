package com.laodmods.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

/*
This is a direct copy of net/minecraftforge/common/util/FakePlayerFactory.java.
All credit to the forge team.
*/

//To be expanded for generic Mod fake players?
public class LocalFakePlayerFactory
{
    private static GameProfile MINECRAFT = new GameProfile("41C82C87-7AfB-4024-BA57-13D2C99CAE77", "[Minecraft]");
    // Map of all active fake player usernames to their entities
    private static Map<GameProfile, LocalFakePlayer> fakePlayers = Maps.newHashMap();
    private static LocalFakePlayer MINECRAFT_PLAYER = null;
    
    public static LocalFakePlayer getMinecraft(WorldServer world)
    {
        if (MINECRAFT_PLAYER == null)
        {
            MINECRAFT_PLAYER = LocalFakePlayerFactory.get(world,  MINECRAFT);
        }
        return MINECRAFT_PLAYER;
    }
    
    /**
     * Get a fake player with a given username,
     * Mods should either hold weak references to the return value, or listen for a 
     * WorldEvent.Unload and kill all references to prevent worlds staying in memory.
     */
    public static LocalFakePlayer get(WorldServer world, GameProfile username)
    {
        if (!fakePlayers.containsKey(username))
        {
            LocalFakePlayer fakePlayer = new LocalFakePlayer(world, username);
            fakePlayers.put(username, fakePlayer);
        }

        return fakePlayers.get(username);
    }

    public static void unloadWorld(WorldServer world)
    {
        Iterator<Entry<GameProfile, LocalFakePlayer>> itr = fakePlayers.entrySet().iterator();
        while (itr.hasNext())
        {
            Entry<GameProfile, LocalFakePlayer> entry = itr.next();
            if (entry.getValue().worldObj == world)
            {
                itr.remove();
            }
        }
    }
}

