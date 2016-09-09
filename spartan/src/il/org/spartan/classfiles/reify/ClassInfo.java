package il.org.spartan.classfiles.reify;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import il.org.spartan.classfiles.*;
import il.org.spartan.classfiles.reify.ConstantPool.*;
import il.org.spartan.collections.*;

/** An in memory representation of a class file.
 * @author Yossi Gil */
public final class ClassInfo extends ConstantPoolEntity {
  public static ClassInfo make(final Class<?> c) {
    return make(CLASSFILES.open(c));
  }

  @SuppressWarnings("synthetic-access") public static AttributedEntity make(final DataInputStream s) {
    return new Builder(s).go();
  }

  public static ClassInfo make(final File f) {
    return new Builder(f).go();
  }

  public static ClassInfo make(final InputStream is) {
    return new Builder(is).go();
  }

  public static ConstantPoolEntity make(final String fileName) {
    return new Builder(fileName).go();
  }

  private static void addLinkComponents(final int[] target, final int[] addition) {
    if (addition == null)
      return;
    for (int i = 0; i < target.length; i++)
      target[i] += addition[i];
  }

  private static ConstructorInfo asConstructor(final TypedEntity t) {
    return new ConstructorInfo(t);
  }

  private static FieldInfo[] asFields(final TypedEntity[] fields) {
    final List<FieldInfo> $ = new ArrayList<>();
    for (final TypedEntity t : fields)
      if (!t.isSynthetic())
        $.add(new FieldInfo(t));
    return $.toArray(new FieldInfo[$.size()]);
  }

  private static InitializerInfo asInitializer(final TypedEntity t) {
    return new InitializerInfo(t);
  }

  private static MethodInfo asMethod(final TypedEntity t) {
    return new MethodInfo(t);
  }

  private static int codeSize(final ExecutableEntity[] es) {
    int $ = 0;
    for (final ExecutableEntity e : es)
      $ += e.codeSize();
    return $;
  }

  private static int cyclomaticComplexity(final ExecutableEntity[] es) {
    int $ = 0;
    for (final ExecutableEntity e : es)
      $ += e.cyclomaticComplexity();
    return $;
  }

  private static int instructionCount(final ExecutableEntity[] es) {
    int $ = 0;
    for (final ExecutableEntity e : es)
      $ += e.instructionCount();
    return $;
  }

  private static boolean isConstructor(final TypedEntity t) {
    return "<init>".equals(t.name);
  }

  private static boolean isInitializer(final TypedEntity t) {
    return "<clinit>".equals(t.name);
  }

  private static boolean isMethod(final TypedEntity t) {
    return !t.name.startsWith("<");
  }

  private static ConstructorInfo[] selectConstructors(final TypedEntity[] executables) {
    final List<ConstructorInfo> $ = new ArrayList<>();
    for (final TypedEntity t : executables)
      if (isConstructor(t) && !t.isSynthetic())
        $.add(asConstructor(t));
    return $.toArray(new ConstructorInfo[$.size()]);
  }

  private static InitializerInfo[] selectInitializers(final TypedEntity[] executables) {
    final List<InitializerInfo> $ = new ArrayList<>();
    for (final TypedEntity t : executables)
      if (isInitializer(t) && !t.isSynthetic())
        $.add(asInitializer(t));
    return $.toArray(new InitializerInfo[$.size()]);
  }

  private static MethodInfo[] selectMethods(final TypedEntity[] executables) {
    final List<MethodInfo> $ = new ArrayList<>();
    for (final TypedEntity t : executables)
      if (isMethod(t) && !t.isSynthetic())
        $.add(asMethod(t));
    return $.toArray(new MethodInfo[$.size()]);
  }

  private static int throwCount(final ExecutableEntity[] es) {
    int $ = 0;
    for (final ExecutableEntity e : es)
      $ += e.throwCount();
    return $;
  }

  private String containingJar = null;
  private String classFileName = null;
  final Map<String, Integer> refsToStatics = new HashMap<>();
  public final String superClass;
  public final ClassConstant[] interfaces;
  public final FieldInfo[] fields;
  public final MethodInfo[] methods;
  public final ConstructorInfo[] constructors;
  public final InitializerInfo[] initializers;
  public final String source;
  public final String packeageName;
  public final String shortName;

  ClassInfo(final Builder b) {
    super(b);
    packeageName = b.packageName;
    shortName = b.shortName;
    superClass = b.superClass;
    interfaces = b.interfaces;
    fields = asFields(b.fields);
    methods = selectMethods(b.executables);
    initializers = selectInitializers(b.executables);
    constructors = selectConstructors(b.executables);
    source = findSource();
  }

  public Abstraction abstraction() {
    return Abstraction.abstraction(this);
  }

  @Attribute public int abstractMethodCount() {
    int $ = 0;
    for (final MethodInfo m : methods)
      if (m.isAbstract())
        $++;
    return $;
  }

