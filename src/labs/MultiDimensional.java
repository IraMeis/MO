package labs;

public class MultiDimensional {

    private static VectorN dihotomia(FunctionN<Double,VectorN> f, VectorN x_0, VectorN x_1, double eps, int max_iters) {

        VectorN x_0_ = new VectorN(x_0);
        VectorN x_1_ = new VectorN(x_1);
        VectorN x_c = new VectorN(x_0.size());
        VectorN dir;

        dir = VectorN.direction(x_0, x_1);

        int cntr = 0;

        for (; cntr != max_iters; cntr++) {
            if (VectorN.magnitude(VectorN.minus(x_1_, x_0_)) < eps)
                break;

            x_c = VectorN.mul(VectorN.plus(x_1_, x_0_), 0.5);

            if (f.perform(VectorN.plus(x_c, VectorN.mul(dir , eps))) > f.perform(VectorN.minus(x_c, VectorN.mul(dir , eps)))) {
                x_1_ = x_c;
                continue;
            }

            x_0_ = x_c;
        }
        return x_c;
    }

    public static VectorN goldenSection(FunctionN<Double,VectorN> f, VectorN x_0, VectorN x_1, double eps, int max_iters) {

        VectorN a = new VectorN(x_0);
        VectorN b = new VectorN(x_1);
        VectorN dx;

        int cntr = 0;
        double one_div_phi = 1.0 / ConstantsFunctionsUtil.phi;

        for (; cntr != max_iters; cntr++) {

            if (VectorN.magnitude(VectorN.minus(x_1, x_0)) < eps)
                break;

            dx = VectorN.mul(VectorN.minus(b, a), one_div_phi);
            x_0 = VectorN.minus(b, dx);
            x_1 = VectorN.plus(a, dx);

            if (f.perform(x_0) > f.perform(x_1)) {
                b = x_0;
                continue;
            }

            a = x_1;
        }

        return VectorN.mul(VectorN.plus(b, a), 0.5);
    }

    public static VectorN fibMethod(FunctionN<Double,VectorN> f, VectorN x_0, VectorN x_1, double eps) {

        VectorN a = new VectorN(x_0);
        VectorN b = new VectorN(x_1);
        VectorN x_0_ = new VectorN(a);
        VectorN x_1_ = new VectorN(b);
        VectorN dx;

        double f_1 = 1.0, f_2 = 2.0, f_3 = 3.0;
        int cntr = 0;

        int max_iters = OneDimensional.closestFibonacci(VectorN.magnitude(VectorN.minus(x_1_, x_0_)) / eps);

        for (; cntr != max_iters; cntr++) {
            if (VectorN.magnitude(VectorN.minus(x_1_, x_0_)) < eps)
                break;

            dx   = VectorN.minus(b, a);
            x_0_ = VectorN.minus(b,  VectorN.mul(dx, (f_1 / f_3)));
            x_1_ = VectorN.plus(b,  VectorN.mul(dx, (f_2 / f_3)));

            f_1 = f_2;
            f_2 = f_3;
            f_3 = f_1 + f_2;

            if (f.perform(x_0_) < f.perform(x_1_)) {
                b = x_0_;
                continue;
            }
            a = x_1_;
        }

        return VectorN.mul(VectorN.plus(x_1_, x_0_), 0.5);
    }

    public static VectorN perCoordDescend(FunctionN<Double,VectorN> f, VectorN x_start, double eps, int max_iters) {
        int cntr = 0;

        VectorN x_0 = new VectorN(x_start);
        VectorN x_1 = new VectorN(x_start);

        double step = 1.0;
        double x_i;
        int opt_coord_n = 0;
        double y_1, y_0;

        while (true) {

            for (int i = 0; i < x_0.size(); i++) {
                cntr++;
                if (cntr == max_iters)
                    return x_0;

                x_1.set(i, x_1.get(i)-eps);
                y_0 = f.perform(x_1);

                x_1.set(i, x_1.get(i)+2*eps);
                y_1 = f.perform(x_1);

                x_1.set(i, x_1.get(i)-eps);

                x_1.set(i, y_0 > y_1 ? x_1.get(i)+step :x_1.get(i)-step);

                x_i = x_0.get(i);

                x_1 = dihotomia(f, x_0, x_1, eps, max_iters);

                x_0 = VectorN.build(x_1);

                if (Math.abs(x_1.get().get(i) - x_i) < eps) {
                    opt_coord_n++;

                    if (opt_coord_n == x_1.size())
                        return x_0;

                    continue;
                }
                opt_coord_n = 0;
            }
        }
    }

