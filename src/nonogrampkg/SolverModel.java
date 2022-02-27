package nonogrampkg;

public class SolverModel {
    private char[][] playerBoard;
    private int BOARD_SIZE;

    public int[][] top_panel;
    public int[][] side_panel;

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

    public int getBoardSize(){return BOARD_SIZE;}

    public char player_board_query(int row, int col){
        return playerBoard[row][col];
    }

    public void player_board_write(int row, int col, char writeChar){
        playerBoard[row][col] = writeChar;
    }

    public String player_board_row_query(int row){
        return new String(playerBoard[row]);
    }

    public String player_board_column_query(int col){
        String retString = "";

        for(int i = 0; i < BOARD_SIZE; i++){
            retString += player_board_query(i, col);
        }

        return retString;
    }

    public void populate_input_to_model(int[][] top, int[][] side){
        top_panel = new int[BOARD_SIZE][];
        for(int i = 0; i < BOARD_SIZE; i++){
            top_panel[i] = new int[top[i].length];
            for(int j = 0; j < top[i].length; j++){
                top_panel[i][j] = top[i][j];
            }
        }

        side_panel = new int[BOARD_SIZE][];
        for(int i = 0; i < BOARD_SIZE; i++){
            side_panel[i] = new int[side[i].length];
            for(int j = 0; j < side[i].length; j++){
                side_panel[i][j] = side[i][j];
            }
        }
    }

    public boolean isSolved(){
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                if(playerBoard[i][j] == '?') return false;
            }
        }
        return true;
    }

}
