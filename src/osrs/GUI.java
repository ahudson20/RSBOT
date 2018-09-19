package osrs;

import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import osrs.tasks.Bank;
import osrs.tasks.Drop;
import osrs.tasks.Mine;
import osrs.tasks.Walk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class GUI extends ClientAccessor {

    public static List<Task> taskList = new ArrayList<Task>();


    public GUI(ClientContext ctx) {
        super(ctx);
        intitComponents();
    }

    public JFrame frame = new JFrame("QuickMiner");

    private void intitComponents() {
        //frame
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        //panel to add to frame
        JPanel content = new JPanel();
        frame.add(content);

        //Radio Button powermine/bank
        JRadioButton radio = new JRadioButton(MineConstants.mineOptions[1]);
        //Radio button powermine keep/drop gems
        JRadioButton gems = new JRadioButton(MineConstants.keepGems);
        //rock dropdown menu
        JComboBox comboBox1 = new JComboBox(MineConstants.rockOptions);
        //location dropdown menu
        JComboBox comboBox2 = new JComboBox(MineConstants.locationOptions);
        //Start button
        JButton start = new JButton(MineConstants.startLabel);
        //Warning Label
        JLabel warning = new JLabel(MineConstants.warningLabel);
        //Warning Label 2
        JLabel warning2 = new JLabel(MineConstants.warningLabel2);

        /**TODO: could add optionality to drop gems while mining?*/
        gems.setEnabled(false);//set false initially, cos bank will just keep anyways.

        //radio button listener
        radio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //if clicked off i.e. unclicked
                //state change == deselected
                //therefor statement is true
                //and combobox is shown
                comboBox2.setEnabled(e.getStateChange() == ItemEvent.DESELECTED);
                gems.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        });


        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rock = (String) comboBox1.getSelectedItem();
                String lock = (String) comboBox2.getSelectedItem();
                if(radio.isSelected()){
                    //taskList.add(new Drop(ctx, true));
                    if(gems.isSelected()){
                        taskList.add(new Drop(ctx, true));
                    }else{
                        taskList.add(new Drop(ctx, false));
                    }
                }else if(!radio.isSelected()){
                    taskList.add(new Bank(ctx));
                    if(lock.equals("Lumbridge Swamp")){
                        taskList.add(new Walk(ctx, MineConstants.LUMBRIDGE_SWAMP));
                    }
                    if(lock.equals("Varrock West")){
                        if(rock.equals("Tin") || rock.equals("Clay") || rock.equals("Copper")){
                            taskList.add(new Walk(ctx, MineConstants.VARROCK_WEST_TIN));
                        }else{
                            taskList.add(new Walk(ctx, MineConstants.VARROCK_WEST_IRON));
                        }
                    }
                    if(lock.equals("Varrock East")){
                        taskList.add(new Walk(ctx, MineConstants.VARROCK_EAST_MINE));
                    }
                }else{
                    ctx.controller.stop();
                }

                if(rock.equals("Tin")){
                    taskList.add(new Mine(ctx, MineConstants.TIN_IDS));
                }else if(rock.equals("Copper")){
                    taskList.add(new Mine(ctx, MineConstants.COPPER_IDS));
                }else if(rock.equals("Iron")){
                    taskList.add(new Mine(ctx, MineConstants.IRON_IDS));
                }else if(rock.equals("Clay")){
                    taskList.add(new Mine(ctx, MineConstants.CLAY_IDS));
                }else{
                    ctx.controller.stop();
                }
                frame.dispose();
            }
        });

        content.setLayout(null);

        radio.setBounds(new Rectangle(new Point(150, 35), comboBox1.getPreferredSize()));
        radio.setSize(new Dimension(200, 30));

        comboBox1.setBounds(new Rectangle(new Point(100, 75), comboBox1.getPreferredSize()));
        comboBox1.setSize(new Dimension(200, 20));

        comboBox2.setBounds(new Rectangle(new Point(100, 115), comboBox2.getPreferredSize()));
        comboBox2.setSize(new Dimension(200 ,20));

        gems.setBounds(new Rectangle(new Point(150, 155), gems.getPreferredSize()));
        gems.setSize(new Dimension(200, 30));

        start.setBounds(new Rectangle(new Point(100, 195), start.getPreferredSize()));
        start.setSize(new Dimension(200, 20));


        /*start.setBounds(new Rectangle(new Point(220, 14), start
                .getPreferredSize()));
        start.setSize(new Dimension(57, 22));*/

        //content.setLayout(new FlowLayout());
        content.setBackground(Color.ORANGE);
        content.add(radio);
        content.add(comboBox1);
        content.add(comboBox2);
        content.add(gems);
        content.add(start);
        content.add(warning);
        content.add(warning2);



    }
}
