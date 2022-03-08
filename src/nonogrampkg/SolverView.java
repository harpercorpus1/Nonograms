/*****************************************
 * Harper Corpus 03.03.2022
 * View Class of the MVC architecture
 * This file holds all logic containing
 * the front end of this Java App. 
 ****************************************/

package nonogrampkg;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class SolverView extends JFrame implements MouseListener, ActionListener {
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
    boolean writable;

    SolverController controller;

    /**
     * This is the constructor that creates the JFrame Object that displays our entire application
     * It calls several other functions to initialize the panels within it to display them.
     * @param size - dimension of the player board = (size x size)
     */
    public SolverView(int size){
        super("Nonogram Solver");

        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
          }catch(Exception e){
           e.printStackTrace(); 
        }

        BOARD_SIZE = size;
        currently_compressed = false;
        writable = true;

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

        this.setMinimumSize(new Dimension(700,850));
        this.setVisible(true);
    }

    /**
     * Creates the panel holding the title at the top of the JFrame.
     */
    public void init_title_panel(){
        firstPanel = new JPanel();
        firstPanel.setMaximumSize(new Dimension(600,100));

        JLabel title = new JLabel("Nonogram Solver");
        title.setFont(new Font("Serif", Font.BOLD, 60));
        title.setForeground((Color.PINK));
        title.setBorder(new EmptyBorder(10,0,0,0));
        firstPanel.setBackground(Color.BLACK);
        firstPanel.add(title);
    }

    /**
     * Generates a GridBagConstraints object that has standardized weights.
     * @param x - x-coordinate position on the display
     * @param y - y-coordinate posiiton on the display
     * @param width - number of coordinate positions to cover horizontally
     * @param height - number of coordinate positions to cover vertically
     * @return the generated GridBagConstraints Object
     */
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

    /**
     * Generates a button with the dimensions defined by the parameter
     * @param dim - Dimension object that holds the correct size of the button
     * @return the generated JButton Object
     */
    public JButton button_factory(Dimension dim){
        JButton btn = new JButton();
        btn.setMinimumSize(dim);
        btn.setPreferredSize(dim);
        btn.setMaximumSize(dim);
        return btn;
    }

    /**
     * Generates a JTextPane Object with the dimensions given by the parameter
     * @param dim - Dimension object that defines the correct size of the button
     * @return the generated JTextPane Object
     */
    public JTextPane textpane_factory(Dimension dim){
        JTextPane text_pane = new JTextPane();
        text_pane.setMinimumSize(dim);
        text_pane.setPreferredSize(dim);
        text_pane.setMaximumSize(dim);
        return text_pane;
    }

    /**
     * Initializes and registers all the objects that are contained in the center panel 
     * of the Display, which holds the player board, and input panels. 
     */
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
        // btn.setOpaque(true);
        btn.setBackground(Color.BLACK);
        btn.setBorderPainted(false);
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
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setFont(new Font("Purisa", Font.BOLD, 40));
                btn.setMargin(new Insets(0,0,0,0));
                btn.setForeground(Color.PINK);
                playerBoard.add(btn, constraints);
                board_buttons.add(btn);
                btn.addMouseListener(this);
            }
        }
    }

    /**
     * Initializes and registers all the objects that are contained in the bottom panel
     * of the Display, holding the solve and clear buttons
     */
    public void init_solver_panel(){
        solveButtonPanel = new JPanel();

        solveButtonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        solveButtonPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        solveButtonPanel.setMaximumSize(new Dimension(600,150));

        JButton btn;

        constraints = generate_constraints(0, 0, 1, 1);
        constraints.fill = GridBagConstraints.BOTH;
        btn = new JButton("Clear Board");
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setOpaque(true);
        btn.addActionListener(this);
        solver_buttons.add(btn);
        solveButtonPanel.add(btn, constraints);

        constraints = generate_constraints(1, 0, 1, 1);
        constraints.fill = GridBagConstraints.BOTH;
        btn = new JButton("Clear Side Panel");
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setOpaque(true);
        btn.addActionListener(this);
        solver_buttons.add(btn);
        solveButtonPanel.add(btn, constraints);

        constraints = generate_constraints(2, 0, 1, 1);
        constraints.fill = GridBagConstraints.BOTH;
        btn = new JButton("Clear Top Panel");
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setOpaque(true);
        btn.addActionListener(this);
        solver_buttons.add(btn);
        solveButtonPanel.add(btn, constraints);

        constraints = generate_constraints(3, 0, 1, 1);
        constraints.fill = GridBagConstraints.BOTH;
        btn = new JButton("Clear All");
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setOpaque(true);
        btn.addActionListener(this);
        solver_buttons.add(btn);
        solveButtonPanel.add(btn, constraints);

        constraints = generate_constraints(0, 1, 2, 1);
        constraints.fill = GridBagConstraints.BOTH;
        btn = new JButton("Toggle Write");
        btn.setBackground(Color.PINK);
        btn.setForeground(Color.BLACK);
        btn.setOpaque(true);
        btn.addActionListener(this);
        solver_buttons.add(btn);
        solveButtonPanel.add(btn, constraints);

        constraints = generate_constraints(2, 1, 2, 1);
        constraints.fill = GridBagConstraints.BOTH;
        btn = new JButton("Solve");
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setOpaque(true);
        btn.addActionListener(this);
        solver_buttons.add(btn);
        solveButtonPanel.add(btn, constraints);


        

        // constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.gridx = 0;
        // constraints.gridy = 0;
        // btn = new JButton("Clear Board");

        // btn.setBackground(Color.WHITE);
        // btn.setForeground(Color.BLACK);
        // btn.setOpaque(true);

        // solveButtonPanel.add(btn, constraints);
        // btn.addActionListener(this);
        // solver_buttons.add(btn);
        
        // constraints.gridx = 1;
        // constraints.gridy = 0;
        // btn = new JButton("Toggle Write");
        // btn.setBackground(Color.PINK);
        // btn.setForeground(Color.BLACK);
        // btn.setOpaque(true);

        // solveButtonPanel.add(btn, constraints);
        // btn.addActionListener(this);
        // solver_buttons.add(btn);
 
        // constraints.gridx = 0;
        // constraints.gridy = 2;
        // constraints.fill = GridBagConstraints.HORIZONTAL;
        // constraints.gridwidth = 2;
        // btn = new JButton("Solve");

        // btn.setBackground(Color.WHITE);
        // btn.setForeground(Color.BLACK);
        // btn.setOpaque(true);

        // solveButtonPanel.add(btn, constraints);
        // btn.addActionListener(this);
        // solver_buttons.add(btn);

        solveButtonPanel.setBackground(Color.BLACK);
    }

    /**
     * Registers the controller to the SolverView Class
     * @param ctr - controller to be registered
     */
    public void registerController(SolverController ctr){
        this.controller = ctr;
    }

    /**
     * Colors a JButton on the playerBoard
     * @param color - color to change the button to
     * @param row - row of the button within the player board
     * @param col - column of the button within the player board
     */
    public void setColor(Color color, int row, int col){
        board_buttons.get(row * BOARD_SIZE + col).setBackground(color);
    }

    /**
     * Holds the logic for displaying the logical '1' for a button to the screen
     * @param row - row of the button that is to be changed
     * @param col - column of the button that is to be changed
     */
    public void setTrue(int row, int col){
        JButton btn = board_buttons.get(row * BOARD_SIZE + col);
        btn.setBackground(Color.PINK);
        btn.setText("");
    }

    /**
     * Holds the logic for displaying the logical '0' for a button to the screen
     * @param row - row of the button that is to be changed
     * @param col - column of the button that is to be changed
     */
    public void setFalse(int row, int col){
        JButton btn = board_buttons.get(row * BOARD_SIZE + col);
        btn.setBackground(Color.BLACK);
        // btn.setText("X");
    }

    /**
     * Displays the button in the default manner
     * @param color - color to change the button to
     * @param row - row of the button to be changed
     * @param col - column of the button to be changed
     */
    public void resetColor(Color color, int row, int col){
        JButton btn = board_buttons.get(row * BOARD_SIZE + col);
        btn.setBackground(color);
        // btn.setText("");
    }

    /**
     * Changes the state of the toggle button
     * @param current_write_symbol - either '1' or '0' showing what state the write condition is in
     */
    public void toggle_button(char current_write_symbol){
        if(current_write_symbol == '1'){
            solver_buttons.get(4).setForeground(Color.BLACK);
            solver_buttons.get(4).setBackground(Color.PINK);
        }else{
            solver_buttons.get(4).setForeground(Color.WHITE);
            solver_buttons.get(4).setBackground(Color.BLACK);
        }
    }

    /**
     * Reads text input from the top input panel
     * @return Array of Strings that contain the input from all nodes of the top panel
     */
    public String[] read_top_panel(){
        String[] top_labels = new String[BOARD_SIZE];
        for(int i = 0; i < BOARD_SIZE; i++){
            top_labels[i] = top_panel.get(i).getText();
        }
        return top_labels;
    }

    /**
     * Reads text input from the side input panel
     * @return Array of Strings that contain the input from all nodes of the side panel
     */
    public String[] read_side_panels(){
        String[] side_labels = new String[BOARD_SIZE];
        for(int i = 0; i < BOARD_SIZE; i++){
            side_labels[i] = side_panel.get(i).getText();
        }
        return side_labels;
    }

    /**
     * Clears all values from the top panel
     */
    public void clear_top_panel(){
        for(int i = 0; i < BOARD_SIZE; i++){
            top_panel.get(i).setText("");
        }
    }

    /**
     * Clears all values from the side panel
     */
    public void clear_side_panel(){
        for(int i = 0; i < BOARD_SIZE; i++){
            side_panel.get(i).setText("");
        }
    }
    

    /**
     * Write text into an input panel on the side of the player board
     * @param ind - index of JTextPane within side panel
     * @param text - text to write to JTextPane
     */
    public void write_to_side_panel(int ind, String text){
        side_panel.get(ind).setText(text);
    }

    /**
     * Write text into an input panel on the top of the player board
     * @param ind - index of JTextPane within top panel
     * @param text - text to write to JTextPane
     */
    public void write_to_top_panel(int ind, String text){
        top_panel.get(ind).setText(text);
    }

    /**
     * Displays an error message
     * @param error_msg - error message to display
     */
    public void show_error(String error_msg){
        JOptionPane.showMessageDialog(this, error_msg, "ERROR", JOptionPane.INFORMATION_MESSAGE );
    }

    /**
     * Displays to show the user when the puzzle inputted is not solvable
     */
    public void show_unsolvable(){
        JOptionPane.showMessageDialog(this, "Unsolvable Puzzle!", "ERROR", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 
     */
    public void display_starter(){
        String IntroMessage = 
        "Welcome to the Nonogram Solver!\n" + 
        "To input in the boxes, please enter your numbers with one space in between\n" + 
        "Draw any patterns you already have on the board!";
        JOptionPane.showMessageDialog(this, IntroMessage, "Welcome!", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * this cuts off the button activation functions by drag, 
     * therefore stopping coloring
     */
    public void disruptDrag(){
        currently_compressed = false;
    }

    /**
     * Makes it so the User can write on the player board
     */
    public void enable_write(){
        writable = true;
    }

    /**
     * Makes it so the User cannot write on the player board
     */
    public void disable_write(){
        writable = false;
    }

    /**
     * Function catches all button clicks in the bottom panel of the screen
     * @param e - ActionEvent, activates on button click
     */
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if (solver_buttons.contains(source)) {
            controller.processSolverButton(solver_buttons.indexOf(source));
        }
    }

    /**
     * Function processes user pressing their mouse
     * @param m - MouseEvent, activates on mouse pressing
     */
    @Override
    public void mousePressed(MouseEvent m){
        if(!writable) return;
        currently_compressed = true;
        Object source = m.getSource();
        if(board_buttons.contains(source)){
            int ind = board_buttons.indexOf(source);
            int row = ind / BOARD_SIZE;
            int col = ind % BOARD_SIZE;
            controller.processButtonClick(row, col);   
        }
    }

    /**
     * Function processes mouse clicks
     * @param m - MouseEvent, activates on mouse clicks a member of the player board
     */
    @Override
    public void mouseClicked(MouseEvent m){
        if(!writable) return;
        Object source = m.getSource();
        if(board_buttons.contains(source)){
            int ind = board_buttons.indexOf(source);
            int row = ind / BOARD_SIZE;
            int col = ind % BOARD_SIZE;
            controller.processButtonClick(row, col); 
        }
    }

    /**
     * Function processes mouse locations on buttons
     * @param m - MouseEvent, activates when a mouse enters each JButton within the player board
     */
    @Override
    public void mouseEntered(MouseEvent m){
        if(!writable) return;
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

    /**
     * Unused Override
     */
    @Override
    public void mouseExited(MouseEvent m){
        //
    }

    /**
     * Function processes when the user releases their mouse click
     * @param m - MouseEvent, activates when the user releases their mouse click
     */
    @Override
    public void mouseReleased(MouseEvent m){
        currently_compressed = false;
    }
}