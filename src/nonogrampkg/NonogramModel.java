package nonogrampkg;

import java.util.Arrays;

public class NonogramModel {
    private char[][] playerBoard;
    private char[][] solutionBoard;
    private int BOARD_SIZE;

    public NonogramModel(int boardSize){
        this.BOARD_SIZE = boardSize;

        init_board();
    }

    public int getBoardSize(){return BOARD_SIZE;}

    public void placeBlock(int row, int col, char symbol){
        playerBoard[row][col] = symbol;
    }

    public void init_board(){
        playerBoard = new char[BOARD_SIZE][BOARD_SIZE];
        resetBoard();
    }

    public void resetBoard(){
        for(int i = 0; i < this.BOARD_SIZE; i++){
            Arrays.fill(playerBoard[i], '_');
        }
    }

    public void load_solution(){
        
    }

    public char player_board_query(int row, int col){
        return playerBoard[row][col];
    }

    public char solution_board_query(int row, int col){
        return solutionBoard[row][col];
    }
}
