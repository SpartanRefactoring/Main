package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

/** TODO test class for renaming constructor parameters
 * @author Dor Ma'ayan
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0020 {
  /** Correct way of trimming does not change */
  @Test public void constructorParameterRenaming_00() {
    topDownTrimming("class A {int x;A(int y) {this.x = y;}}")//
        .gives("class A {int x;A(int x) {this.x = x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_001() {
    topDownTrimming("class A {int x;A(int y,int z) {this.x = z;}}")//
        .gives("class A {int x;A(int y,int x) {this.x = x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_01() {
    topDownTrimming("class A {int x;int z;A(int y, int k) {this.x = y;this.z = k;}}")
        .gives("class A {int x;int z;A(int x, int k) {this.x = x;this.z = k;}}")
        .gives("class A {int x;int z;A(int x, int z) {this.x = x;this.z = z;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_02() {
    topDownTrimming("class A {int x;A(int y) {++this.x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_02a() {
    topDownTrimming("class A {int x;A(int y) {this.x += y;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_03() {
    topDownTrimming("class A {int x;A(int y, int k) {this.x = k;}}")//
        .gives("class A {int x;A(int y, int x) {this.x = x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_04() {
    topDownTrimming("class A {int x;A(int y) {this.x = y;}A(int y, int z) {this.x = y;}}")
        .gives("class A {int x;A(int x) {this.x = x;}A(int x, int z) {this.x = x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_05() {
    topDownTrimming("class A {int x;A(int y) {this.x = y;}}")//
        .gives("class A {int x;A(int x) {this.x = x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_06() {
    topDownTrimming("class A {int x;A(int y, int x) {this.x = y;++x;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_06a() {
    topDownTrimming("class A {int x;A(int y, int x) {this.x = y;}}")//
        .stays();
  }

  @Test public void constructorParameterRenaming_07b() {
    topDownTrimming("class A {int x;A(int y, int z) {this.x = y;this.x = z;}}")//
        .gives("class A {int x;A(int x, int z) {this.x = x;this.x = z;}}")//
        .stays();
  }
}
