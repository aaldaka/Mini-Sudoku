package sudoku.solver;

import sudoku.model.Sudoku;
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
        // play(sudoku, difficulty); //not done yet
    }

    private String askDifficulty() {
        while (true) {
            System.out.println("Choose difficulty: Easy | Medium | Hard");
            String answer = input.nextLine().trim().toLowerCase();

            if (answer.equals("easy")) {
                return "easy";
            }
            if (answer.equals("medium")) {
                return "easy";
            }
            if (answer.equals("hard")) {
                return "easy";
            }
            System.out.println("Please choose easy, medium or hard");
        }
    }

    // implement api
    private Sudoku getPuzzle(String difficulty) {
        return pickLocalPuzzle(difficulty);
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


}