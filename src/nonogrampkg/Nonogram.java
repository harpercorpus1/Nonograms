package nonogrampkg;

public final class Nonogram{
    public static void main(String[] args){

        SolverView screen = new SolverView(15);
        SolverModel model = new SolverModel(15);
        SolverController ctr = new SolverController(model, screen);
        screen.registerController(ctr);
        ctr.draw_rocco();
    }
}