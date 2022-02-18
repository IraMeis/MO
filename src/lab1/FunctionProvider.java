package lab1;

import java.util.function.Function;
import java.util.stream.Stream;

public class FunctionProvider {

    private static final double fi1=(1+Math.sqrt(5))/2;

    /**
     * считает число фибоначчи с заданным номером
     * @param num номер числа фибоначчи
     * @return
     */
    public static int getFib(int num) {
        if (num<1) throw new RuntimeException("wrong fib number");
        if (num<3) return 1;
        return Stream.iterate(new int[]{1, 1}, arr -> new int[]{arr[1], arr[0] + arr[1]})
                .skip(num-1)
                .limit(num)
                //.map(y -> y[1])
                .mapToInt(y -> y[0])
                .toArray()[0];
    }

    /**
     * массив чисел фибоначчи до n-ого включиельно
     * @param num номер последнего числа фибоначчи
     * @return
     */
    public static int[] getFibs(int num) {
        if (num<1) throw new RuntimeException("wrong fib number");
        if (num==1) return new int[]{1};
        if (num==2) return new int[]{1,1};
        int[] res = new int[num];
        res[0]=res[1]=1;
        for(int i=2;i<num;++i){
            res[i]=res[i-1]+res[i-2];
        }
        return res;
    }

    /**
     * метод золотого сечениня
     * @param f функция
     * @param x0 a
     * @param x1 b
     * @param eps точность
     * @return
     */
    public static double goldenSection(Function<Double,Double> f, double x0, double x1, double eps){
        System.out.println("goldenSection started");

        double y1, y2, temp0, temp1;
        int i=1;
        while (true){
            temp0=x0+(x1-x0)/fi1;
            temp1=x1-(x1-x0)/fi1;
            y1=f.apply(temp0);
            y2=f.apply(temp1);

            if(y1>y2) x1=temp0;
            else x0=temp1;

            ++i;

            if(Math.abs(x1-x0)<2*eps){
                System.out.println("iteration "+i);
                System.out.println("goldenSection ended");
                return (x1+x0)/2;
            }
        }
    }

    /**
     * метод чисел фибоначчи v1
     * @param f функция
     * @param x0 a
     * @param x1 b
     * @param iter число итераций
     * @return
     */
    public static double fibMethodV1(Function<Double,Double> f, double x0, double x1, int iter){
        System.out.println("fibMethodV1 started\nwith defined iteration number " +iter);

        double y1, y2,
                temp0 = x0+(x1-x0)*(getFib(iter-2)/(double)getFib(iter)),
                temp1 = x0+(x1-x0)*(getFib(iter-1)/(double)getFib(iter));
        y1=f.apply(temp0);
        y2=f.apply(temp1);
        for(int n=iter; n>0;--n){
            if(y1>y2) {
                x0=temp0;
                temp0=temp1;
                temp1=x1-(temp0-x0);
                y1=y2;
                y2=f.apply(temp1);
            }
            else {
                x1=temp1;
                temp1=temp0;
                temp0=x0+(x1-temp1);
                y2=y1;
                y1=f.apply(temp0);
            }

        }
        System.out.println("fibMethodV1 ended");
        return (x1+x0)/2;
    }

    /**
     * метод чисел фибоначчи v2
     * @param f
     * @param x0
     * @param x1
     * @param iter
     * @return
     */
    public static double fibMethodV2(Function<Double,Double> f, double x0, double x1, int iter){
        System.out.println("fibMethodV2 started\nwith defined iteration number " +iter);

        int[] fibs = getFibs(iter);

        double temp0, temp1, y1, y2;
        --iter;
        temp0 = x0+(x1-x0)*(fibs[iter-2]/(double)fibs[iter]);
        temp1 = x0+(x1-x0)*(fibs[iter-1]/(double)fibs[iter]);

        System.out.println("accuracy " +(x1-x0)/fibs[iter]);

        for(int n=1; n<iter;++n) {
            y1 = f.apply(temp0);
            y2 = f.apply(temp1);
            if (y1 < y2) {
                x1 = temp1;
                temp1 = temp0;
                temp0 = x0 + (x1 - x0) * (fibs[iter - 2 - n] / (double) fibs[iter - n]);
            } else {
                x0 = temp0;
                temp0 = temp1;
                temp1 = x0 + (x1 - x0) * (fibs[iter - 1 - n] / (double) fibs[iter - n]);
            }
        }

        System.out.println("fibMethodV2 ended");
        return (x1+x0)/2;
    }

    /**
     * метод дихотоми
     * @param f функция
     * @param x0 a
     * @param x1 b
     * @param eps точность
     * @return
     */
    public static double dihotomia (Function<Double,Double> f, double x0, double x1, double eps){
        System.out.println("dihotomia started");

        double y1, y2, temp0, temp1;
        int i=1;
        while (true){
            temp0=(x0+x1)/2 - eps;
            temp1=(x0+x1)/2 + eps;
            y1=f.apply(temp0);
            y2=f.apply(temp1);

            if(y1<y2) x1=temp0;
            else x0=temp1;

            ++i;

            if(Math.abs(x1-x0)<2*eps){
                System.out.println("iteration "+i);
                System.out.println("dihotomia ended");
                return (x1+x0)/2;
            }
        }
    }
}