  @Attribute public int accessedPublicConstructorsCount() {
    int $ = 0;
    for (final ConstructorInfo c : constructors)
      if (c.isPublic() && isAccessed(c))
        $++;
    return $;
  }

  @Attribute public int accessedPublicFieldsCount() {
    int $ = 0;
    for (final FieldInfo f : fields)
      if (f.isPublic() && isAccessed(f))
        $++;
    return $;
  }

  @Attribute public int accessedPublicMethodsCount() {
    int $ = 0;
    for (final MethodInfo m : methods)
      if (m.isPublic() && isAccessed(m))
        $++;
    return $;
  }

  public void addRefToStatic(final String staticFieldOrMethod) {
    MapUtil.addToValue(refsToStatics, staticFieldOrMethod, 1);
  }

  @Attribute public int codeSize() {
    return codeSize(methods) + codeSize(constructors) + codeSize(initializers);
  }

  @Attribute public int concreteMethodCount() {
    int $ = 0;
    for (final MethodInfo m : methods)
      if (!m.isAbstract())
        $++;
    return $;
  }

  @Attribute public int constructorsCount() {
    return constructors.length;
  }

  @Attribute public int cyclomaticComplexity() {
    return cyclomaticComplexity(methods);
  }

  @Attribute public int defaultConstructorCount() {
    int $ = 0;
    for (final ConstructorInfo c : constructors)
      if (c.isDefault())
        $++;
    return $;
  }

  @Attribute public int defaultFieldCount() {
    int $ = 0;
    for (final FieldInfo f : fields)
      if (f.isDefault())
        $++;
    return $;
  }

  @Attribute public int defaultMethodCount() {
    int $ = 0;
    for (final MethodInfo m : methods)
      if (m.isDefault())
        $++;
    return $;
  }

  @Override public boolean equals(final Object obj) {
    if (obj == null)
      return false;
    if (obj.getClass() != getClass())
      return false;
    final ClassInfo other = (ClassInfo) obj;
    return other.name().equals(name()) && other.fieldsCount() == fieldsCount() && other.methodCount() == methodCount()
        && other.constructorsCount() == constructorsCount() && other.referencedClasses() == referencedClasses()
        && other.referencedDoubles() == referencedDoubles() && other.referencedInts() == referencedInts()
        && other.referencedFloats() == referencedFloats() && other.referencedStrings() == referencedStrings()
        && other.referencedLongs() == referencedLongs() && other.codeSize() == codeSize();
  }

  @Attribute public int fieldsCount() {
    return fields.length;
  }

  public String getClassFileName() {
    return classFileName;
  }

  public String getContainingJar() {
    return containingJar;
  }

  @Override public int hashCode() {
    return name().hashCode();
  }

  @Attribute public int initializersCount() {
    return initializers.length;
  }

  @Attribute public int instructionCount() {
    return instructionCount(methods) + instructionCount(constructors) + instructionCount(initializers);
  }

  @Attribute public int interfacesCount() {
    return interfaces.length;
  }

  /** Returns true if this {@code Class} object represents an annotation type.
   * Note that if this method returns true, {@link #isInterface()} would also
   * return true, as all annotation types are also interfaces.
   * @return {@code true} if this class object represents an annotation type;
   *         {@code false} otherwise
   * @since 1.5 */
  public boolean isAnnotation() {
    return (flags & ANNOTATION) != 0;
  }

  public boolean isClass() {
    return !isInterface() && !isEnum() && !isAnnotation();
  }

  public boolean isEnum() {
    return (flags & ENUM) != 0;
  }

  /** Determines if the specified {@code Class} object represents an interface
   * type.
   * @return {@code true} if this object represents an interface; {@code false}
   *         otherwise. */
  public boolean isInterface() {
    return Modifier.isInterface(flags);
  }

  /** Returns {@code true} if this class is a synthetic class; returns
   * {@code false} otherwise.
   * @return {@code true} if and only if this class is a synthetic class as
   *         defined by the Java Language Specification.
   * @since 1.5 */
  @Override public boolean isSynthetic() {
    return (flags & SYNTHETIC) != 0;
  }

  public Kind kind() {
    return Kind.kind(this);
  }

  @Attribute public int lackOfCohesion() {
    int emptyIntersect = 0;
    int nonEmptyIntersect = 0;
    for (final MethodInfo m1 : methods) {
      final Set<String> s1 = m1.instanceVariables();
      for (final MethodInfo m2 : methods) {
        if (m1.hashCode() >= m2.hashCode()) // inspect each pair only once
          continue;
        final Set<String> s2 = m2.instanceVariables();
        s2.retainAll(s1);
        boolean found = false;
        for (final String s : s2)
          if (s.startsWith(name + ":")) {
            nonEmptyIntersect++;
            found = true;
            break;
          }
        if (!found)
          emptyIntersect++;
      }
    }
    return Math.max(emptyIntersect - nonEmptyIntersect, 0);
  }

