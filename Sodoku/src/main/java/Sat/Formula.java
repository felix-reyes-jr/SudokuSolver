package Sat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Felix Reyes
 * @date 2/27/22
 * a class that represents a boolean formula
 */
public class Formula {

    /**
     * list of clauses within the formula
     */
    private final List<Clause> clauseList = new ArrayList<>();

    /**
     * list of variable for the corresponding clause, used strictly for output
     */
    private List<Clause> clauseVarList = new ArrayList<>();

    /**
     * list of boolean assignments as 1(true) and 0(false) to be manipulated and used to check satisfiability
     */
    private List<Integer> assignmentList = new ArrayList<>();


    /**
     * adds a clause to the clauseList and the clauseVarList
     * @param c the clause to be added
     */
    public void addClause(Clause c) {
        clauseList.add(c);
        clauseVarList.add(c);
    }

    /**
     * adds a 0(false) to the assignmentList, starting the booleanList to all 0s
     */
    public void addVal(){
        assignmentList.add(0);
    }

    /**
     * runs through all possible assignment combinations until either satisfied or until all combinations are exhausted
     * @return false iff all combinations have been used
     * @return true iff all clauses have been satisfied
     */
    public boolean isSat() {
        while(!exhausted()) {
            for (int i = 0; i < clauseList.size(); i++) {
                if (!clauseList.get(i).isSat(assignmentList)) {
                    flipAssignment();
                    i = -1;
                    if(exhausted()){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * goes through the assignmentList and finds the right most 0, changes it to a 1, then switches every assignment after it
     */
    public void flipAssignment() {
        for(int i = assignmentList.size()-1; i >= 0; i--){
            if(assignmentList.get(i) == 0){
                assignmentList.set(i, 1);
                for(int j = i+1; j < assignmentList.size(); j++){
                    assignmentList.set(j, 0);
                }
                i = -1;
            }
        }
    }

    /**
     * checks to see if all possible combinations have been used
     * @return true iff the assignmentList is all 1s, meaning that every possible combination has been used
     * @return false iff at least one of the assignments in assignmentList is 0(false)
     */
    public boolean exhausted() {
        for(int i = 0; i<assignmentList.size();i++){
            int var = assignmentList.get(i);
            if(var == 0){
               return false;
            }
        }
        return true;
    }

    /**
     * a toString for the formula class
     * @return the formatted formula as a cnf as a String
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        String separator = "";
        for (Clause clause : clauseVarList) {
            sb.append(separator);
            separator = String.valueOf("\n");
            sb.append(clause);
            sb. append(" 0");
        }

        return sb.append("").toString();
    }
}
