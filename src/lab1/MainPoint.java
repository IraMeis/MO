package lab1;

public class MainPoint {
    public static void main(String[] args){

        System.out.println(FunctionProvider.dihotomia(x->x*x+x+1,-10,10,1e-7)+"\n\n");
        System.out.println(FunctionProvider.goldenSection(x->x*x+x+1,-10,10,1e-7)+"\n\n");
        System.out.println(FunctionProvider.fibMethodV1(x->x*x+x+1,-10,10,40)+"\n\n");
        System.out.println(FunctionProvider.fibMethodV2(x->x*x+x+1,-10,10,40)+"\n\n");
    }
}
