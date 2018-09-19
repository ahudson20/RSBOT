package osrs;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
//import osrs.tasks.Mine;
import org.powerbot.script.rt4.Constants;
import osrs.tasks.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(name="QuickMining", description="tutorial", properties="client=4; author=Anaru; topic=999;")
public class QuickMining extends PollingScript<ClientContext> implements PaintListener {
    //List<Task> taskList = new ArrayList<>();
    int startExp = 0;

    @Override
    public void start(){

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new GUI(ctx).frame.setVisible(true);
                //new GUI(ctx).frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }

        });

       ///////////////////////
        /*String userMineChoice = ""+(String) JOptionPane.showInputDialog(null, "Bank or Powermine", "QuickMiner", JOptionPane.PLAIN_MESSAGE, null, mineOptions, mineOptions[1]);
        String userRockChoice = ""+(String) JOptionPane.showInputDialog(null, "Choose Rock", "QuickMiner", JOptionPane.PLAIN_MESSAGE, null, rockOptions, rockOptions[0]);

        if(userMineChoice.equals("Powermine")){//Can only powermine if already at location i.e. wont walk to location.
            taskList.add(new Drop(ctx));
        }else if(userMineChoice.equals("Bank")){
            taskList.add(new Bank(ctx));
            if(userRockChoice.equals("Copper")){
                String userLocationChoice = ""+(String) JOptionPane.showInputDialog(null, "Choose Location", "QuickMiner", JOptionPane.PLAIN_MESSAGE, null, Arrays.copyOfRange(locationOptions, 0, 1), locationOptions[0]);
                if(userLocationChoice.equals("Lumbridge Swamp")){
                    taskList.add(new Walk(ctx, MineConstants.LUMBRIDGE_SWAMP));
                }else{
                    ctx.controller.stop();
                }
            }else if(userRockChoice.equals("Tin")){
                String userLocationChoice = ""+(String) JOptionPane.showInputDialog(null, "Choose Location", "QuickMiner", JOptionPane.PLAIN_MESSAGE, null, locationOptions, locationOptions[0]);
                if(userLocationChoice.equals("Lumbridge Swamp")){
                    taskList.add(new Walk(ctx, MineConstants.LUMBRIDGE_SWAMP));
                }else if(userLocationChoice.equals("Varrock West")){
                    taskList.add(new Walk(ctx, MineConstants.VARROCK_WEST_TIN));
                }else{
                    ctx.controller.stop();
                }
            }else if(userRockChoice.equals("Iron")){
                String userLocationChoice = ""+(String) JOptionPane.showInputDialog(null, "Choose Location", "QuickMiner", JOptionPane.PLAIN_MESSAGE, null, Arrays.copyOfRange(locationOptions, 1, 2), locationOptions[1]);
                if(userLocationChoice.equals("Varrock West")){
                    taskList.add(new Walk(ctx, MineConstants.VARROCK_WEST_IRON));
                }else{
                    ctx.controller.stop();
                }
            }else if(userRockChoice.equals("Clay")){
                String userLocationChoice = ""+(String) JOptionPane.showInputDialog(null, "Choose Location", "QuickMiner", JOptionPane.PLAIN_MESSAGE, null, Arrays.copyOfRange(locationOptions, 1, 2), locationOptions[1]);
                if(userLocationChoice.equals("Varrock West")){
                    taskList.add(new Walk(ctx, MineConstants.VARROCK_WEST_CLAY));
                }else{
                    ctx.controller.stop();
                }
            }
        }else{
            //JOptionPane.showMessageDialog(null, "Please move to " + userLocationChoice);
            ctx.controller.stop();
        }

        /**
         * Rock Choice.
         */

        ////////////////
        /*if(userRockChoice.equals("Tin")){
            taskList.add(new Mine(ctx, MineConstants.TIN_IDS));
        }else if(userRockChoice.equals("Copper")){
            taskList.add(new Mine(ctx, MineConstants.COPPER_IDS));
        }else if(userRockChoice.equals("Iron")){
            taskList.add(new Mine(ctx, MineConstants.IRON_IDS));
        }else if(userRockChoice.equals("Clay")){
            taskList.add(new Mine(ctx, MineConstants.CLAY_IDS));
        }else{
            ctx.controller.stop();
        }*/
    //////////////////////////////

        /*if(userMineChoice.equals("Powermine")){
            taskList.add(new Drop(ctx));
        }else if(userMineChoice.equals("Bank")){
            taskList.add(new Bank(ctx));
            if(userLocationChoice.equals("Lumbridge Swamp")){
                taskList.add(new Walk(ctx, MineConstants.LUMBRIDGE_SWAMP));
            }else{
                taskList.add(new Walk(ctx, MineConstants.VARROCK_WEST_TIN));
            }
        }else{
            //JOptionPane.showMessageDialog(null, "Please move to " + userLocationChoice);
            ctx.controller.stop();
        }

        /**
         * Rock Choice.
         */
        /*if(userRockChoice.equals("Tin")){
            taskList.add(new Mine(ctx, MineConstants.TIN_IDS));
        }else if(userRockChoice.equals("Copper")){
            taskList.add(new Mine(ctx, MineConstants.COPPER_IDS));
        }else{
            ctx.controller.stop();
        }*/



        startExp = ctx.skills.experience(Constants.SKILLS_MINING);
    }

    @Override
    public void poll() {
        /*for(Task task: taskList){
            if(ctx.controller.isStopping()){
                break;
            }
            if(task.activate()){
                task.execute();
                break;
            }
        }*/
        for(Task task: GUI.taskList){
            if(ctx.controller.isStopping()){
                break;
            }
            if(task.activate()){
                task.execute();
                break;
            }
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds/1000) % 60;//minutes into seconds.
        long minutes = (milliseconds/(1000*60) % 60);
        long hours = (milliseconds/(1000*60*60)) % 24;

        int expGained = ctx.skills.experience(Constants.SKILLS_MINING) - startExp;

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, 150, 100);//solid black box, slightly transparent

        g.setColor(Color.ORANGE);
        g.drawRect(0, 0, 150, 100);//white border on box

        g.drawString("QuickMiner", 20, 20);

        g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 20, 40);

        g.drawString("Exp Gained: " + expGained, 20, 60);
        g.drawString("Exp/Hour: " + ((int)(expGained * (3600000D / milliseconds))), 20, 80);
        //g.drawString("Ores: " + MineConstants.ORES_MINED, 20, 100);

    }
}
