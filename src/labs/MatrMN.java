package labs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatrMN {
    private List<VectorN> vv;

    @Override
    public String toString() {
        if(this.size()==0)
            return null;

        StringBuilder builder =new StringBuilder();
        for (int i =0;i<size();++i) {
            builder.append(get(i).toString());
            builder.append("\n");
        }

        return builder.substring(0, builder.toString().length()-1);
    }

    public MatrMN (int rows, int cols){
        vv=new ArrayList<>(rows);
        for (int i=0; i<rows; ++i)
            vv.add(new VectorN(cols));

    }

    public MatrMN (int rows){
        vv=new ArrayList<>(rows);
        for (int i=0; i<rows; ++i)
            vv.add(new VectorN());

    }

    public MatrMN (VectorN ... args){
        vv=new ArrayList<>();
        vv.addAll(Arrays.asList(args));
    }

    public MatrMN (MatrMN m){
        m.vv=new ArrayList<>(get());
    }

    public static MatrMN build(int rows, int cols){
        return new MatrMN(rows,cols);
    }

    public static MatrMN build(int i){
        return new MatrMN(i,i);
    }

    public int size (){
        return vv.size();
    }

    public List<VectorN> get (){
        return vv;
    }

    public Double get(int i1, int i2){
        return vv.get(i1).get(i2);
    }

    public VectorN get(int i1){
        return vv.get(i1);
    }

    public VectorN set(int i1, VectorN x){
        return vv.set(i1, x);
    }

    public void set(int i1, int i2, Double x){
        vv.get(i1).set(i2, x);
    }

    private static MatrMN zeros(int rows, int cols) {
        return new MatrMN (rows, cols);
    }

    private static MatrMN zeros(int size) {
        return new MatrMN(size, size);
    }

    public static int[] size(MatrMN mat) {
        if (mat.size() == 0)
            return new int[]{mat.size(), -1};

        return new int[]{ mat.size(), mat.get(0).size() };
    }

    public static MatrMN hessian(FunctionN<Double,VectorN> func, VectorN x, double eps) {

        MatrMN res = zeros(x.size(), x.size());
        int row, col;
        for (row = 0; row < x.size(); row++) {
            for (col = 0; col <= row; col++) {
                double h = VectorN.partial2(func, x, row, col, eps);
                res.set(row, col, h);
                res.set(col, row, res.get(row,col));
            }
        }
        return res;
    }

    public static MatrMN invert(MatrMN mat) {
        int rows = mat.size();
        int cols = mat.get(0).size();

        if (rows != cols)
            throw new RuntimeException("rows!=cols");

        MatrMN l = zeros(mat.size(), mat.get(0).size()),
               u = zeros(mat.size(), mat.get(0).size()),
               inv;

        lu(mat, l, u);

        VectorN b, col;

        b=VectorN.build(rows);

        inv = zeros(rows);

        for (int i = 0; i < cols; i++) {
            b.set( i, 1.0);
            col = linsolve(l, u, b);

            if (col == null || col.size() == 0)
                throw new RuntimeException("col size==0 or col is null");

            b.set(i, 0.0);
            for (int j = 0; j < rows; j++)
                inv.set(j, i, col.get(j));

        }
        return inv;
    }

    static void lu(MatrMN mat, MatrMN low, MatrMN up)
    {
        int rows = mat.size();
        int cols = mat.get(0).size();

        if (rows != cols)
            throw new RuntimeException("rows!=cols");

        if (!Arrays.equals(size(mat), size(low)))
            low = zeros(rows, cols);

        if (!Arrays.equals(size(mat), size(up)))
            up = zeros(rows, cols);

        int i = 0, j = 0, k = 0;

        for (i = 0; i < rows; i++) {
            for (j = 0; j < rows; j++) {
                if (j >= i) {

                    low.set(j, i, mat.get(j,i));

                    for (k = 0; k < i; k++)
                        low.set(j,i, low.get(j,i)-low.get(j,k)*up.get(k,i));

                }
            }

            for (j = 0; j < cols; j++) {
                if (j < i)
                    continue;

                if (j == i) {
                    up.set(i, j, 1.);
                    continue;
                }

                up.set(i,j, mat.get(i,j)/low.get(i,i));

                for (k = 0; k < i; k++)
                up.set(i,j, up.get(i,j)-((low.get(i,k)*up.get(k,j))/low.get(i,i)));

            }
        }
    }

    public static VectorN linsolve(MatrMN low, MatrMN up, VectorN b) {

        double det = 1.0;

        VectorN x, z;

        for (int i = 0; i < up.size(); i++)
            det *= (up.get(i,i)*up.get(i,i));


        if (Math.abs(det) < 1e-12)
            return null;

        z = VectorN.build(low.size());

        double tmp;

        for (int i = 0; i < z.size(); i++) {
            tmp = 0.0;
            for (int j = 0; j < i; j++)
                tmp += z.get(j)*low.get(i,j);

            z.set(i, (b.get(i)-tmp)/low.get(i,i));
        }

        x=VectorN.build(low.size());

        for (int i = z.size() - 1; i >= 0; i--) {
            tmp = 0.0;
            for (int j = i + 1; j < z.size(); j++)
                tmp += x.get(j) * up.get(i,j);

            x.set(i, z.get(i)-tmp);
        }

        return x;
    }

    public static VectorN mul(MatrMN mat, VectorN vec) {
        var _size = size(mat);
        if (_size[1] != vec.size())
            throw new RuntimeException("wrong matr size");

        VectorN result =VectorN.build(_size[0]);
        int cntr = 0;
        for (int i=0; i<mat.size();++i)
        result.set(cntr++, VectorN.dot(mat.get(i),vec));

        return result;
    }

    public static VectorN mul(VectorN vec, MatrMN mat) {
        var _size = size(mat);
        if (_size[1] != vec.size())
            throw new RuntimeException("wrong matr size");

        VectorN result=VectorN.build(_size[1]);
        for (int col = 0; col < _size[1]; col++) {
            result.set(0,0.);

            for (int row = 0; row < _size[1]; row++)
                result.set(col, result.get(col)+mat.get(col, row)*vec.get(row));

        }
        return result;
    }

    public static MatrMN mul(MatrMN a, MatrMN b) {
        var a_size = size(a);
        var b_size = size(b);

        if (a_size[1] != b_size[0])
            throw new RuntimeException("error :: matrix by matrix multipliction");

        MatrMN result = new MatrMN(a_size[0],b_size[1]);

        for (int row = 0; row < a_size[0]; row++)
            for (int col = 0; col < b_size[1]; col++)
                for (int k = 0; k < b_size[0]; k++)
                    result.set(row,col, result.get(row,col) + a.get(row,k) * b.get(k,col));

        return result;
    }

    public static MatrMN mul(MatrMN mat, double a){

        MatrMN res=new MatrMN(mat);

        for (int row = 0; row < mat.size(); row++)
            for (int col = 0; col < mat.get(0).size(); col++)
                mat.set(row, col, mat.get(row,col)*a);

        return res;
    }

    public static MatrMN plus(MatrMN a, MatrMN b) {
        var _size = size(a);
        if (!Arrays.equals(_size, size(b)))
            throw new RuntimeException("error :: matrix + matrix");

        MatrMN result = new MatrMN(a.size());
        for (int i = 0; i < result.size(); i++)
            result.set(i,VectorN.plus(a.get(i),b.get(i)));

        return result;
    }

    public static MatrMN minus(MatrMN a, MatrMN b) {
        var _size = size(a);
        if (!Arrays.equals(_size, size(b)))
            throw new RuntimeException("error :: matrix + matrix");

        MatrMN result = new MatrMN(a.size());
        for (int i = 0; i < result.size(); i++)
            result.set(i,VectorN.minus(a.get(i),b.get(i)));

        return result;
    }

    public static VectorN linsolve(MatrMN mat, VectorN b) {

        int rows = mat.size();
        int cols = mat.get(0).size();

        if (rows != cols)
            throw new RuntimeException("error :: matrix inversion :: non square matrix");

        MatrMN low = zeros(mat.size(), mat.get(0).size()),
                up = zeros(mat.size(), mat.get(0).size());

        lu(mat, low, up);

        return linsolve(low,  up, b);
    }

    public static void matrixTest() {
        VectorN b=new VectorN( 1.,2.,3. );
	/*
	0.05
	0.3
	0.05
	*/
        MatrMN matrix= new MatrMN( new VectorN(8.,1.,6.), new VectorN(3.,5.,7.), new VectorN(4.,9.,2.));

        System.out.println(matrix);
        System.out.println("matrix summ ");
        System.out.println(plus(matrix, matrix));
        System.out.println("matrix diff");
        System.out.println(minus(matrix, matrix));
        System.out.println("matrix mult");
        System.out.println(mul(matrix, matrix));
        MatrMN l = zeros(matrix.size(), matrix.get(0).size()),
               u = zeros(matrix.size(), matrix.get(0).size());
        lu(matrix, l, u);
        System.out.println("matrix l: ");
        System.out.println(l);
        System.out.println("matrix u: ");
        System.out.println(u);
        System.out.println("matrix lu: ");
        System.out.println(mul(l,  u));
        VectorN x = linsolve(matrix, b);

        System.out.println("x : ");
        System.out.println(x);
        System.out.println("Ax - b:");
        System.out.println(VectorN.minus(mul(matrix, x),b));
        System.out.println("A*inv(A): ");
        System.out.println(mul(matrix, invert(matrix)));
        System.out.println("Hessian: ");
        VectorN x_ = new VectorN( 1.,2.,3. );
        System.out.println(hessian(ConstantsFunctionsUtil.fTest, x_, ConstantsFunctionsUtil.epsilon1));
    }
}
