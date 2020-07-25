package fluent.ly;

import java.util.Collection;

public class to {
  public static void out(final String ¢) {
    System.out.print(¢);
  }

  public static void out(final String name, final boolean b) {
    System.out.printf("%s = %b\n", name, Boolean.valueOf(b));
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
    var n = 0;
    for (final Object ¢ : os) {
      if (++n > dump.MAX_FIRST && n <= os.size() - dump.MAX_LAST) {
        System.out.print("\t...\n");
        return;
      }
      System.out.printf("\t%2d) %s\n", Integer.valueOf(n), ¢);
    }
  }

  public static void out(final String name, final int i) {
    System.out.printf("%s = %d\n", name, Integer.valueOf(i));
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
      System.out.printf("Total of %d %s:\n\t%s\n", Integer.valueOf(os.length), name, separate.these(os).by("\n\t"));
  }
}
