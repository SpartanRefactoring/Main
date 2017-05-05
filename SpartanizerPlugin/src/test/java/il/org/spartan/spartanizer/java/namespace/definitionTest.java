package il.org.spartan.spartanizer.java.namespace;

import static fluent.ly.azzert.*;

import java.io.*;
import java.lang.annotation.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Annotation;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.namespace.ZZZ___Fixture_ModelClass.InnerEnum.*;
import il.org.spartan.spartanizer.meta.*;
import il.org.spartan.utils.*;

/** @formatter:off */
@Target(ElementType.TYPE) @annotation @interface annotation { /**/ }
@Target(ElementType.METHOD) @annotation @interface annotationMemberDeclaration { /**/ }
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE }) @annotation @interface catch¢ { /**/ }
@Target({ ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE }) @annotation @interface class¢ { /**/ }
/** TDD of {@link definition}
 * @author Yossi Gil
 * @since 2016-12-15 */

@SuppressWarnings({ "static-method", "javadoc" })
public class definitionTest extends MetaFixture {
  @field private final Initializer initializer = find(Initializer.class);
  @field private final TypeDeclaration clazz = find(TypeDeclaration.class);
  @field private final Map<String, MarkerAnnotation> annotations = new LinkedHashMap<String, MarkerAnnotation>() {
    @field static final long serialVersionUID = 1;
    {
      put("@Test", null);
      put("@Ignore", null);
      put("@Override", null);
      put("@annotation", null);
      put("@class¢", null);
      put("@enum¢", null);
      put("@interface¢", null);
      as.list(definition.Kind.values()).forEach(λ -> put("@" + λ, null));
    }
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
    assert the.headOf(descendants.whoseClassIs(AnnotationTypeDeclaration.class).from(reflectedCompilationUnit())) != null;
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
      for ( final SimpleName ¢ : annotees.of(a))
        assert ¢ != null;
  }

  @Test public void a10() {
    for (@foreach final MarkerAnnotation ¢ : markers())
      assert annotations.containsKey(¢ + "") : "I did not see marker annotation:" + ¢;
  }

  @Test public void a11() {
    markers().forEach(λ -> annotations.put(λ + "", λ));
    for (final String ¢ : annotations.keySet())
      assert annotations.get(¢) != null : "Annotation " + ¢ + " not used; what I saw was: \n" + markers();
  }

  @Test public void a12() {
    for (@foreach final Annotation a : annotations())
      for ( @foreach final SimpleName ¢ : annotees.of(a))
        assert ¢ != null : a;
  }

  @ScopeSize(41) @Test public void a13() {
    markers().forEach(λ -> annotations.put(λ + "", λ));
    assert annotations.get("@try¢") != null;
  }

  @Test @method public void a14() {
    markers().forEach(λ -> annotations.put(λ + "", λ));
    assert annotations.get("@catch¢") != null;
  }

  @Test public void a15() {
    markers().forEach(λ -> annotations.put(λ + "", λ));
    assert annotations.get("@field") != null;
  }

  @Test @method public void a16() {
    markers().forEach(λ -> annotations.put(λ + "", λ));
    assert annotations.get("@Ignore") != null;
  }

  @Test @method public void a17() {
    markers().forEach(λ -> annotations.put(λ + "", λ));
    assert annotations.get("@enumConstant") != null;
  }

  @Test public void a18() {
    markers().stream().filter(λ -> definition.Kind.has((λ + "").substring(1))).forEach(a -> annotees.of(a)
        .forEach(λ -> azzert.that(a + "\n\t" + λ + "/" + λ.getClass() + ":\n\t" + definition.kind(λ), "@" + definition.kind(λ), is(a + ""))));
  }

  @Test @method public void a19() {
    for (final Annotation a : annotations()) {
       final SingleMemberAnnotation ¢ = az.singleMemberAnnotation(a);
      if (¢ != null)
        assert the.headOf(annotees.of(az.singleMemberAnnotation(¢))) != null : ¢;
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
    assert definition.Kind.has("enum¢") : definition.Kind.enum¢;
  }

  @Test public void a24() {
    assert definition.Kind.has("for¢") : definition.Kind.for¢;
  }

  @Test public void a25() {
    markers().stream().filter(λ -> "@for¢".equals(λ + "")).forEach(a -> annotees.of(a)
        .forEach(λ -> azzert.that(a + "\n\t" + λ + "/" + λ.getClass() + ":\n\t" + definition.kind(λ), "@" + definition.kind(λ), is(a + ""))));
  }

  @Test public void a26() {
    markers().stream().filter(λ -> "@try¢".equals(λ + "")).forEach(a -> annotees.of(a)
        .forEach(λ -> azzert.that(a + "\n\t" + λ + "/" + λ.getClass() + ":\n\t" + definition.kind(λ), "@" + definition.kind(λ), is(a + ""))));
  }

  @Test @method public void a27() {
    for (final Annotation a : annotations()) {
       final SingleMemberAnnotation x = az.singleMemberAnnotation(a);
      if (x != null && x.getTypeName().getFullyQualifiedName().endsWith(ScopeSize.class.getSimpleName() + ""))
        azzert.that(x + ": " + annotees.of(x) + ancestry(the.headOf(annotees.of(x))), scope.of(the.headOf(annotees.of(x))).size(),
            is(value(x)));
    }
  }

  @Test @method public void a28() {
    for (final Annotation a : annotations()) {
       final SingleMemberAnnotation ¢ = az.singleMemberAnnotation(a);
      if (¢ == null)
        continue;
      final List<SimpleName> ns = annotees.of(¢);
      assert ns != null;
      final SimpleName n = the.headOf(ns);
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
      final SimpleName n = the.headOf(ns);
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
      final SimpleName n = the.headOf(ns);
      assert n != null;
      if (!"raisin".equals(n + ""))
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
      final SimpleName n = the.headOf(ns);
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
      final SimpleName n = the.headOf(ns);
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
        final SimpleName n = the.headOf(annotees.of(x));
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
        final SimpleName n = the.headOf(annotees.of(x));
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
        final SimpleName n = the.headOf(annotees.of(x));
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
        final SimpleName n = the.headOf(annotees.of(x));
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
        final SimpleName n = the.headOf(annotees.of(x));
        if (!DummyClass.class.getSimpleName().equals(n + ""))
          continue;
        azzert.that(x + ": " + n + "/" + definition.kind(n), scope.of(n).size(), is(value(x)));
      }
    }
  }

