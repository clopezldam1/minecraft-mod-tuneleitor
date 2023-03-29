package es.mariaanasanz.ut7.mods.impl;

import es.mariaanasanz.ut7.mods.base.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
        BlockPos pos = event.getPos();  //.getY(), .getX(), .getZ()
        BlockState state = event.getLevel().getBlockState(pos);
        Player player = event.getEntity();
//        ItemStack heldItem = player.getMainHandItem();
        
        //my variables:
        Level world = player.getCommandSenderWorld();
//        ItemStack objetoUsado = event.getItemStack(); //@NotNull ¿?
//        BlockPos targetedBlockPos = new BlockPos(pos.getX(),pos.getY(),pos.getZ());
    
        Block grassBlock = Blocks.GRASS_BLOCK;
        Block targetedBlock = state.getBlock();
    
    
//        //____________________________________________________________________________________________________
//        //original 'if' code: (2 souts)
//        if (ItemStack.EMPTY.equals(heldItem)) { //ItemStack es un Enum que guarda todos los objetos existentes en el juego (empty = air = null)
//            System.out.println("La mano esta vacia");
//            if (state.getBlock().getName().getString().trim().toLowerCase().endsWith("log")) {
//                System.out.println("¡Has hecho click sobre un tronco!");
//            }
//        }
    
        if(event.getItemStack().getItem().toString().trim().endsWith("pickaxe")) { //si la herramienta empleada es un pico
            if (targetedBlock.equals(grassBlock)) { //si se ha usado un pico sobre un bloque de hierva
                generarTunel(event);
            }
        }
    
//        /** COMO ACTUALIZAR  BLOQUES EN CLIENTE Y SERVIDOR: (poner ambas sentencias seguidas para que se actualice correctamente en ambos) -- explicado por Javi
//         *
//         * event.getLevel().setBlock(, ,); ==ACTUALIZAR BLOQUE EN EL CLIENTE (LO QUE VES, TEMPORAL)
//         * Minecraft.getInstance().level.setBlock(, , ); == ACTUALIZAR BLOQUE EN EL SERVIDOR (LO QUE OCURRE, PERMANENTE)
//         */
        
        
        //my souts: -----------------------------------------------------------------------------------------------
