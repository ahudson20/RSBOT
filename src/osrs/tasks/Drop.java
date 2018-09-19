package osrs.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Item;
import osrs.MineConstants;
import osrs.Task;

import java.awt.*;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import org.powerbot.script.Random;


public class Drop extends Task {

    private boolean keepGems;

    public Drop(ClientContext ctx, boolean keepGems) {
        super(ctx);
        this.keepGems = keepGems;
    }

    public boolean getKeepGems(){
        return this.keepGems;
    }

    public boolean shifty(){
        return (ctx.varpbits.varpbit(1055) & 131072) > 0;
    }// Check if shift-drop is enabled

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() > 27;
    }

    @Override
    public void execute() {
        if (getKeepGems() == true) {
            dropping("(.*ore)|(Clay)|(Coal)");// dont drop gems
        }else{
            dropping("(.*ore)|(Clay)|(Coal)|(Uncut.*)");// drop all gems
        }
    }

    public void dropping(String string){
        for (Item item : ctx.inventory.select().name(Pattern.compile(string))) {// drop all items with this name
            if (ctx.controller.isStopping() || ctx.controller.isSuspended()) {//if user has clicked stop/pause then don't finish loop, just break out.
                break;
            }

            if (!ctx.game.tab(Game.Tab.INVENTORY)) {//if not on inventory tab, click into inventory
                ctx.game.tab(Game.Tab.INVENTORY);
            }

            final int startAmount = ctx.inventory.select().count();

            Point point = item.centerPoint();//center point on item.
            Random rand = new Random();

            if (shifty()) {
                ctx.input.send("{VK_SHIFT down}");//click shift
                ctx.input.click(point.x + rand.nextInt(-10, 10), point.y + rand.nextInt(-10, 10), true);//click item, with randomised x and y co-ords
            } else {
                item.interact("Drop");// if shift-drop is not enabled, just right click drop
            }

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count() != startAmount;
                }
            }, 25, 20);//randomly generate the wait times between dropping
        }

        if (shifty()) {
            ctx.input.send("{VK_SHIFT up}");//release shift.
        }
    }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////
//if ((ctx.varpbits.varpbit(1055) & 131072) > 0) { //shift drop enabled
//ctx.input.send("{VK_SHIFT down}");//click shift
//ctx.input.click(point.x + rand.nextInt((10-10)+1)-10, point.y + rand.nextInt((10-10)+1)-10, true);
//ctx.input.send("{VK_SHIFT up}");//release shift
//}

//if(shifty()){
// ctx.input.send("{VK_SHIFT down}");//click shift
//}
/////////////////////////////////////////////////////////////////////////////////////////////////////
