package lab1;

public class MainPoint {
    public static void main(String[] args){
        FunctionProvider functionProvider = new FunctionProvider();

        System.out.println(functionProvider.dihotomia(x->x*x+x+1,-10,10,0.0000001)+"\n\n");
        System.out.println(functionProvider.goldenSection(x->x*x+x+1,-10,10,0.0000001)+"\n\n");
        System.out.println(functionProvider.fibMethod(x->x*x+x+1,-10,10,40)+"\n\n");
    }
}
