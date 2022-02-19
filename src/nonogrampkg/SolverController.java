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
        // if(checkCorrect(row, col)){
        if(curSymbol == 'T')
            screen.setColor(Color.PINK, row, col);
        else 
            screen.setColor(Color.BLACK, row, col);

        model.player_board_write(row, col, curSymbol);
//        }else{

//          screen.disruptDrag();
    }
//    }

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

    // public int validate_usable_int(String panel_ele){
    //     try{
    //         int val = Integer.parseInt(panel_ele);
    //     }catch(NumberFormatException nfe){
    //         return -1;
    //     }
        
    // }

    public int[] validate_panel(String[] panel){
        for(int i = 0; i < model.getBoardSize(); i++){
            try{
                int val = Integer.parseInt(panel[i])
            }
        }
        return 
    }

    public void solve(){
        String[] top = screen.read_top_panel();
        String[] side = screen.read_side_panels();
        

    }

//     public boolean checkCorrect(int row, int col){
//         return true;
//         //return model.solution_board_query(row, col) == curSymbol;
//     }
// }
}