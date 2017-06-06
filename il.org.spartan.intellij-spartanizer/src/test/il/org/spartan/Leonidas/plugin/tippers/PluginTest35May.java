package il.org.spartan.Leonidas.plugin.tippers;

import static spartanizer.SpartanizerUtils.eval;

/**
 * @author Michal Cohen
 * @since 12/01/17
 */
public class PluginTest35May {

    int x;

    public static void main(String[] args) {
        int x = 7;
        Integer z = 4;
        Integer y = eval(4).unless(x > 0); // replace with unless nano pattern

        // remove braces
        if (x > 3)
            x++;

        // remove braces
        while (!(x > 3))
            x++;

        // remove double negation
        if (x > 4)
            System.out.print("banana");

        // flip condition
        if (x > 2)
            x++;

        // go fluent
        class Bloop {
            private int s;

            Bloop setX(int ¢) {
                this.s = ¢;
                return this;
            }

            int get() {
                return s;
            }
        }

        // change to x++
        x++;

        String s = "banana";
        // flip equals
        "split".equals(s);
    }

    private static void foo(boolean ¢) {
        System.out.print(¢);
    }

    // should change parameter to cent
    int f(int ¢) {
        ¢ = ¢ + 5;
        return ¢ + 1;
    }
}

