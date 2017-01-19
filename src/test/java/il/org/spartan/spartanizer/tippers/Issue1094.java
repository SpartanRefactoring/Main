package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.meta.*;

/** Unit tests with some type information.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-17 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@Ignore
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1094 extends MetaFixture {
  @Test public void b() {
    trimmingOf("void f(){ try {} finally{}}")//
        .gives("void f(){ }")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("void f() { try { a(); } finally { }}") //
        .using(TryStatement.class, new TryBodyNotEmptyNoCatchesNoFinallyRemove()) //
        .gives("void f(){{ a();} }")//
        .gives("void f(){ a(); }")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("void f() { try { a(); b(); } finally { }}") //
        .using(TryStatement.class, new TryBodyNotEmptyNoCatchesNoFinallyRemove()) //
        .gives("void f(){{ a();b();} }")//
        .gives("void f(){ a();b(); }")//
        .stays();
  }

  @Test public void b1() {
    trimmingOf("void f(){ try {} finally{}}")//
        .gives("void f(){ }")//
        .stays();
  }

  @Test public void c1() {
    trimmingOf("void f() { try { a(); } finally { }}") //
        .gives("void f(){{ a();} }")//
        .gives("void f(){ a(); }")//
        .stays();
  }

  @Test public void d1() {
    trimmingOf("void f() { try { a(); b(); } finally { }}") //
        .gives("void f(){{ a();b();} }")//
        .gives("void f(){ a();b(); }")//
        .stays();
  }

  @Test public void ba() {
    trimmingOf("void f(){ try {} finally{}}")//
        .gives("void f(){ }")//
        .stays();
  }

  @Test public void ca() {
    trimmingOf("void f() { try(File f = new File()) { a(); } finally { }}") //
        .using(TryStatement.class, new TryBodyNotEmptyNoCatchesNoFinallyRemove()) //
        .gives("void f(){{ a();} }")//
        .gives("void f(){ a(); }")//
        .stays();
  }

  @Test public void da() {
    trimmingOf("void f() { try(File f = new File()) { a(); b(); } finally { }}") //
        .using(TryStatement.class, new TryBodyNotEmptyNoCatchesNoFinallyRemove()) //
        .gives("void f(){{ a();b();} }")//
        .gives("void f(){ a();b(); }")//
        .stays();
  }

  @Test public void b1a() {
    trimmingOf("void f(){ try(File f = new File()) {} finally{}}")//
        .gives("void f(){ }")//
        .stays();
  }

  @Test public void c1a() {
    trimmingOf("void f() { try(File f = new File()) { a(); } finally { }}") //
        .gives("void f(){{ a();} }")//
        .gives("void f(){ a(); }")//
        .stays();
  }

  @Test public void d1a() {
    trimmingOf("void f() { try(File f = new File()) { a(); b(); } finally { }}") //
        .gives("void f(){{ a();b();} }")//
        .gives("void f(){ a();b(); }")//
        .stays();
  }
}
