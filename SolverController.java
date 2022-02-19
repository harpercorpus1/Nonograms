package nonogrampkg;

import java.awt.Color;

public class SolverController{
    private SolverView screen;
    private SolverModel model;
    private char curSymbol = 'T';

    public SolverController(SolverModel model, SolverView view){
        this.model = model;
        this.screen = view;
    }

    public void processButtonClick(int row, int col){
        if(curSymbol == 'T')
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
        if(curSymbol == 'T'){
            curSymbol = 'F';
        }else{
            curSymbol = 'T';
        }
        screen.toggle_button(curSymbol);
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
                    return "Top Contains Negative Value " + Integer.toString(i) + "," + Integer.toString(j);
                }
                sum += model.top_panel[i][j];
            }
            if((sum + (model.top_panel[i].length-1)) > BOARD_SIZE){
                "Top Contains Value out of bounds " + Integer.toString(i) + "," + Integer.toString(j);
            }
        }

        for(int i = 0; i < model.side_panel.length; i++){
            sum = 0;
            for(int j = 0; j < model.side_panel[i].length; j++){
                if(model.side_panel[i][j] < 0){
                    return "Side Contains Negative Value " + Integer.toString(i) + "," + Integer.toString(j);
                }
                sum += model.side_panel[i][j];
            }
            if((sum + (model.side_panel[i].length-1)) > BOARD_SIZE){
                "Side Contains Value out of bounds " + Integer.toString(i) + "," + Integer.toString(j);
            }
        }

        return "No Errors";
    }

    public void solve(){
        String[] string_top = screen.read_top_panel();
        String[] string_side = screen.read_side_panels();
        int[][] top = process_string(string_top);
        int[][] side = process_string(string_side);
        
        if(top == null){
            screen.show_empty_error("Top Row Has Empty Locations");
            return;
        }

        if(side == null){
            screen.show_empty_error("Side Has Empty Locations");
            return;
        }

        model.populate_input_to_model(top, side);

        String error_msg = bounds_check();
        if(error_msg != "No Errors"){
            screen.show_bounds_error(error_msg);
            break;
        }

        

    }
}