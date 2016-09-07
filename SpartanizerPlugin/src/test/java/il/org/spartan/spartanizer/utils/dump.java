package il.org.spartan.spartanizer.utils;

import static il.org.spartan.spartanizer.utils.Out.*;

import java.lang.management.*;
import java.lang.reflect.*;
import java.util.*;

/** A class to print all properties of an arbitrary object which can be
 * retrieved by getters methods (i.e., getXXX()) methods and boolean inspection
 * methods (i.e., isXXX()), as can be determined by reflection information.
 * @author Yossi Gil
 * @since 24/07/2007 */
public class dump {
  public static void go(final Class<?> c) {
    out("\n\n--IDENTIFICATION--\n");
    out("Simple Name", c.getSimpleName());
    out("Canonical Name", c.getCanonicalName());
    out("Name", c.getName());
    out("toString", c + "");
    out("super class", c.getSuperclass());
    out("generic super class", c.getGenericSuperclass());
    out("class", c.getClass());
    out("component type", c.getComponentType());
    // out("protection domain", c.getProtectionDomain());
    out("class loader", c.getClassLoader());
    out("--MODIFIERS--\n");
    final int flags = c.getModifiers();
    out("Package", c.getPackage());
    out("Modifiers (decimal form)", flags);
    out("Modifiers (binary form)", ReflectionAnalyzer.toBinary(flags));
    out("IsSynthetic", c.isSynthetic());
    out("IsPrimitive", c.isPrimitive());
    out("IsFinal", Modifier.isFinal(flags));
    out("IsAbstract", Modifier.isAbstract(flags));
    out("IsStatic", Modifier.isStatic(flags));
    out("IsStrictfp", Modifier.isStrict(flags));
    out("--Visibility--\n");
    out("IsPublic", Modifier.isPublic(flags));
    out("IsPrivate", Modifier.isPrivate(flags));
    out("IsProtected", Modifier.isProtected(flags));
    out("--MEMBERS\n");
    out("fields", c.getFields());
    out("methods", c.getMethods());
    out("constructors", c.getConstructors());
    out("declared fields", c.getDeclaredFields());
    out("declared methods", c.getDeclaredMethods());
    out("declared constructors", c.getDeclaredConstructors());
    out("--CLASS SIGNATURE--\n");
    out("interfaces", c.getInterfaces());
    out("annotations", c.getAnnotations());
    out("type parameters", c.getTypeParameters());
    out("declared annotations", c.getDeclaredAnnotations());
    out("generic interfaces", c.getGenericInterfaces());
    out("--CONTAINERS--\n");
    out("declared classes", c.getDeclaredClasses());
    out("declaring class", c.getDeclaringClass());
    out("enclosing class", c.getEnclosingClass());
    out("enclosing constructor", c.getEnclosingConstructor());
    out("enclosing method", c.getEnclosingMethod());
    out("--CLASS MEMBERS--\n");
    out("public classes", c.getClasses());
    out("declared classes", c.getDeclaredClasses());
    out("declared annotations", c.getDeclaredAnnotations());
    out("---------------------------\n");
  }

  public static void go(final Object o[], final String... ss) {
    for (final String s : ss)
      out(s);
    out("elements", o);
  }

  public static <T> void go(final List<T> ts, final String... ss) {
    out("Exploring list");
    for (final String s : ss)
      out(s);
    for (final T t : ts)
      dump.data(t);
  }

  public static void data(final Object o, final String... ss) {
    for (final String s : ss)
      out(s);
    if (o == null) {
      out("NULL");
      return;
    }
    final Class<?> c = o.getClass();
    out("\n\n--BEGIN " + c.getSimpleName() + " object: " + o + "" + "\n");
    out("Class canonical name", c.getCanonicalName());
    out("Class name", c.getName());
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
        final Object $ = m.invoke(o);
        if ($ == null) {
          out(name, "null");
          continue;
        }
        if ($ instanceof Object[])
          out(name, (Object[]) $);
        if (!($ instanceof Collection))
          out(name, $);
        else {
          @SuppressWarnings("unchecked") final Collection<Object> os = (Collection<Object>) $;
          out(name, os);
        }
      } catch (final Throwable e) {
        // For some reason, a reflection call to method
        // getContent() in URL objects throws this exception.
        // We do not have much to do in this and other similar cases.
        out(name, m.getName() + " THROWS " + e);
      }
    }
    out("--END OBJECT--\n\n");
    System.out.flush();
  }

  public static void main(final String[] args) {
    // Explore.go(Package.class);
    final ClassLoadingMXBean a = ManagementFactory.getClassLoadingMXBean();
    System.out.println(a.getLoadedClassCount());
    System.out.println(a.getTotalLoadedClassCount());
    System.out.println(a.getUnloadedClassCount());
    dump.data(ManagementFactory.getClassLoadingMXBean());
    final CompilationMXBean b = ManagementFactory.getCompilationMXBean();
    System.out.println(b.getTotalCompilationTime());
    System.out.println(b.getName());
    System.out.println(b.isCompilationTimeMonitoringSupported());
    System.exit(1);
    dump.go(ManagementFactory.getGarbageCollectorMXBeans());
    dump.go(ManagementFactory.getMemoryManagerMXBeans());
    dump.go(ManagementFactory.getMemoryPoolMXBeans());
    dump.data(ManagementFactory.getOperatingSystemMXBean());
    dump.data(ManagementFactory.getPlatformMBeanServer());
    dump.data(ManagementFactory.getRuntimeMXBean());
    dump.data(ManagementFactory.getThreadMXBean());
  }
}
