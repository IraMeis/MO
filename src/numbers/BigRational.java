package numbers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

//числа переводит, но результат удобочитаемее не становится
public class BigRational {

    private BigInteger chislitel;
    private BigInteger znamenatel;

    private final static int mantissaBits=53;

    public BigRational(BigInteger chislitel, BigInteger znamenatel){
        this.chislitel=chislitel;
        this.znamenatel=znamenatel;
    }

    @Override
    public String toString() {
        return "BigRational { " +
                "chislitel = " + chislitel +
                ", znamenatel = " + znamenatel +
                " }";
    }

    public static BigRational fromDouble(double num){
        int exponent=Math.getExponent(num);
        long man = Math.round(Math.scalb(num, mantissaBits-exponent));
        long den = Math.round(Math.scalb(1.0, mantissaBits-exponent));
        return new BigRational(BigInteger.valueOf(man),BigInteger.valueOf(den));
    }

    public static List<BigRational> fromDoubleMas(double[] num){
        List<BigRational> res = new ArrayList<>();
        for (var number : num)
            res.add(fromDouble(number));
        return res;
    }

    public static void fromDoublePrint(double num){
        int exponent=Math.getExponent(num);
        long man = Math.round(Math.scalb(num, mantissaBits-exponent));
        long den = Math.round(Math.scalb(1.0, mantissaBits-exponent));
        System.out.println(new BigRational(BigInteger.valueOf(man),BigInteger.valueOf(den)));
    }

    public static void fromDoublePrintMas(double[] num){
        for (var number : num)
            fromDoublePrint(number);
    }
}