//        StringBuilder sb = new StringBuilder();
//        sb.append("____________________________________________________________________________________________________________");
//        sb.append("[PlayerInteractEvent.RightClickBlock] Saber información generica sobre el evento: "+"\n"+
//                        "\t"+"¿Ese objeto usado es una PALA (de cualquier tipo)? == " + event.getItemStack().getItem().toString().trim().endsWith("shovel")+"\n"+
//                        "\t"+"EL NOMBRE DEL OBJETO USADO (con toString y trim): " + event.getItemStack().getItem().toString().trim() +"\n"+
//                        "\t"+"Saber la posición del bloque afectado y si ese bloque puede transformarse (aka, is it mutable? where is it?): " + event.getPos() +"\n"+
//                        "\t"+"Obtener el nombre del mundo en el que se realizó la acción (aka, world file name): "+ event.getLevel() +"\n"+
//                        "\t"+"Saber que objeto ha sido usado para la acción y en qué cantidad: "+ event.getItemStack() +"\n"+
//                        "\t"+"Saber con que mano has realizado la acción [returns: InteractionHand]: "+ event.getHand() +"\n"
////                      "\t"+"" +"\n"+
//                    );
//
//        sb.append("[BlockPos] la posicion del bloque afectado es (aka, pos del targeted block):  " + pos +"\n");
//
//        sb.append("[BlockState] El estado ANTERIOR del bloque que ha sido afectado es (aka, WHICH BLOCK WAS TARGETED?): (state.?)" + state + "\n"+
//                        "\t"+"Nombre ANTERIOR del bloque afectado (aka, name del targeted block): "+state.getBlock() +"\n"
////                      "\t"+"" +"\n"+
//                    );
//
//        sb.append("[Player] La información sobre el jugador es: (player.?) " + "\n"+
//                        "\t"+"Nombre jugador: " + player.getName() + "\n"+
//                        "\t"+"Objeto que jugador lleva en mano DERECHA(MAIN_HAND): "+player.getMainHandItem() +"\n"+
//                        "\t"+"Ver que objetos hay en ambas manos del jugador [MAIN_HAND, OFF_HAND]: "+player.getHandSlots() +"\n"+
//                        "\t"+"Obtener coordenada X de la posicion del bloque where im standing: "+player.getBlockX() +"\n"+
//                        "\t"+"Obtener coordenada Y de la posicion del bloque where im standing: "+player.getBlockY() +"\n"+
//                        "\t"+"Obtener coordenada Z de la posicion del bloque where im standing: "+player.getBlockZ() +"\n"+
//                        "\t"+"Saber en que posicion está el bloque where player is standing: "+player.getOnPos() +"\n"+
//                        "\t"+"Saber posicion XYZ del jugador en el momento de la acción: "+player.position() +"\n"+
//                        "\t"+"Obtener objeto en la mano izquierda del jugador: "+player.getUseItem() +"\n"+
//                        "\t"+"Saber si jugador sigue sosteniendo el objeto usado: "+player.isHolding(event.getItemStack().getItem()) +"\n"+
//                        "\t"+"saber si el jugador está sobre suelo firme: "+player.isOnGround() +"\n"+
//                        "\t"+"Saber a qué dirección está facing el jugador: "+player.getDirection() +"\n"+
//                        "\t"+"Obtener el bloque sobre el que el jugador is standing: "+player.getFeetBlockState() +"\n"
////                      "\t"+"" +"\n"+
//                    );
//
//        sb.append("[ItemStack] El item que sostiene el jugador en MAIN_HAND es: (heldItem.?) " + "\n"+
//                        "\t"+"Nombre del item: "+ heldItem.getItem()+"\n"+
//                        "\t"+"Cantidad del item: "+ heldItem.getCount() +"\n"+
//                        "\t"+"descripcion del item: "+ heldItem.getDescriptionId() +"\n"
////                      "\t"+"" +"\n"+
//                    );
//
//        sb.append("[Level] Información general sobre el mundo: (world.?) " + "\n"+
//                        "\t"+"Obtener el nombre del mundo: "+world.toString() +"\n"+
//                        "\t"+"Saber en qué dimensión está el jugador (ej. Nether): "+world.dimension() +"\n"+
//                        "\t"+"Saber en qué Y coord está el nivel del mar: "+world.getSeaLevel() +"\n"
////                      "\t"+"" +"\n"+
//                    );
//        sb.append("____________________________________________________________________________________________________________");
////        System.out.println(sb);
//        //-----------------------------------------------------------------------------------------------------------
//
    }
   
    public void generarTunel(PlayerInteractEvent.RightClickBlock event){
        //Posición del bloque (y otros):
        BlockPos pos = event.getPos();  //.getY(), .getX(), .getZ()
        BlockPos targetedBlockPos = new BlockPos(pos.getX(),pos.getY(),pos.getZ());
        BlockState state = event.getLevel().getBlockState(pos);
        Block targetedBlock = state.getBlock();
        
        //Bloques y herramientas:
        Block grassBlock = Blocks.GRASS_BLOCK;
        Block greenGlass = Blocks.GREEN_STAINED_GLASS;
        Block orangeGlass = Blocks.ORANGE_STAINED_GLASS;
        Block clearGlass = Blocks.GLASS;
        Block blackGlass = Blocks.BLACK_STAINED_GLASS;
        Block airBlock = Blocks.AIR;
        Block railBlock = Blocks.RAIL; //POWERED_RAIL -> for more speed (if needed)
        Item minecartItem = Items.MINECART;
//        TagKey<Item> pickAxes = Tags.Items.TOOLS_PICKAXES; //objetos pico de cualquier tipo
        
        //obtener el mundo del jugador:
        Level world = event.getLevel();
    
        //Orientación del jugador (and other player-related things):
        Player player = event.getEntity();
        Direction facing = player.getDirection();
        BlockState blockStandingOn = player.getBlockStateOn();
        BlockPos blockStandingOnPos = player.getOnPos();
    
        //ESCALERAS: (top/bottom)
        int coordYStairsBottom = targetedBlockPos.getY(); //coordenada Y de peldaño escaleras
        int coordZStairsBottom = targetedBlockPos.getZ(); //coordenada Z de peldaño escaleras
        int coordXStairsBottom = targetedBlockPos.getX(); //coordenada X de peldaño escaleras
        
        int coordZBlockStairsTop = targetedBlockPos.getZ(); //coordenada Z de techo escaleras
        int coordXBlockStairsTop = targetedBlockPos.getX(); //coordenada X de techo escaleras
        
        //PARED: (left & right)
        int coordYWall = targetedBlockPos.getY(); //empezamos a construir la pared al ras del escalónn
        int coordXWall = targetedBlockPos.getX();
        int coordZWall = targetedBlockPos.getZ();
        
        
//        colocarVagoneta();
        BlockPos minecartPos = targetedBlockPos.above();
        
        
       
        

//        if(event.getItemStack().getItem().toString().trim().endsWith("pickaxe")) { //si la herramienta empleada es un pico
//            if (targetedBlock.equals(grassBlock)) { //si se ha usado un pico sobre un bloque de hierva
                while (coordYStairsBottom >= 5) { //parar de generar el tunel en la coordenada Y = 5 (jugador debe usar pico en un bloque que esté por encima para que se genere el tunel)
//                  --------------------------------------------------------------------------------------------------------------------------------------------
                    if (facing.toString().equals("south")) { //genera tunel hacia el sur (aka, where ur facing)
                        
                        addBlock(world, clearGlass.defaultBlockState(), coordXStairsBottom, coordYStairsBottom, coordZStairsBottom); //BOTTOM
                        addBlock(world, blackGlass.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 5, coordZStairsBottom); //TOP
                        
                        addBlock(world, railBlock.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 1, coordZStairsBottom); //LOWER MIDDLE
                        for (int i = 2; i <=4 ; i++) {
                            addBlock(world, airBlock.defaultBlockState(), coordXStairsBottom, coordYWall + i, coordZWall); //HIGHER MIDDLE
                        }
            
                        coordZStairsBottom++;
                        coordZBlockStairsTop++;
                      //  coordYBlockStairsTop; SE QUEDA SIEMPRE IGUAL
            
                        for (int j = 1; j <= 5; j++) { //quiero que ponga 5 bloques más porque empiezo desde la esquina inferior exterior, al ras de los escalones
                            addBlock(world, greenGlass.defaultBlockState(), coordXWall-1, coordYWall, coordZWall); //RIGHT WALL
                            addBlock(world, orangeGlass.defaultBlockState(), coordXWall+1, coordYWall, coordZWall); //LEFT WALL
                
                            coordYWall++;
                        }
                        coordYWall -= 5;
            
                        coordZWall++; //valido para pared izquierda y derecha
                        //  coordXBlockWallLeftNorth; NO CAMBIA MAS QUE 1 VEZ
                        //  coordXBlockWallRightNorth;
//                  --------------------------------------------------------------------------------------------------------------------------------------------
                    } else if (facing.toString().equals("west")) {
                        addBlock(world, clearGlass.defaultBlockState(), coordXStairsBottom, coordYStairsBottom, coordZStairsBottom); //BOTTOM
                        addBlock(world, blackGlass.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 5, coordZStairsBottom); //TOP
    
                        addBlock(world, railBlock.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 1, coordZStairsBottom); //LOWER MIDDLE
                        for (int i = 2; i <=4 ; i++) {
                            addBlock(world, airBlock.defaultBlockState(), coordXStairsBottom, coordYWall + i, coordZWall); //HIGHER MIDDLE
                        }
            
                        coordXStairsBottom--;
                        coordXBlockStairsTop--;
                        // coordZBlockStairs; SE MANTIENE IGUAL
            
                        for (int j = 0; j <= 4; j++) { //quiero que ponga 5 bloques más porque empiezo desde la esquina inferior exterior, al ras de los escalones
                            addBlock(world, greenGlass.defaultBlockState(), coordXWall, coordYWall, coordZWall-1); //RIGHT WALL
                            addBlock(world, orangeGlass.defaultBlockState(), coordXWall, coordYWall, coordZWall+1); //LEFT WALL
                
                            coordYWall++;
                        }
                        coordYWall -= 5;
            
                        //  coordZBlockWallLeftEast; NO CAMBIA MAS QUE 1 VEZ
                        //  coordZBlockWallRightEast;
                        coordXWall--; //valido para pared izquierda y derecha
//                  --------------------------------------------------------------------------------------------------------------------------------------------
                    } else if (facing.toString().equals("north")) {
                        addBlock(world, clearGlass.defaultBlockState(), coordXStairsBottom, coordYStairsBottom, coordZStairsBottom); //BOTTOM
                        addBlock(world, blackGlass.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 5, coordZStairsBottom); //TOP
    
                        addBlock(world, railBlock.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 1, coordZStairsBottom); //LOWER MIDDLE
                        for (int i = 2; i <=4 ; i++) {
                            addBlock(world, airBlock.defaultBlockState(), coordXStairsBottom, coordYWall + i, coordZWall); //HIGHER MIDDLE
                        }

                        coordZStairsBottom--;
                        coordZBlockStairsTop--;
                        //  coordXBlockStairs; //SE QUEDA IGUAL
            
                        for (int j = 0; j <= 4; j++) { //quiero que ponga 5 bloques más porque empiezo desde la esquina inferior exterior, al ras de los escalones
                            addBlock(world, greenGlass.defaultBlockState(), coordXWall+1, coordYWall, coordZWall); //RIGHT WALL
                            addBlock(world, orangeGlass.defaultBlockState(), coordXWall-1, coordYWall, coordZWall); //LEFT WALL
                
                            coordYWall++;
                        }
                        coordYWall -= 5;
            
                        coordZWall--; //valido para pared izquierda y derecha
                        //   coordXBlockWallLeftSouth; NO CAMBIA MAS QUE 1 VEZ
                        //    coordXBlockWallRightSouth;
//                  --------------------------------------------------------------------------------------------------------------------------------------------
            
                    } else if (facing.toString().equals("east")) {
                        addBlock(world, clearGlass.defaultBlockState(), coordXStairsBottom, coordYStairsBottom, coordZStairsBottom); //BOTTOM
                        addBlock(world, blackGlass.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 5, coordZStairsBottom); //TOP
                        
                        addBlock(world, railBlock.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 1, coordZStairsBottom); //LOWER MIDDLE
                        for (int i = 2; i <=4 ; i++) {
                            addBlock(world, airBlock.defaultBlockState(), coordXStairsBottom, coordYWall + i, coordZWall); //HIGHER MIDDLE
                        }
            
                        coordXStairsBottom++;
                        coordXBlockStairsTop++;
//                      coordZWall; //SE QUEDA IGUAL
            
                        for (int j = 0; j <= 4; j++) { //quiero que ponga 5 bloques más porque empiezo desde la esquina inferior exterior, al ras de los escalones
                             addBlock(world, greenGlass.defaultBlockState(), coordXWall, coordYWall, coordZWall+1); //RIGHT WALL
                             addBlock(world, orangeGlass.defaultBlockState(), coordXWall, coordYWall, coordZWall-1); //LEFT WALL
                
                            coordYWall++;
                        }
                        coordYWall -= 5;
            
                        //   coordZBlockWallLeftWest;
                        //   coordZBlockWallRightWest;
                        coordXWall++;
                    }
                    coordYStairsBottom--; //las escaleras siempre bajan 1 en altura, tanto en el suelo como en el techo
                    coordYWall--; //las paredes tambien van bajando 1 en altura siempre, a la par que las escaleras
                }
//              --------------------------------------------------------------------------------------------------------------------------------------------
//            }
//        }
        
        generarEntradaTunel(facing, world, blockStandingOnPos);

        BlockPos lastTunnelBlockPos = new BlockPos(coordXStairsBottom, coordYStairsBottom, coordZStairsBottom);
        generarFinTrayecto(facing, world, lastTunnelBlockPos);
        
    }
    
    /**
     * Este método te añadirá un bloque más en las 4 partes del tunel: escalones, techo, pared derecha y pared izquierda
     * (llamar a este método hasta terminar de cubrir toda la longitud del tunel)
     * 
     * @param world - obtiene el mundo en el que el jugador está
     * @param newBlock - el bloque al que quieres transformar el selectedBlock 
     * @param coordX - coordenada x del bloque que quieres transformar
     * @param coordY - coordenada y del bloque que quieres transformar
     * @param coordZ - coordenada z del bloque que quieres transformar
     */
    public void addBlock(Level world, BlockState newBlock, int coordX, int coordY, int coordZ){
        BlockPos newPos = new BlockPos(coordX, coordY, coordZ);
        world.setBlock(newPos, newBlock,5010); //modifies block in client
    }
  
    /**
     * Este método crea una entrada bonita para el tunel,
     * sólo abarcando la parte del túnel que sale a la superficie
     * donde el jugador está
     *
     * @param facing - dirección hacia donde mira el jugador
     * @param world - el mundo en el que el jugador está
     * @param targetedBlockPos - la posición del bloque al que apunta el jugador (aka, primer escalón del túnel)
     */
    public void generarEntradaTunel(Direction facing, Level world, BlockPos targetedBlockPos){
        Block acaciaPlanksBlock = Blocks.ACACIA_PLANKS;
    
        int coordY = targetedBlockPos.getY();
    
        int coordXLeft = 0;
        int coordXRight = 0;
    
        int coordZLeft = 0;
        int coordZRight = 0;
        
        System.out.println("GENERAR ENTRADA TÚNEL");
        
        for (int i = 0; i < 4; i++) {
            switch (facing.toString()){
                case "south":
                    coordXLeft--;
                    coordXRight++;
                    break;
        
                case "west":
                    coordZLeft--;
                    coordZRight++;
                    break;
        
                case "north":
                    coordXLeft++;
                    coordXRight--;
                    break;
        
                case "east":
                    coordZLeft++;
                    coordZRight--;
                    break;
            }
    
            for (int j = 0; j < 4; j++) {
                addBlock(world, acaciaPlanksBlock.defaultBlockState(), coordXRight, coordY,coordZRight);
                addBlock(world, acaciaPlanksBlock.defaultBlockState(), coordXLeft, coordY,coordZLeft);
                
                coordY++;
            }
            coordY -= 5;
            
        }
    }
    
    /**
     * Este método creará una área segura al final del tunel
     * para prevenir que el jugador no corra peligro haya lo
     * que haya al final del tunel en el nivel Y=5
     *
     * @param facing - dirección hacia donde mira el jugador
     * @param world - el mundo en el que el jugador está
     * @param lastTunnelBlockPos - posicion del último bloque del tunel (aka, último escalón)
     */
    public void generarFinTrayecto(Direction facing, Level world, BlockPos lastTunnelBlockPos){
        int coordY = lastTunnelBlockPos.getY(); //obtener la coordenada Y del último escalón del túnel
        
        int coordXLeft = 0;
        int coordXRight = 0;
        
        int coordZLeft = 0;
        int coordZRight = 0;
        
        BlockState bricksBlock = Blocks.BRICKS.defaultBlockState();
        BlockState airBlock = Blocks.AIR.defaultBlockState();
        
        for (int i = 1; i < 7; i++) {
            switch (facing.toString()){
                case "south":
                        coordXLeft--;
                        coordXRight++;
                    break;
        
                case "west":
                        coordZLeft--;
                        coordZRight++;
                    break;
        
                case "north":
                        coordXLeft++;
                        coordXRight--;
                    break;
        
                case "east":
                        coordZLeft++;
                        coordZRight--;
                    break;
            }
            
            addBlock(world,bricksBlock,coordXLeft,coordY,coordZLeft); //adds floor on left
            addBlock(world,bricksBlock,coordXRight,coordY,coordZRight); //adds floor on right
            
            addBlock(world,bricksBlock,coordXLeft,coordY+4,coordZLeft); //adds roof on left
            addBlock(world,bricksBlock,coordXRight,coordY+4,coordZRight); //adds roof on right
    
            for (int j = 0; j < 4; j++) {
                addBlock(world,airBlock,coordXRight,coordY+j,coordZRight); //rellena área on AIRE on right
                addBlock(world,airBlock,coordXLeft,coordY+j,coordZLeft); //rellena área on AIRE on left
            }
        }
    }
    
    
    //    }
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
