package es.mariaanasanz.ut7.mods.impl;

import es.mariaanasanz.ut7.mods.base.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** [ENUNCIADO]
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
public class Tuneleitor extends DamMod implements IInteractEvent {
    
    public Tuneleitor() {
        super();
    }
    
    @Override
    public String autor() {
        return "Cristina López Lusarreta";
    }
    
    /**
     * Con este método vamos a tomar una serie de decisiones en base a lo
     * ocurrido durante un tipo de evento específico, en este caso,
     * el de usar una herramienta con la mano sobre un bloque (RightClickBlock).
     * Según lo que haya ocurrido en el evento, generaremos el túnel o no.
     *
     * @param event - el evento que queremos registrar (PlayerInteractEvent.RightClickBlock)
     */
    @Override
    @SubscribeEvent
    public void onPlayerTouch(PlayerInteractEvent.RightClickBlock event) { //registramos el evento de "usar" herramienta (con cualquier mano)
        //Bloque afectado por el evento: (targeted block)
        BlockPos pos = event.getPos();  //.getY(), .getX(), .getZ() --> posición del bloque afectado
        BlockState state = event.getLevel().getBlockState(pos); // estado del bloque afectado (qué bloque es)
        BlockPos targetedBlockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        Block targetedBlock = state.getBlock();
        
        //Otras variables:
        Item usedItem = event.getItemStack().getItem(); //herramienta empleada en el evento
        Block grassBlock = Blocks.GRASS_BLOCK; //bloque de hierva
        
        //Generar túnel si se cumplen las condiciones durante el evento:
        if (usedItem.toString().trim().endsWith("pickaxe")) { //si la herramienta empleada es un pico
            if (targetedBlock.equals(grassBlock) && targetedBlockPos.getY() > 5) { //si se ha usado un pico sobre un bloque de hierva y bloque está por encima de coord.Y = 5
                generarTunel(event);
            }
        }
        
    }
    
