package fluent.ly;

import java.lang.management.*;
import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.*;

import il.org.spartan.*;

/** A class to print all properties of an arbitrary object which can be
 * retrieved by getters methods (i.e., getXXX()) methods and boolean inspection
 * methods (i.e., isXXX()), as can be determined by reflection information.
 * @author Yossi Gil
 * @since 24/07/2007 */
public class dump {
  /** Dump a class object
   * @param ¢ JD */
  public static void go(@NotNull final Class<?> ¢) {
    out.out("\n\n--IDENTIFICATION--\n");
    out.out("Simple Name", ¢.getSimpleName());
    out.out("Canonical Name", ¢.getCanonicalName());
    out.out("Name", ¢.getName());
    out.out("toString", ¢ + "");
    out.out("super class", ¢.getSuperclass());
    out.out("generic super class", ¢.getGenericSuperclass());
    out.out("class", ¢.getClass());
    out.out("component type", ¢.getComponentType());
    // out("protection domain", c.getProtectionDomain());
    out.out("class loader", ¢.getClassLoader());
    out.out("--MODIFIERS--\n");
    final int flags = ¢.getModifiers();
    out.out("Package", ¢.getPackage());
    out.out("Modifiers (decimal form)", flags);
    out.out("Modifiers (binary form)", ReflectionAnalyzer.toBinary(flags));
    out.out("IsSynthetic", ¢.isSynthetic());
    out.out("IsPrimitive", ¢.isPrimitive());
    out.out("IsFinal", Modifier.isFinal(flags));
    out.out("IsAbstract", Modifier.isAbstract(flags));
    out.out("IsStatic", Modifier.isStatic(flags));
    out.out("IsStrictfp", Modifier.isStrict(flags));
    out.out("--Visibility--\n");
    out.out("IsPublic", Modifier.isPublic(flags));
    out.out("IsPrivate", Modifier.isPrivate(flags));
    out.out("IsProtected", Modifier.isProtected(flags));
    out.out("--MEMBERS\n");
    out.out("fields", ¢.getFields());
    out.out("methods", ¢.getMethods());
    out.out("constructors", ¢.getConstructors());
    out.out("declared fields", ¢.getDeclaredFields());
    out.out("declared methods", ¢.getDeclaredMethods());
    out.out("declared constructors", ¢.getDeclaredConstructors());
    out.out("--CLASS SIGNATURE--\n");
    out.out("interfaces", ¢.getInterfaces());
    out.out("annotations", ¢.getAnnotations());
    out.out("type parameters", ¢.getTypeParameters());
    out.out("declared annotations", ¢.getDeclaredAnnotations());
    out.out("generic interfaces", ¢.getGenericInterfaces());
    out.out("--CONTAINERS--\n");
    out.out("declared classes", ¢.getDeclaredClasses());
    out.out("declaring class", ¢.getDeclaringClass());
    out.out("enclosing class", ¢.getEnclosingClass());
    out.out("enclosing constructor", ¢.getEnclosingConstructor());
    out.out("enclosing method", ¢.getEnclosingMethod());
    out.out("--CLASS MEMBERS--\n");
    out.out("public classes", ¢.getClasses());
    out.out("declared classes", ¢.getDeclaredClasses());
    out.out("declared annotations", ¢.getDeclaredAnnotations());
    out.out("---------------------------\n");
  }
  public static <T> void go(@NotNull final List<T> ts, @NotNull final String... ss) {
    out.out("Exploring list");
    for (final String ¢ : ss)
      out.out(¢);
    for (final T ¢ : ts)
      dump.go(¢);
  }
  public static void go(final Object os[], @NotNull final String... ss) {
    for (final String ¢ : ss)
      out.out(¢);
    out.out("elements", os);
  }
  public static void go(final @Nullable Object o, @NotNull final String... ss) {
    for (final String ¢ : ss)
      out.out(¢);
    if (o == null) {
      out.out("NULL");
      return;
    }
    final Class<?> c = o.getClass();
    out.out("\n\n--BEGIN " + c.getSimpleName() + " object: " + o + "\n");
    out.out("Class canonical name", c.getCanonicalName());
    out.out("Class name", c.getName());
    for (@NotNull final Method m : c.getMethods()) {
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
        final Object $ = m.invoke(o);
        if ($ == null) {
          out.out(name, "null");
          continue;
        }
        if ($ instanceof Object[])
          out.out(name, (Object[]) $);
        out.out(name, !($ instanceof Collection) ? $ : (Collection<Object>) $);
      } catch (@NotNull final Throwable ¢) {
        // For some reason, a reflection call to method
        // getContent() in URL objects throws this exception.
        // We do not have much to do in this and other similar cases.
        out.out(name, m.getName() + " THROWS " + ¢);
      }
    }
    out.out("--END OBJECT--\n\n");
    System.out.flush();
  }
  public static void main(final String[] args) {
    // Explore.go(Package.class);
    final ClassLoadingMXBean a = ManagementFactory.getClassLoadingMXBean();
    System.out.println(a.getLoadedClassCount());
    System.out.println(a.getTotalLoadedClassCount());
    System.out.println(a.getUnloadedClassCount());
    dump.go(ManagementFactory.getClassLoadingMXBean());
    final CompilationMXBean b = ManagementFactory.getCompilationMXBean();
    System.out.println(b.getTotalCompilationTime());
    System.out.println(b.getName());
    System.out.println(b.isCompilationTimeMonitoringSupported());
    System.exit(1);
    dump.go(ManagementFactory.getGarbageCollectorMXBeans());
    dump.go(ManagementFactory.getMemoryManagerMXBeans());
    dump.go(ManagementFactory.getMemoryPoolMXBeans());
    dump.go(ManagementFactory.getOperatingSystemMXBean());
    dump.go(ManagementFactory.getPlatformMBeanServer());
    dump.go(ManagementFactory.getRuntimeMXBean());
    dump.go(ManagementFactory.getThreadMXBean());
  }
}
