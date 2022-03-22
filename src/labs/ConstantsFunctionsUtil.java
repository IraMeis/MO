package labs;

public class ConstantsFunctionsUtil {

    public static double epsilon1 =1e-3;
    public static double phi = (1 + Math.sqrt(5));
    public static double epsilon2 =1e-3;
    public static int maxIter = 1000;

    public static FunctionN<Double,VectorN> fTest = arg ->{
        double val = 0;
        for (int i=0; i<arg.size();++i)
            val += (arg.get(i) - 2) * (arg.get(i) - 2);
        return val;
    };

    public static FunctionN<Double,VectorN> f1 = arg -> (arg.get(0)-2)*(arg.get(0)-2)*(arg.get(1)-2)*(arg.get(1)-2);
    public static FunctionN<Double,VectorN> f2 = arg -> (arg.get(0) - 5) * arg.get(0) + (arg.get(1) - 3) * arg.get(1);
}
