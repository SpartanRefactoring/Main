package il.org.spartan.spartanizer.java.namespace;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lisp.*;

import java.io.*;
import java.lang.annotation.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Annotation;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.*;
import il.org.spartan.iteration.closures.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.namespace.ZZZ___Fixture_ModelClass.InnerEnum.*;
import il.org.spartan.spartanizer.utils.*;

/** TDD of {@link definition}
 * @author Yossi Gil
 * @since 2016-12-15 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class definitionTest extends ReflectiveTester {
  @field final Initializer initializer = find(Initializer.class);
  @field final TypeDeclaration clazz = find(TypeDeclaration.class);
  @field final Map<String, MarkerAnnotation> annotations = new LinkedHashMap<String, MarkerAnnotation>() {
    {
      put("@Test", null);
      put("@Ignore", null);
      put("@Override", null);
      put("@annotation", null);
      put("@class¢", null);
      put("@enum¢", null);
      put("@interface¢", null);
      for (@ScopeSize(1) @foreach final definition.Kind ¢ : definition.Kind.values())
        put("@" + ¢, null);
    }
    @field static final long serialVersionUID = 1L;
  };

  @Test public void a01() {
    assert initializer != null;
  }

  @Test public void a02() {
    assert (initializer + "").contains("¢");
  }

  @Test public void a03() {
    assert clazz != null;
  }

  @Test public void a04() {
    assert (clazz + "").contains("¢");
  }

  @Test public void a05() {
    assert first(searchDescendants.forClass(AnnotationTypeDeclaration.class).from(myCompilationUnit())) != null;
  }

  @Test public void a06() {
    new ZZZ___Fixture_ModelClass().hashCode();
  }

  @Test public void a07() {
    ZZZ___Fixture_ModelClass.InnerEnum.enumConstant2.hashCode();
  }

  @Test public void a08() {
    ZZZ___Fixture_ModelClass.InnerEnum.enumConstant1.hashCode();
  }

  @Test public void a09() {
    for (final MarkerAnnotation a : markers())
      for (final SimpleName ¢ : annotees.of(a))
        assert ¢ != null;
  }

  @Test public void a10() {
    for (@foreach final MarkerAnnotation ¢ : markers())
      assert annotations.containsKey(¢ + "") : "I did not see marker annotation:" + ¢;
  }

  @Test public void a11() {
    for (final MarkerAnnotation ¢ : markers())
      annotations.put(¢ + "", ¢);
    for (final String ¢ : annotations.keySet())
      assert annotations.get(¢) != null : "Annotation " + ¢ + " not used; what I saw was: \n" + markers();
  }

  @Test public void a12() {
    for (@foreach final Annotation a : annotations())
      for (@foreach final SimpleName ¢ : annotees.of(a))
        assert ¢ != null : a;
  }

  @ScopeSize(45) @Test public void a13() {
    for (final MarkerAnnotation ¢ : markers())
      annotations.put(¢ + "", ¢);
    assert annotations.get("@try¢") != null;
  }

  @Test @method public void a14() {
    for (@foreach final MarkerAnnotation ¢ : markers())
      annotations.put(¢ + "", ¢);
    assert annotations.get("@catch¢") != null;
  }

  @Test public void a15() {
    for (@foreach final MarkerAnnotation ¢ : markers())
      annotations.put(¢ + "", ¢);
    assert annotations.get("@field") != null;
  }

  @Test @method public void a16() {
    for (@foreach final MarkerAnnotation ¢ : markers())
      annotations.put(¢ + "", ¢);
    assert annotations.get("@Ignore") != null;
  }

  @Test @method public void a17() {
    for (final MarkerAnnotation ¢ : markers())
      annotations.put(¢ + "", ¢);
    assert annotations.get("@enumConstant") != null;
  }

  @Test public void a18() {
    for (final MarkerAnnotation a : markers())
      if (definition.Kind.has((a + "").substring(1)))
        for (final SimpleName ¢ : annotees.of(a))
          azzert.that(a + "\n\t" + ¢ + "/" + ¢.getClass() + ":\n\t" + definition.kind(¢), //
              "@" + definition.kind(¢), is(a + ""));
  }

  @Test @method public void a19() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation ¢ = az.singleMemberAnnotation(a);
      if (¢ != null)
        assert first(annotees.of(az.singleMemberAnnotation(¢))) != null : ¢;
    }
  }

  @Test public void a20() {
    assert !definition.Kind.has(null);
  }

  @Test public void a21() {
    assert !definition.Kind.has(hashCode() + "");
  }

  @Test public void a22() {
    assert definition.Kind.has("method");
  }

  @Test public void a23() {
    assert definition.Kind.has("enum¢");
  }

  @Test public void a24() {
    assert definition.Kind.has("for¢");
  }

  @Test public void a25() {
    for (final MarkerAnnotation a : markers())
      if ("@for¢".equals(a + ""))
        for (final SimpleName ¢ : annotees.of(a))
          azzert.that(a + "\n\t" + ¢ + "/" + ¢.getClass() + ":\n\t" + definition.kind(¢), "@" + definition.kind(¢), is(a + ""));
  }

  @Test public void a26() {
    for (final MarkerAnnotation a : markers())
      if ("@try¢".equals(a + ""))
        for (@foreach final SimpleName ¢ : annotees.of(a))
          azzert.that(a + "\n\t" + ¢ + "/" + ¢.getClass() + ":\n\t" + definition.kind(¢), "@" + definition.kind(¢), is(a + ""));
  }

  @Test @method public void a27() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation x = az.singleMemberAnnotation(a);
      if (x != null && x.getTypeName().getFullyQualifiedName().endsWith(ScopeSize.class.getSimpleName() + ""))
        azzert.that(x + ": " + annotees.of(x) + ancestry(first(annotees.of(x))), scope.of(first(annotees.of(x))).size(), is(value(x)));
    }
  }

  @Test @method public void a28() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation ¢ = az.singleMemberAnnotation(a);
      if (¢ == null)
        continue;
      final List<SimpleName> ns = annotees.of(¢);
      assert ns != null;
      final SimpleName n = first(ns);
      assert n != null;
      final List<? extends ASTNode> s = scope.of(n);
      assert s != null : fault.dump() + //
          "\n\t scope = " + s + //
          "\n\t ¢ = " + ¢ + //
          "\n\t n = " + n + //
          "\n\t ns = " + ns + //
          fault.done();
    }
  }

  @Test @method public void a29() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation ¢ = az.singleMemberAnnotation(a);
      if (¢ == null)
        continue;
      final List<SimpleName> ns = annotees.of(¢);
      assert ns != null;
      final SimpleName n = first(ns);
      assert n != null;
      if (!"hashCode".equals(n + ""))
        continue;
      final List<? extends ASTNode> s = scope.of(n);
      assert s != null : fault.dump() + //
          "\n\t scope = " + s + //
          "\n\t ¢ = " + ¢ + //
          "\n\t n = " + n + //
          "\n\t ns = " + ns + //
          fault.done();
    }
  }

  @Test @method public void a30() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation ¢ = az.singleMemberAnnotation(a);
      if (¢ == null)
        continue;
      final List<SimpleName> ns = annotees.of(¢);
      assert ns != null;
      final SimpleName n = first(ns);
      assert n != null;
      if (!"c3".equals(n + ""))
        continue;
      final List<? extends ASTNode> s = scope.of(n);
      assert s != null : fault.dump() + //
          "\n\t scope = " + s + //
          "\n\t ¢ = " + ¢ + //
          "\n\t n = " + n + //
          "\n\t ns = " + ns + //
          fault.done();
    }
  }

  @Test @method public void a31() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation ¢ = az.singleMemberAnnotation(a);
      if (¢ == null)
        continue;
      final List<SimpleName> ns = annotees.of(¢);
      assert ns != null;
      final SimpleName n = first(ns);
      assert n != null;
      if (!"enumConstant1".equals(n + ""))
        continue;
      final List<? extends ASTNode> s = scope.of(n);
      assert s != null : fault.dump() + //
          "\n\t scope = " + s + //
          "\n\t ¢ = " + ¢ + //
          "\n\t n = " + n + //
          "\n\t ns = " + ns + //
          fault.done();
    }
  }

  @Test @method public void a32() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation ¢ = az.singleMemberAnnotation(a);
      if (¢ == null)
        continue;
      final List<SimpleName> ns = annotees.of(¢);
      assert ns != null;
      final SimpleName n = first(ns);
      assert n != null;
      if (!"annotation".equals(n + ""))
        continue;
      final List<? extends ASTNode> s = scope.of(n);
      assert s != null : fault.dump() + //
          "\n\t scope = " + s + //
          "\n\t ¢ = " + ¢ + //
          "\n\t n = " + n + //
          "\n\t ns = " + ns + //
          fault.done();
    }
  }

  @Test @method public void a33() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation x = az.singleMemberAnnotation(a);
      if (x != null && (x.getTypeName() + "").equals(ScopeSize.class.getSimpleName() + "")) {
        final SimpleName n = first(annotees.of(x));
        if (!"fenum".equals(n + ""))
          continue;
        azzert.that(x + ": " + n + "/" + definition.kind(n), scope.of(n).size(), is(value(x)));
      }
    }
  }

  @Test @method public void a34() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation x = az.singleMemberAnnotation(a);
      if (x != null && (x.getTypeName() + "").equals(ScopeSize.class.getSimpleName() + "")) {
        final SimpleName n = first(annotees.of(x));
        if (!InterfaceInAnEnum.class.getSimpleName().equals(n + ""))
          continue;
        final int size = scope.of(n).size();
        assert size >= 0;
        azzert.that(x + ": " + n + "/" + definition.kind(n) + ancestry(n), size, is(value(x)));
      }
    }
  }

  @Test @method public void a35() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation x = az.singleMemberAnnotation(a);
      if (x != null && (x.getTypeName() + "").equals(ScopeSize.class.getSimpleName() + "")) {
        final SimpleName n = first(annotees.of(x));
        if (!DummyAnnotation.class.getSimpleName().equals(n + ""))
          continue;
        azzert.that(x + ": " + n + "/" + definition.kind(n), scope.of(n).size(), is(value(x)));
      }
    }
  }

  @Test @method public void a37() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation x = az.singleMemberAnnotation(a);
      if (x != null && (x.getTypeName() + "").equals(ScopeSize.class.getSimpleName() + "")) {
        final SimpleName n = first(annotees.of(x));
        if (!DummyInterface.class.getSimpleName().equals(n + ""))
          continue;
        azzert.that(x + ": " + n + "/" + definition.kind(n), scope.of(n).size(), is(value(x)));
      }
    }
  }

  @Test @method public void a38() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation x = az.singleMemberAnnotation(a);
      if (x != null && (x.getTypeName() + "").equals(ScopeSize.class.getSimpleName() + "")) {
        final SimpleName n = first(annotees.of(x));
        if (!DummyClass.class.getSimpleName().equals(n + ""))
          continue;
        azzert.that(x + ": " + n + "/" + definition.kind(n), scope.of(n).size(), is(value(x)));
      }
    }
  }

  List<MarkerAnnotation> markers() {
    return searchDescendants.forClass(MarkerAnnotation.class).from(myCompilationUnit());
  }

  static String ancestry(final ASTNode n) {
    String $ = "";
    int i = 0;
    for (final ASTNode p : ancestors.of(n))
      $ += "\n\t + " + i++ + ": " + wizard.trim(p + "") + "/" + p.getClass().getSimpleName();
    return $;
  }

  static int value(final SingleMemberAnnotation x) {
    return az.throwing.int¢(az.numberLiteral(x.getValue()).getToken());
  }

  @RunWith(Parameterized.class)
  public static class ScopeSizeTest extends ReflectiveTester {
    static final String SCOPE_SIZE = ScopeSize.class.getSimpleName() + "";

    public ScopeSizeTest(final SimpleName name, final Integer ScopeSize, final definition.Kind kind) {
      assert name != null;
      assert ScopeSize != null;
      this.name = name;
      scopeSize = ScopeSize;
      this.kind = kind;
    }

    private final SimpleName name;
    private final Integer scopeSize;
    private final definition.Kind kind;

    @Test public void test() {
      azzert.that(
          "\n name = " + name + //
          "\n expected = " + scopeSize + //
          "\n got = " + scope.of(name).size() + //
              "\n\t kind = " + kind + //
              ancestry(name) + //
              "\n\t scope = " + scope.of(name)//
          , scope.of(name).size(), is(scopeSize.intValue()));
    }

    @Parameters(name = "{index} {0}/{2}={1}") public static Collection<Object[]> data() {
      final List<Object[]> $ = new ArrayList<>();
      for (final Annotation a : new definitionTest().annotations()) {
        final SingleMemberAnnotation sma = az.singleMemberAnnotation(a);
        if (sma != null && (sma.getTypeName() + "").equals(SCOPE_SIZE)) {
          int expected = value(sma);
          for (final SimpleName ¢ : annotees.of(sma)) {
            $.add(as.array(¢, Integer.valueOf(expected), definition.kind(¢)));
            if (definition.kind(¢) != definition.Kind.field)
              --expected;
          }
        }
      }
      return $;
    }
  }

  @RunWith(Parameterized.class)
  public static class SingleMarkerTest extends ReflectiveTester {
    public SingleMarkerTest(final definition.Kind kind, SimpleName name) {
      assert name != null;
      this.name = name;
      this.kind = kind;
    }

    private final SimpleName name;
    private final definition.Kind kind;

    @Test public void test() {
      azzert.that(
          "\n name = " + name + //
              "\n\t kind = " + kind + //
              ancestry(name) + //
              "\n\t scope = " + scope.of(name)//
          , definition.kind(name), is(kind));
    }

    @Parameters(name = "{index}] {0} {1}") public static Collection<Object[]> data() {
      final List<Object[]> $ = new ArrayList<>();
      for (final MarkerAnnotation a : new definitionTest().markers()) {
        final String key = (a + "").substring(1);
        if (!definition.Kind.has(key))
          continue;
        for (final SimpleName ¢ : annotees.of(a))
          $.add(as.array(definition.Kind.valueOf(key), ¢));
      }
      return $;
    }
  }
}

/** @formatter:off */

