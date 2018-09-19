package osrs.tasks;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import osrs.Task;
import osrs.Walker;

public class Walk extends Task {

    public final Tile[] pathToBank;
    private final Walker walker = new Walker(ctx);



    public Walk(ClientContext ctx, Tile pathToBank[]) {
        super(ctx);
        this.pathToBank = pathToBank;
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() > 27 || (ctx.inventory.select().count() < 28 && pathToBank[0].distanceTo(ctx.players.local()) > 6);
        //either invent full therefore walk to bank
        //or invent not full and not near rocks, walk to rocks
    }

    @Override
    public void execute() {
        // if not already running,
        //and energy > 45,
        //then run.
        Random rand = new Random();
        if(!ctx.movement.running() && ctx.movement.energyLevel() > rand.nextInt(35, 55)){
            ctx.movement.running(true);
        }

        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if(ctx.inventory.select().count() > 27){
                walker.walkPath(pathToBank);
            }else{
                walker.walkPathReverse(pathToBank);
            }
            //walker.walkPath(pathToBank);
        }
    }
}
