package osrs.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Objects;
import osrs.MineConstants;
import osrs.Task;

import java.util.concurrent.Callable;

public class Mine extends Task {

    public final int ROCK_IDS[];

    Tile rockLocation = Tile.NIL;

    public Mine(ClientContext ctx, int[] ROCK_IDS) {
        super(ctx);
        this.ROCK_IDS = ROCK_IDS;
    }

    @Override
    public boolean activate() {
        return ctx.objects.select().at(rockLocation).id(ROCK_IDS).poll().equals(ctx.objects.nil()) || ctx.players.local().animation() == -1;
        //drop has priority on task list therefore invent wont ever get full
        //if animation is -1 i.e. not doing anything
        //or rock is at rock location is not there(nil)
        //then execute(mine)
    }

    @Override
    public void execute() {
        int start = ctx.inventory.id(ROCK_IDS).count();
        GameObject rockToMine = ctx.objects.select().id(ROCK_IDS).nearest().poll();//find nearest rock .nearest().poll();

        rockLocation = rockToMine.tile();//set rock location

        rockToMine.interact("Mine");//mine

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() != -1;
            }
        }, 200, 10);
        //Condition.wait(() -> ctx.players.local().animation() != -1, 200, 10);//wait 2 secs before trying again

    }
}