    /**
     * Este método será el que genere el túnel cuando el jugador
     * use un pico sobre un bloque de hierva.
     * Se tendrá en cuenta la dirección hacia dónde está mirando el jugador
     * para construir el túnel (pues depende de ella).
     * También, colocará una vagoneta frente al jugador para acelerar el trayecto
     * y reproducirá un efecto de sonido al crear el túnel.
     *
     * @param event - el evento que fue registrado al usar una herramienta (cumpliendo las condiciones)
     */
    public void generarTunel(PlayerInteractEvent.RightClickBlock event) {
        //Posición del bloque afectado: (targeted block during event)
        BlockPos pos = event.getPos();  //.getY(), .getX(), .getZ() --> del bloque afectado
        BlockPos targetedBlockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        
        //Bloques que componen el túnel:
        Block whiteStainedGlass = Blocks.WHITE_STAINED_GLASS;
        Block birchPlanks = Blocks.BIRCH_PLANKS;
        Block seaLantern = Blocks.SEA_LANTERN;
        Block airBlock = Blocks.AIR;
        Block railBlock = Blocks.RAIL; //POWERED_RAIL -> for more speed (if needed)
        
        //Obtener el mundo del jugador:
        Level world = event.getLevel();
        
        //Orientación y posición inicial del jugador:
        Player player = event.getEntity();
        Direction facing = player.getDirection();
//        BlockPos blockStandingOnPos = player.getOnPos(); //variable sólo necesaria para uno de las llamadas a métodos descartadas
        
        //ESCALERAS: (top/bottom)
        int coordYStairsBottom = targetedBlockPos.getY(); //coordenada Y de PELDAÑO escaleras
        int coordZStairsBottom = targetedBlockPos.getZ(); //coordenada Z de PELDAÑO escaleras
        int coordXStairsBottom = targetedBlockPos.getX(); //coordenada X de PELDAÑO escaleras
        
        int coordZBlockStairsTop = targetedBlockPos.getZ(); //coordenada Z de TECHO escaleras
        int coordXBlockStairsTop = targetedBlockPos.getX(); //coordenada X de TECHO escaleras
        
        //PARED: (left & right)
        int coordYWall = targetedBlockPos.getY(); //PARED - coordenada Y (empezamos a construir la pared al ras del escalón)
        int coordXWall = targetedBlockPos.getX(); //PARED - coordenada X
        int coordZWall = targetedBlockPos.getZ(); //PARED - coordenada Z
        
        //Colocar vagoneta:
        BlockPos minecartPos = targetedBlockPos.above();
        colocarVagoneta(world, minecartPos);
        
        //Reproducir un sonido al generar túnel:
        SoundEvent generarTunelSound = SoundEvents.UI_TOAST_CHALLENGE_COMPLETE; //aka, Minecraft Rare Achievement Sound Effect
        player.playSound(generarTunelSound);
        
        //Generar túnel:
        while (coordYStairsBottom >= 5) { //parar de generar el tunel en la coordenada Y = 5
//           ------------------------------------------------------------------------------------------------------------------------------------------------------------
            if (facing.toString().equals("south")) { //genera tunel hacia el SOUTH (where you're currently facing)
                
                addBlock(world, birchPlanks.defaultBlockState(), coordXStairsBottom, coordYStairsBottom, coordZStairsBottom); //BOTTOM
                addBlock(world, seaLantern.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 5, coordZStairsBottom); //TOP
                
                addBlock(world, railBlock.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 1, coordZStairsBottom); //LOWER MIDDLE
                for (int i = 2; i <= 4; i++) {
                    addBlock(world, airBlock.defaultBlockState(), coordXStairsBottom, coordYWall + i, coordZWall); //HIGHER MIDDLE
                }
    
                coordZStairsBottom++;
                coordZBlockStairsTop++;
                
                for (int j = 1; j <= 5; j++) { //quiero que ponga 5 bloques más porque empiezo desde la esquina inferior exterior, al ras de los escalones
                    addBlock(world, whiteStainedGlass.defaultBlockState(), coordXWall - 1, coordYWall, coordZWall); //RIGHT WALL
                    addBlock(world, whiteStainedGlass.defaultBlockState(), coordXWall + 1, coordYWall, coordZWall); //LEFT WALL
    
                    coordYWall++;
                }
                coordYWall -= 5; //reinicio la Y para volver a la altura del primer bloque de la pared
    
                coordZWall++;
//           ------------------------------------------------------------------------------------------------------------------------------------------------------------
            } else if (facing.toString().equals("west")) { //genera túnel hacia el WEST
                addBlock(world, birchPlanks.defaultBlockState(), coordXStairsBottom, coordYStairsBottom, coordZStairsBottom); //BOTTOM
                addBlock(world, seaLantern.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 5, coordZStairsBottom); //TOP
    
                addBlock(world, railBlock.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 1, coordZStairsBottom); //LOWER MIDDLE
                for (int i = 2; i <= 4; i++) {
                    addBlock(world, airBlock.defaultBlockState(), coordXStairsBottom, coordYWall + i, coordZWall); //HIGHER MIDDLE
                }
    
                coordXStairsBottom--;
                coordXBlockStairsTop--;
    
                for (int j = 0; j <= 4; j++) { //quiero que ponga 5 bloques más porque empiezo desde la esquina inferior exterior, al ras de los escalones
                    addBlock(world, whiteStainedGlass.defaultBlockState(), coordXWall, coordYWall, coordZWall - 1); //RIGHT WALL
                    addBlock(world, whiteStainedGlass.defaultBlockState(), coordXWall, coordYWall, coordZWall + 1); //LEFT WALL
        
                    coordYWall++;
                }
                coordYWall -= 5; //reinicio la Y para volver a la altura del primer bloque de la pared
                
                coordXWall--;
//           ------------------------------------------------------------------------------------------------------------------------------------------------------------
            } else if (facing.toString().equals("north")) { //genera túnel hacia el NORTH
                addBlock(world, birchPlanks.defaultBlockState(), coordXStairsBottom, coordYStairsBottom, coordZStairsBottom); //BOTTOM
                addBlock(world, seaLantern.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 5, coordZStairsBottom); //TOP
    
                addBlock(world, railBlock.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 1, coordZStairsBottom); //LOWER MIDDLE
                for (int i = 2; i <= 4; i++) {
                    addBlock(world, airBlock.defaultBlockState(), coordXStairsBottom, coordYWall + i, coordZWall); //HIGHER MIDDLE
                }
                
                coordZStairsBottom--;
                coordZBlockStairsTop--;
    
                for (int j = 0; j <= 4; j++) { //quiero que ponga 5 bloques más porque empiezo desde la esquina inferior exterior, al ras de los escalones
                    addBlock(world, whiteStainedGlass.defaultBlockState(), coordXWall + 1, coordYWall, coordZWall); //RIGHT WALL
                    addBlock(world, whiteStainedGlass.defaultBlockState(), coordXWall - 1, coordYWall, coordZWall); //LEFT WALL
        
                    coordYWall++;
                }
                coordYWall -= 5; //reinicio la Y para volver a la altura del primer bloque de la pared
    
                coordZWall--;
//           ------------------------------------------------------------------------------------------------------------------------------------------------------------
            } else if (facing.toString().equals("east")) { //genera túnel hacia el EAST
                addBlock(world, birchPlanks.defaultBlockState(), coordXStairsBottom, coordYStairsBottom, coordZStairsBottom); //BOTTOM
                addBlock(world, seaLantern.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 5, coordZStairsBottom); //TOP
                
                addBlock(world, railBlock.defaultBlockState(), coordXStairsBottom, coordYStairsBottom + 1, coordZStairsBottom); //LOWER MIDDLE
                for (int i = 2; i <= 4; i++) {
                    addBlock(world, airBlock.defaultBlockState(), coordXStairsBottom, coordYWall + i, coordZWall); //HIGHER MIDDLE
                }
    
                coordXStairsBottom++;
                coordXBlockStairsTop++;
    
                for (int j = 0; j <= 4; j++) { //quiero que ponga 5 bloques más porque empiezo desde la esquina inferior exterior, al ras de los escalones
                    addBlock(world, whiteStainedGlass.defaultBlockState(), coordXWall, coordYWall, coordZWall + 1); //RIGHT WALL
                    addBlock(world, whiteStainedGlass.defaultBlockState(), coordXWall, coordYWall, coordZWall - 1); //LEFT WALL
        
                    coordYWall++;
                }
                coordYWall -= 5; //reinicio la Y para volver a la altura del primer bloque de la pared
                
                coordXWall++;
            }
//           ------------------------------------------------------------------------------------------------------------------------------------------------------------
            
            coordYStairsBottom--; //las escaleras siempre bajan 1 en altura, tanto en el suelo como en el techo
            coordYWall--; //las paredes tambien van bajando 1 en altura siempre, a la par que las escaleras
        }
        
        //Funcionalidades extra del mod: (ambas DESCARTADAS)
//         generarEntradaTunel(facing, world, blockStandingOnPos);

//         BlockPos lastTunnelBlockPos = new BlockPos(coordXStairsBottom, coordYStairsBottom, coordZStairsBottom);
//         generarFinTrayecto(facing, world, lastTunnelBlockPos);
    
    }
    
