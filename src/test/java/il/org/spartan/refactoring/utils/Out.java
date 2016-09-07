package il.org.spartan.refactoring.utils;

import java.util.Collection;

import il.org.spartan.*;

public class Out {
  static final int MAX_FIRST = 20;
  static final int MAX_LAST = 10;
  
  public static void out(final String s) {
    System.out.print(s);
  }
  public static void out(final String name, final Object a) {
    System.out.printf(a == null ? "No %s\n" : "%s = %s\n", name, a);
  }
  public static void out(final String name, final int a) {
    System.out.printf("%s = %d\n", name, (a));
  }
  public static void out(final String name, final boolean v) {
    System.out.printf("%s = %b\n", name, (v));
  }
  public static void out(final String name, final Object[] os) {
    assert name != null;
    if (os == null || os.length <= 0)
      System.out.printf("No %s\n", name);
    else if (os.length == 1)
      System.out.printf("Only one %s: %s\n", name, os[0]);
    else
      System.out.printf("Total of %d %s:\n\t%s\n", os.length, name, separate.these(os).by("\n\t"));
  }
  public static void out(final String name, final Collection<Object> a) {
    assert name != null;
    if (a == null || a.size() <= 0)
      System.out.printf("No %s\n", name);
    else if (a.size() == 1)
      System.out.printf("Only 1 %s: %s\n", name, a.iterator().next());
    else {
      System.out.printf("Total of %d %s:\n", (a.size()), name);
      int n = 0;
      final Once ellipsis = new Once("\t...\n");
      for (final Object o : a)
        if (++n > MAX_FIRST && n <= a.size() - MAX_LAST)
          System.out.print(ellipsis);
        else
          System.out.printf("\t%2d) %s\n", (n), o);
    }
  }
}
