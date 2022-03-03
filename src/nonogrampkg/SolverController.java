/*****************************************
 * Harper Corpus 03.03.2022
 * Controller Class of the MVC architecture
 * This file holds all logic containing
 * computations/solving/data movement 
 * logic of the Java App. 
 ****************************************/

package nonogrampkg;

import java.awt.Color;
import java.util.ArrayList;

public class SolverController{
    private SolverView screen;
    private SolverModel model;
    private char curSymbol = '1';

    /**
     * This Constructor registers the model and the view objects given by 
     * the parameters of the same name. 
     * @param model - class that we use to store and query our data table
     * @param view - class that we use to define the logic of our UI
     */
    public SolverController(SolverModel model, SolverView view){
        this.model = model;
        this.screen = view;
    }

    /**
     * Processes a button click within the player board
     * will color the button clicked on according to the current state of
     * the toggle button. Also writes to the model's data.
     * @param row - row of button that was clicked on
     * @param col - column of button that was clicked on
     */
    public void processButtonClick(int row, int col){
        if(curSymbol == '1')
            screen.setTrue(row, col);
        else
            screen.setFalse(row, col);
            
        model.player_board_write(row, col, curSymbol);
    }

    /**
     * Processes the buttons inside the bottom panel of the screen
     * @param ind - defined as the index of the button that was clicked.
     */
    public void processSolverButton(int ind){
        switch(ind){
            case 0: clear_board();      break;
            case 1: toggle_write();     break;
            case 2: solve();            break;
            default:
                // Error Condition
                throw new IllegalArgumentException(Integer.toString(ind));
        }
    }

    /** 
     * resets player board in the model as well as the screen
    */
    public void clear_board(){
        model.clear_board();
        for(int row = 0; row < model.getBoardSize(); row++){
            for(int col = 0; col < model.getBoardSize(); col++){
                screen.resetColor(Color.LIGHT_GRAY, row, col);
            }
        }
        screen.clear_top_panel();
        screen.clear_side_panel();
        screen.enable_write();
    }

    /**
     * toggles the correct/space toggle switch
     */
    public void toggle_write(){
        if(curSymbol == '1'){
            curSymbol = '0';
        }else{
            curSymbol = '1';
        }
        screen.toggle_button(curSymbol);
    }

    /**
     * This is used to process the input given by the text boxes at the top and
     * side of the player board
     * This function will remove all unnecessary whitespace, and then will parse integers
     * out from the strings, and return them as an integer array
     * @param panel_str - the string read from the text input area
     * @return an array of integers containing the row and column labels
     */
    public int[][] process_string(String[] panel_str){
        String[][] split = new String[model.getBoardSize()][];

        for(int i = 0; i < panel_str.length; i++){
            String[] tempArr = panel_str[i].trim().replaceAll("\\s+", " ").split(" ");
            split[i] = new String[tempArr.length];
            for(int j = 0; j < tempArr.length; j++){
                split[i][j] = tempArr[j];
            }
        }

        int[][] int_vals = new int[model.getBoardSize()][];
        for(int i = 0; i < model.getBoardSize(); i++){
            int_vals[i] = new int[split[i].length];
        }

        for(int i = 0; i < split.length; i++){
            if(split[i].length == 0){
                return null;
            }
            for(int j = 0; j < split[i].length; j++){
                try{
                    int_vals[i][j] = Integer.parseInt(split[i][j].trim());
                }catch(NumberFormatException nfe){
                    return null;
                }
            }
        }
        return int_vals;     
    }

