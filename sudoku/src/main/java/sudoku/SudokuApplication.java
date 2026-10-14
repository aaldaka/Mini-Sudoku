package sudoku;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sudoku.model.Sudoku;
import sudoku.solver.SudokuSolver;

@SpringBootApplication
public class SudokuApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SudokuApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Sudoku sudoku = Sudoku.load("puzzle1.txt");
		SudokuSolver solver = new SudokuSolver();
		if (solver.solve(sudoku)) {
			sudoku.printBoard("Puzzle 1 Solved");
			sudoku.saveSolution();
		} else {
			System.out.println("Puzzle 1 has no solution");
		}
	}
}