  @Attribute public int methodCount() {
    int $ = 0;
    for (final MethodInfo mi : methods)
      if (!mi.isObjectMethod())
        $++;
    return $;
  }

  @Attribute public int privateConstructorCount() {
    int $ = 0;
    for (final ConstructorInfo c : constructors)
      if (c.isPrivate())
        $++;
    return $;
  }

  @Attribute public int privateFieldCount() {
    int $ = 0;
    for (final FieldInfo f : fields)
      if (f.isPrivate())
        $++;
    return $;
  }

  @Attribute public int privateMethodCount() {
    int $ = 0;
    for (final MethodInfo m : methods)
      if (m.isPrivate())
        $++;
    return $;
  }

  @Attribute public int protectedConstructorCount() {
    int $ = 0;
    for (final ConstructorInfo c : constructors)
      if (c.isProtected())
        $++;
    return $;
  }

  @Attribute public int protectedFieldCount() {
    int $ = 0;
    for (final FieldInfo f : fields)
      if (f.isProtected())
        $++;
    return $;
  }

  @Attribute public int protectedMethodCount() {
    int $ = 0;
    for (final MethodInfo mi : methods)
      if (!mi.isObjectMethod() && mi.isProtected())
        $++;
    return $;
  }

  @Attribute public int publicConstructorCount() {
    int $ = 0;
    for (final ConstructorInfo c : constructors)
      if (c.isPublic())
        $++;
    return $;
  }

  @Attribute public int publicFieldCount() {
    int $ = 0;
    for (final FieldInfo f : fields)
      if (f.isPublic())
        $++;
    return $;
  }

  @Attribute public int publicMethodCount() {
    int $ = 0;
    for (final MethodInfo mi : methods)
      if (!mi.isObjectMethod() && mi.isPublic())
        $++;
    return $;
  }

  @Attribute public int publicNonStaticFieldCount() {
    int $ = 0;
    for (final FieldInfo f : fields)
      if (f.isPublic() && !f.isStatic())
        $++;
    return $;
  }

  @Attribute public int publicNonStaticMethodCount() {
    int $ = 0;
    for (final MethodInfo mi : methods)
      if (!mi.isObjectMethod() && mi.isPublic() && !mi.isStatic())
        $++;
    return $;
  }

  @Attribute public int publicStaticFieldCount() {
    int $ = 0;
    for (final FieldInfo f : fields)
      if (f.isPublic() && f.isStatic())
        $++;
    return $;
  }

  @Attribute public int publicStaticMethodCount() {
    int $ = 0;
    for (final MethodInfo mi : methods)
      if (!mi.isObjectMethod() && mi.isPublic() && mi.isStatic())
        $++;
    return $;
  }

  @Attribute public int referencedClasses() {
    return constantPool.getReferencedClasses().length;
  }

  @Attribute public int referencedDoubles() {
    return constantPool.getReferencedDoubles().length;
  }

  @Attribute public int referencedFloats() {
    return constantPool.getReferencedFloats().length;
  }

  @Attribute public int referencedInts() {
    return constantPool.getReferencedInts().length;
  }

  @Attribute public int referencedLongs() {
    return constantPool.getReferencedLongs().length;
  }

  @Attribute public int referencedStrings() {
    return constantPool.getReferencedStrings().length;
  }

  public int[] referencesToClass(final String className) {
    final int[] $ = new int[LinkComponents.values().length];
    for (final MethodInfo m : methods)
      addLinkComponents($, m.referencesToClass(className));
    for (final ConstructorInfo c : constructors)
      addLinkComponents($, c.referencesToClass(className));
    for (final InitializerInfo i : initializers)
      addLinkComponents($, i.referencesToClass(className));
    if (superClass.equals(className))
      $[LinkComponents.SuperClass.ordinal()]++;
    for (final ClassConstant i : interfaces)
      if (i.getClassName().equals(className))
        $[LinkComponents.SuperInterface.ordinal()]++;
    for (final FieldInfo f : fields)
      if (f.type.toString().equals(className))
        $[LinkComponents.FieldDeclaration.ordinal()]++;
    return $;
  }

  @Attribute public int responseForClass() {
    final String[] referencedMethods = constantPool.getReferencedMethods();
    int $ = 0;
    for (final String s : referencedMethods)
      if (!s.startsWith(name + ":"))
        $++;
    return $ + methodCount();
  }

  public void setClassFileName(final String fileName) {
    classFileName = fileName;
  }

  public void setContainingJar(final String jar) {
    containingJar = jar;
  }

  @Attribute public int throwCount() {
    return throwCount(methods) + throwCount(constructors) + throwCount(initializers);
  }

