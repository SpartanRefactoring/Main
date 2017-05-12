package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.azzert.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Test for analyze.type
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0775 {
  @Test public void a() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(make.ast("class C{}")))) + "", is("C"));
  }

  @Test public void b() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(make.ast("class C<T>{}")))) + "", is("C<T>"));
  }

  @Test public void c() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(make.ast("class C<U,V>{}")))) + "", is("C<U,V>"));
  }

  @Test public void d() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(make.ast("class C<U extends E,V>{}")))) + "", is("C<U,V>"));
  }

  @Test public void e() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(make.ast("class C<U extends Class<V>,V>{}")))) + "", is("C<U,V>"));
  }

  @Test public void f() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(make.ast("class C<U extends Class<V<W>>,V>{}")))) + "", is("C<U,V>"));
  }

  @Test public void g() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(make.ast("class C implements D{}")))) + "", is("C"));
  }

  @Test public void h() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(make.ast("class C extends D{}")))) + "", is("C"));
  }

  @Test public void i() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(make.ast("class C implements D,E{}")))) + "", is("C"));
  }

  @Test public void j() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(make.ast("class C implements D,E,F{}")))) + "", is("C"));
  }

  @Test public void k() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(make.ast("class C<L, M, R> extends W<L, M, R>{}")))) + "", is("C<L,M,R>"));
  }

  @Test public void l() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(make.ast("@CombinedAnnotation({@SimpleAnnotation(id=4)}) public class C{}")))) + "",
        is("C"));
  }

  @Test public void m() {
    azzert
        .that(
            type(az.typeDeclaration(findFirst
                .typeDeclaration(make.ast("@GwtIncompatible private static final class C<D extends Comparable> implements Serializable {}")))) + "",
            is("C<D>"));
  }

  @Test public void n() {
    azzert
        .that(
            type(az.typeDeclaration(findFirst
                .typeDeclaration(make.ast("@GwtIncompatible private static final class C<D extends Comparable,E> implements Serializable {}")))) + "",
            is("C<D,E>"));
  }

  @Test public void o() {
    azzert.that(
        type(az.typeDeclaration(findFirst.typeDeclaration(
            make.ast("abstract static class C<  K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>>extends ReentrantLock")))) + "",
        is("C<K,V,E,S>"));
  }

  @Test public void p() {
    azzert.that(type(az.enumDeclaration(findFirst.abstractTypeDeclaration(make.ast("enum C{}")))) + "", is("C"));
  }

  @Test public void q() {
    azzert.that(type(findFirst.abstractTypeDeclaration(make.ast("interface InstructionComparator {}"))) + "", is("InstructionComparator"));
  }
}