    /**
     * Este método te añadirá un bloque más en las 4 partes del tunel:
     * escalones, techo, pared derecha y pared izquierda.
     * Debes llamar a este método hasta terminar de cubrir toda la longitud del túnel.
     * NOTA: Este método también se podrá usar para generar cualquier bloque aunque no sea parte del túnel
     *
     * @param world    - obtiene el mundo en el que el jugador está
     * @param newBlock - el bloque al que quieres transformar el selectedBlock
     * @param coordX   - coordenada x del bloque que quieres transformar
     * @param coordY   - coordenada y del bloque que quieres transformar
     * @param coordZ   - coordenada z del bloque que quieres transformar
     */
    public void addBlock(Level world, BlockState newBlock, int coordX, int coordY, int coordZ) {
        BlockPos newPos = new BlockPos(coordX, coordY, coordZ);
        world.setBlock(newPos, newBlock, 5010); //modifies block in client
    }
    
    /**
     * Este método creará una vagoneta sobre los railes del primer escalón del túnel
     * para que el jugador se suba a ella y navegue el túnel mucho más rápido.
     *
     * @param world - obtiene el mundo en el que el jugador está
     * @param minecartPos - posición en la que deberá colocarse la vagoneta
     */
    public void colocarVagoneta(Level world, BlockPos minecartPos) {
        //Colocar vagoneta:
        Minecart minecart = EntityType.MINECART.create(world); //crear una vagoneta en el mundo
        minecart.setPos(minecartPos.getX(), minecartPos.getY(), minecartPos.getZ()); //asignar una posición a la vagoneta
        world.addFreshEntity(minecart); //colocar vagoneta creada en el mundo (en la posición que le fue asignada)
        
        //Reproducir sonido al colocar vagoneta:
        SoundEvent minecartPlacedSound = SoundEvents.UI_LOOM_SELECT_PATTERN;
        minecart.playSound(minecartPlacedSound);
    }
    