  private String findSource() {
    final AttributeInfo a = findAttribute("SourceFile");
    return a == null ? "" : a.reader(constantPool).readStringConstant();
  }

  private boolean isAccessed(final TypedEntity t) {
    for (final ExecutableEntity e : methods)
      if (e.isAccessed(t, name))
        return true;
    for (final ExecutableEntity e : constructors)
      if (e.isAccessed(t, name))
        return true;
    for (final ExecutableEntity e : initializers)
      if (e.isAccessed(t, name))
        return true;
    return false;
  }

  public enum Abstraction {
    ABSTRACT, FINAL, PLAIN;
    public static Abstraction abstraction(final ClassInfo c) {
      if (c.isAbstract())
        return ABSTRACT;
      if (c.isFinal())
        return FINAL;
      return PLAIN;
    }
  }

  /** @author Yossi Gil
   * @since 21 November 2011 */
  public static class Builder extends RobustReader {
    /** Magic number identifying Java class files */
    static final int MAGIC = 0xCAFEBABE;
    final int majorVersion;
    final int minorVersion;
    final ConstantPool constantPool;
    final int accessFlags;
    final AttributeInfo[] attributes;
    final TypedEntity[] executables;
    final TypedEntity[] fields;
    final ClassConstant[] interfaces;
    final String superClass;
    final String name;
    final String shortName;
    final String packageName;

    /** Instantiate this class from a given file name.
     * @param c a non-<code><b>null</b></code> representing a valid
     *        {@link Class} object */
    public Builder(final Class<?> c) {
      this(CLASSFILES.open(c));
    }

    /** Instantiate this class from a given file name.
     * @param f a non-<code><b>null</b></code> representing a valid
     *        <tt>.class</tt> file name @ * in case the file could not be opened
     *        or processed */
    public Builder(final File f) {
      this(asFileInputStream(f));
    }

    /** Instantiate this class from a given input stream
     * @param is a non-<code><b>null</b></code> representing an input stream
     *        containing a valid <tt>.class</tt> @ * in case the file could not
     *        be opened or processed */
    public Builder(final InputStream is) {
      this(asDataInputStream(is));
    }

    /** Instantiate this class from a given file name.
     * @param fileName a non-<code><b>null</b></code> representing a valid
     *        <tt>.class</tt> file name */
    public Builder(final String fileName) {
      this(new File(fileName));
    }

    /** Extracts the constant pool from the specified data stream of a class
     * file.
     * @param s a non-<code><b>null</b></code> providing a stream of a class
     *        file starting at the first byte. @ * in case of reading errors or
     *        invalid class file. */
    private Builder(final DataInputStream s) {
      super(s);
      if (readInt() != MAGIC)
        recordError(new Exception("Not a class file: bad magic number."));
      minorVersion = readUnsignedShort();
      majorVersion = readUnsignedShort();
      constantPool = new ConstantPool(this);
      final ConstantPoolReader r = new ConstantPoolReader(s, constantPool);
      accessFlags = readUnsignedShort();
      final int classIndex = readUnsignedShort();
      name = r.readClassName(classIndex);
      shortName = r.classShortName(classIndex);
      packageName = r.classPackage(classIndex);
      superClass = r.readClassName();
      interfaces = r.readClasses();
      fields = r.readMembers();
      executables = r.readMembers();
      attributes = r.readAttributes();
    }

    public String[] getReferencedClasses() {
      return constantPool.getReferencedClasses();
    }

    public double[] getReferencedDoubles() {
      return constantPool.getReferencedDoubles();
    }

    public float[] getReferencedFloats() {
      return constantPool.getReferencedFloats();
    }

    public int[] getReferencedInts() {
      return constantPool.getReferencedInts();
    }

    public long[] getReferencedLongs() {
      return constantPool.getReferencedLongs();
    }

    public String[] getReferencedStrings() {
      return constantPool.getReferencedStrings();
    }

    public ClassInfo go() {
      try {
        close();
      } catch (final Throwable e1) {
        e1.printStackTrace();
      }
      if (hasErrors())
        return null;
      try {
        return new ClassInfo(this);
      } catch (final CorruptClassFile e) {
        return null;
      }
    }
  }

  public enum Kind {
    INTERFACE, ANNOTATION, CLASS, ENUM;
    public static Kind kind(final ClassInfo c) {
      if (c.isInterface())
        return Kind.INTERFACE;
      if (c.isClass())
        return Kind.CLASS;
      if (c.isEnum())
        return Kind.ENUM;
      if (c.isAnnotation())
        return Kind.ANNOTATION;
      return null;
    }
  }

  public enum LinkComponents {
    SuperClass, SuperInterface, FieldDeclaration, MethodDeclaration, Instantiation, FieldAccess, StaticFieldAccess, MethodInvocation, StaticMethodInvocation
  }
}
