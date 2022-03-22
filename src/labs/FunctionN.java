package labs;

// это лучше не копипастить - юзайте стандартный интерфейс Function (точно такой же)
@FunctionalInterface
public interface FunctionN<S, T> {

    S perform(T arg);
}
