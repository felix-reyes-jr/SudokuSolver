package Sat;

import java.io.*;
import java.util.Scanner;

/**
 * @author Felix Reyes
 * @date 2/27/22
 * a class that reads a sudoku puzzle, converts it into a boolean formula and a 2d array
 * formats the formula as a cnf file to be used
 */
public class Formulator {

    /**
     * the file to be read and tested
     */
    File file;

    /**
     * the formula of the new file
     */
    Formula formula;

    /**
     * the read sudoku file input put into a 2d array
     */
    int[][] sudukoInput;

    /**
     * number of clauses for the formula
     */
    int numClauses = 0;

    /**
     * number of variables within the formula
     */
    int numVars;

    /**
     * used for decoding
     */
    int codeNum;

    /**
     * the constructor for the Formulator class
     * @param file to be read and tested
     */
    public Formulator(File file){
        this.file = file;
    }

    /**
     * the reader for the Formulator class, reads in a sudoku file, adds each value(including zeros) to an 2d array
     * then creates the clauses needed.
     * @throws FileNotFoundException
     * @return a cnf file based on created clauses
     */
    public Formula readFile() throws FileNotFoundException {
        formula = new Formula();
        int arrayRow = 0;
        int arrayCol = 0;
        int row = arrayRow + 1;
        int col = 1;
        int n = 0;
        Scanner sc = new Scanner(file);
        arrayRow = 0;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] values = line.split(" ");
            if (values.length == 1) {
                n = Integer.parseInt(values[0]);
                sudukoInput = new int[n*n][n*n];
                numVars = encodCoord(n*n, n*n, n*n); //for the number of encoded variables we are generating
                codeNum = n*n + 1;
            }
            else {
                arrayCol = 0;
                col = arrayCol + 1;
                for (String v : values) {
                    int num = Integer.parseInt(v);
                    sudukoInput[arrayRow][arrayCol] = num;
                    if (num != 0) {
                        predifinedValue(row, col, num);
                    }
                    arrayCol++;
                    col++;
                }
                arrayRow++;
                row++;
            }
        }
        clauseBuilder(n);
       // System.out.println("Number of variables: " + numVars);   //not needed but nice to see
       // System.out.println("Number of clauses: " + numClauses);  //not needed but nice to see
        return formula;
    }

    /**
     * creates a clauses for a predetermined value at a specified row and column
     * @param row the row the value belongs to
     * @param col the column the value belongs to
     * @param val the predetermined value
     */
    private void predifinedValue(int row, int col, int val){
                Clause clause = new Clause();
                int varNum = encodCoord(row, col, val);
                clause.addVariable(varNum);
                formula.addClause(clause);
                numClauses++;
    }

    /**
     * Creates all clause for the read sudoku input
     * @param n the length and height of each sub square in the sudoku
     */
    private void clauseBuilder(int n) {
        cellClause(n);
        valueDependantClauses(n);
    }

    /**
     * create clauses for the constraint: the cell at (row,col) has at (least/most) one value
     * @param n the length and height of each sub square in the sudoku
     */
    private void cellClause(int n){
        Clause c;
        Clause c2;
        int varNum;
        int varNum2;
        for (int row = 1; row <= n * n; row++) { // r runs over 1,...,n
            for (int col = 1; col <= n * n; col++) {
                // The cell at (row,col) has at least one value
                c = new Clause();
                for (int v = 1; v <= n * n; v++) {
                    varNum = encodCoord(row, col, v);
                    c.addVariable(varNum);
                }
                formula.addClause(c);
                numClauses++;
                // The cell at (row,col) has at most one value
            for (int v = 1; v <= n * n; v++) {
                for (int w = v + 1; w <= n * n; w++) {
                    c2 = new Clause();
                    varNum = encodCoord(row, col, v);
                    varNum2 = encodCoord(row, col, w);
                    varNum = varNum * -1;
                    varNum2 = varNum2 * -1;
                    c2.addVariable(varNum);
                    c2.addVariable(varNum2);
                    formula.addClause(c2);
                    numClauses++;
                }
            }
            }
        }
    }

    /**
     * create clauses for the rows, columns, and squares constraints that depend on a value
     * @param n the length and height of each sub square in the sudoku
     */
    private void valueDependantClauses(int n) {
        int N = n*n;
        for (int v = 1; v <= N; v++) {
            // Each row has the value v
            rowHasEach(v, N);
            // Each column has the value v
            colHasEach(v, N);
            //Each square has the value v
            sqrHasEach(v, n);
            //at most a row has only one of each value
            atMostRow(v, N);
            //at most a column has only one of each value
            atMostCol(v,N);
            //at most a square has only one of each value
            atMostSqr(v,n);
        }
    }

    /**
     * create clauses for the constraint: Each row has the value v
     * @param v the value needed
     * @param N number of cell in a row and colum
     */
    private void rowHasEach(int v, int N){
        // Each row has the value v
        for (int row = 1; row <= N; row++) {
            Clause rowClause = new Clause();
            for (int col = 1; col <= N; col++) {
                int r = row;
                int cl = col;
                int varNum = encodCoord(r, cl, v);
                rowClause.addVariable(varNum);
            }
            formula.addClause(rowClause);
            numClauses++;
        }
    }

    /**
     * create clauses for the constraint: Each column has the value v
     * @param v the value needed
     * @param N number of cell in a row and colum
     */
    private void colHasEach(int v, int N){
        // Each column has the value v
        for (int col = 1; col <= N; col++) {
            Clause colClause = new Clause();
            for (int row = 1; row <= N; row++) {
                int r = row;
                int cl = col;
                int varNum = encodCoord(r, cl, v);
                colClause.addVariable(varNum);
            }
            formula.addClause(colClause);
            numClauses++;
        }
    }

    /**
     * create clauses for the constraint: Each square has the value v
     * @param v the value needed
     * @param n number of cell in a square row or column
     */
    private void sqrHasEach(int v, int n){
        //Each square has the value v
        for (int sqrw = 0; sqrw < n; sqrw++) {
            for (int sqcol = 0; sqcol < n; sqcol++) {
                Clause sqrClause = new Clause();
                for (int rwd = 1; rwd <= n; rwd++) {
                    for (int cold = 1; cold <= n; cold++) {
                        int rw = sqrw * n + rwd;
                        int cl = sqcol * n + cold;
                        int varNum = encodCoord(rw, cl, v);
                        sqrClause.addVariable(varNum);
                    }
                }
                formula.addClause(sqrClause);
                numClauses++;
            }
        }
    }

    /**
     * create clauses for the constraint: at most a row has only one of each value
     * @param v the value needed
     * @param N number of cell in a row and colum
     */
    private void atMostRow(int v, int N){
        //at most a row has only one of each value
        for (int rw = 1; rw <= N; rw++) {
            for (int mainCl = 1; mainCl <= N; mainCl++) { //loops though each column
                for (int cl = mainCl + 1; cl <= N; cl++) { //loops thought each column passed the main column
                    Clause rowClause = new Clause();
                    int varNum = encodCoord(rw, mainCl, v);
                    int varNum2 = encodCoord(rw, cl, v);
                    varNum = varNum * -1;
                    varNum2 = varNum2 * -1;
                    rowClause.addVariable(varNum);
                    rowClause.addVariable(varNum2);
                    formula.addClause(rowClause);
                    numClauses++;
                }
            }
        }
    }

    /**
     * create clauses for the constraint: at most a column has only one of each value
     * @param v the value needed
     * @param N number of cell in a row and colum
     */
    private void atMostCol(int v, int N){
        //at most a column has only one of each value
        for (int cl = 1; cl <= N; cl++) {
            for (int mainRw = 1; mainRw <= N; mainRw++) { //loops though each row
                for (int rw = mainRw + 1; rw <= N; rw++) { //loops though each row passed the main row
                    Clause colClause = new Clause();
                    int varNum = encodCoord(mainRw, cl, v);
                    int varNum2 = encodCoord(rw, cl, v);
                    varNum = varNum * -1;
                    varNum2 = varNum2 * -1;
                    colClause.addVariable(varNum);
                    colClause.addVariable(varNum2);
                    formula.addClause(colClause);
                    numClauses++;
                }
            }
        }
    }

    /**
     * create clauses for the constraint: at most a square has only one of each value
     * @param v the value needed
     * @param n number of cell in a square row or column
     */
    private void atMostSqr(int v, int n){
        //at most a square has only one of each value
        for (int sqrw = 0; sqrw < n; sqrw++) {
            for (int sqcol = 0; sqcol < n; sqcol++) {
                Clause sqrClause = new Clause();
                for (int mainrwd = 1; mainrwd <= n; mainrwd++) {
                    for (int mainCold = 1; mainCold <= n; mainCold++) {
                        int startCold = mainCold;
                        for (int rwd = mainrwd; rwd <= n; rwd++) {
                            for (int cold = startCold + 1; cold <= n; cold++) {
                                int mainrw = sqrw * n + mainrwd;
                                int rw = sqrw * n + rwd;
                                int mainCl = sqcol * n + mainCold;
                                int cl = sqcol * n + cold;
                                int varNum = encodCoord(mainrw, mainCl, v);
                                int varNum2 = encodCoord(rw, cl, v);
                                varNum = varNum * -1;
                                varNum2 = varNum2 * -1;
                                sqrClause.addVariable(varNum);
                                sqrClause.addVariable(varNum2);
                                formula.addClause(sqrClause);
                                numClauses++;
                                sqrClause = new Clause();
                            }
                            startCold = 0;
                        }
                    }
                }
            }
        }
    }

    /**
     * encodes the information given and produces an ecoded variable number
     * that when decoded will give the row and column for a value in the sudoku puzzle
     * @param row the row given to be encoded
     * @param col the column given to be encoded
     * @param val the value needed
     * @return the encoded variable number
     */
    private int encodCoord(int row, int col, int val){
        int eRow = row*codeNum*codeNum;
        int eCol = col*codeNum;
        return eRow+eCol+val;
    }

    /***
     * produces the formulators sudoku file as a 2d array
     * @return sudukoInput the array to be produced
     */
    public int[][] getSudukoInput(){
        return sudukoInput;
    }

    /**
     * uses the formula of the sudoku and the number of variables and clauses
     * to create a cnf formatted file
     * @return the cnf formatted file of the formula
     * @throws IOException
     */
    public File cnfConvert() throws IOException {
                File tempFile = File.createTempFile("CNFTemp", null);
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(tempFile, true)));
                writer.println("p cnf "+ numVars + " " + numClauses);
                writer.print(formula.toString());
                writer.close();
                return tempFile;
            }

}
