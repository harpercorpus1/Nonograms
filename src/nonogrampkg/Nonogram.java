/*****************************************
 * Harper Corpus 03.03.2022
 * This is the file containing the main 
 * function for this Java App. Very short 
 * and simple. 
 ****************************************/

 package nonogrampkg;

public final class Nonogram{
    public static void main(String[] args){

        SolverView screen = new SolverView(15);
        SolverModel model = new SolverModel(15);
        SolverController ctr = new SolverController(model, screen);
        screen.registerController(ctr);
        if(args.length == 1){
            ctr.load_tester(args[0]);
        }else if(args.length > 1){
            System.out.println("Usage [./Nonogram.sh [optional tester command]");
            return;
        }
    }
}