package es.mariaanasanz.ut7.mods.impl;

import es.mariaanasanz.ut7.mods.base.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
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
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        //my variables:
        Level world = event.getLevel();
        
        //original 'if' code:
        if (ItemStack.EMPTY.equals(heldItem)) { //ItemStack es un Enum que guarda todos los objetos existentes en el juego (empty = air = null)
            System.out.println("La mano esta vacia");
            if (state.getBlock().getName().getString().trim().toLowerCase().endsWith("log")) {
                System.out.println("¡Has hecho click sobre un tronco!");
            }
        }
        
        //my souts:
        System.out.println("[PlayerInteractEvent.RightClickBlock] Saber información generica sobre el evento: "+"\n"+
                                "\t"+"¿Ese objeto usado es una PALA (de cualquier tipo)? == " + event.getItemStack().getItem().toString().trim().endsWith("shovel")+"\n"+
                                "\t"+"EL NOMBRE DEL OBJETO USADO (con toString y trim): " + event.getItemStack().getItem().toString().trim() +"\n"+
                                "\t"+"Saber la posición del bloque afectado y si ese bloque puede transformarse (aka, is it mutable? where is it?): " + event.getPos() +"\n"+
                                "\t"+"Obtener el nombre del mundo en el que se realizó la acción (aka, world file name): "+ event.getLevel() +"\n"+
                                "\t"+"Saber que objeto ha sido usado para la acción y qué cantidad: "+ event.getItemStack() +"\n"+
                                "\t"+"Saber con que mano has realizado la acción [returns: InteractionHand]: "+ event.getHand() +"\n"
//                                "\t"+"" +"\n"+
                            );
        
        System.out.println("[BlockPos] la posicion del bloque afectado es (aka, pos del targeted block):  " + pos +"\n");
        
        System.out.println("[BlockState] El estado ANTERIOR del bloque que ha sido afectado ES (aka, WHICH BLOCK WAS TARGETED?): (state.?)" + state + "\n"+
                                "\t"+"Nombre ANTERIOR del bloque afectado (aka, name del targeted block): "+state.getBlock() +"\n"
//                                "\t"+"" +"\n"+
                            );
        
        System.out.println("[Player] La información sobre el jugador es: (player.?) " + "\n"+
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
                                "\t"+"saber si el jugador está sobre suelo firme: "+player.isOnGround() +"\n"
//                               "\t"+"" +"\n"+
                            );
        
        System.out.println("[ItemStack] El item que sostiene el jugador en MAIN_HAND es: (heldItem.?) " + "\n"+
                                "\t"+"Nombre del item: "+ heldItem.getItem()+"\n"+
                                "\t"+"Cantidad del item: "+ heldItem.getCount() +"\n"+
                                "\t"+"descripcion del item: "+ heldItem.getDescriptionId() +"\n"
//                                "\t"+"" +"\n"+
                            );
    
        System.out.println("[Level] Información general sobre el mundo: (world.?) " + "\n"+
                                "\t"+"Obtener el nombre del mundo: "+world.toString() +"\n"+
                                "\t"+"Saber en qué dimensión está el jugador (ej. Nether): "+world.dimension() +"\n"+
                                "\t"+"Saber en qué Y coord está el nivel del mar: "+world.getSeaLevel() +"\n"
//                                "\t"+"" +"\n"+
                            );
    
        /**
         *   LO QUE SE IMPRIME TRAS EJECUTAR EL METODO:
         *             ¡Has hecho click derecho! AAAA
         *             ¡Has usado un objeto! OBJETO:DEFAULT
         *             [BlockPos] la posicion del bloque afectado es: MutableBlockPos{x=20, y=66, z=11}
         *             [BlockState] El estado del bloque afectado ahora es: Block{minecraft:dirt_path}
         *             [Player] El jugador es: ServerPlayer['Dev'/152, l='ServerLevel[PruebasTuneleitor]', x=21.65, y=67.00, z=12.64]
         *             [ItemStack] El item que sostiene el jugador es:  1 wooden_shovel
         *
         *
         *
         *   CONCLUSIONES RESULTADOS DE LAS PRUEBAS:
         *       event.getUseItem().name() == DEFAULT (??)
         *       pos ==  MutableBlockPos{x=20, y=66, z=11}
         *       state == Block{minecraft:dirt_path}
         *       player == ServerPlayer['Dev'/152, l='ServerLevel[PruebasTuneleitor]', x=21.65, y=67.00, z=12.64]
         *       heldItem == 1 wooden_shovel
         * __________________________________________________________________
         *     ¡Has hecho click derecho! {ONCE}
         *
         *     [event.getUseItem()]¡Has usado un objeto! OBJETO:DEFAULT
         *         ¿Ese objeto es una pala? == false
         *         event.getPos() == MutableBlockPos{x=-10, y=66, z=5}
         *         event.getLevel() == ServerLevel[PruebasTuneleitor]
         *         event.getUseBlock() == DEFAULT
         *         event.getItemStack() == 1 wooden_shovel
         *         [returns: InteractionHand] event.getHand() == MAIN_HAND
         *
         *     [BlockPos] la posicion del bloque afectado es: MutableBlockPos{x=-10, y=66, z=5}
         *
         *     [BlockState] El estado ACTUAL del bloque que ha sido afectado ahora es: Block{minecraft:grass_block}[snowy=false]
         *         Nombre ACTUAL del bloque afectado: Block{minecraft:grass_block}
         *         state.getMaterial() == net.minecraft.world.level.material.Material@440e28fa
         *
         *     [Player] La información sobre el jugador es: (player)
         *         Nombre jugador == literal{Dev}
         *         Objeto que jugador lleva en mano principal == 1 wooden_shovel
         *         ver que tiene en los handSlots: [1 wooden_shovel, 1 air]
         *         player.getBlockX() == -9
         *         player.getBlockY() == 67
         *         player.getBlockZ() == 4
         *         saber en que posicion está el bloque where im standing: BlockPos{x=-9, y=66, z=4}
         *         player.position() == (-8.118774775207585, 67.0, 4.301416443852919)
         *         player.getUseItem() == 1 air
         *         player.isHolding(event.getItemStack().getItem()) == true
         *         saber si el jugador está sobre suelo firme: true
         *         saber si el jugador está sprinteando: false
         *         saber si el jugador está en el agua (sobre ella o bajo ella): false
         *
         *     [ItemStack] El item que sostiene el jugador es: (heldItem)
         *         Nombre del item: wooden_shovel
         *         Cantidad de ese item: 1
         *         heldItem.getDescriptionId() == item.minecraft.wooden_shovel
         *         heldItem.isEmpty() == false
         *         Saber si el objeto que tiene en la mano está encantado: false
         * ________________________________________________________________________________
         *
         */
   
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
