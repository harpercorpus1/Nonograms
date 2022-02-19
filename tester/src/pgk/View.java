package src.pgk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.GapContent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class View extends JFrame implements MouseListener, ActionListener {
    private static int BOARD_SIZE;

    private JPanel mainPanel;
    private JPanel firstPanel;
    private JPanel playerBoard;
    private JPanel solveButtonPanel;

    private List<JButton> board_buttons;
    private List<JButton> solver_buttons;

    private List<JTextPane> top_panel;
    private List<JTextPane> side_panel;

    boolean currently_compressed;

    public View(int size){
        super("View");

        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
          }catch(Exception e){
           e.printStackTrace(); 
        }

        BOARD_SIZE = size;
        currently_compressed = false;

        board_buttons = new ArrayList<JButton>();
        solver_buttons = new ArrayList<JButton>();
        top_panel = new ArrayList<JTextPane>();
        side_panel = new ArrayList<JTextPane>();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        init_title_panel();
        init_nonogram_board();
        init_solver_panel();

        mainPanel.add(firstPanel);
        mainPanel.add(playerBoard);
        mainPanel.add(solveButtonPanel);

        this.setContentPane(mainPanel);
        mainPanel.setBackground(Color.BLACK);

        this.setSize(600,700);
        this.setMinimumSize(new Dimension(700,900));
        this.setVisible(true);
    }

    public void init_title_panel(){
        firstPanel = new JPanel();
        firstPanel.setMaximumSize(new Dimension(600,100));

        JLabel title = new JLabel("Solver");
        title.setFont(new Font("Serif", Font.BOLD, 60));
        title.setForeground((Color.PINK));
        title.setBorder(new EmptyBorder(10,0,0,0));
        firstPanel.setBackground(Color.BLACK);
        firstPanel.add(title);
    }

    public GridBagConstraints generate_constraints(int x, int y, int width, int height){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        return constraints;
    }

    public JButton button_factory(Dimension dim){
        JButton btn = new JButton();
        btn.setMinimumSize(dim);
        btn.setPreferredSize(dim);
        btn.setMaximumSize(dim);
        return btn;
    }

    public JTextPane textpane_factory(Dimension dim){
        JTextPane text_pane = new JTextPane();
        text_pane.setMinimumSize(dim);
        text_pane.setPreferredSize(dim);
        text_pane.setMaximumSize(dim);
        return text_pane;
    }

    public void init_nonogram_board(){
        playerBoard = new JPanel();
        playerBoard.setLayout(new GridBagLayout());
        playerBoard.setMaximumSize(new Dimension(700,600));
        playerBoard.setBackground(Color.BLACK);

        JButton btn;
        JTextPane tp;
        GridBagConstraints constraints;

        int dim1 = 40;
        int dim2 = 60;

        Dimension gamebtn_dim = new Dimension(dim1, dim1);
        Dimension nullbtn_dim = new Dimension(dim2, dim2);

        Dimension top_pane_dim = new Dimension(dim1, dim2);
        Dimension side_pane_dim = new Dimension(dim2, dim1);
        
        // empty button
        btn = button_factory(nullbtn_dim);
        constraints = generate_constraints(0, 0, 3, 3);
        btn.setOpaque(true);
        playerBoard.add(btn, constraints);

        for(int i = 0; i < 15; i++){
            tp = textpane_factory(top_pane_dim);
            constraints = generate_constraints(i+3, 0, 1,3);
            tp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            playerBoard.add(tp, constraints);
            top_panel.add(tp);
        }

        for(int i = 0; i < 15; i++){
            tp = textpane_factory(side_pane_dim);
            constraints = generate_constraints(0, i+3, 3, 1);
            tp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            playerBoard.add(tp, constraints);
            side_panel.add(tp);

            for(int j = 0; j < 15; j++){
                btn = button_factory(gamebtn_dim);
                constraints = generate_constraints(j+3, i+3, 1, 1);
                playerBoard.add(btn, constraints);
                board_buttons.add(btn);
            }
        }
    }

    public void init_solver_panel(){
        solveButtonPanel = new JPanel();
        solveButtonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        solveButtonPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        solveButtonPanel.setMaximumSize(new Dimension(600,200));

        JButton btn;

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        btn = new JButton("Clear Board");

        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setOpaque(true);

        solveButtonPanel.add(btn, constraints);
        btn.addActionListener(this);
        solver_buttons.add(btn);
        
        constraints.gridx = 1;
        constraints.gridy = 0;
        btn = new JButton("Toggle I/O");
        btn.setBackground(Color.PINK);
        btn.setForeground(Color.BLACK);
        btn.setOpaque(true);

        solveButtonPanel.add(btn, constraints);
        btn.addActionListener(this);
        solver_buttons.add(btn);
 
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 2;
        btn = new JButton("Solve");

        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setOpaque(true);

        solveButtonPanel.add(btn, constraints);
        btn.addActionListener(this);
        solver_buttons.add(btn);

        solveButtonPanel.setBackground(Color.BLACK);
    }

    public void setColor(Color color, int row, int col){
        board_buttons.get(row * BOARD_SIZE + col).setBackground(color);
    }

    public void resetColor(Color color, int row, int col){
        board_buttons.get(row * BOARD_SIZE + col).setBackground(color);
    }

    public void toggle_button(char current_write_symbol){
        if(current_write_symbol == 'T'){
            solver_buttons.get(1).setForeground(Color.BLACK);
            solver_buttons.get(1).setBackground(Color.PINK);
        }else{
            solver_buttons.get(1).setForeground(Color.WHITE);
            solver_buttons.get(1).setBackground(Color.BLACK);
        }
    }

    public String[] read_top_panel(){
        String[] top_labels = new String[BOARD_SIZE];
        for(int i = 0; i < BOARD_SIZE; i++){
            top_labels[i] = top_panel.get(i).getText();
        }
        return top_labels;
    }

    public void clear_top_panel(){
        for(int i = 0; i < BOARD_SIZE; i++){
            top_panel.get(i).setText("");
        }
    }

    public void clear_side_panel(){
        for(int i = 0; i < BOARD_SIZE; i++){
            side_panel.get(i).setText("");
        }
    }

    public String[] read_side_panels(){
        String[] side_labels = new String[BOARD_SIZE];
        for(int i = 0; i < BOARD_SIZE; i++){
            side_labels[i] = side_panel.get(i).getText();
        }
        return side_labels;
    }

    public void disruptDrag(){
        currently_compressed = false;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        
    }

    @Override
    public void mousePressed(MouseEvent m){
        currently_compressed = true;
        Object source = m.getSource();
        if(board_buttons.contains(source)){
            int ind = board_buttons.indexOf(source);
            int row = ind / BOARD_SIZE;
            int col = ind % BOARD_SIZE;
        }
    }

    @Override
    public void mouseClicked(MouseEvent m){
        Object source = m.getSource();
        if(board_buttons.contains(source)){
            int ind = board_buttons.indexOf(source);
            int row = ind / BOARD_SIZE;
            int col = ind % BOARD_SIZE;
        }
    }

    @Override
    public void mouseEntered(MouseEvent m){
        if(!currently_compressed){
            return;
        }
        Object source = m.getSource();
        if(board_buttons.contains(source)){
            int ind = board_buttons.indexOf(source);
            int row = ind / BOARD_SIZE;
            int col = ind % BOARD_SIZE;
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
