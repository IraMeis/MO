package numbers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

//пока лучший вариант

/**
 * переводит double в дробное число вида [числитель, знаменатель] - fromDouble
 * или [целая часть, числитель, знаменатель] - fromDoubleWithIntPart
 *
 * в первом варианте знак числа определяется знаком числителя;
 * во втором - знаком целой части (при ее наличии, дробные части без знака, считается, что дробная часть имеет тот же знак, что и целая:
 *  -1, 1, 4 == -5/4) либо знаком числителя дроби
 */
public class NumberMapper {

    public static Number[] fromDouble(double value) {
        return toRational(BigDecimal.valueOf(value));
    }

    public static Number[] fromDoubleWithIntPart(double value) {
        return toRationalWithIntPart(BigDecimal.valueOf(value));
    }

    public static void fromDoublePrint (double value){
        System.out.println(Arrays.toString(fromDouble(value)));
    }

    public static void fromDoubleWithIntPartPrint (double value){
        System.out.println(Arrays.toString(fromDoubleWithIntPart(value)));
    }

    public static void fromDoublePrintMas (double[] values){
        for (var number : values)
            fromDoublePrint(number);
    }

    public static void fromDoubleWithIntPartPrintMas (double[] values){
        for (var number : values)
            fromDoubleWithIntPartPrint(number);
    }

    private static BigInteger[] toRational(BigDecimal value) {
        BigInteger numerator, denominator;

        // Zero is 0 / 1
        if (value.signum() == 0) {
            numerator = BigInteger.ZERO;
            denominator = BigInteger.ONE;
        }

        else {
            BigDecimal bd = value.stripTrailingZeros(); // 1.20 -> 1.2

            if (bd.scale() < 0)
                bd = bd.setScale(0); //1.7e3 -> 1700

            numerator = bd.unscaledValue(); // 1.25 -> 125
            denominator = BigDecimal.valueOf(1, -bd.scale()).toBigInteger(); // 1.25 -> 100

            // Normalize, 12/8 -> 3/2
            BigInteger gcd = numerator.gcd(denominator);
            if (! gcd.equals(BigInteger.ONE)) {
                numerator = numerator.divide(gcd);
                denominator = denominator.divide(gcd);
            }
        }
        return new BigInteger[]{ numerator, denominator };
    }

    private static BigDecimal[] separate (BigDecimal bigDecimal){
        BigDecimal intPart = new BigDecimal(bigDecimal.intValue());
        return new BigDecimal[]{ intPart, bigDecimal.subtract(intPart)};
    }

    private static BigInteger[] toRationalWithIntPart(BigDecimal value) {
        BigDecimal[] separated = separate(value);
        BigInteger[] drob = toRational(separated[0].compareTo(BigDecimal.ZERO) < 0 ?
                separated[1].multiply(BigDecimal.valueOf(-1)) : separated[1]);
        return new BigInteger[]{ separated[0].toBigInteger(), drob[0], drob[1] };
    }

}