   Collection<MarkerAnnotation> markers() {
    return descendants.whoseClassIs(MarkerAnnotation.class).from(reflectedCompilationUnit());
  }
}
// @formatter:off
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
@annotation @interface delme {  String[] value(); }
// @formatter:on
@ScopeSize(23)
@annotation
@interface DummyAnnotation {
  /**/}

@ScopeSize(23)
@class¢
class DummyClass { /**/}

@ScopeSize(23)
@enum¢
enum DummyEnum {
  /**/ }

@ScopeSize(23)
@interface¢
interface DummyInterface {/**/ }

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE })
@annotation
@interface enum¢ {
  /**/ }

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
@annotation
@interface enumConstant {
  /**/ }

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
@annotation
@interface field {
  /**/ }

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE })
@annotation
@interface for¢ {
  /**/ }

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, })
@annotation
@interface foreach {
  /**/ }

@Target({ ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE })
@annotation
@interface interface¢ {
  /**/ }

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, })
@interface lambda {
  /* lambda parameter */
}

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, })
@interface local {
  /* local variable */
}

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@annotation
@interface method {
  /**/ }

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, })
@interface parameter {
  /**/ }

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE })
@annotation
@interface ScopeSize {
  int value();
}

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, })
@interface try¢ {
  /**/ }

