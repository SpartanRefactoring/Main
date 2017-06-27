package spartanizer;

import java.util.List;
import java.util.function.Function;

public class SpartanizerUtils {

    public static class Eval<T> {
		T y;

		public Eval(T y) {
			this.y = y;
		}

		public T unless(boolean x) {
			return !x ? y : null;
		}
	}

    public static <T> Eval<T> eval(T y) { return new Eval<T>(y); }

    public static <T> T last(List<T> ¢) {
        return ¢.get(¢.size() - 1);
    }

    public static <T,R> R nullConditional(T x, Function<T,R> f){return x == null ? null : f.apply(x);}

    public static class Defaults<T> {
        T then;

        public Defaults(T then){
            this.then = then;
        }

        public T to(T else$){
            return then != null ? then : else$;
        }
    }

    public static <T> Defaults<T> defaults(T then){
        return new Defaults<T>(then);
    }

    public @interface SpartaDefeat {}
}