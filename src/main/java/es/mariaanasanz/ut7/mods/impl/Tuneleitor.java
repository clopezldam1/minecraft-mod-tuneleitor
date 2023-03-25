package es.mariaanasanz.ut7.mods.impl;

import es.mariaanasanz.ut7.mods.base.*;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

/**
 * TUNELEITOR:
 *
 * Crea un mod que permita al jugador generar un túnel desde la posición
 *  en la que se encuentra hasta el nivel 5 del suelo.
 * Para ello, el jugador deberá interactuar con UN PICO en un trozo de tierra
 *  (es decir, hacer click derecho sobre un trozo de tierra).
 * El túnel deberá ir descendiendo con escaleras o simplemente
 *  con diferencias de 1 altura hasta llegar al nivel 5.
 * La altura del túnel deberá ser de 4 y la anchura de 3.
 * Deberás recubrir el túnel (es decir, las paredes y techo) con cristal.
 * ¡Cuidado! Los bloques que destruyas para generar el túnel no deberán dropearse.
 */
@Mod(DamMod.MOD_ID)
public class Tuneleitor extends DamMod implements IBlockBreakEvent, IServerStartEvent,
        IItemPickupEvent, ILivingDamageEvent, IUseItemEvent, IFishedEvent,
        IInteractEvent, IMovementEvent {

    
    public Tuneleitor(){
        super();
    }

    @Override
    public String autor() {
        return "Cristina López Lusarreta";
    }

    @Override
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockPos pos = event.getPos();
        System.out.println("Bloque destruido en la posicion "+pos);
    }

    @Override
    @SubscribeEvent
    public void onServerStart(ServerStartingEvent event) {
        LOGGER.info("Server starting");
    }

    @Override
    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        LOGGER.info("Item recogido");
        System.out.println("Item recogido");
    }

    @Override
    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        System.out.println("evento LivingDamageEvent invocado "+event.getEntity().getClass()+" provocado por "+event.getSource().getEntity());
    }

    @Override
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        System.out.println("evento LivingDeathEvent invocado "+event.getEntity().getClass()+" provocado por "+event.getSource().getEntity());

    }

    @Override
    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        LOGGER.info("evento LivingEntityUseItemEvent invocado "+event.getEntity().getClass());
        System.out.println("has utilzado el item " + event.getItem());
    }


    @Override
    @SubscribeEvent
    public void onPlayerFish(ItemFishedEvent event) {
        System.out.println("¡Has pescado un pez!");
    }

    @Override
    @SubscribeEvent
    public void onPlayerTouch(PlayerInteractEvent.RightClickBlock event) {
        System.out.println("¡Has hecho click derecho! {ONCE}");
        
        //original variables:
        BlockPos pos = event.getPos();  //.getY(), .getX(), .getZ()
        BlockState state = event.getLevel().getBlockState(pos);
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        
        //my variables:
        Level world = event.getLevel();
        ItemStack objetoUsado = event.getItemStack(); //@NotNull ¿?
        BlockPos targetedBlockPos = new BlockPos(pos.getX(),pos.getY(),pos.getZ());
        Block targetedBlock = state.getBlock();
        BlockState blockStandingOn = player.getBlockStateOn();
        Block grassBlock = Blocks.GRASS_BLOCK;
        Block glassBlock = Blocks.GLASS;
        Block airBlock = Blocks.AIR;
        TagKey<Item> pickAxes = Tags.Items.TOOLS_PICKAXES; //objetos pico de cualquier tipo
        //GameTestHelper helper = new GameTestHelper(null); ESTO ES INUTIL LOL
        ChunkAccess chunkTargeted1 = event.getLevel().getChunk(targetedBlockPos);
        //LevelChunk chunkTargeted2 = event.getLevel().getChunk(targetedBlockPos.getX(), targetedBlockPos.getZ());
        //BlockEntity chunkTargeted3 = event.getLevel().getBlockEntity(targetedBlockPos);
        
        //____________________________________________________________________________________________________
        //original 'if' code: (2 souts)
        if (ItemStack.EMPTY.equals(heldItem)) { //ItemStack es un Enum que guarda todos los objetos existentes en el juego (empty = air = null)
            System.out.println("La mano esta vacia");
            if (state.getBlock().getName().getString().trim().toLowerCase().endsWith("log")) {
                System.out.println("¡Has hecho click sobre un tronco!");
            }
        }
        
        //MY CODE:
        System.out.println("\n---- PROBAR COSAS IDK (sacar mas variables) ----");
            System.out.println("targetedBlock: "+targetedBlock);
            System.out.println("blockStandingOn.getBlock(): "+blockStandingOn.getBlock()); //ESTO SALE COMO AIRE SIEMPRE, ES EL BLOQUE DE ENCIMA DE EL QUE ESTOY PISANDO
            System.out.println("\ttargetedBlock.asItem().getDefaultInstance() == " + targetedBlock.asItem().getDefaultInstance());
            System.out.println("\ttargetedBlock.asItem().getDefaultInstance().getItem == " + targetedBlock.asItem().getDefaultInstance().getItem());
            System.out.println("\tblockStandingOn.getBlock().asItem().getDefaultInstance().getItem() == " + blockStandingOn.getBlock().asItem().getDefaultInstance().getItem());
            System.out.println("\tblockStandingOn.getBlock().asItem().getDefaultInstance() == " + blockStandingOn.getBlock().asItem().getDefaultInstance()+"\n");
           
            if( ItemStack.isSame(targetedBlock.asItem().getDefaultInstance(), blockStandingOn.getBlock().asItem().getDefaultInstance()) ){
                System.out.println("\tEl bloque sobre el que está standing ES IGUAL  que el bloque al que apunta");
            }else{
                System.out.println("\tSON BLOQUES DISTINTOS al que se le apunta y sobre el que está");
            }
        
        System.out.println("\n---- TRANSFORMAR TARGETED BLOCK ----");
            if(targetedBlock.equals(grassBlock)){ //COMPROBACIÓN ANTES DE REEMPLAZAR EL BLOQUE AL QUE APUNTO
                System.out.println("\thas actuado sobre un bloque de hierva [ANTES]");
            }else{
                System.out.println("\tNO ESTAS ACTUANDO SOBRE BLOQUE DE HIERVA [ANTES]"); //DA MAL (sale que targeted block es aire)
            }
            
            //cambiar el targetedblock a uno de cristal
                //event.getLevel().onBlockStateChange(targetedBlockPos,targetedBlock.defaultBlockState(),glassBlock.defaultBlockState());
                chunkTargeted1.setBlockState(targetedBlockPos, glassBlock.defaultBlockState(), true); //ESTO FUNCIONA pero con retardo
                chunkTargeted1.setBlockState(targetedBlockPos, glassBlock.defaultBlockState(), true); //ESTO FUNCIONA pero con retardo
    
        //chunkTargeted2.setBlockState(targetedBlockPos, glassBlock.defaultBlockState(), true); //esto no funciona
                //chunkTargeted3.setBlockState(targetedBlock.defaultBlockState()); ESTO PETA
        
            if(targetedBlock.equals(glassBlock)){ //COMPROBACIÓN ANTES DE REEMPLAZAR EL BLOQUE AL QUE APUNTO
                System.out.println("\tse ha transformado en un bloque de cristal [DESPUES]"); //DA BIEN
            }else if(targetedBlock.equals(grassBlock)){
                System.out.println("\tel bloque no ha sido afectado, sigue siendo hierva [DESPUES]");
            }else{
                System.out.println("\ttodo ha salido mal y no sé en qué se ha convertido ese bloque [DESPUES]");
            }
        
    
        //my souts: -----------------------------------------------------------------------------------------------
        StringBuilder sb = new StringBuilder();
        sb.append("____________________________________________________________________________________________________________");
        sb.append("[PlayerInteractEvent.RightClickBlock] Saber información generica sobre el evento: "+"\n"+
                        "\t"+"¿Ese objeto usado es una PALA (de cualquier tipo)? == " + event.getItemStack().getItem().toString().trim().endsWith("shovel")+"\n"+
                        "\t"+"EL NOMBRE DEL OBJETO USADO (con toString y trim): " + event.getItemStack().getItem().toString().trim() +"\n"+
                        "\t"+"Saber la posición del bloque afectado y si ese bloque puede transformarse (aka, is it mutable? where is it?): " + event.getPos() +"\n"+
                        "\t"+"Obtener el nombre del mundo en el que se realizó la acción (aka, world file name): "+ event.getLevel() +"\n"+
                        "\t"+"Saber que objeto ha sido usado para la acción y en qué cantidad: "+ event.getItemStack() +"\n"+
                        "\t"+"Saber con que mano has realizado la acción [returns: InteractionHand]: "+ event.getHand() +"\n"
//                      "\t"+"" +"\n"+
                    );
        
        sb.append("[BlockPos] la posicion del bloque afectado es (aka, pos del targeted block):  " + pos +"\n");
        
        sb.append("[BlockState] El estado ANTERIOR del bloque que ha sido afectado es (aka, WHICH BLOCK WAS TARGETED?): (state.?)" + state + "\n"+
                        "\t"+"Nombre ANTERIOR del bloque afectado (aka, name del targeted block): "+state.getBlock() +"\n"
//                      "\t"+"" +"\n"+
                    );
        
        sb.append("[Player] La información sobre el jugador es: (player.?) " + "\n"+
                        "\t"+"Nombre jugador: " + player.getName() + "\n"+
                        "\t"+"Objeto que jugador lleva en mano DERECHA(MAIN_HAND): "+player.getMainHandItem() +"\n"+
                        "\t"+"Ver que objetos hay en ambas manos del jugador [MAIN_HAND, OFF_HAND]: "+player.getHandSlots() +"\n"+
                        "\t"+"Obtener coordenada X de la posicion del bloque where im standing: "+player.getBlockX() +"\n"+
                        "\t"+"Obtener coordenada Y de la posicion del bloque where im standing: "+player.getBlockY() +"\n"+
                        "\t"+"Obtener coordenada Z de la posicion del bloque where im standing: "+player.getBlockZ() +"\n"+
                        "\t"+"Saber en que posicion está el bloque where player is standing: "+player.getOnPos() +"\n"+
                        "\t"+"Saber posicion XYZ del jugador en el momento de la acción: "+player.position() +"\n"+
                        "\t"+"Obtener objeto en la mano izquierda del jugador: "+player.getUseItem() +"\n"+
                        "\t"+"Saber si jugador sigue sosteniendo el objeto usado: "+player.isHolding(event.getItemStack().getItem()) +"\n"+
                        "\t"+"saber si el jugador está sobre suelo firme: "+player.isOnGround() +"\n"+
                        "\t"+"Obtener el bloque sobre el que el jugador is standing: "+player.getFeetBlockState() +"\n"
//                      "\t"+"" +"\n"+
                    );
        
        sb.append("[ItemStack] El item que sostiene el jugador en MAIN_HAND es: (heldItem.?) " + "\n"+
                        "\t"+"Nombre del item: "+ heldItem.getItem()+"\n"+
                        "\t"+"Cantidad del item: "+ heldItem.getCount() +"\n"+
                        "\t"+"descripcion del item: "+ heldItem.getDescriptionId() +"\n"
//                      "\t"+"" +"\n"+
                    );
    
        sb.append("[Level] Información general sobre el mundo: (world.?) " + "\n"+
                        "\t"+"Obtener el nombre del mundo: "+world.toString() +"\n"+
                        "\t"+"Saber en qué dimensión está el jugador (ej. Nether): "+world.dimension() +"\n"+
                        "\t"+"Saber en qué Y coord está el nivel del mar: "+world.getSeaLevel() +"\n"
//                      "\t"+"" +"\n"+
                    );
        sb.append("____________________________________________________________________________________________________________");
//        System.out.println(sb);
        //-----------------------------------------------------------------------------------------------------------
        
   
    }

    @Override
    @SubscribeEvent
    public void onPlayerWalk(MovementInputUpdateEvent event) {
        if(event.getEntity() instanceof Player){
            if(event.getInput().down){
                System.out.println("down "+event.getInput().down);
            }
            if(event.getInput().up){
                System.out.println("up "+event.getInput().up);
            }
            if(event.getInput().right){
                System.out.println("right "+event.getInput().right);
            }
            if(event.getInput().left){
                System.out.println("left "+event.getInput().left);
            }
        }
    }
    
//    @SubscribeEvent
//    public void onPlayerUseItem(PlayerInteractEvent.RightClickBlock event){ //ej. cuando jugador usa el pico (right-click)
//        System.out.println("¡Has usado un objeto! OBJETO:" + event.getUseItem().name());
//
//        BlockPos pos = event.getPos();
//        System.out.println("[BlockPos] la posicion del bloque afectado es: " + pos);
//
//        BlockState state = event.getLevel().getBlockState(pos);
//        System.out.println("[BlockState] El estado del bloque afectado ahora es: " + state);
//
//        Player player = event.getEntity();
//        System.out.println("[Player] El jugador es: " + player);
//
//        ItemStack heldItem = player.getMainHandItem();
//        System.out.println("[ItemStack] El item que sostiene el jugador es:  " + heldItem);
//    }
}