    /**
     * checks to see that the values in the top and side panels are all valid, 
     * specifically making sure that all values can fit within the player board size
     * @return returns a string indicating errors in the input, or "No Errors" if none
     */
    public String bounds_check(){
        int sum;
        for(int i = 0; i < model.top_panel.length; i++){
            sum = 0;
            for(int j = 0; j < model.top_panel[i].length; j++){
                if(model.top_panel[i][j] < 0){
                    return "Top Contains Negative Value at Space " + Integer.toString(i+1);
                }
                sum += model.top_panel[i][j];
            }
            if((sum + (model.top_panel[i].length-1)) > model.getBoardSize()){
                return "Top Contains Value out of bounds at Space " + Integer.toString(i+1);
            }
        }

        for(int i = 0; i < model.side_panel.length; i++){
            sum = 0;
            for(int j = 0; j < model.side_panel[i].length; j++){
                if(model.side_panel[i][j] < 0){
                    return "Side Contains Negative Value at Space " + Integer.toString(i+1);
                }
                sum += model.side_panel[i][j];
            }
            if((sum + (model.side_panel[i].length-1)) >  model.getBoardSize()){
                return "Side Contains Value out of bounds at Space " + Integer.toString(i+1);
            }
        }

        return "No Errors";
    }

    /**
     * Cycles through all the player board and gets the values
     * @return the values of the player board as an ArrayList of Strings
     */
    public ArrayList<String> get_board(){
        ArrayList<String> retList = new ArrayList<>();
        for(int i = 0; i < model.getBoardSize(); i++){
            retList.add(model.player_board_row_query(i));
        }
        return retList;
    }

