package fluent.ly;

import java.lang.reflect.*;
import java.util.*;

import il.org.spartan.*;

/** A class to print all properties of an arbitrary object which can be
 * retrieved by getters methods (i.e., getXXX()) methods and boolean inspection
 * methods (i.e., isXXX()), as can be determined by reflection information.
 * @author Yossi Gil
 * @since 24/07/2007 */
public class dump {
  static final int MAX_FIRST = 20;
  static final int MAX_LAST = 10;

  public static String entry(final String name, final boolean b) {
    return String.format("%s = %b\n", name, Boolean.valueOf(b));
  }
  public static String entry(final String name, final Collection<Object> os) {
    assert name != null;
    if (os == null || os.isEmpty())
      return String.format("No %s\n", name);
    if (os.size() == 1)
      return String.format("Only 1 %s: %s\n", name, os.iterator().next());
    String $ = String.format("Total of %d %s:\n", Integer.valueOf(os.size()), name);
    int n = 0;
    for (final Object ¢ : os) {
      if (++n > MAX_FIRST && n <= os.size() - MAX_LAST)
        $ += "\t...\n";
      $ += String.format("\t%2d) %s\n", Integer.valueOf(n), ¢);
    }
    return $;
  }
  public static String entry(final String name, final int i) {
    return String.format("%s = %d\n", name, Integer.valueOf(i));
  }
  public static String entry(final String name, final Object value) {
    return String.format((value == null ? "No" : "%s =") + " %s\n", name, value);
  }
  public static String entry(final String name, final Object[] os) {
    assert name != null;
    return os == null || os.length <= 0 ? String.format("No %s\n", name)
        : os.length == 1 ? String.format("Only one %s: %s\n", name, os[0])
            : String.format("Total of %d %s:\n\t%s\n", Integer.valueOf(os.length), name, separate.these(os).by("\n\t"));
  }
  /** Dump a class object
   * @param ¢ JD
   * @return */
  public static String of(final Class<?> ¢) {
    String $ = "\n\n--IDENTIFICATION--\n" + entry("Simple Name", ¢.getSimpleName()) + entry("Canonical Name", ¢.getCanonicalName())
        + entry("Name", ¢.getName()) + entry("toString", ¢ + "") + entry("super class", ¢.getSuperclass())
        + entry("generic super class", ¢.getGenericSuperclass()) + entry("class", ¢.getClass()) + entry("component type", ¢.getComponentType())
        + entry("class loader", ¢.getClassLoader()) + "--MODIFIERS--\n";
    final int flags = ¢.getModifiers();
    $ += entry("Package", ¢.getPackage()) + entry("Modifiers (decimal form)", flags)
        + entry("Modifiers (binary form)", ReflectionAnalyzer.toBinary(flags)) + entry("IsSynthetic", ¢.isSynthetic())
        + entry("IsPrimitive", ¢.isPrimitive()) + entry("IsFinal", Modifier.isFinal(flags)) + entry("IsAbstract", Modifier.isAbstract(flags))
        + entry("IsStatic", Modifier.isStatic(flags)) + entry("IsStrictfp", Modifier.isStrict(flags)) + "--Visibility--\n"
        + entry("IsPublic", Modifier.isPublic(flags)) + entry("IsPrivate", Modifier.isPrivate(flags))
        + entry("IsProtected", Modifier.isProtected(flags)) + "--MEMBERS\n" + entry("fields", ¢.getFields()) + entry("methods", ¢.getMethods())
        + entry("constructors", ¢.getConstructors()) + entry("declared fields", ¢.getDeclaredFields())
        + entry("declared methods", ¢.getDeclaredMethods()) + entry("declared constructors", ¢.getDeclaredConstructors()) + "--CLASS SIGNATURE--\n"
        + entry("interfaces", ¢.getInterfaces()) + entry("annotations", ¢.getAnnotations()) + entry("type parameters", ¢.getTypeParameters())
        + entry("declared annotations", ¢.getDeclaredAnnotations()) + entry("generic interfaces", ¢.getGenericInterfaces()) + "--CONTAINERS--\n"
        + entry("declared classes", ¢.getDeclaredClasses()) + entry("declaring class", ¢.getDeclaringClass())
        + entry("enclosing class", ¢.getEnclosingClass()) + entry("enclosing constructor", ¢.getEnclosingConstructor())
        + entry("enclosing method", ¢.getEnclosingMethod()) + "--CLASS MEMBERS--\n" + entry("public classes", ¢.getClasses())
        + entry("declared classes", ¢.getDeclaredClasses()) + entry("declared annotations", ¢.getDeclaredAnnotations());
    return $ + "---------------------------\n";
  }
  public static <T> String of(final List<T> ts, final String... ss) {
    String $ = "Exploring list";
    for (final String ¢ : ss)
      $ += ¢;
    for (final T ¢ : ts)
      $ += dump.of(¢);
    return $;
  }
  public static String of(final Object os[], final String... ss) {
    String $ = "";
    for (final String ¢ : ss)
      $ += ¢;
    return $ + entry("elements", os);
  }
  @SuppressWarnings("unchecked") public static String of(final Object o, final String... ss) {
    String $ = "";
    for (final String ¢ : ss)
      $ += ¢;
    if (o == null)
      return $ + "NULL";
    final Class<?> c = o.getClass();
    $ += "\n\n--BEGIN " + c.getSimpleName() + " object: " + o + "\n" + entry("Class canonical name", c.getCanonicalName())
        + entry("Class name", c.getName()) + entry("Class simple name", c.getSimpleName());
    for (final Method m : c.getMethods()) {
      if (m.getParameterTypes().length != 0)
        continue;
      String name = m.getName();
      if ("getClass".equals(name) || "toString".equals(name))
        continue;
      if (name.matches("^get[A-Z].*$"))
        name = name.replaceFirst("^get", "");
      else if (name.matches("^is[A-Z].*$"))
        name = name.replaceFirst("^is", "");
      else if ("size".equals(name))
        name = "size";
      else if (!name.matches("^to[A-Z].*$"))
        continue;
      try {
        final Object oo = m.invoke(o);
        if (oo == null) {
          $ += entry(name, "null");
          continue;
        }
        $ += entry(name, oo instanceof Object[] ? (Object[]) oo : o instanceof Collection ? (Collection<Object>) oo : oo);
      } catch (final Throwable ¢) {
        // For some reason, a reflection call to method
        // getContent() in URL objects throws this exception.
        // We do not have much to do in this and other similar cases.
        $ += entry(name, m.getName() + " THROWS " + ¢);
      }
    }
    final List<Field> fieldList = new ArrayList<>();
    for (Class<?> ¢ = c; ¢ != null; ¢ = ¢.getSuperclass())
      fieldList.addAll(Arrays.asList(¢.getDeclaredFields()));
    for (final Field f : fieldList)
      try {
        f.setAccessible(true);
        $ += entry(f + "", f.get(o));
      } catch (IllegalArgumentException | IllegalAccessException e) {
        $ += entry(f + "", "???");
        continue;
      }
    return $ + "--END OBJECT--\n\n";
  }
}
