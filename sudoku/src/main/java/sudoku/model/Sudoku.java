package sudoku.model;

import sudoku.utils.SudokuFileUtils;

import java.io.FileNotFoundException;

public class Sudoku {

    private final SudokuCell[][] cells = new SudokuCell[9][9];
    private final String fileName;

    public Sudoku(int[][] numbers, String fileName) {
        this.fileName = fileName;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = new SudokuCell(numbers[row][col]);
            }
        }
    }

    public static Sudoku load(String fileName) {
        int[][] numbers = SudokuFileUtils.readPuzzle(fileName);
        return new Sudoku(numbers, fileName);
    }

    public void saveSolution() throws FileNotFoundException {
        SudokuFileUtils.saveSolution(this, fileName);
    }

    public SudokuCell getCell(int row, int col) {
        return cells[row][col];
    }

    // checker
    public boolean isValidPlacement(int row, int col, int value) {
        for (int i = 0; i < 9; i++) {
            if (cells[row][i].getValue() == value) {
                return false;
            }
            if (cells[i][col].getValue() == value) {
                return false;
            }
        }

        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (cells[r][c].getValue() == value) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printBoard(String title) {
        System.out.println("\t\t" + title);
        System.out.println(boardToString());
    }

    public String boardToString() {
        String text = "";
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                text += cells[row][col].getValue();
                if (col == 2 || col == 5) {
                    text += "  |  ";
                } else if (col < 8) {
                    text += "  ";
                }
            }
            text += "\n";
            if (row == 2 || row == 5) {
                text += "----------------------------------\n\n";
            }
        }
        return text;
    }
}