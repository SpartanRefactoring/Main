package il.org.spartan.spartanizer.ast.navigate;

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
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

// @formatter:on
@annotation
@Target({ ElementType.TYPE })
@interface annotation {
  /**/ }

@annotation
@Target({ ElementType.METHOD })
@interface annotationMemberDeclaration {
  /**/ }

@annotation
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE })
@interface catch¢ {
  /**/ }

@annotation
@Target({ ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE })
@interface class¢ {
  /**/ }

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
      for (@foreach final definition.Kind ¢ : definition.Kind.values())
        put("@" + ¢, null);
    }
    @field static final long serialVersionUID = 1L;
  };

  @Test public void a01() {
    assert initializer != null;
  }

  @Test public void a02() {
    assert (initializer + "").indexOf("¢") >= 0;
  }

  @Test public void a03() {
    assert clazz != null;
  }

  @Test public void a04() {
    assert (clazz + "").indexOf("¢") >= 0;
  }

  @Test public void a05() {
    assert first(searchDescendants.forClass(AnnotationTypeDeclaration.class).from(myCompilationUnit())) != null;
  }

  @Test public void a06() {
    new ZZZModelClass().hashCode();
  }

  @Test public void a07() {
    ZZZModelClass.InnerEnum.enumConstant2.hashCode();
  }

  @Test public void a08() {
    ZZZModelClass.InnerEnum.enumConstant1.hashCode();
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

  @Test @scopeSize(44) public void a13() {
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
      if (x != null && x.getTypeName().getFullyQualifiedName().endsWith(scopeSize.class.getSimpleName() + ""))
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
      if (x != null && (x.getTypeName() + "").equals(scopeSize.class.getSimpleName() + "")) {
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
      if (x != null && (x.getTypeName() + "").equals(scopeSize.class.getSimpleName() + "")) {
        final SimpleName n = first(annotees.of(x));
        if (!"InterfaceInAnEnum".equals(n + ""))
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
      if (x != null && (x.getTypeName() + "").equals(scopeSize.class.getSimpleName() + "")) {
        final SimpleName n = first(annotees.of(x));
        if (!"DummyAnnotation".equals(n + ""))
          continue;
        azzert.that(x + ": " + n + "/" + definition.kind(n), scope.of(n).size(), is(value(x)));
      }
    }
  }

  @Test @method public void a37() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation x = az.singleMemberAnnotation(a);
      if (x != null && (x.getTypeName() + "").equals(scopeSize.class.getSimpleName() + "")) {
        final SimpleName n = first(annotees.of(x));
        if (!"DummyInterface".equals(n + ""))
          continue;
        azzert.that(x + ": " + n + "/" + definition.kind(n), scope.of(n).size(), is(value(x)));
      }
    }
  }

  @Test @method public void a38() {
    for (final Annotation a : annotations()) {
      final SingleMemberAnnotation x = az.singleMemberAnnotation(a);
      if (x != null && (x.getTypeName() + "").equals(scopeSize.class.getSimpleName() + "")) {
        final SimpleName n = first(annotees.of(x));
        if (!"DummyClass".equals(n + ""))
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
  public static class ______ extends ReflectiveTester {
    public ______(final SimpleName name, final Integer scopeSize, final definition.Kind kind) {
      assert name != null;
      assert scopeSize != null;
      this.name = name;
      this.scopeSize = scopeSize;
      this.kind = kind;
    }

    private final SimpleName name;
    private final Integer scopeSize;
    private final definition.Kind kind;

    @Test public void test() {
      azzert.that(
          "\n name = " + name + //
              "\n\t kind = " + kind + //
              ancestry(name) + //
              "\n\t scope = " + scope.of(name)//
          , scope.of(name).size(), is(scopeSize.intValue()));
    }

    @Parameters(name = "{index} {0}/{2}={1}") public static Collection<Object[]> data() {
      final List<Object[]> $ = new ArrayList<>();
      for (final Annotation a : new definitionTest().annotations()) {
        final SingleMemberAnnotation sma = az.singleMemberAnnotation(a);
        if (sma != null && (sma.getTypeName() + "").equals(scopeSize.class.getSimpleName() + "")) {
          final SimpleName name = first(annotees.of(sma));
          $.add(as.array(name, Integer.valueOf(value(sma)), definition.kind(name)));
        }
      }
      return $;
    }
  }
}

@scopeSize(22)
@annotation
@interface DummyAnnotation {
  /**/}

@class¢
@scopeSize(22)
class DummycClass { /**/}

@enum¢
@scopeSize(22)
enum DummyEnum {
  /**/ }

@interface¢
@scopeSize(22)
interface DummyInterface {/**/ }

@annotation
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE })
@interface enum¢ {
  /**/ }

