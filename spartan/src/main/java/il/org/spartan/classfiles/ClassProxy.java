package il.org.spartan.classfiles;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.security.ProtectionDomain;

import il.org.spartan.classfiles.reify.ClassInfo;

/** An enhanced representation of the reflective information found in the
 * standard library {@link Class}, providing information found in the constants'
 * pool.
 * <p>
 * Implementation uses the <b>Proxy Design Pattern</b> for both that the
 * encapsulated {@link Class} and for the constants' pool. Construction is
 * either by the <em>class name</em> or by the {@link Class} object. In the
 * latter case, the constant pool is not read from the <tt>.class</tt> file
 * unless clients call services that require information found only this pool.
 * <p>
 * Similarly, if only the class name is given, no attempt is made to create the
 * reflective {@link Class} object, unless the client tries to use a service
 * which requires this object.
 * <p>
 * Instantiation of the {@link Class} object is carried out by examining the
 * <em>current</em> value of the {@link CLASSPATH}.
 * @author Yossi Gil
 * @param <T> Type whose reflective information is represented here.
 * @see CLASSPATH */
public class ClassProxy<T> {
  /** Tries to find the reflective {@link Class} information of a class given by
   * name, even if this name uses a dot character (<code>'.'</code>), instead of
   * a dollar (<code>'$'</code>) to separate the name of an inner class from the
   * name of its containing class.
   * @param className a dot separated class name
   * @return a {@link Class} with the reflective information for the given class
   *         name
   * @throws ClassNotFoundException in case the class could not be located */
  public static Class<?> findClass(final String className) throws ClassNotFoundException {
    return CLASSPATH.load(normalizeClassName(className));
  }
  /** Tries to determine which dot ('<tt>.</tt>') characters in a class name are
   * represents package name nesting and which are represent class nesting. This
   * is necessary since a dot representing class nesting is to be replaced by a
   * dollar character ('<tt>$</tt>') in translating the class name into a file
   * name. In this translation, other dots are mapped to directory structure
   * nesting.
   * <p>
   * There is no syntactical difference between the two dot kinds, so the
   * distinction is made by trying to determine whether a class is an inner
   * class by either loading the class with {@link Class#forName} and if this
   * fails for some reason, by trying to locate the <tt>.class</tt> file on the
   * file system.
   * @param className a dot separated full class name
   * @return the inputs, where dots that are found to represent class nesting
   *         are replaced by dollars.
   * @see CLASSFILES */
  public static String normalizeClassName(final String className) {
    for (String $ = className;;) {
      if (CLASSFILES.open($) != null)
        return $;
      try {
        // Trying loading $
        CLASSPATH.load($);
        // If successful, we have a valid class name.
        return $;
      } catch (@SuppressWarnings("unused") final NoClassDefFoundError __) {
        // This exceptions is thrown when the class file itself can be
        // opened but it refers to other classes which cannot be found
        // in the
        // class path.
        return $;
      } catch (@SuppressWarnings("unused") final ClassNotFoundException __) {
        // This exception is thrown when the class file could not be
        // found. Not much to do except carry on with fingers
        // crossed, hoping that the subsequent replacement of a dot by a
        // dollar will yield a normalized class name.
      }
      final String newName = $.replaceFirst("\\.([a-zA-Z0-9$]+)$", "\\$$1");
      if ($.equals(newName))
        return className;
      $ = newName;
    }
  }

  /** The encapsulated {@link Class} reflective object. */
  private Class<? extends T> clazz;
  /** The name of this class. */
  private String className;
  /** The constant' pool information, initialized lazily. */
  private ClassInfo inner;