@annotation @Target({ ElementType.TYPE }) @interface annotation { /**/ }
@annotation @Target({ ElementType.METHOD }) @interface annotationMemberDeclaration { /**/ }
@annotation
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE })
@interface catch¢ {
  /**/ }

@annotation
@Target({ ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE })
@interface class¢ {
  /**/ }

@ScopeSize(23) @annotation @interface DummyAnnotation { /**/}
@ScopeSize(23) @class¢ class DummyClass { /**/}
@ScopeSize(23) @enum¢ enum DummyEnum { /**/ }
@ScopeSize(23) @interface¢ interface DummyInterface {/**/ }
@annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE }) @interface enum¢ { /**/ }
@annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE }) @interface enumConstant { /**/ }
@annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE }) @interface field { /**/ }
@annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE}) @interface for¢ { /**/ }
@annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, }) @interface foreach { /**/ }
@annotation @Target({ ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE }) @interface interface¢ { /**/ }
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, }) @interface lambda { /** lambda parameter */ }
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, }) @interface local { /** local variable */ }
@annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD }) @interface method { /**/ }
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, }) @interface parameter { /**/ }
@annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE }) @interface ScopeSize { int value(); }
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, }) @interface try¢ { /**/ }

// @formatter:on
@annotation
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
@interface above {
  String[] value();
}

