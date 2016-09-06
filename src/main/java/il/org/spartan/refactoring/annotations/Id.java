package il.org.spartan.refactoring.annotations;

/** @author Alex Kopzon
 * @year 2016 */
public @interface Id {
  Class<?> clazz();

  String name();
}