package numbers;

import java.util.Arrays;

public class NumbersApp {

    private static double[] testMas = new double[]{
            Math.PI,
            Math.E,
            1.25,
            1,
            0,
            -1.25,
            1.25e9,
            1.25e-9,
            0.5,
            -0.6667,
            5.34,
            -1.9999999999
    };

    public static void main (String[] args){

        System.out.println("BigRational variant");
        BigRational.fromDoublePrintMas(testMas);

        System.out.println("\nchislitel and znamenatel");
        NumberMapper.fromDoublePrintMas(testMas);

        System.out.println("\ninteger part, chislitel and znamenatel");
        NumberMapper.fromDoubleWithIntPartPrintMas(testMas);

        System.out.println("\ntest data");
        System.out.println(Arrays.toString(testMas));
    }

}
