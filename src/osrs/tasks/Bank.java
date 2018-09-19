package osrs.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import osrs.Task;

import java.util.concurrent.Callable;

public class Bank extends Task {

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() > 27 && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 6;
        //if full
        //and close to bank
        // then bank
    }

    @Override
    public void execute() {
        /**If te bank is already open,
         * make note of inventory count,
         * deposit inventory,
         * wait 5secs before trying again if inventory count hasnt changed.
         */
        if(ctx.bank.opened()){
            final int inititalCount = ctx.inventory.select().count();
            if(ctx.bank.depositInventory()){
                if(ctx.bank.open()){
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().count() != inititalCount;
                        }
                    }, 250, 20);
                }
            }
        }else{
            /**
             * if in view,
             * open bank,
             * wait 5secs until retrying.
             */
            if(ctx.bank.inViewport()) {
                if(ctx.bank.open()){
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.bank.opened();
                        }
                    }, 250, 20);
                }
            }else{
                /**
                 * turn camera to nearest bank,
                 * open bank,
                 * wait 5secs before trying again.
                 */
                ctx.camera.turnTo(ctx.bank.nearest());
                if(ctx.bank.open()){
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.bank.opened();
                        }
                    }, 250, 20);
                }
            }
        }
    }
}
