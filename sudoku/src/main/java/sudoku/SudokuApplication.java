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
	public void run(String... args) throws Exception {
		Scanner input = new Scanner(System.in);

		System.out.println("1 = Solve all puzzle files");
		System.out.println("2 = Play");
		String choice = input.nextLine().trim();

		if (choice.equals("1")) {
			solveAllFiles();
		} else if (choice.equals("2")) {
			new SudokuGame(input).start();
		} else {
			System.out.println("Unknown choice, please type 1 or 2");
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

				if (solver.solve(sudoku)) {
					sudoku.printBoard(name + " Solved");
					sudoku.saveSolution();
				} else {
					System.out.println(name + " has no solution\n");
				}
			} catch (Exception e) {
				System.out.println("Skipping " + fileName + ": " + e.getMessage() + "\n");
			}

			System.out.println("=========================================\n");
		}
	}
}
