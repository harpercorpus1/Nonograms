package nonogrampkg;

public final class Nonogram{
    public static void main(String[] args){
        // NonogramView screen = new NonogramView(15);
        // NonogramModel model = new NonogramModel(15);
        // NonogramController ctr = new NonogramController(model, screen);
        // screen.registerController(ctr);

        SolverView screen = new SolverView(15);
        SolverModel model = new SolverModel(15);
        SolverController ctr = new SolverController(model, screen);
        screen.registerController(ctr);
        ctr.draw_rocco();
    }
}