@Ignore
@class¢
class ZZZ___Fixture_ModelClass {
  /** This code is never used, it is to model our test */
  {
    // This should never happen
    if (new Object().hashCode() == new Object().hashCode() && hashCode() != hashCode())
      try (@ScopeSize(4) @try¢ FileReader xxxxx = new FileReader("a");
          @ScopeSize(3) @try¢ FileReader yyyyy = new FileReader("b" + xxxxx.getEncoding())) {
        @local int c1 = xxxxx.read();
        @local final int z = 2 * c1;
        @local int c2 = yyyyy.read() + new Object() {
          @ScopeSize(4) @field int fieldInAnonymousClass;
          @ScopeSize(4) @field int anotherFieldInAnonymousClass;

          @Override @ScopeSize(4) @method public int hashCode() {
            @local final Function<Object, String> $ = (@ScopeSize(1) @lambda final Object o) -> o + "";
            @local final Function<Object, String> something = (@ScopeSize(1) @lambda final Object o) -> {
              o.getClass();
              return o + "";
            };
            for (@ScopeSize(1) @foreach final char ¢ : (this + "").toCharArray())
              return sum(super.hashCode(), hashCode() * $.hashCode() + ¢);
            return sum(super.hashCode(), hashCode() * $.hashCode()) + something.hashCode();
          }

          @ScopeSize(4) @method int sum(@ScopeSize(1) @parameter final int a, @ScopeSize(1) @parameter final int b) {
            return z + a + b + fieldInAnonymousClass + anotherFieldInAnonymousClass;
          }
        }.hashCode();
        c1 ^= c2;
        ++c1;
        @local final int c0 = c1 - c2;
        --c2;
        c2 ^= c1;
        @ScopeSize(7) @local int c3 = c1 + c2;
        @ScopeSize(5) @local int c8;
        ++c2;
        @SuppressWarnings("unused") @above("c2") int __;
        c8 = ++c3;
        if (c1 == c2 * c8)
          throw new CloneNotSupportedException(c0 * c3 + "");
        @SuppressWarnings("unused") @above({"c2", "c1"}) int ___;
      } catch (@ScopeSize(1) @catch¢ final FileNotFoundException x) {
        for (@ScopeSize(3) @for¢ int j583 = 0; j583 < 10; --j583) {
          @local @ScopeSize(2) final int a = 2 * j583 + hashCode();
          System.out.println(a * a + j583 * hashCode());
        }
        for (@ScopeSize(4) @for¢ int a34j = 0, a; a34j < 10; --a34j) {
          a = 2 * a34j + hashCode();
          System.out.println(a * a + a34j * hashCode());
        }
        for (@ScopeSize(6) @for¢ int j123 = 0, ¢ = 0 + j123; ¢ < 10 * j123; j123 *= 2, --j123, ¢ = j123) {
          final int a = 2 * j123 + hashCode();
          System.out.println(a * a + j123 * hashCode());
          ¢ += j123;
          System.out.println(a * a + j123 * hashCode());
        }
        for (@ScopeSize(7) @for¢ int kay3, j = 0, ¢ = 0 + j; ¢ < 10 * j; j *= 2, --j, ¢ = j) {
          kay3 = hashCode();
          ¢ += j;
          ++kay3;
          System.out.println(kay3);
        }
        x.printStackTrace();
      } catch (@catch¢ final CloneNotSupportedException | IOException ¢) {
        ¢.printStackTrace();
      }
  }