    /** [DESCARTADO]
     * Este método crea una entrada bonita para el tunel,
     * sólo abarcando la parte del túnel que sale a la superficie
     * donde el jugador está
     *
     * @param facing           - dirección hacia donde mira el jugador
     * @param world            - el mundo en el que el jugador está
     * @param targetedBlockPos - la posición del bloque al que apunta el jugador (aka, primer escalón del túnel)
     */
    public void generarEntradaTunel(Direction facing, Level world, BlockPos targetedBlockPos) {
        Block acaciaPlanksBlock = Blocks.ACACIA_PLANKS;
    
        int coordY = targetedBlockPos.getY();
    
        int coordXLeft = 0;
        int coordXRight = 0;
    
        int coordZLeft = 0;
        int coordZRight = 0;
        
        System.out.println("GENERAR ENTRADA TÚNEL");
        
        for (int i = 0; i < 4; i++) {
            switch (facing.toString()) {
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
                addBlock(world, acaciaPlanksBlock.defaultBlockState(), coordXRight, coordY, coordZRight);
                addBlock(world, acaciaPlanksBlock.defaultBlockState(), coordXLeft, coordY, coordZLeft);
                
                coordY++;
            }
            coordY -= 5;
            
        }
    }
    
    /** [DESCARTADO]
     * Este método creará una área segura al final del tunel
     * para prevenir que el jugador no corra peligro haya lo
     * que haya al final del tunel en el nivel Y=5
     *
     * @param facing             - dirección hacia donde mira el jugador
     * @param world              - el mundo en el que el jugador está
     * @param lastTunnelBlockPos - posicion del último bloque del tunel (aka, último escalón)
     */
    public void generarFinTrayecto(Direction facing, Level world, BlockPos lastTunnelBlockPos) {
        int coordY = lastTunnelBlockPos.getY(); //obtener la coordenada Y del último escalón del túnel
        
        int coordXLeft = 0;
        int coordXRight = 0;
        
        int coordZLeft = 0;
        int coordZRight = 0;
        
        BlockState bricksBlock = Blocks.BRICKS.defaultBlockState();
        BlockState airBlock = Blocks.AIR.defaultBlockState();
        
        for (int i = 1; i < 7; i++) {
            switch (facing.toString()) {
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
            
            addBlock(world, bricksBlock, coordXLeft, coordY, coordZLeft); //adds floor on left
            addBlock(world, bricksBlock, coordXRight, coordY, coordZRight); //adds floor on right
            
            addBlock(world, bricksBlock, coordXLeft, coordY + 4, coordZLeft); //adds roof on left
            addBlock(world, bricksBlock, coordXRight, coordY + 4, coordZRight); //adds roof on right
    
            for (int j = 0; j < 4; j++) {
                addBlock(world, airBlock, coordXRight, coordY + j, coordZRight); //rellena área on AIRE on right
                addBlock(world, airBlock, coordXLeft, coordY + j, coordZLeft); //rellena área on AIRE on left
            }
        }
    }

}
