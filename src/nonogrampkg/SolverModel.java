package nonogrampkg;

public class SolverModel {
    private char[][] playerBoard;
    private int[] top_panel;
    private int[] side_panel;
    private int BOARD_SIZE;

    public SolverModel(int boardSize){
        this.BOARD_SIZE = boardSize;
        init_board();
    }

    public void init_board(){
        playerBoard = new char[BOARD_SIZE][BOARD_SIZE];
        clear_board();
    }

    public void clear_board(){
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                playerBoard[i][j] = '?';
            }
        }
    }

    // public void populate_board_state(char[][] board_state){
    //     for(int i = 0; i < BOARD_SIZE; i++){
    //         for(int j = 0; j < BOARD_SIZE; j++){
    //             playerBoard[i][j] = board_state[i][j];
    //         }
    //     }
    // }

    public int getBoardSize(){return BOARD_SIZE;}

    public char player_board_query(int row, int col){
        return playerBoard[row][col];
    }

    public void player_board_write(int row, int col, char writeChar){
        playerBoard[row][col] = writeChar;
    }

    // public char solution_board_query(int row, int col){
    //     return solutionBoard[row][col];
    // }

}
