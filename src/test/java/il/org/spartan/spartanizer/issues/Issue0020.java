package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** TODO test class for renaming constructor parameters
 * @author Dor Ma'ayan
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0020 {
  /** Correct way of trimming does not change */
  @Test public void constructorParameterRenaming_00() {
    trimminKof("class A {int x;A(int y) {this.x = y;}}")//
        .gives("class A {int x;A(int x) {this.x = x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_001() {
    trimminKof("class A {int x;A(int y,int z) {this.x = z;}}")//
        .gives("class A {int x;A(int y,int x) {this.x = x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_01() {
    trimminKof("class A {int x;int z;A(int y, int k) {this.x = y;this.z = k;}}")
        .gives("class A {int x;int z;A(int x, int k) {this.x = x;this.z = k;}}")
        .gives("class A {int x;int z;A(int x, int z) {this.x = x;this.z = z;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_02() {
    trimminKof("class A {int x;A(int y) {++this.x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_02a() {
    trimminKof("class A {int x;A(int y) {this.x += y;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_03() {
    trimminKof("class A {int x;A(int y, int k) {this.x = k;}}")//
        .gives("class A {int x;A(int y, int x) {this.x = x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_04() {
    trimminKof("class A {int x;A(int y) {this.x = y;}A(int y, int z) {this.x = y;}}")
        .gives("class A {int x;A(int x) {this.x = x;}A(int x, int z) {this.x = x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_05() {
    trimminKof("class A {int x;A(int y) {this.x = y;}}")//
        .gives("class A {int x;A(int x) {this.x = x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_06() {
    trimminKof("class A {int x;A(int y, int x) {this.x = y;++x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_06a() {
    trimminKof("class A {int x;A(int y, int x) {this.x = y;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_07b() {
    trimminKof("class A {int x;A(int y, int z) {this.x = y;this.x = z;}}")//
        .gives("class A {int x;A(int x, int z) {this.x = x;this.x = z;}}")//
        .stays();
  }
}
