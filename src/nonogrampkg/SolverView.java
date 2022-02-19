package nonogrampkg;

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

public class SolverView extends JFrame implements MouseListener, ActionListener {
//    private final List<JButton> board;
    private static int BOARD_SIZE;
    private static final int GAME_BUTTON_SIZE = 20;
    private Dimension BUTTON_DIM; 

    // private int[] topLabel;
    // private int[] sideLabel;
    // private JFrame frame;

    private JPanel mainPanel;
    private JPanel firstPanel;
    private JPanel playerBoard;
    private JPanel solveButtonPanel;

    private List<JButton> board_buttons;
    private List<JButton> solver_buttons;

    private List<JTextPane> top_panel;
    private List<JTextPane> side_panel;

    boolean currently_compressed;

    SolverController controller;

    public SolverView(int size){
        super("SolverView");

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
        this.setMinimumSize(new Dimension(600,700));
        this.setVisible(true);
    }

    public void init_title_panel(){
        firstPanel = new JPanel();
        firstPanel.setMaximumSize(new Dimension(600,100));

        JLabel title = new JLabel("Solver");
        title.setFont(new Font("Serif", Font.BOLD, 60));
        title.setForeground((Color.PINK));
        title.setBorder(new EmptyBorder(15,0,0,0));
        firstPanel.setBackground(Color.BLACK);
        firstPanel.add(title);
    }

    public void init_nonogram_board(){
        BUTTON_DIM = new Dimension(GAME_BUTTON_SIZE, GAME_BUTTON_SIZE);
        JButton btn;

        playerBoard = new JPanel();
        playerBoard.setLayout(new GridLayout(BOARD_SIZE+1,BOARD_SIZE+1));
        playerBoard.setMaximumSize(new Dimension(500,500));

        btn = new JButton();
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        playerBoard.add(btn);

        JTextPane tp;
        AbstractDocument abdoc;
        for(int i = 0; i < BOARD_SIZE; i++){
            tp = new JTextPane();
            StyledDocument doc = tp.getStyledDocument();
            if(doc instanceof AbstractDocument){
                abdoc = (AbstractDocument)doc;
                abdoc.setDocumentFilter(new DocumentSizeFilter(2));
            }
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            Font f = new Font(Font.SANS_SERIF, 3, 20);
            tp.setFont(f);
            tp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            playerBoard.add(tp);
            top_panel.add(tp);
        }
        
        for(int i = 0; i < BOARD_SIZE; i++){

            tp = new JTextPane();
            StyledDocument doc = tp.getStyledDocument();
            if(doc instanceof AbstractDocument){
                abdoc = (AbstractDocument)doc;
                abdoc.setDocumentFilter(new DocumentSizeFilter(2));
            }
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            Font f = new Font(Font.SANS_SERIF, 3, 20);
            tp.setFont(f);
            tp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            playerBoard.add(tp);
            side_panel.add(tp);

            for(int j = 0; j < BOARD_SIZE; j++) {
                btn = new JButton();
                btn.setBackground(Color.LIGHT_GRAY);
                board_buttons.add(btn);

                btn.setMinimumSize(BUTTON_DIM);
                btn.setPreferredSize(BUTTON_DIM);
                btn.setMaximumSize(BUTTON_DIM);

                btn.setOpaque(true);

                btn.addMouseListener(this);
                playerBoard.add(btn);
            }
        }

        playerBoard.setBackground(Color.BLACK);
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
        //btn.setBorderPainted(false);

        solveButtonPanel.add(btn, constraints);
        btn.addActionListener(this);
        solver_buttons.add(btn);
        
        constraints.gridx = 1;
        constraints.gridy = 0;
        btn = new JButton("Toggle I/O");
        btn.setBackground(Color.PINK);
        btn.setForeground(Color.BLACK);
        btn.setOpaque(true);
        //btn.setBorderPainted(false);

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
        //btn.setBorderPainted(false);

        solveButtonPanel.add(btn, constraints);
        btn.addActionListener(this);
        solver_buttons.add(btn);

        solveButtonPanel.setBackground(Color.BLACK);
    }

    public void registerController(SolverController ctr){
        this.controller = ctr;
    }

    public void setColor(Color color, int row, int col){
        board_buttons.get(row * BOARD_SIZE + col).setBackground(color);
        //board_buttons.get(row * BOARD_SIZE + col).setBorderPainted(false);
    }

    public void resetColor(Color color, int row, int col){
        board_buttons.get(row * BOARD_SIZE + col).setBackground(color);
        // board_buttons.get(row * BOARD_SIZE + col).setBorderPainted(true);
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

    // public void showIncorrect(){
    //     final JOptionPane pane = new JOptionPane("Try Again");
    //     final JDialog d = pane.createDialog((JFrame)null, "");
    //     d.setLocation(100,100);
    //     d.setVisible(true);
    // }

    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if (solver_buttons.contains(source)) {
            controller.processSolverButton(solver_buttons.indexOf(source));
        }
    }

    @Override
    public void mousePressed(MouseEvent m){
        currently_compressed = true;
        Object source = m.getSource();
        if(board_buttons.contains(source)){
            int ind = board_buttons.indexOf(source);
            int row = ind / BOARD_SIZE;
            int col = ind % BOARD_SIZE;
            controller.processButtonClick(row, col);   
        }
    }

    @Override
    public void mouseClicked(MouseEvent m){
        Object source = m.getSource();
        if(board_buttons.contains(source)){
            int ind = board_buttons.indexOf(source);
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
        if(board_buttons.contains(source)){
            int ind = board_buttons.indexOf(source);
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