    /**
     * Tests to see if the board has changed since the last iteration 
     * used to see if the board is unsolvable
     * @param lastBoard - values at all locations of the board prior to this round of computations
     * @param curBoard - values at all locations after the computing cycle
     * @return returns false if lastBoard and curBoard are the same
     */
    public boolean board_changed(ArrayList<String> lastBoard, ArrayList<String> curBoard){
        for(int i = 0; i < model.getBoardSize(); i++){
            for(int j = 0; j < model.getBoardSize(); j++){
                if(!lastBoard.get(i).equals(curBoard.get(i))){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * holds all the logic for creating a solution to the puzzle. 
     * This function is equivalent to one cycle of solving, this function will be
     * called more than once to solve the complete puzzle.
     */
    public void solve(){
        screen.disable_write();

        String[] string_top = screen.read_top_panel();
        String[] string_side = screen.read_side_panels();
        int[][] top = process_string(string_top);
        int[][] side = process_string(string_side);
        
        if(top == null){
            screen.show_error("Top Row Has Empty Locations");
            return;
        }

        if(side == null){
            screen.show_error("Side Has Empty Locations");
            return;
        }

        model.populate_input_to_model(top, side);

        String error_msg = bounds_check();
        if(error_msg != "No Errors"){
            screen.show_error(error_msg);
            return;
        }

        ArrayList<String> lastBoard;
        ArrayList<String> thisBoard;

        while(!model.isSolved()){
            lastBoard = get_board();
            color_all_guaranteed();
            thisBoard = get_board();

            if(!board_changed(lastBoard, thisBoard)){
                // process unsolvable board
                screen.show_unsolvable();
                break;
            }
        }
    }


    /* -------------------------- solving Methods begin here -------------------------- */


    /**
     * Adds zeros to the front of a binary string, such that they'll all be
     * the same length.
     * @param binary_string - binary string that needs to be padded with leading zeros
     * @return the binary string parameter with the correct number of 0's added to the front
     * to be the same length as the player board row.
     */
    public String pad(String binary_string){
        return String.valueOf('0').repeat(model.getBoardSize() - binary_string.length()) + binary_string;
    }
    
    /**
     * Generates all the possible configurations of binary strings
     * to represent the numbers between 0 and 2 ^ ( board size ) - 1
     * @return an ArrayList containing all the binary strings
     */
    public ArrayList<String> all_possible(){
        ArrayList<String> retList = new ArrayList<String>();
        for(int i = 0; i <  Math.pow(2, model.getBoardSize()); i++){
            retList.add(pad(Integer.toBinaryString(i)));
        }
        return retList;
    }

    /**
     * Generates the panel pattern that represents the binary string 
     * that is passed as the argument.
     * Example: 001101001 -> (2, 1, 1)
     * @param binary_string - binary string that will be converted to the pattern
     * @return the pattern generated from the input
     */
    public ArrayList<Integer> get_pattern(String binary_string){
        int curSum = 0;
        ArrayList<Integer> ar = new ArrayList<Integer>();
        for(int i = 0; i < binary_string.length(); i++){
            if(binary_string.charAt(i) == '1'){
                curSum++;
            }else{
                if(curSum != 0){
                    ar.add(curSum);
                    curSum = 0;
                }
            }
        }
        if(curSum != 0){
            ar.add(curSum);
        }
        if(ar.size() == 0){
            ar.add(0);
        }
        return ar;
    }

    /**
     * Checks to see if the potential pattern is a possible future configuration of the current pattern
     * all non-question mark characters must match
     * Example: potential = 0011100 current_pattern = ??1??0? = True
     * @param potential - represents the string that is being tested 
     * @param current_pattern - represents the string that potential string must match
     * @return boolean, true if potential is a possible future configuration of the current pattern
     */
    public boolean contains (String potential, String current_pattern){
        if(potential.length() != current_pattern.length()) return false;
        for(int i = 0; i < current_pattern.length(); i++){

            if(current_pattern.charAt(i) != '?' && current_pattern.charAt(i) != potential.charAt(i)){

                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @param panel_pattern
     * @param current_pattern
     * @return
     */
    public ArrayList<String> get_all_matching_pattern(int[] panel_pattern, String current_pattern){
        ArrayList<String> all_solutions = all_possible();

        ArrayList<String> all_contains = new ArrayList<>();

        for(int i = 0; i < all_solutions.size(); i++){
            if(contains(all_solutions.get(i), current_pattern)){
                all_contains.add(all_solutions.get(i));
            }
        }

        ArrayList<String> sol = new ArrayList<>();
        for(int i = 0; i < all_contains.size(); i++){
            ArrayList<Integer> test = get_pattern(all_contains.get(i));
            if(test.size() != panel_pattern.length) continue;
            for(int j = 0; j < panel_pattern.length; j++){
                if(test.get(j) != panel_pattern[j]){
                    break;
                }
                if(j == (panel_pattern.length-1)){
                    sol.add(all_contains.get(i));
                }
            }
        }

        return sol;
    }

    /**
     * Parses through all possible configurations, and checks if there are any 
     * positions within the possible strings that are always '1' or '0', if there are
     * any, these positions are considered "guaranteed" and we can write to them.
     * @param possible - ArrayList of all possible strings generated by another function
     * @return returns a string containing '?', '1', '0' characters representing guaranteed 
     * values.
     */
    public String get_guaranteed(ArrayList<String> possible){
        if(possible.size() == 0){
            return String.valueOf('?').repeat(model.getBoardSize());
        }
        int[] counts = new int[model.getBoardSize()];
        for(int i = 0; i < possible.size(); i++){
            for(int j = 0; j < possible.get(i).length(); j++){
                if(possible.get(i).charAt(j) == '1'){
                    counts[j]++;
                }
            }
        }
        String retString = "";
        for(int i = 0; i < counts.length; i++){
            if(counts[i] == 0){
                retString += '0';
            }else if(counts[i] == possible.size()){
                retString += '1';
            }else{
                retString += '?';
            }
        }
        return retString;
    }

    /**
     * This function generates all the blocks within the column/row that are 
     * guaranteed to be '1' or '0', and colors those blocks on the screen, as well
     * as writing to the model
     */
    public void color_all_guaranteed(){
        for(int i = 0; i < model.getBoardSize(); i++){
            ArrayList<String> possible = get_all_matching_pattern(model.side_panel[i], model.player_board_row_query(i));
            String guaranteed = get_guaranteed(possible);
            for(int j = 0; j < model.getBoardSize(); j++){
                if(guaranteed.charAt(j) == '1'){
                    screen.setTrue(i, j);
                    model.player_board_write(i, j, '1');
                }
                if(guaranteed.charAt(j) == '0'){
                    screen.setFalse(i, j);
                    model.player_board_write(i, j, '0');
                }
            }
        }

        for(int i = 0; i < model.getBoardSize(); i++){
            ArrayList<String> possible = get_all_matching_pattern(model.top_panel[i], model.player_board_column_query(i));
            String guaranteed = get_guaranteed(possible);
            for(int j = 0; j < model.getBoardSize(); j++){
                if(guaranteed.charAt(j) == '1'){
                    screen.setTrue(j, i);
                    model.player_board_write(j, i, '1');
                }
                if(guaranteed.charAt(j) == '0'){
                    screen.setFalse(j, i);
                    model.player_board_write(j, i, '0');
                }
            }
        }
    }

    /**
     * This function is used when the user does not have a nonograms puzzle to input, 
     * but still would like to see the alorithm working
     * Calling this function will populate the side and top panels with values that 
     * will draw according to the users choice
     * @param unprocessed_cla - the name of the animal to load onto the nonograms board.
     */
    public void load_tester(String unprocessed_cla){
        String[] rocco = {   
            "4 3",      "1 6 2",        "1 2 2 1 1",        "1 2 2 1 1",    "3 2 3", 
            "2 1 3",    "1 1 1",        "2 1 4 1",          "1 1 1 1 2",    "1 4 2", 
            "1 1 2 1",  "2 7 1",        "2 1 1 2",          "1 2 1",        "3 3",
                        
            "3 2",      "1 1 1 1",      "1 2 1 2",          "1 2 1 1 3",    "1 1 2 1",
            "2 3 1 2",  "9 3",          "2 3",              "1 2",          "1 1 1 1", 
            "1 4 1",    "1 2 2 2",      "1 1 1 1 1 1 2",    "2 1 1 2 1 1",  "3 4 3 1"
            };
        
        String[] david = {   
            "3",        "4",            "5",                "4",            "5", 
            "6",        "3 2 1",        "2 2 5",            "4 2 6",        "8 2 3", 
            "8 2 1 1",  "2 6 2 1",      "4 6",              "2 4",          "1",
                            
            "3",        "5",            "4 3",              "7",            "5",
            "3",        "5",            "1 8",              "3 3 3",        "7 3 2",
            "5 4 2",    "8 2",          "10",               "2 3",          "6"
        };
                             
        String[] geoffrey = {   
            "2",        "3 3",          "2 2 2",            "4 6",          "5 7", 
            "2 7",      "3 1 4",        "7",                "2 4",          "2 6 2", 
            "12",       "15",           "4 10",             "1 9",          "4 8",

            "2 2 2 1",  "2 2 1 1 2 1",  "4 1 1 2 1",        "5 5",          "2 5",
            "7",        "1 1 5",        "11",               "12",           "13", 
            "8 5",      "1 3 5",        "5 5",              "3 6",          "6"
        };

        String[] maurice = {   
            "2",        "3",            "1 1",              "1 1 2 1",      "2 2 1 1", 
            "2 6 2",    "1 2 2",        "1 3 2",            "3 2 5",        "2 1 1",
            "1 1",      "4 2 1",        "2 2 2 2",          "1 1 2 5",      "2 5",

            "3",        "4 2 1",        "3 1 1 1",          "3 1 2 1",      "2 1 2",
            "4 2 2",    "2 2 2",        "1 3 1",            "2 2 1",        "4 3", 
            "1 1 2 2",  "1 1 1",        "1 1 1",            "5 2",          "3 2 3"
        };

        String processed_cla = unprocessed_cla.trim().toLowerCase();
        String[] Loaded_Strings;

        if(processed_cla.equals("geoffrey")){
            Loaded_Strings = geoffrey;
        }else if(processed_cla.equals("david")){
            Loaded_Strings = david;
        }else if(processed_cla.equals("maurice")){
            Loaded_Strings = maurice;
        }else{
            if(!processed_cla.equals("rocco")){
                System.out.print("You entered [" + processed_cla + "], ");
                System.out.println("which is not a valid animal name...");
                System.out.println("Here is Rocco for your trouble.");
            }
            Loaded_Strings = rocco;
        }

        for(int i = 0; i < model.getBoardSize(); i++){
            screen.write_to_top_panel(i, Loaded_Strings[i]);
            screen.write_to_side_panel(i, Loaded_Strings[model.getBoardSize() + i]);
        }
    }
}