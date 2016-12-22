package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lisp.*;

import java.io.*;
import java.lang.annotation.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Annotation;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.iteration.closures.*;

/** TDD of {@link scope}
 * @author Yossi Gil
 * @since 2016-12-15 */
@SuppressWarnings("static-method")
public class definitionTest extends ReflectiveTester {
  @field final Initializer initializer = find(Initializer.class);
  @field final TypeDeclaration clazz = find(TypeDeclaration.class);
  @field final Map<String, MarkerAnnotation> annotations = new LinkedHashMap<String, MarkerAnnotation>() {
    {
      put("@Test", null);
      put("@Ignore", null);
      put("@Override", null);
      for (@foreach final definition.Kind ¢ : definition.Kind.values())
        put("@" + ¢, null);
    }
    @field static final long serialVersionUID = 1L;
  };

  @Test public void a() {
    assert initializer != null;
  }

  List<Annotation> annotations() {
    return searchDescendants.forClass(Annotation.class).from(myCompilationUnit());
  }

  @Test public void b() {
    assert (initializer + "").indexOf("¢") >= 0;
  }

  @Test public void c() {
    assert clazz != null;
  }

  @Test public void d() {
    assert (clazz + "").indexOf("¢") >= 0;
  }

  @Test public void e() {
    assert first(searchDescendants.forClass(AnnotationTypeDeclaration.class).from(myCompilationUnit())) != null;
  }

  @Test public void f() {
    new ___().hashCode();
  }

  @Test public void g() {
    ___.Dummy.a1.hashCode();
  }

  @Test public void h() {
    ___.Dummy.a1.hashCode();
  }

  @Test public void i() {
    for (final MarkerAnnotation a : markers())
      for (final SimpleName ¢ : annotees.of(a))
        assert ¢ != null;
  }

  @Test public void r() {
    for (final MarkerAnnotation a : markers())
      if (definition.Kind.has((a + "").substring(1)))
        for (final SimpleName ¢ : annotees.of(a))
          azzert.that(a + "\n\t" + ¢ + "/" + ¢.getClass() + ":\n\t" + definition.kind(¢), "@" + definition.kind(¢), is(a + ""));
  }

  @Test public void x() {
    for (final MarkerAnnotation a : markers())
      if ("@for¢".equals(a + ""))
        for (final SimpleName ¢ : annotees.of(a))
          azzert.that(a + "\n\t" + ¢ + "/" + ¢.getClass() + ":\n\t" + definition.kind(¢), "@" + definition.kind(¢), is(a + ""));
  }

  @Test public void y() {
    for (final MarkerAnnotation a : markers())
      if ("@try¢".equals(a + ""))
        for (@foreach final SimpleName ¢ : annotees.of(a))
          azzert.that(a + "\n\t" + ¢ + "/" + ¢.getClass() + ":\n\t" + definition.kind(¢), "@" + definition.kind(¢), is(a + ""));
  }

  @Test public void s() {
    assert !definition.Kind.has(null);
  }

  @Test public void t() {
    assert !definition.Kind.has(hashCode() + "");
  }

  @Test public void u() {
    assert definition.Kind.has("method");
  }

  @Test public void v() {
    assert definition.Kind.has("enum¢");
  }

  @Test public void w() {
    assert definition.Kind.has("for¢");
  }

  @Test public void j() {
    for (@foreach final MarkerAnnotation ¢ : markers())
      assert annotations.containsKey(¢ + "") : ¢;
  }

  @Test public void k() {
    for (final MarkerAnnotation ¢ : markers())
      annotations.put(¢ + "", ¢);
    for (final String ¢ : annotations.keySet())
      assert annotations.get(¢) != null : "Annotatio " + ¢ + " not used; what I saw was: \n" + markers();
  }

  @Test public void l() {
    for (@foreach final Annotation a : annotations())
      for (@foreach final SimpleName ¢ : annotees.of(a))
        assert ¢ != null;
  }

