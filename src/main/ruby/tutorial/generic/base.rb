java_import 'cpw.mods.fml.common.Mod'
java_import 'cpw.mods.fml.common.Mod.EventHandler'
java_import 'cpw.mods.fml.common.Mod.Instance'
java_import 'cpw.mods.fml.common.SidedProxy'
java_import 'cpw.mods.fml.common.event.FMLInitializationEvent'
java_import 'cpw.mods.fml.common.event.FMLPostInitializationEvent'
java_import 'cpw.mods.fml.common.event.FMLPreInitializationEvent'
java_import 'cpw.mods.fml.common.network.NetworkMod'

java_package 'tutorial.generic'

java_annotation 'Mod(modid="GenericModID", name="Generic", version="0.0.0")'
java_annotation 'NetworkMod(clientSideRequired=true)'
class Generic

        java_annotation 'Instance(value = "GenericModID")'
        @instance;
        
        java_annotation 'SidedProxy(clientSide="tutorial.generic.ClientProxy", serverSide="tutorial.generic.CommonProxy")'
        @proxy;
        
        java_annotation 'EventHandler'
        def preInit(event)
        end
        
        java_annotation 'EventHandler'
        def load(event)
          @proxy.registerRenderers()
					p 'woooo'
        end
        
        java_annotation 'EventHandler'
        def postInit(event)
        end
end

class CommonProxy
	def registerRenderers
	end
end

java_import 'net.minecraftforge.client.MinecraftForgeClient'

class ClientProxy < CommonProxy
	java_annotation 'Override'
	def registerRenderers
	end
end
