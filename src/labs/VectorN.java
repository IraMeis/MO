package labs;

import java.util.ArrayList;
import java.util.List;

public class VectorN {
    private List<Double> vector;

    private VectorN(){}

    VectorN(int N){
        vector=new ArrayList<Double>(N);
        for (int i=0; i<N;++i)
            vector.add(0.);
    }

    public static VectorN build(int N){
        return new VectorN(N);
    }

    public static VectorN build(VectorN v){
        return new VectorN(v);
    }

    VectorN(Double... args){
        vector = new ArrayList<Double>(List.of(args));
    }

    VectorN(VectorN v){
        vector = new ArrayList<Double>(v.vector);
    }

    public int size (){
        return vector.size();
    }

    public List<Double> get (){
        return vector;
    }

    public Double get (int i){
        return vector.get(i);
    }

    void set (int i, Double x){
        vector.set(i,x);
    }

    @Override
    public int hashCode() {
        return vector.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return vector.equals(obj);
    }

    @Override
    public String toString() {
        return vector.toString();
    }

    public VectorN plus(VectorN b) {
        if (size() != b.size())
            throw new RuntimeException("collision in vector size");

        for (int i = 0; i < size(); i++)
            set(i, get(i) + b.get(i));

        return this;
    }

    public static VectorN plus(VectorN a, VectorN b) {
        return new VectorN(a).plus(b);
    }

    public static VectorN plus(VectorN a, Double x) {
        VectorN res = new VectorN(a);

        for (int i = 0; i < a.size(); i++)
            res.set(i, res.get(i) + x);

        return res;
    }

    /**
     *
     * @param a
     * @param b
     * @return a-b
     */
    public static VectorN minus(VectorN a, VectorN b) {
        if (a.size() != b.size())
            throw new RuntimeException("collision in vector size");

        VectorN res = new VectorN(a);

        for (int i = 0; i < a.size(); i++)
            res.set(i, res.get(i) - b.get(i));

        return res;
    }

    public static VectorN minus(VectorN a, Double x) {
        VectorN res = new VectorN(a);

        for (int i = 0; i < a.size(); i++)
            res.set(i, res.get(i) - x);

        return res;
    }

    public static VectorN mul(VectorN a, Double x) {
        VectorN res = new VectorN(a);

        for (int i = 0; i < a.size(); i++)
            res.set(i, res.get(i) * x);

        return res;
    }

    public static double dot(VectorN a, VectorN b) {
        if (a.size() != b.size())
            throw new RuntimeException("collision in vector size");

        double res = 0.0;

        for (int i = 0; i < a.size(); i++)
            res += a.get(i) * b.get(i);

        return res;
    }
    public double magnitude() {
        double mag = 0;
        for (int i = 0; i < size(); i++)
            mag += get(i) * get(i);

        return Math.sqrt(mag);
    }

    public static double magnitude(VectorN a) {
        double mag = 0;
        for (int i = 0; i < a.size(); i++)
            mag += a.get(i) * a.get(i);

        return Math.sqrt(mag);
    }

    public VectorN normalize() {
        double mag = 1.0 / magnitude();
        for (int i = 0; i < size(); i++)
            set(i, get(i) * mag);
        return this;
    }

    /**
     *
     * @return
     */
    public VectorN normalized() {
        VectorN copy = new VectorN(this);
        double mag = 1.0 / copy.magnitude();
        for (int i = 0; i < copy.size(); i++)
            copy.set(i, copy.get(i) * mag);
        return copy;
    }

    public static VectorN normalize(VectorN vec) {
        double mag = 1.0 / magnitude(vec);
        for (int i = 0; i < vec.size(); i++)
            vec.set(i, vec.get(i) * mag);

        return vec;
    }

    public static VectorN direction(VectorN a, VectorN b) {
        return normalize(minus(b, a));
    }

    public static VectorN gradient(FunctionN<Double, VectorN> fun, VectorN x, double eps) {
        VectorN df = new VectorN(x.size());
        VectorN x_l = new VectorN(x);
        VectorN x_r = new VectorN(x);

        for (int i = 0; i < x.size(); i++) {
            x_l.set(i, x_l.get(i) - eps);
            x_r.set(i, x_r.get(i) + eps);

            df.set(i, (fun.perform(x_r) - fun.perform(x_l)) / (2 * eps));

            x_l.set(i, x_l.get(i) + eps);
            x_r.set(i, x_r.get(i) - eps);
        }
        return df;
    }

    public static double partial (FunctionN<Double, VectorN> func, VectorN x, int coord_index, double eps) {

        if (x.size() <= coord_index)
            throw new RuntimeException("Partial derivative index out of bounds!");

        x.set(coord_index, x.get(coord_index)+ eps);
        double f_r = func.perform(x);

        x.set(coord_index, x.get(coord_index)- 2 * eps);
        double f_l = func.perform(x);

        x.set(coord_index, x.get(coord_index)+ eps);

        return (f_r - f_l) / eps * 0.5;
    }

    public static double partial2(FunctionN<Double, VectorN> func, VectorN x, int coord_index_1, int coord_index_2, double eps) {

        if (x.size() <= coord_index_2)
            throw new RuntimeException("Partial derivative index out of bounds!");

        x.set(coord_index_2, x.get(coord_index_2) - eps);
        double f_l = partial(func, x, coord_index_1, eps);

        x.set(coord_index_2, x.get(coord_index_2) + 2 * eps);
        double f_r = partial(func, x, coord_index_1, eps);

        x.set(coord_index_2, x.get(coord_index_2) - eps);

        return (f_r - f_l) / eps * 0.5;
    }
}