    public static VectorN gradientDescend(FunctionN<Double,VectorN> f, VectorN x_start, double eps, int max_iters) {

        VectorN x_i   = new VectorN(x_start);
        VectorN x_i_1 = new VectorN(x_start.size()), grad;

        int cntr = 0;

        while (true) {
            cntr++;
            if (cntr == max_iters)
                break;

            grad  = VectorN.gradient(f, x_i, eps);
            x_i_1 = VectorN.minus(x_i, grad);
            x_i_1 = dihotomia(f, x_i, x_i_1, eps, max_iters);

            if (VectorN.magnitude(VectorN.minus(x_i_1, x_i)) < eps)
                break;

            x_i = x_i_1;
        }
        return VectorN.mul(VectorN.plus(x_i_1, x_i), 0.5);
    }

    public static VectorN conjGradientDescend(FunctionN<Double,VectorN> f, VectorN x_start, double eps, int max_iters) {

        VectorN x_i = new VectorN(x_start);
        VectorN x_i_1 = new VectorN(x_start.size());
        VectorN s_i = VectorN.mul(VectorN.gradient(f, x_start, eps),(-1.0)), s_i_1;

        double omega;
        int cntr = 0;
        while (true) {
            cntr++;

            if (cntr == max_iters)
                break;

            x_i_1 = VectorN.plus(x_i, s_i);
            x_i_1 = dihotomia(f, x_i, x_i_1, eps, max_iters);

            if (VectorN.magnitude(VectorN.minus(x_i_1, x_i)) < eps)
                break;

            s_i_1 = VectorN.gradient(f, x_i_1, eps);

            omega = Math.pow(VectorN.magnitude(s_i_1), 2) / Math.pow(VectorN.magnitude(s_i), 2);

            s_i=VectorN.minus(VectorN.mul(s_i, omega),s_i_1);

            x_i = VectorN.build(x_i_1);
        }
        return VectorN.mul(VectorN.plus(x_i_1, x_i), 0.5);
    }

    public static VectorN newtoneRaphson(FunctionN<Double,VectorN> f, VectorN x_start, double eps, int max_iters) {

        VectorN x_i = new VectorN(x_start);
        VectorN x_i_1 = new VectorN(x_start.size()), grad;
        MatrMN hess;
        int cntr = 0;

        while (true) {
            cntr++;
            if (cntr == max_iters)
                break;

            grad = VectorN.gradient(f, x_i, eps);

            hess = MatrMN.invert(MatrMN.hessian(f, x_i, eps));

            x_i_1 = VectorN.minus(x_i , MatrMN.mul(hess , grad));

            if (VectorN.magnitude(VectorN.minus(x_i_1, x_i)) < eps)
                break;
            x_i = VectorN.build(x_i_1);
        }
        return VectorN.mul(VectorN.plus(x_i_1, x_i), 0.5);
    }

    public static void lab2(FunctionN<Double,VectorN> f) {
        System.out.println("\n////////////////////\n");
        System.out.println( "/// Lab. work #2 ///\n");
        System.out.println("////////////////////\n\n");

        VectorN x_1 = new VectorN( 0., 0. );
        VectorN x_0 = new VectorN( 5., 5. );
        VectorN x__ = new VectorN( -2., 15. );
        System.out.println("x_0 = " + x_0 + ", x_1 = " + x_1 + "\n");
        System.out.println("dihotomia              : " +
                MultiDimensional.dihotomia(f, x_1, x_0, ConstantsFunctionsUtil.epsilon2, ConstantsFunctionsUtil.maxIter));
        System.out.println("goldenSection          : " +
                MultiDimensional.goldenSection(f, x_0, x_1, ConstantsFunctionsUtil.epsilon2, ConstantsFunctionsUtil.maxIter));
        System.out.println("fibonacci              : " +
                MultiDimensional.fibMethod(f, x_1, x_0, ConstantsFunctionsUtil.epsilon2));
        System.out.println("PerCoordDescend        : " +
                MultiDimensional.perCoordDescend(f, x__, ConstantsFunctionsUtil.epsilon2, ConstantsFunctionsUtil.maxIter));
    }

    public static void lab3(FunctionN<Double,VectorN> f) {

        System.out.println("\n////////////////////\n");
        System.out.println( "/// Lab. work #3 ///\n");
        System.out.println("////////////////////\n\n");

        VectorN x_1 = new VectorN( 0.0, 0.0 );

        System.out.println("GradientDescend        : " +
                MultiDimensional.gradientDescend(f, x_1, ConstantsFunctionsUtil.epsilon2, ConstantsFunctionsUtil.maxIter));
        System.out.println("СonjGradientDescend    : " +
                MultiDimensional.conjGradientDescend(f, x_1, ConstantsFunctionsUtil.epsilon2, ConstantsFunctionsUtil.maxIter));

    }

    public static void lab4(FunctionN<Double,VectorN> f) {

        System.out.println("\n////////////////////\n");
        System.out.println( "/// Lab. work #4 ///\n");
        System.out.println("////////////////////\n\n");

        VectorN x_1 = new VectorN( 0.0, 0.0);

        System.out.println("NewtoneRaphson         : " +
                MultiDimensional.newtoneRaphson(f, x_1, ConstantsFunctionsUtil.epsilon2, ConstantsFunctionsUtil.maxIter));
    }
}