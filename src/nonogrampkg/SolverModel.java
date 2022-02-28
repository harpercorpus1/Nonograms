package nonogrampkg;

public class SolverModel {
    private char[][] playerBoard;
    private int BOARD_SIZE;

    public int[][] top_panel;
    public int[][] side_panel;

    /**
     * This constructor creates the data stores necessary to hold the 
     * values for the board, and calls another function to populate them
     * @param boardSize - size of the player board
     */
    public SolverModel(int boardSize){
        this.BOARD_SIZE = boardSize;
        init_board();
    }

    /**
     * creates an empty array to hold the player board, and calls another function to populate it
     */
    public void init_board(){
        playerBoard = new char[BOARD_SIZE][BOARD_SIZE];
        clear_board();
    }

    /**
     * Resets all values of the players board to '?'
     */
    public void clear_board(){
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                playerBoard[i][j] = '?';
            }
        }
    }

    /**
     * @return the size of the player's board
     */
    public int getBoardSize(){return BOARD_SIZE;}

    /**
     * @param row - row of position to query
     * @param col - column of position to query
     * @return the value at the position queried
     */
    public char player_board_query(int row, int col){
        return playerBoard[row][col];
    }

    /**
     * This function writes whatever character desired into the player board
     * @param row - row of position to write to
     * @param col - column of position to write to
     * @param writeChar - what character to write to the position
     */
    public void player_board_write(int row, int col, char writeChar){
        playerBoard[row][col] = writeChar;
    }

    /**
     * 
     * @param row
     * @return
     */
    public String player_board_row_query(int row){
        return new String(playerBoard[row]);
    }

    /**
     * 
     * @param col
     * @return
     */
    public String player_board_column_query(int col){
        String retString = "";

        for(int i = 0; i < BOARD_SIZE; i++){
            retString += player_board_query(i, col);
        }

        return retString;
    }

    /**
     * This function will write the processed input from the input panels in the View class,
     * into the model for storage.
     * @param top - 2D array representing column layers from the top panel
     * @param side - 2D array representing row layers from the side panel
     */
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

    /**
     * @return boolean, true if every position in the player board is full.
     */
    public boolean isSolved(){
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                if(playerBoard[i][j] == '?') return false;
            }
        }
        return true;
    }

}
