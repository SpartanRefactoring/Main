package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.meta.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests of {@link TryBodyNotEmptyNoCatchesNoFinallyRemove}
 * @author Yossi Gil
 * @since 2017-01-17 */
//
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1094 extends MetaFixture {
  @Test public void b() {
    trimmingOf("void f(){ try {} finally{}}")//
        .gives("void f(){ }")//
        .stays();
  }
  @Test public void b1() {
    trimmingOf("void f(){ try {} finally{}}")//
        .gives("void f(){ }")//
        .stays();
  }
  @Test public void b1a() {
    trimmingOf("void f(){ try(File f = new File()) {} finally{}}")//
        .gives("void f(){ }")//
        .stays();
  }
  @Test public void ba() {
    trimmingOf("void f(){ try {} finally{}}")//
        .gives("void f(){ }")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("void f() { try { a(); } finally { }}") //
        .using(new TryBodyNotEmptyNoCatchesNoFinallyRemove(), TryStatement.class) //
        .gives("void f(){{ a();} }")//
        .gives("void f(){ a(); }")//
        .stays();
  }
  @Test public void c1() {
    trimmingOf("void f() { try { a(); } finally { }}") //
        .using(new TryBodyNotEmptyNoCatchesNoFinallyRemove(), TryStatement.class) //
        .gives("void f(){{ a();} }")//
        .gives("void f(){ a(); }")//
        .stays();
  }
  @Test public void c1a() {
    trimmingOf("void f() { try(File f = new File()) { a(); } finally { }}") //
        .stays();
  }
  @Test public void c1x() {
    trimmingOf("void f() { try { a(); } finally { }}") //
        .gives("void f(){{ a();} }")//
        .gives("void f(){ a(); }")//
        .stays();
  }
  @Test public void ca() {
    trimmingOf("void f() { try(File f = new File()) { a(); } finally { }}") //
        .using(new TryBodyNotEmptyNoCatchesNoFinallyRemove(), TryStatement.class) //
        .stays();
  }
  @Test public void d() {
    trimmingOf("void f() { try { a(); b(); } finally { }}") //
        .using(new TryBodyNotEmptyNoCatchesNoFinallyRemove(), TryStatement.class) //
        .gives("void f(){{ a();b();} }")//
        .gives("void f(){ a();b(); }")//
        .stays();
  }
  @Test public void d1() {
    trimmingOf("void f() { try { a(); b(); } finally { }}") //
        .using(new TryBodyNotEmptyNoCatchesNoFinallyRemove(), TryStatement.class) //
        .gives("void f(){{ a();b();} }")//
        .gives("void f(){ a();b(); }")//
        .stays();
  }
  @Test public void d1a() {
    trimmingOf("void f() { try(File f = new File()) { a(); b(); } finally { }}") //
        .stays();
  }
  @Test public void da() {
    trimmingOf("void f() { try(File f = new File()) { a(); b(); } finally { }}") //
        .using(new TryBodyNotEmptyNoCatchesNoFinallyRemove(), TryStatement.class) //
        .stays();
  }
}
