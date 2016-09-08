package il.org.spartan.spartanizer.utils;

import java.util.*;

import il.org.spartan.*;

public class Out {
  static final int MAX_FIRST = 20;
  static final int MAX_LAST = 10;

  public static void out(final String s) {
    System.out.print(s);
  }

  public static void out(final String name, final Object a) {
    System.out.printf((a == null ? "No" : "%s =") + " %s\n", name, a);
  }

  public static void out(final String name, final int i) {
    System.out.printf("%s = %d\n", name, new Integer(i));
  }

  public static void out(final String name, final boolean b) {
    System.out.printf("%s = %b\n", name, new Boolean(b));
  }

  public static void out(final String name, final Object[] os) {
    assert name != null;
    if (os == null || os.length <= 0)
      System.out.printf("No %s\n", name);
    else if (os.length == 1)
      System.out.printf("Only one %s: %s\n", name, os[0]);
    else
      System.out.printf("Total of %d %s:\n\t%s\n", new Integer(os.length), name, separate.these(os).by("\n\t"));
  }

  public static void out(final String name, final Collection<Object> os) {
    assert name != null;
    if (os == null || os.isEmpty()) {
      System.out.printf("No %s\n", name);
      return;
    }
    if (os.size() == 1) {
      System.out.printf("Only 1 %s: %s\n", name, os.iterator().next());
      return;
    }
    System.out.printf("Total of %d %s:\n", Integer.valueOf(os.size()), name);
    int n = 0;
    for (final Object o : os) {
      if (++n > MAX_FIRST && n <= os.size() - MAX_LAST) {
        System.out.print("\t...\n");
        return;
      }
      System.out.printf("\t%2d) %s\n", Integer.valueOf(n), o);
      continue;
    }
  }
}
