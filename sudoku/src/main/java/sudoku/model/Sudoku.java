package sudoku.model;

import sudoku.utils.SudokuFileUtils;

import java.io.FileNotFoundException;

public class Sudoku {

    private final SudokuCell[][] cells = new SudokuCell[9][9];
    private final String fileName;
    private static final String LAVENDER = "\u001B[38;2;226;178;245m";
    private static final String RESET = "\u001B[0m";

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

    public String saveSolution(String stats){
        return SudokuFileUtils.saveSolution(this, fileName, stats);
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
        String line = "+-------+-------+-------+";

        System.out.println(title);
        System.out.println(line);

        for (int row = 0; row < 9; row++) {
            String text = "| ";
            for (int col = 0; col < 9; col++) {
                int value = cells[row][col].getValue();

                // show empty cells as a dot
                if (value == 0) {
                    text += ". ";
                } else if (cells[row][col].isGiven()) {
                    text += value + " ";
                } else {
                    text += LAVENDER + value + RESET + " ";
                }

                // a bar after every 3 columns
                if (col == 2 || col == 5 || col == 8) {
                    text += "| ";
                }
            }
            System.out.println(text.trim());

            // a border after every 3 rows
            if (row == 2 || row == 5 || row == 8) {
                System.out.println(line);
            }
        }
        System.out.println();
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

    // how many starting numbers the puzzle has
    public int countClues() {
        int givenNumbers = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (getCell(row, col).isGiven()) {
                    givenNumbers++;
                }
            }
        }
        return givenNumbers;
    }

    // Fallback if api doesnt work it picks from local puzzles dir based on the givem mumbers
    public String getDifficulty() {
        int clues = countClues();
        if (clues >= 40){
            return "easy";
        }
        if (clues >= 32){
            return "medium";
        }
        return "hard";
    }

    public boolean isFull() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (getCell(row, col).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }


}