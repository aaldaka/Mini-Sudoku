package sudoku.solver;

import sudoku.model.Sudoku;

public class SudokuSolver {
    public boolean solve(Sudoku sudoku) {

    }

    // MRV huesristic addition/optimization
    private int[] findBestCell(Sudoku sudoku) {
        int[] best = new int[2]; // row,col
        int least = 10; //higher than 9 as a starter

        for (int row=0; row< 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (!sudoku.getCell(row, col).isEmpty()){
                    continue;
                }
                int possibilities = countOptions(sudoku, row, col);
                if (possibilities < least){
                    least = possibilities;
                    best[0]= row;
                    best[1]= col;
                }

            }
        }
        if (least == 10){
            return null;
        }
        return best;
        }

    // checks each empty cell, how many numbers can go in the cell ie if if the number is not repeated in the same roq/col/grid, it'll count as a possibility
    private int countOptions(Sudoku sudoku, int row, int col) {
        int possibleNum = 0;
        for (int i=1; i<= 9; i++){
            if (sudoku.isValidPlacement(row,col,i)){
                possibleNum++;
            }
        }
        return possibleNum;
    }

}