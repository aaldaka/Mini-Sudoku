package sudoku;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sudoku.model.Sudoku;
import sudoku.solver.SudokuSolver;
import sudoku.utils.SudokuFileUtils;

@SpringBootApplication
public class SudokuApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SudokuApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
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