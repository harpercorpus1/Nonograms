package nonogrampkg;

import java.awt.Color;

public class NonogramController{
    private NonogramView screen;
    private NonogramModel model;
    private char curSymbol = 'X';

    public NonogramController(NonogramModel model, NonogramView view){
        this.model = model;
        this.screen = view;
    }

    public void processButtonClick(int row, int col){
        if(checkCorrect(row, col)){
            screen.setColor(Color.PINK, row, col);
        }else{
            screen.disruptDrag();
            screen.showIncorrect();
        }
    }

    public boolean checkCorrect(int row, int col){
        return true;
        //return model.solution_board_query(row, col) == curSymbol;
    }
}