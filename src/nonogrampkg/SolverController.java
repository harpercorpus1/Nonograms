package nonogrampkg;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class SolverController{
    private SolverView screen;
    private SolverModel model;
    private char curSymbol = '1';

    public SolverController(SolverModel model, SolverView view){
        this.model = model;
        this.screen = view;
    }

    public void processButtonClick(int row, int col){
        if(curSymbol == '1')
            screen.setTrue(row, col);
        else
            screen.setFalse(row, col);
            
        model.player_board_write(row, col, curSymbol);
    }

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

    public void clear_board(){
        model.clear_board();
        for(int row = 0; row < model.getBoardSize(); row++){
            for(int col = 0; col < model.getBoardSize(); col++){
                screen.resetColor(Color.LIGHT_GRAY, row, col);
            }
        }
        screen.clear_top_panel();
        screen.clear_side_panel();
    }

    public void toggle_write(){
        if(curSymbol == '1'){
            curSymbol = '0';
        }else{
            curSymbol = '1';
        }
        screen.toggle_button(curSymbol);
    }

    public void display_unsolvable(){

    }

    public int[][] process_string(String[] param){
        String[][] split = new String[model.getBoardSize()][];

        for(int i = 0; i < param.length; i++){
            String[] tempArr = param[i].trim().replaceAll("\\s+", " ").split(" ");
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

    public ArrayList<String> get_board(){
        ArrayList<String> retList = new ArrayList<>();
        for(int i = 0; i < model.getBoardSize(); i++){
            retList.add(model.player_board_row_query(i));
        }
        return retList;
    }

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

    public void solve(){
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
            }
        }
    }


    /* -------------------------- solving Methods begin here -------------------------- */

    public String pad(String arg){
        return String.valueOf('0').repeat(model.getBoardSize() - arg.length()) + arg;
    }
    
    public ArrayList<String> all_possible(){
        ArrayList<String> retList = new ArrayList<String>();
        for(int i = 0; i <  Math.pow(2, model.getBoardSize()); i++){
            retList.add(pad(Integer.toBinaryString(i)));
        }
        return retList;
    }

    public ArrayList<Integer> get_pattern(String arg){
        int curSum = 0;
        ArrayList<Integer> ar = new ArrayList<Integer>();
        for(int i = 0; i < arg.length(); i++){
            if(arg.charAt(i) == '1'){
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

    public boolean contains (String potential, String current_pattern){
        if(potential.length() != current_pattern.length()) return false;
        for(int i = 0; i < current_pattern.length(); i++){

            if(current_pattern.charAt(i) != '?' && current_pattern.charAt(i) != potential.charAt(i)){

                return false;
            }
        }
        return true;
    }

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

    public void add_tester_values(){
        screen.write_to_top_panel(0, "4 3");
        screen.write_to_top_panel(1, "1 6 2");
        screen.write_to_top_panel(2, "1 2 2 1 1");
        screen.write_to_top_panel(3, "1 2 2 1 1");
        screen.write_to_top_panel(4, "3 2 3");

        screen.write_to_top_panel(5, "2 1 3");
        screen.write_to_top_panel(6, "1 1 1");
        screen.write_to_top_panel(7, "2 1 4 1");
        screen.write_to_top_panel(8, "1 1 1 1 2");
        screen.write_to_top_panel(9, "1 4 2");
        
        screen.write_to_top_panel(10, "1 1 2 1");
        screen.write_to_top_panel(11, "2 7 1");
        screen.write_to_top_panel(12, "2 1 1 2");
        screen.write_to_top_panel(13, "1 2 1");
        screen.write_to_top_panel(14, "3 3");

        screen.write_to_side_panel(0, "3 2");
        screen.write_to_side_panel(1, "1 1 1 1");
        screen.write_to_side_panel(2, "1 2 1 2");
        screen.write_to_side_panel(3, "1 2 1 1 3");
        screen.write_to_side_panel(4, "1 1 2 1");

        screen.write_to_side_panel(5, "2 3 1 2");
        screen.write_to_side_panel(6, "9 3");
        screen.write_to_side_panel(7, "2 3");
        screen.write_to_side_panel(8, "1 2");
        screen.write_to_side_panel(9, "1 1 1 1");

        screen.write_to_side_panel(10, "1 4 1");
        screen.write_to_side_panel(11, "1 2 2 2");
        screen.write_to_side_panel(12, "1 1 1 1 1 1 2");
        screen.write_to_side_panel(13, "2 1 1 2 1 1");
        screen.write_to_side_panel(14, "3 4 3 1");

    }


}