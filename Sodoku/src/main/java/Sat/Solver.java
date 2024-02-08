package Sat;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.*;
import java.io.*;

/**
 * a class that solves a cnf converted sudoku puzzle and outputs its solution
 */
public class Solver {
    ISolver satSolver = SolverFactory.newDefault();            //utilize sat4j solver
    Reader reader = new DimacsReader(satSolver);
    IProblem problem;

    /**
     * The contructor for the Solver class, solvers a  sudoku puzzle using the given formulator
     * @param formulator gives the solver the cnf formatted file to solve
     * @throws IOException
     * @throws ContradictionException
     * @throws ParseFormatException
     */
    public Solver(Formulator formulator) throws IOException, ContradictionException, ParseFormatException {
        satSolver.setTimeout(120); // 2 minute timeout
        formulator.readFile();
        File cnfFile = formulator.cnfConvert();
        try {
            problem = reader.parseInstance(String.valueOf(cnfFile));
            if (problem.isSatisfiable()) {
                System.out.println("Satisfiable !");
                System.out.println("output:");
                outPutFinishedSudoku(UpdateSudokuArray(formulator));
            } else {
                System.out.println("Unsatisfiable !");
            }

        } catch (FileNotFoundException e) {

        } catch (ParseFormatException e) {

        } catch (IOException e) {

        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");

        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");
        }

    }

    /**
     * Takes in a formulater to use it's sudoku array and fills in
     * all empty cells with their correct value
     * @param formulator the formulator to be used
     * @return the updated sudoku array
     */
    private int[][] UpdateSudokuArray(Formulator formulator) {
        int N = formulator.codeNum;
        int[] ans = problem.model();
        int[][] output = formulator.getSudukoInput();
        for (int i = 0; i < ans.length; i++) {
            int num = ans[i];
            if (num > 0) {
               int row = ((num / N) / N) % N;
               int col = (num / N) % N;
               int val = num % N;
               output[row-1][col-1] = val;
            }
        }
        return output;
    }

    /**
     * takes in an array and prints its contents
     * @param input a 2d array to be printed
     */
    private void outPutFinishedSudoku(int[][] input){
        for (int row = 0; row < input.length; row++) {
            for (int col = 0; col < input.length; col++) {
                System.out.print(input[row][col] + " ");
            }
            System.out.println("");
        }
        System.out.println("\n");
    }


}