@Ignore
@class¢
@knows("__ definitionTest")
@SuppressWarnings("ALL")
class ZZZ___Fixture_ModelClass {
  /** This code is never used, it is to model our test */
  {
    // This should never happen
    if (new Object().hashCode() == new Object().hashCode() && hashCode() != hashCode()) {
      final int lemon = hashCode();
      try (
          /** First resource */
          @ScopeSize(4) @try¢ FileReader myFirstFileReader = new FileReader("a");
          /** Second resource */
          @ScopeSize(3) @try¢ FileReader resourceInTry = new FileReader("b" + myFirstFileReader.getEncoding()))
      /* Try body */
      {
        @knows({ "myFirstFileReader", "lemon" }) @local int localVariableInTryBlock = myFirstFileReader.read();
        @knows({ "localVariableInTryBlock", "myFirstFileReader" }) @local final int z = 2 * lemon * localVariableInTryBlock;
        @knows("resourceInTry") @local int localVariableNewClass = resourceInTry.read() + new Object() {
          @ScopeSize(4) @field @knows("localVariableNewClass") int fieldInAnonymousClass;
          @ScopeSize(4) @field int anotherFieldInAnonymousClass;

          @Override @ScopeSize(4) @method public int hashCode() {
            @local final Function<Object, String> $ = (@ScopeSize(1) @lambda final Object ¢) -> ¢ + "",
                something = (@ScopeSize(1) @lambda final Object ¢) -> {
                  ¢.getClass();
                  return ¢ + "";
                };
            for (@ScopeSize(1) @foreach final char ¢ : (this + "").toCharArray())
              return sum(super.hashCode(), hashCode() * $.hashCode() + ¢);
            return sum(super.hashCode(), hashCode() * $.hashCode()) + something.hashCode();
          }
          @ScopeSize(4) @method int sum(@ScopeSize(1) @parameter final int a, @ScopeSize(1) @parameter final int b) {
            return z + a + b + fieldInAnonymousClass + anotherFieldInAnonymousClass;
          }
        }.hashCode();
        localVariableInTryBlock ^= localVariableNewClass;
        ++localVariableInTryBlock;
        @local final int c0 = localVariableInTryBlock - localVariableNewClass;
        --localVariableNewClass;
        localVariableNewClass ^= localVariableInTryBlock;
        @ScopeSize(5) @knows({ "localVariableNewClass", "raisin" }) @local int raisin = localVariableInTryBlock + localVariableNewClass;
        @ScopeSize(3) @local int c8;
        ++localVariableNewClass;
        c8 = ++raisin;
        if (localVariableInTryBlock == c8 * localVariableNewClass)
          throw new CloneNotSupportedException(c0 * raisin + "");
      } catch (@knows("myIgnoredException") @ScopeSize(1) @catch¢ final FileNotFoundException myIgnoredException) {
        for (@foreign("resourceInTry") @knows({ "myIgnoredException", "water" }) @ScopeSize(3) @for¢ int water = 0; water < 10; --water) {
          @knows({ "water", "myIgnoredException", "fig" }) @local @ScopeSize(2) final int fig = 2 * water + hashCode();
          q(fig * fig + water * hashCode());
        }
        for (@ScopeSize(4) @for¢ int orange = 0, apple; orange < 10; --orange) {
          apple = 2 * orange + hashCode();
          @knows({ "orange", "myIgnoredException", "apple" }) final int banana = 2 * apple * (apple - 21) + 2;
          q(apple * apple + banana * banana * orange * hashCode());
          q(banana);
          q(apple * orange);
          orange *= banana - apple;
        }
        for (@knows("carrot") final int carrot : toString().toCharArray()) {
          @knows("carrot") final int cherry = 2 * carrot * (carrot - 21) + 2;
          q(carrot * cherry + cherry);
        }
        for (@ScopeSize(6) @for¢ int melon = 0, ¢ = 0 + melon; ¢ < 10 * melon; melon *= 2, --melon, ¢ = melon) {
          @knows({ "melon", "¢" }) final int variableInPlainFor = 2 * melon + hashCode();
          q(melon * hashCode() + variableInPlainFor * variableInPlainFor);
          ¢ += melon;
          q(melon * hashCode() + variableInPlainFor * variableInPlainFor);
        }
        for (@ScopeSize(8) @for¢ int pear, j = 0, variableInPlainFor = 12, ¢ = 0 + j; ¢ < 10 * j; j *= 2, --j, ¢ = j) {
          @knows({ "variableInPlainFor", "pear", "j", "anotherVariableInAnotherPlainFor" }) int anotherVariableInAnotherPlainFor = 2 * hashCode() + j
              + variableInPlainFor;
          pear = hashCode() * anotherVariableInAnotherPlainFor;
          anotherVariableInAnotherPlainFor >>>= pear;
          ¢ += j + anotherVariableInAnotherPlainFor;
          ++pear;
          q(pear + anotherVariableInAnotherPlainFor);
        }
        myIgnoredException.printStackTrace();
      } catch (@catch¢ final IOException | CloneNotSupportedException ¢) {
        note.io(¢);
      }
      @knows("lemon") @foreign({ "¢", "x", "bread", "pear", "resourceInTry" }) final int a = hashCode();
      q(a * a);
    }
  }

  private void q(final int ¢) {
    q(¢);
  }

  @annotation
  @interface foo {
    @ScopeSize(5) @field @knows({ "bar", "__ Bar", "foo", "fubar" }) int bar = 12;
    @ScopeSize(5) @field int foo = bar;
    @ScopeSize(5) @field int fubar = foo << bar;
    @ScopeSize(5) @field Bar acuda = Bar.abra, cadbara = Bar.cadabra;

    @ScopeSize(5)
    @enum¢
    enum Bar {
      @ScopeSize(3)
      @enumConstant
      @knows({ "cadabra", "vaz/0", "abra" })
      abra, @ScopeSize(3)
      @enumConstant
      cadabra;
      @knows({ "cadabra", "vaz/0", "abra" }) Bar vaz() {
        return vaz();
      }
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

    @ScopeSize(6)
    @annotation
    @interface AnnotationInAnEnum {
      @ScopeSize(7) @field int aaaaa = 1;
      @ScopeSize(7) @field int bbbbb = 2 * aaaaa, ccccc = aaaaa * bbbbb, ddddd = 2;

      // @formatter:on
      // @formatter:off
      @annotationMemberDeclaration @ScopeSize(7) int u1();
      @annotationMemberDeclaration @ScopeSize(7) int u2();
      @annotationMemberDeclaration @ScopeSize(7) int u3();
      @annotationMemberDeclaration @ScopeSize(7) int u4();
      @annotationMemberDeclaration @ScopeSize(7) int u5() default 1;
    }

    @ScopeSize(6)
    abstract class ClassInAnEnum {
      @ScopeSize(1) @knows({ "__ ClassInAnEnum", "b" }) @method abstract void abstractMethodInClass(@parameter @ScopeSize(0) int a,
          @parameter @ScopeSize(0) int b);
    }

    @ScopeSize(6)
    interface InterfaceInAnEnum {
      @ScopeSize(1) @method void u();
    }
  }

  @interface¢
  interface InnerInterface {
    @ScopeSize(4) @field int staticFieldInInnerInterface = 0;

    @ScopeSize(4) @method static int staticMethodInInnerInterface() {
      return 12;
    }

    @ScopeSize(4) @method default int defaultMethodInInnerInterface() {
      return 12;
    }

    @ScopeSize(4) @method int methodInInnerInterface();
  }
}
