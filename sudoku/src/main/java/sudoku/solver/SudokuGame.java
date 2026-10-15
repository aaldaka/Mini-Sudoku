package sudoku.solver;

import sudoku.model.Sudoku;
import sudoku.utils.SudokuApiClient;
import sudoku.utils.SudokuFileUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SudokuGame {

    private final Scanner input;
    private final SudokuSolver solver = new SudokuSolver();

    public SudokuGame(Scanner input) {
        this.input = input;
    }

    public void start() {
        String difficulty = askDifficulty();
        Sudoku sudoku = getPuzzle(difficulty);

        if (sudoku == null) {
            System.out.println("No puzzles found for " + difficulty);
            return;
        }

        sudoku.printBoard("Difficulty: " + difficulty);
         play(sudoku, difficulty);
    }

    private String askDifficulty() {
        while (true) {
            System.out.println("Choose difficulty: Easy | Medium | Hard");
            String answer = input.nextLine().trim().toLowerCase();

            if (answer.equals("easy")) {
                return "easy";
            }
            if (answer.equals("medium")) {
                return "medium";
            }
            if (answer.equals("hard")) {
                return "hard";
            }
            System.out.println("Please choose easy, medium or hard");
        }
    }

// tries the API first, falls back to the local files
    private Sudoku getPuzzle(String difficulty) {
        try {
            int[][] numbers = SudokuApiClient.fetchPuzzle(difficulty);
            String fileName = SudokuFileUtils.nextOnlineName();
            Sudoku sudoku = new Sudoku(numbers, fileName);
            SudokuFileUtils.savePuzzle(sudoku, fileName);
            System.out.println("Got a new puzzle online! Saved as " + fileName);
            return sudoku;
        } catch (Exception e) {
            System.out.println("Couldn't get an online puzzle (" + e.getMessage() + "), using a local one");
            return pickLocalPuzzle(difficulty);
        }
    }

    private Sudoku pickLocalPuzzle(String difficulty) {
        List<Sudoku> puzzles = new ArrayList<>();

        for (String name : SudokuFileUtils.listPuzzles()) {
            if (!name.endsWith(".txt")) {
                continue;
            }
            try {
                Sudoku sudoku = Sudoku.load(name);
                if (sudoku.getDifficulty().equals(difficulty)) {
                    puzzles.add(sudoku);
                }
            } catch (Exception e) {
                // broken file, skip it
            }
        }

        if (puzzles.isEmpty()) {
            return null;
        }

        int random = (int) (Math.random() * puzzles.size());
        return puzzles.get(random);
    }

    private void play(Sudoku sudoku, String difficulty) {
        System.out.println("How to play:");
        System.out.println("  row col value   e.g. 1 3 4 puts a 4 in row 1, column 3");
        System.out.println("  row col 0       clears a cell");
        System.out.println("  solve           let the computer finish it");
        System.out.println("  quit            give up");
        System.out.println();

        LocalTime start = LocalTime.now();

        while (!sudoku.isFull()) {
            sudoku.printBoard("Difficulty: " + difficulty);
            System.out.print("> ");
            String line = input.nextLine().trim().toLowerCase();

            if (line.equals("quit")) {
                System.out.println("Thank you for playing! See ya");
                return;
            }

            if (line.equals("solve")) {
                if (solver.solve(sudoku)) {
                    sudoku.printBoard("I'll be your hero and solve it for you");
                    System.out.println("Time: " + formatTime(LocalTime.now().toSecondOfDay() - start.toSecondOfDay()));                    return;
                }
                System.out.println("No solution from here, try clearing some of your numbers");
                continue;
            }

            placeNumber(sudoku, line);
        }

        long seconds = LocalTime.now().toSecondOfDay() - start.toSecondOfDay();
        sudoku.printBoard("Congrats, you solved it! It took you..");
        System.out.println("Time: " + formatTime(seconds));
    }

    private void placeNumber(Sudoku sudoku, String line) {
         String[] parts = line.split(" ");
        if (parts.length != 3){
            System.out.println("You have to input all 3: Row Col and Value");
            return;
        }

         int row, col, value;

        try{
        row = Integer.parseInt(parts[0]);
        col = Integer.parseInt(parts[1]);
        value = Integer.parseInt(parts[2]);
        }catch (NumberFormatException e) {
            System.out.println("You must input numbers only");
            return;
        }

        if (row < 1 || row > 9 || col < 1 || col > 9 || value < 0 || value >9){
            System.out.println("The bounds for row/col: 1-9, and for values: 0-9");
            return;
        }

        // user inputs ind+1, we have to return the actual index
        row--;
        col--;

        if (sudoku.getCell(row, col).isGiven()){
            System.out.println("You cannot change a given value from the puzzle");
            return;
        }

        // regardless if a new num or clearnig the cell, it should always start with zero
        sudoku.getCell(row,col).setValue(0);

        // no need to continue fetching a value
        if (value ==0){
            return;
        }

        if (!sudoku.isValidPlacement(row,col,value)){
            System.out.println("This placement is incorrect");
            return;
        }

        sudoku.getCell(row,col).setValue(value);
    }

    private String formatTime(long seconds) {
        return (seconds / 60) + " min " + (seconds % 60) + " sec";
    }


}