@annotation
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
@interface enumConstant {
  /**/ }

@annotation
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
@interface field {
  /**/ }

@annotation
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
@interface for¢ {
  /**/ }

@annotation
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, })
@interface foreach {
  /**/ }

@annotation
@Target({ ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE })
@interface interface¢ {
  /**/ }

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, })
@interface lambda {
  /** lambda parameter */
}

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, })
@interface local {
  /** local variable */
}

@annotation
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@interface method {
  /**/ }

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, })
@interface parameter {
  /**/ }

@annotation
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE })
@interface scopeSize {
  int value();
}

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, })
@interface try¢ {
  /**/ }
// @formatter:on

@Ignore
@class¢
class ZZZModelClass {
  /** This code is never used, it is to model our test */
  {
    // This should never happen
    if (new Object().hashCode() == new Object().hashCode() && hashCode() != hashCode())
      try (@try¢ FileReader f1 = new FileReader("a"); @try¢ FileReader f2 = new FileReader("b")) {
        @local int c1 = f1.read();
        @local final int z = 2 * c1;
        @local int c2 = f2.read() + new Object() {
          @field @scopeSize(4) int x;
          @field @scopeSize(4) int y;

          @Override @method @scopeSize(4) public int hashCode() {
            @local final Function<Object, String> $ = (@lambda final Object o) -> o + "";
            for (@foreach final char ¢ : (this + "").toCharArray())
              return sum(super.hashCode(), hashCode() * $.hashCode() + ¢);
            return sum(super.hashCode(), hashCode() * $.hashCode());
          }

          @method @scopeSize(4) int sum(@parameter final int a, @parameter final int b) {
            return z + a + b + x + y;
          }
        }.hashCode();
        c1 ^= c2;
        ++c1;
        @local final int c0 = c1 - c2;
        --c2;
        c2 ^= c1;
        @scopeSize(5) @local int c3 = c1 + c2;
        @scopeSize(3) @local int c8;
        ++c2;
        c8 = ++c3;
        if (c1 == c2 * c8)
          throw new CloneNotSupportedException(c0 * c3 + "");
      } catch (@scopeSize(1) @catch¢ final FileNotFoundException x) {
        for (@for¢ int j = 0, ¢ = 0; ¢ < 10; j = 1, --j)
          ¢ += j;
        x.printStackTrace();
      } catch (@catch¢ final CloneNotSupportedException | IOException ¢) {
        ¢.printStackTrace();
      }
  }

  @interface¢
  interface A {
    @method @scopeSize(2) int asdfa();

    @field @scopeSize(2) int i = 0;
  }

  @enum¢
  enum InnerEnum {
    @scopeSize(6)
    @enumConstant
    enumConstant1() {
      @Override public int fenum() {
        return InnerEnum.enumConstant2.hashCode();
      }
    },
    @scopeSize(6)
    @enumConstant
    enumConstant2() {
      @Override public int fenum() {
        return InnerEnum.enumConstant1.hashCode();
      }
    };
    @method @scopeSize(6) public int fenum() {
      return hashCode() * enumConstant1.hashCode() + enumConstant2.fenum();
    }

    @annotation
    @scopeSize(6)
    @interface AnnotationInAnEnum {
      // @formatter:off
      @annotationMemberDeclaration @scopeSize(7) int u1();
      @annotationMemberDeclaration @scopeSize(7) int u2();
      @annotationMemberDeclaration @scopeSize(7) int u3();
      @annotationMemberDeclaration @scopeSize(7) int u4();
      @annotationMemberDeclaration @scopeSize(7) int u5() default 1;
      @field @scopeSize(7) static final int  i = 1;
      @field @scopeSize(7) static int  j = i, k = 1;
      // @formatter:on
    }

    @scopeSize(6)
    abstract class ClassInAnEnum {
      @method @scopeSize(1) abstract void u();
    }

    @scopeSize(6)
    interface InterfaceInAnEnum {
      @method @scopeSize(1) void u();
    }
  }
}
