package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.meta.*;
import il.org.spartan.spartanizer.tippers.*;

/** Unit tests of {@link TryBodyNotEmptyNoCatchesNoFinallyRemove}
 * @author Yossi Gil
 * @since 2017-01-17 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1094 extends MetaFixture {
  @Test public void b() {
    trimminKof("void f(){ try {} finally{}}")//
        .gives("void f(){ }")//
        .stays();
  }

  @Test public void b1() {
    trimminKof("void f(){ try {} finally{}}")//
        .gives("void f(){ }")//
        .stays();
  }

  @Test public void b1a() {
    trimminKof("void f(){ try(File f = new File()) {} finally{}}")//
        .gives("void f(){ }")//
        .stays();
  }

  @Test public void ba() {
    trimminKof("void f(){ try {} finally{}}")//
        .gives("void f(){ }")//
        .stays();
  }

  @Test public void c() {
    trimminKof("void f() { try { a(); } finally { }}") //
        .using(TryStatement.class, new TryBodyNotEmptyNoCatchesNoFinallyRemove()) //
        .gives("void f(){{ a();} }")//
        .gives("void f(){ a(); }")//
        .stays();
  }

  @Test public void c1() {
    trimminKof("void f() { try { a(); } finally { }}") //
        .using(TryStatement.class, new TryBodyNotEmptyNoCatchesNoFinallyRemove()) //
        .gives("void f(){{ a();} }")//
        .gives("void f(){ a(); }")//
        .stays();
  }

  @Test public void c1a() {
    trimminKof("void f() { try(File f = new File()) { a(); } finally { }}") //
        .stays();
  }

  @Test public void c1x() {
    trimminKof("void f() { try { a(); } finally { }}") //
        .gives("void f(){{ a();} }")//
        .gives("void f(){ a(); }")//
        .stays();
  }

  @Test public void ca() {
    trimminKof("void f() { try(File f = new File()) { a(); } finally { }}") //
        .using(TryStatement.class, new TryBodyNotEmptyNoCatchesNoFinallyRemove()) //
        .stays();
  }

  @Test public void d() {
    trimminKof("void f() { try { a(); b(); } finally { }}") //
        .using(TryStatement.class, new TryBodyNotEmptyNoCatchesNoFinallyRemove()) //
        .gives("void f(){{ a();b();} }")//
        .gives("void f(){ a();b(); }")//
        .stays();
  }

  @Test public void d1() {
    trimminKof("void f() { try { a(); b(); } finally { }}") //
        .using(TryStatement.class, new TryBodyNotEmptyNoCatchesNoFinallyRemove()) //
        .gives("void f(){{ a();b();} }")//
        .gives("void f(){ a();b(); }")//
        .stays();
  }

  @Test public void d1a() {
    trimminKof("void f() { try(File f = new File()) { a(); b(); } finally { }}") //
        .stays();
  }

  @Test public void da() {
    trimminKof("void f() { try(File f = new File()) { a(); b(); } finally { }}") //
        .using(TryStatement.class, new TryBodyNotEmptyNoCatchesNoFinallyRemove()) //
        .stays();
  }
}
