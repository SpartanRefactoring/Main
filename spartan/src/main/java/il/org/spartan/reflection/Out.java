package il.org.spartan.reflection;

import java.util.Collection;

import fluent.ly.idiomatic;
import il.org.spartan.utils.Once;
import il.org.spartan.utils.Separate;

public class Out {
  static final int MAX_FIRST = 20;
  static final int MAX_LAST = 10;

  public static void out(final String ¢) {
    System.out.print(¢);
  }

  public static void out(final String name, final boolean v) {
    System.out.printf("%s = %b\n", name, idiomatic.box(v));
  }

  public static void out(final String name, final Collection<Object> a) {
    assert name != null;
    if (a == null || a.isEmpty())
      System.out.printf("No %s\n", name);
    else if (a.size() == 1)
      System.out.printf("Only 1 %s: %s\n", name, a.iterator().next());
    else {
      System.out.printf("Total of %d %s:\n", idiomatic.box(a.size()), name);
      int n = 0;
      for (final Object ¢ : a)
        if (++n > MAX_FIRST && n <= a.size() - MAX_LAST)
          System.out.print(new Once("\t...\n"));
        else
          System.out.printf("\t%2d) %s\n", idiomatic.box(n), ¢);
    }
  }

  public static void out(final String name, final int a) {
    System.out.printf("%s = %d\n", name, idiomatic.box(a));
  }

  public static void out(final String name, final Object a) {
    System.out.printf((a == null ? "No" : "%s =") + " %s\n", name, a);
  }

  public static void out(final String name, final Object[] os) {
    assert name != null;
    if (os == null || os.length <= 0)
      System.out.printf("No %s\n", name);
    else if (os.length == 1)
      System.out.printf("Only one %s: %s\n", name, os[0]);
    else
      System.out.printf("Total of %d %s:\n\t%s\n", idiomatic.box(os.length), name, Separate.by(os, "\n\t"));
  }
}
