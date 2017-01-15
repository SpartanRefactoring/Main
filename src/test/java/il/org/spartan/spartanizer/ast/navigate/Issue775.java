package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Test for analyze.type
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue775 {
  @Test public void a() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C{}")))) + "", is("C"));
    // TODO Auto-generated method stub
  }

  @Test public void b() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C<T>{}")))) + "", is("C<T>"));
    // TODO Auto-generated method stub
  }

  @Test public void c() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C<U,V>{}")))) + "", is("C<U,V>"));
    // TODO Auto-generated method stub
  }

  @Test public void d() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C<U extends E,V>{}")))) + "", is("C<U,V>"));
    // TODO Auto-generated method stub
  }

  @Test public void e() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C<U extends Class<V>,V>{}")))) + "", is("C<U,V>"));
    // TODO Auto-generated method stub
  }

  @Test public void f() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C<U extends Class<V<W>>,V>{}")))) + "", is("C<U,V>"));
    // TODO Auto-generated method stub
  }

  @Test public void g() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C implements D{}")))) + "", is("C"));
    // TODO Auto-generated method stub
  }

  @Test public void h() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C extends D{}")))) + "", is("C"));
    // TODO Auto-generated method stub
  }

  @Test public void i() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C implements D,E{}")))) + "", is("C"));
    // TODO Auto-generated method stub
  }

  @Test public void j() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C implements D,E,F{}")))) + "", is("C"));
    // TODO Auto-generated method stub
  }

  @Test public void k() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(ast("class C<L, M, R> extends W<L, M, R>{}")))) + "", is("C<L,M,R>"));
    // TODO Auto-generated method stub
  }

  @Test public void l() {
    azzert.that(type(az.typeDeclaration(findFirst.typeDeclaration(ast("@CombinedAnnotation({@SimpleAnnotation(id=4)}) public class C{}")))) + "",
        is("C"));
    // TODO Auto-generated method stub
  }

  @Test public void m() {
    azzert.that(
        type(az.typeDeclaration(
            findFirst.typeDeclaration(ast("@GwtIncompatible private static final class C<D extends Comparable> implements Serializable {}")))) + "",
        is("C<D>"));
    // TODO Auto-generated method stub
  }

  @Test public void n() {
    azzert.that(
        type(az.typeDeclaration(
            findFirst.typeDeclaration(ast("@GwtIncompatible private static final class C<D extends Comparable,E> implements Serializable {}")))) + "",
        is("C<D,E>"));
    // TODO Auto-generated method stub
  }

  @Test public void o() {
    azzert.that(
        type(az.typeDeclaration(findFirst.typeDeclaration(
            ast("abstract static class C<  K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>>extends ReentrantLock")))) + "",
        is("C<K,V,E,S>"));
    // TODO Auto-generated method stub
  }

  @Test public void p() {
    azzert.that(type(az.enumDeclaration(findFirst.abstractTypeDeclaration(ast("enum C{}")))) + "", is("C"));
    // TODO Auto-generated method stub
  }

  @Test public void q() {
    azzert.that(type(findFirst.abstractTypeDeclaration(ast("interface InstructionComparator {}"))) + "", is("InstructionComparator"));
    // TODO Auto-generated method stub
  }
}
