package es.mariaanasanz.ut7.mods.impl;

import es.mariaanasanz.ut7.mods.base.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
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
        
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        Player player = event.getEntity();
        ItemStack heldItem = player.getMainHandItem();
        
        if (ItemStack.EMPTY.equals(heldItem)) {
            System.out.println("La mano esta vacia");
            if (state.getBlock().getName().getString().trim().toLowerCase().endsWith("log")) {
                System.out.println("¡Has hecho click sobre un tronco!");
            }
        }
    
        System.out.println("[event.getUseItem()]¡Has usado un objeto! OBJETO:" + event.getUseItem()+ "\n"+
                                "\t"+"¿Ese objeto usado es una PALA (de cualquier tipo)? == " + event.getItemStack().getItem().toString().trim().endsWith("shovel")+"\n"+
                                "\t"+"event.getPos() == " + event.getPos() +"\n"+
                                "\t"+"event.getLevel() == "+ event.getLevel() +"\n"+
                                "\t"+"event.getUseBlock() == "+event.getUseBlock() +"\n"+
                                "\t"+"[event.getItemStack()] CUAL FUE EL OBJETO USADO == "+ event.getItemStack() +"\n"+
                                "\t"+"[returns: InteractionHand // event.getHand()] QUE MANO HA SIDO USADA PARA LA ACCION == "+ event.getHand() +"\n"
//                                "\t"+"" +"\n"+
                            );
        
        System.out.println("[BlockPos] la posicion del bloque afectado es: " + pos);
        
        System.out.println("[BlockState] El estado ANTERIOR del bloque que ha sido afectado ES (aka, WHICH BLOCK WAS TARGETED?): " + state + "\n"+
                                "\t"+"Nombre ANTERIOR del bloque afectado: "+state.getBlock() +"\n"+
                                "\t"+"state.getMaterial() == "+ state.getMaterial() +"\n"
//                                "\t"+"" +"\n"+
                            );
        
        System.out.println("[Player] La información sobre el jugador es: (player) " + "\n"+
                                "\t"+"Nombre jugador == " + player.getName() + "\n"+
                                "\t"+"Objeto que jugador lleva en mano DERECHA(MAIN_HAND) "+player.getMainHandItem() +"\n"+
                                "\t"+"ver que tiene en los handSlots: "+player.getHandSlots() +"\n"+
                                "\t"+"player.getBlockX() == "+player.getBlockX() +"\n"+
                                "\t"+"player.getBlockY() == "+player.getBlockY() +"\n"+
                                "\t"+"player.getBlockZ() == "+player.getBlockZ() +"\n"+
                                "\t"+"saber en que posicion está el bloque where im standing: "+player.getOnPos() +"\n"+
                                "\t"+"player.position() == "+player.position() +"\n"+
                                "\t"+"player.getUseItem() == "+player.getUseItem() +"\n"+
                                "\t"+"player.isHolding(event.getItemStack().getItem()) == "+player.isHolding(event.getItemStack().getItem()) +"\n"+
                                "\t"+"saber si el jugador está sobre suelo firme: "+player.isOnGround() +"\n"+
                                "\t"+"saber si el jugador está sprinteando: "+player.isSprinting() +"\n"+
                                "\t"+"saber si el jugador está en el agua (SOLO SI ESTA TOCANDO EL AGUA O BAJO ELLA): "+player.isInWater() +"\n"
//                               "\t"+"" +"\n"+
                            );
        System.out.println("[ItemStack] El item que sostiene el jugador es: (heldItem) " + "\n"+
                                "\t"+"Nombre del item: "+ heldItem.getItem()+"\n"+
                                "\t"+"Cantidad de ese item: "+ heldItem.getCount() +"\n"+
                                "\t"+"heldItem.getDescriptionId() == "+ heldItem.getDescriptionId() +"\n"+
                                "\t"+"heldItem.isEmpty() == "+ heldItem.isEmpty() +"\n"+
                                "\t"+"Saber si el objeto que tiene en la mano está encantado: "+ heldItem.isEnchanted()+"\n"
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