  /** Instantiate with a given {@link Class} object
   * @param clazz some {@link Class} object */
  public ClassProxy(final Class<? extends T> clazz) {
    assert clazz != null;
    this.clazz = clazz;
    this.className = clazz.getName();
  }
  /** Instantiate with a given class name in the format returned by
   * {@link Class#getName()}
   * @param className a non-<code><b>null</b></code> representing the class
   *        name. */
  public ClassProxy(final String className) {
    assert className != null;
    this.className = normalizeClassName(className);
  }
  // ------------------------------------------------
  // Delegation to encapsulated {@link Class} object
  // ------------------------------------------------
  public Class<?> asSubclass(final Class<?> ¢) throws ClassNotFoundException {
    return clazz().asSubclass(¢);
  }
  public T cast(final Object ¢) throws ClassNotFoundException {
    return clazz().cast(¢);
  }
  public String className() {
    return className != null ? className : (className = retrieveClassName());
  }
  public boolean desiredAssertionStatus() throws ClassNotFoundException {
    return clazz().desiredAssertionStatus();
  }
  @Override public boolean equals(final Object $) {
    try {
      return clazz().equals($);
    } catch (@SuppressWarnings("unused") final ClassNotFoundException __) {
      return false;
    }
  }
  public <A extends Annotation> A getAnnotation(final Class<A> annotationClass) throws ClassNotFoundException {
    return clazz().getAnnotation(annotationClass);
  }
  public Annotation[] getAnnotations() throws ClassNotFoundException {
    return clazz().getAnnotations();
  }
  public String getCanonicalName() throws ClassNotFoundException {
    return clazz().getCanonicalName();
  }
  public Class<?>[] getClasses() throws ClassNotFoundException {
    return clazz().getClasses();
  }
  public ClassLoader getClassLoader() throws ClassNotFoundException {
    return clazz().getClassLoader();
  }
  public Class<?> getComponentType() throws ClassNotFoundException {
    return clazz().getComponentType();
  }
  public Constructor<?> getConstructor(final Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
    return clazz().getConstructor(parameterTypes);
  }
  public Constructor<?>[] getConstructors() throws SecurityException, ClassNotFoundException {
    return clazz().getConstructors();
  }
  public Annotation[] getDeclaredAnnotations() throws ClassNotFoundException {
    return clazz().getDeclaredAnnotations();
  }
  public Class<?>[] getDeclaredClasses() throws SecurityException, ClassNotFoundException {
    return clazz().getDeclaredClasses();
  }
  public Constructor<?> getDeclaredConstructor(final Class<?>... parameterTypes)
      throws ClassNotFoundException, NoSuchMethodException, SecurityException {
    return clazz().getDeclaredConstructor(parameterTypes);
  }
  public Constructor<?>[] getDeclaredConstructors() throws SecurityException, ClassNotFoundException {
    return clazz().getDeclaredConstructors();
  }
  public Field getDeclaredField(final String name) throws NoSuchFieldException, SecurityException, ClassNotFoundException {
    return clazz().getDeclaredField(name);
  }
  public Field[] getDeclaredFields() throws SecurityException, ClassNotFoundException {
    return clazz().getDeclaredFields();
  }
  public Method getDeclaredMethod(final String name, final Class<?>... parameterTypes)
      throws NoSuchMethodException, SecurityException, ClassNotFoundException {
    return clazz().getDeclaredMethod(name, parameterTypes);
  }
  public Method[] getDeclaredMethods() throws SecurityException, ClassNotFoundException {
    return clazz().getDeclaredMethods();
  }
  public Class<?> getDeclaringClass() throws ClassNotFoundException {
    return clazz().getDeclaringClass();
  }
  public Class<?> getEnclosingClass() throws ClassNotFoundException {
    return clazz().getEnclosingClass();
  }
  public Constructor<?> getEnclosingConstructor() throws ClassNotFoundException {
    return clazz().getEnclosingConstructor();
  }
  public Method getEnclosingMethod() throws ClassNotFoundException {
    return clazz().getEnclosingMethod();
  }
  public T[] getEnumConstants() throws ClassNotFoundException {
    return clazz().getEnumConstants();
  }
  public Field getField(final String name) throws NoSuchFieldException, SecurityException, ClassNotFoundException {
    return clazz().getField(name);
  }
  public Field[] getFields() throws SecurityException, ClassNotFoundException {
    return clazz().getFields();
  }
  public Type[] getGenericInterfaces() throws ClassNotFoundException {
    return clazz().getGenericInterfaces();
  }
  public Type getGenericSuperclass() throws ClassNotFoundException {
    return clazz().getGenericSuperclass();
  }
  public Class<?>[] getInterfaces() throws ClassNotFoundException {
    return clazz().getInterfaces();
  }
  public Method getMethod(final String name, final Class<?>... parameterTypes)
      throws NoSuchMethodException, SecurityException, ClassNotFoundException {
    return clazz().getMethod(name, parameterTypes);
  }
  public Method[] getMethods() throws SecurityException, ClassNotFoundException {
    return clazz().getMethods();
  }
  public int getModifiers() throws ClassNotFoundException {
    return clazz().getModifiers();
  }
  public String getName() throws ClassNotFoundException {
    return clazz().getName();
  }
  public Package getPackage() throws ClassNotFoundException {
    return clazz().getPackage();
  }
  public ProtectionDomain getProtectionDomain() throws ClassNotFoundException {
    return clazz().getProtectionDomain();
  }
  /** Which other classes does this class refer to?
   * @return an array with names of all classes that this class uses
   * @throws ClassNotFoundException */
  public String[] getReferencedClasses() throws ClassNotFoundException {
    return classInfo().getReferencedClasses();
  }
  /** Which <code><b>double</b></code>s are found in this class's constants'
   * pool?
   * @return an array with <code><b>double</b></code>s that this class uses.
   * @throws ClassNotFoundException in case the constants' pool could not be
   *         read or found */
  public double[] getReferencedDoubles() throws ClassNotFoundException {
    return classInfo().getReferencedDoubles();
  }
  /** Which <code><b>float</b></code>s are found in this class's constants'
   * pool?
   * @return an array with <code><b>float</b></code>s that this class uses.
   * @throws ClassNotFoundException in case the constants' pool could not be
   *         read or found */
  public float[] getReferencedFloats() throws ClassNotFoundException {
    return classInfo().getReferencedFloats();
  }
  /** Which <code><b>int</b></code>s are found in this class's constants' pool?
   * @return an array with <code><b>int</b></code>s that this class uses.
   * @throws ClassNotFoundException in case the constants' pool could not be
   *         read or found */
  public int[] getReferencedInts() throws ClassNotFoundException {
    return classInfo().getReferencedInts();
  }
  /** Which <code><b>long</b></code>s are found in this class's constants' pool?
   * @return an array with <code><b>long</b></code>s that this class uses.
   * @throws ClassNotFoundException in case the constants' pool could not be
   *         read or found */
  public long[] getReferencedLongs() throws ClassNotFoundException {
    return classInfo().getReferencedLongs();
  }
  /** Which {@link String}s does this class use?
   * @return an array with all {@link String}s that this class uses @throws
   *         ClassNotFoundException in case the constants' pool could not be
   *         read or found */
  public String[] getReferencedStrings() throws ClassNotFoundException {
    return classInfo().getReferencedStrings();
  }
  public URL getResource(final String name) throws ClassNotFoundException {
    return clazz().getResource(name);
  }
  public InputStream getResourceAsStream(final String name) throws ClassNotFoundException {
    return clazz().getResourceAsStream(name);
  }
  public Object[] getSigners() throws ClassNotFoundException {
    return clazz().getSigners();
  }
  public String getSimpleName() throws ClassNotFoundException {
    return clazz().getSimpleName();
  }
  public Class<?> getSuperclass() throws ClassNotFoundException {
    return clazz().getSuperclass();
  }
  public TypeVariable<?>[] getTypeParameters() throws ClassNotFoundException {
    return clazz().getTypeParameters();
  }
  @Override public int hashCode() {
    try {
      return clazz().hashCode();
    } catch (final ClassNotFoundException e) {
      return 0;
    }
  }
  public boolean isAnnotation() throws ClassNotFoundException {
    return clazz().isAnnotation();
  }
  public boolean isAnnotationPresent(final Class<? extends Annotation> annotationClass) throws ClassNotFoundException {
    return clazz().isAnnotationPresent(annotationClass);
  }
  public boolean isAnonymousClass() throws ClassNotFoundException {
    return clazz().isAnonymousClass();
  }
  public boolean isArray() throws ClassNotFoundException {
    return clazz().isArray();
  }
  public boolean isAssignableFrom(final Class<?> ¢) throws ClassNotFoundException {
    return clazz().isAssignableFrom(¢);
  }
  public boolean isEnum() throws ClassNotFoundException {
    return clazz().isEnum();
  }
  public boolean isInstance(final Object ¢) throws ClassNotFoundException {
    return clazz().isInstance(¢);
  }
  public boolean isInterface() throws ClassNotFoundException {
    return clazz().isInterface();
  }
  public boolean isLocalClass() throws ClassNotFoundException {
    return clazz().isLocalClass();
  }
  public boolean isMemberClass() throws ClassNotFoundException {
    return clazz().isMemberClass();
  }
  public boolean isPrimitive() throws ClassNotFoundException {
    return clazz().isPrimitive();
  }
  public boolean isSynthetic() throws ClassNotFoundException {
    return clazz().isSynthetic();
  }
  public T newInstance() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    return clazz().newInstance();
  }
  @Override public String toString() {
    try {
      return clazz() + "";
    } catch (final ClassNotFoundException __) {
      return null;
    }
  }
  /** Fill in constants' pool information, if it was not filled before.
   * @return the filled-in {@link #inner} data member
   * @throws IOException in case the constants' pool could not be read
   * @throws ClassNotFoundException in case the constants' pool could not be
   *         found */
  private ClassInfo classInfo() throws ClassNotFoundException {
    return inner != null ? inner : (inner = retrievePool());
  }
  private Class<? extends T> clazz() throws ClassNotFoundException {
    return clazz != null ? clazz : (clazz = retrieveClazz());
  }
  private InputStream open() throws ClassNotFoundException {
    final InputStream $ = CLASSFILES.open(className());
    if ($ == null)
      throw new ClassNotFoundException();
    return $;
  }
  private String retrieveClassName() {
    assert clazz != null;
    final String $ = clazz.getName();
    for (Class<?> ¢ = clazz; ¢ != null;) {
      if (¢.isMemberClass()) {
        ¢ = ¢.getDeclaringClass();
        $.replaceFirst("\\.([a-zA-Z0-9$]+)$", "\\$$1");
        continue;
      }
      if (¢.isLocalClass() || ¢.isAnonymousClass()) {
        ¢ = ¢.getEnclosingClass();
        $.replaceFirst("\\.([a-zA-Z0-9$]+)$", "\\$$1");
      }
    }
    return $;
  }
  private Class<? extends T> retrieveClazz() throws ClassNotFoundException {
    assert className != null;
    @SuppressWarnings("unchecked") final Class<T> $ = (Class<T>) findClass(className);
    return $;
  }
  private ClassInfo retrievePool() throws ClassNotFoundException {
    return ClassInfo.make(open());
  }
}
