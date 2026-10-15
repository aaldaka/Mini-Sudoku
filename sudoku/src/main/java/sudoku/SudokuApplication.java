package sudoku;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sudoku.model.Sudoku;
import sudoku.solver.SudokuGame;
import sudoku.solver.SudokuSolver;
import sudoku.utils.SudokuFileUtils;

import java.util.Scanner;

@SpringBootApplication
public class SudokuApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SudokuApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner input = new Scanner(System.in);

		while (true) {
			System.out.println("""
					 ____  _   _ ____   ___  _  ___   _
					/ ___|| | | |  _ \\ / _ \\| |/ / | | |
					\\___ \\| | | | | | | | | | ' /| | | |
					 ___) | |_| | |_| | |_| | . \\| |_| |
					|____/ \\___/|____/ \\___/|_|\\_\\\\___/
					
					      === SUDOKU GAME ===
					
					  1. Solve All Puzzle Files
					  2. Play Sudoku
					  3. Exit
					""");
			System.out.print("  Enter your choice: ");

			String choice = input.nextLine().trim();

			if (choice.equals("1")) {
				solveAllFiles();
			} else if (choice.equals("2")) {
				new SudokuGame(input).start();
			} else if (choice.equals("3")){
				System.out.println("Thank you for playing, see ya!");
				return;
			}
			else {
				System.out.println("Unknown choice, please type 1 or 2");
			}
		}
	}

	private void solveAllFiles() {
		String[] files = SudokuFileUtils.listPuzzles();

		if (files.length == 0) {
			System.out.println("No puzzles found in uploads/puzzles");
			return;
		}

		SudokuSolver solver = new SudokuSolver();
		for (String fileName : files) {
			if (!fileName.endsWith(".txt")) {
				continue;
			}

			String name = fileName.replace(".txt", "");

			try {
				Sudoku sudoku = Sudoku.load(fileName);
				sudoku.printBoard(name);

				long startTime = System.currentTimeMillis();
				if (solver.solve(sudoku)) {
					long ms = System.currentTimeMillis() - startTime;
					sudoku.printBoard(name + " Solved");
					sudoku.saveSolution("Difficulty: " + sudoku.getDifficulty() + "\n"
							+ "Solved by: the computer\n"
							+ "Solved in: " + ms + " ms");
				}else {
					System.out.println(name + " has no solution\n");
				}
			} catch (Exception e) {
				System.out.println("Skipping " + fileName + ": " + e.getMessage() + "\n");
			}

			System.out.println("=========================================\n");
		}
	}
}