  @annotation
  @interface foo {
    @ScopeSize(5) @field static int bar = 12;
    @ScopeSize(5) @field static int foo = bar;
    @ScopeSize(5) @field static int fubar = foo << bar;

    @ScopeSize(5)
    @enum¢
    enum Bar {
      @ScopeSize(3)
      @enumConstant
      abra, @enumConstant
      @ScopeSize(3)
      cadabra;
      Bar vaz() {
        return vaz();
      }
    }

    @ScopeSize(5) @field static Bar acuda = Bar.abra, cadbara = Bar.cadabra;
  }

  @interface¢
  interface InnerInterface {
    @ScopeSize(4) @method int methodInInnerInterface();

    @ScopeSize(4) @field int staticFieldInInnerInterface = 0;

    @ScopeSize(4) @method static int staticMethodInInnerInterface() {
      return 12;
    }

    @ScopeSize(4) @method default int defaultMethodInInnerInterface() {
      return 12;
    }
  }

  @enum¢
  enum InnerEnum {
    @ScopeSize(6)
    @enumConstant
    enumConstant1() {
      @Override public int fenum() {
        return InnerEnum.enumConstant2.hashCode();
      }
    },
    @ScopeSize(6)
    @enumConstant
    enumConstant2() {
      @Override public int fenum() {
        return InnerEnum.enumConstant1.hashCode();
      }
    };
    @ScopeSize(6) @method public int fenum() {
      return hashCode() * enumConstant1.hashCode() + enumConstant2.fenum();
    }

    @annotation
    @ScopeSize(6)
    @interface AnnotationInAnEnum {
      // @formatter:off
      @annotationMemberDeclaration @ScopeSize(7) int u1();
      @annotationMemberDeclaration @ScopeSize(7) int u2();
      @annotationMemberDeclaration @ScopeSize(7) int u3();
      @annotationMemberDeclaration @ScopeSize(7) int u4();
      @annotationMemberDeclaration @ScopeSize(7) int u5() default 1;
      @ScopeSize(7) @field static final int  aaaaa = 1;
      @ScopeSize(7) @field static int  bbbbb = 2* aaaaa, ccccc =  aaaaa * bbbbb, ddddd=2;
      // @formatter:on
    }

    @ScopeSize(6)
    abstract class ClassInAnEnum {
      @ScopeSize(1) @method abstract void abstractMethodInClass(@parameter @ScopeSize(0) int a, @parameter @ScopeSize(0) int b);
    }

    @ScopeSize(6)
    interface InterfaceInAnEnum {
      @ScopeSize(1) @method void u();
    }
  }
}
