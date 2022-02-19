package nonogrampkg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class NonogramView extends JFrame implements MouseListener  {
//    private final List<JButton> board;
    private static int BOARD_SIZE;
    private static final int GAME_BUTTON_SIZE = 10;

    private int[] topLabel;
    private int[] sideLabel;
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel firstPanel;
    private JPanel playerBoard;
    private List<JButton> buttons;

    boolean currently_compressed;

    NonogramController controller;

    public NonogramView(int size){
        super("Nonograms");

        BOARD_SIZE = size;
        currently_compressed = false;

        buttons = new ArrayList<JButton>();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        firstPanel = new JPanel();
        firstPanel.setLayout(new GridLayout(4,4));
        firstPanel.setMaximumSize(new Dimension(400,400));

        JButton btn;
        for(int i = 1; i <= 4; i++){
            for(int j = 1; j <= 4; j++){
                btn = new JButton();
                btn.setPreferredSize(new Dimension(100,100));
                firstPanel.add(btn);
            }
        }

        init_nonogram_board();

        mainPanel.add(firstPanel);
        mainPanel.add(playerBoard);

        this.setContentPane(mainPanel);

        this.setSize(520,600);
        this.setMinimumSize(new Dimension(520,660));
        this.setVisible(true);
    }

    public void init_nonogram_board(){
        JButton btn;

        playerBoard = new JPanel();
        playerBoard.setLayout(new GridLayout(BOARD_SIZE+1,BOARD_SIZE+1));
        playerBoard.setMaximumSize(new Dimension(540,200));

        btn = new JButton();
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        playerBoard.add(btn);

        JLabel label;
        for(int i = 0; i < BOARD_SIZE; i++){
            label = new JLabel("top");
            playerBoard.add(label);
        }
        for(int i = 0; i < BOARD_SIZE; i++){
            label = new JLabel("end");
            playerBoard.add(label);
            label.setPreferredSize(new Dimension(GAME_BUTTON_SIZE,GAME_BUTTON_SIZE));
            for(int j = 0; j < BOARD_SIZE; j++) {
                btn = new JButton();
                buttons.add(btn);
                btn.setPreferredSize(new Dimension(GAME_BUTTON_SIZE, GAME_BUTTON_SIZE));
                btn.setOpaque(true);
                // btn.addActionListener(this);

                btn.addMouseListener(this);
                playerBoard.add(btn);
            }
        }
    }

    public void registerController(NonogramController ctr){
        this.controller = ctr;
    }

    public void setColor(Color color, int row, int col){
        buttons.get(row * BOARD_SIZE + col).setBackground(color);
        buttons.get(row * BOARD_SIZE + col).setBorderPainted(false);
    }

    public void disruptDrag(){
        currently_compressed = false;
    }

    public void showIncorrect(){
        final JOptionPane pane = new JOptionPane("Try Again");
        final JDialog d = pane.createDialog((JFrame)null, "");
        d.setLocation(100,100);
        d.setVisible(true);
    }

    // @Override
    // public void actionPerformed(ActionEvent e){
    //     Object source = e.getSource();
    //     System.out.println("In Action Listener");
    //     if (buttons.contains(source)) {
    //         int index = buttons.indexOf(source);
    //         int row = index / BOARD_SIZE;
    //         int col = index % BOARD_SIZE;
    //         controller.processButtonClick(row, col);
    //     }
    // }

    @Override
    public void mousePressed(MouseEvent m){
        currently_compressed = true;
        Object source = m.getSource();
        if(buttons.contains(source)){
            int ind = buttons.indexOf(source);
            int row = ind / BOARD_SIZE;
            int col = ind % BOARD_SIZE;
            controller.processButtonClick(row, col);   
        }
    }

    @Override
    public void mouseClicked(MouseEvent m){
        Object source = m.getSource();
        if(buttons.contains(source)){
            int ind = buttons.indexOf(source);
            int row = ind / BOARD_SIZE;
            int col = ind % BOARD_SIZE;
            controller.processButtonClick(row, col); 
        }
    }

    @Override
    public void mouseEntered(MouseEvent m){
        if(!currently_compressed){
            return;
        }
        Object source = m.getSource();
        if(buttons.contains(source)){
            int ind = buttons.indexOf(source);
            int row = ind / BOARD_SIZE;
            int col = ind % BOARD_SIZE;
            controller.processButtonClick(row, col);
        }
    }

    @Override
    public void mouseExited(MouseEvent m){
        //
    }

    @Override
    public void mouseReleased(MouseEvent m){
        currently_compressed = false;
    }
    
}