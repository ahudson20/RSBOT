package scripts;

//import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

@Script.Manifest(name="GoblinKiller", description = "Hows it going?", properties ="author=Anaru; topic=999; client=4;")
public class GoblinKiller extends PollingScript<ClientContext> {
    final static int mID[] = {2481};
    final static int fID = 347;

    @Override
    public void start(){}

    @Override
    public void stop(){}

    @Override
    public void poll() {
        //constant loop
        if(hasFood()){
            if(needsHeal()){
                heal();
            }else if(shouldAttack()){
                attack();
            }
        }
    }

    public boolean needsHeal(){
        return ctx.combat.health() < 20;
    }

    public boolean hasFood(){
        return ctx.inventory.select().id(fID).count() > 0;
    }

    public boolean shouldAttack(){
        return !ctx.players.local().inCombat();
    }

    public void attack(){
        final Npc mToAttack = ctx.npcs.select().id(mID).select(new Filter<Npc>(){
            @Override
            public boolean accept(Npc npc) {
                return !npc.inCombat();
            }
        }).nearest().poll();

        mToAttack.interact("Attack");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inCombat();
            }
        }, 200, 20);
    }


    public void heal(){
        //.select() clears previous result for item, and re-does result
        Item foodToEat = ctx.inventory.select().id(fID).poll();//gives you all food in invent., poll gives the top result
        foodToEat.interact(true, "Eat", "Herring");

        final int startingHealth = ctx.combat.health();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final int currentHealth = ctx.combat.health();
                return currentHealth != startingHealth;
            }
        }, 150, 20);

    }
}