  @Test public void m() {
    for (final MarkerAnnotation ¢ : markers())
      annotations.put(¢ + "", ¢);
    assert annotations.get("@try¢") != null;
  }

  List<MarkerAnnotation> markers() {
    return searchDescendants.forClass(MarkerAnnotation.class).from(myCompilationUnit());
  }

  @Test public void n() {
    for (@foreach final MarkerAnnotation ¢ : markers())
      annotations.put(¢ + "", ¢);
    assert annotations.get("@catch¢") != null;
  }

  @Test public void o() {
    for (@foreach final MarkerAnnotation ¢ : markers())
      annotations.put(¢ + "", ¢);
    assert annotations.get("@field") != null;
  }

  @Test @method public void p() {
    for (@foreach final MarkerAnnotation ¢ : markers())
      annotations.put(¢ + "", ¢);
    assert annotations.get("@Ignore") != null;
  }

  @Test @method public void q() {
    for (final MarkerAnnotation ¢ : markers())
      annotations.put(¢ + "", ¢);
    assert annotations.get("@enumConstant") != null;
  }

  @Ignore
  @class¢
  static class ___ {
    @interface¢
    interface A {
      @method int f();

      @field int i = 0;
    }

    /** This code is never used, it is to model our test */
    {
      // This should never happen
      if (new Object().hashCode() == new Object().hashCode() && hashCode() != hashCode())
        try (@try¢ FileReader f1 = new FileReader("a"); @try¢ FileReader f2 = new FileReader("b")) {
          @local int c1 = f1.read();
          @local final int z = 2 * c1;
          @local int c2 = f2.read() + new Object() {
            @field int x, y;

            @Override public int hashCode() {
              @local final Function<Object, String> $ = (@lambda final Object o) -> o + "";
              for (@foreach final char ¢ : (this + "").toCharArray())
                return sum(super.hashCode(), hashCode() * $.hashCode() + ¢);
              return sum(super.hashCode(), hashCode() * $.hashCode());
            }

            int sum(@parameter final int a, @parameter final int b) {
              return z + a + b + x + y;
            }
          }.hashCode();
          c1 ^= c2;
          ++c1;
          @local final int c0 = c1 - c2;
          --c2;
          c2 ^= c1;
          @local int c3 = c1 + c2;
          ++c3;
          if (c1 == c2)
            throw new CloneNotSupportedException(c0 * c3 + "");
        } catch (@catch¢ final FileNotFoundException x) {
          for (@for¢ int j = 0, ¢ = 0; ¢ < 10; j = 1, --j)
            ¢ += j;
          x.printStackTrace();
        } catch (@catch¢ final CloneNotSupportedException | IOException ¢) {
          ¢.printStackTrace();
        }
    }

    @Ignore
    @enum¢
    enum Dummy {
      // @formatter:off
      @enumConstant a1() { @Override public int f() { return a2.hashCode(); } },
      @enumConstant a2() { @Override public int f() { return a1.hashCode(); } };
      @method public int f() { return a1.f() + a2.f(); }
      // @formatter:on
      @Ignore
      interface empty {
        @Test void f();
      }
    }
  }

  // @formatter:off
  @annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE }) private @interface annotation { /** Empty */ }
    @annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE }) private @interface interface¢ { /** Empty */ }
    @annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE }) private @interface class¢ { /** Empty */ }
    @annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.TYPE }) private @interface enum¢ { /** Empty */ }
    @annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.METHOD }) private @interface method { /** Empty */ }
    @annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE }) private @interface enumConstant { /** Empty */ }
    @annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE }) private @interface field { /** Empty */ }
    @annotation @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE }) private @interface for¢ { /** Empty */ }
    @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE }) private @interface foreach { /** Empty */ }
    @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE }) private @interface local { /** local variable */ }
    @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE }) private @interface parameter { /** Empty */ }
    @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE }) private @interface try¢ { /** Empty */ }
    @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE }) private @interface lambda { /** lambda parameter */ }
    @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE }) private @interface catch¢ { /** Empty */ }
    // @formatter:on
}
