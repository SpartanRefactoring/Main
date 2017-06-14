package il.org.spartan.spartanizer.issues;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.athenizer.zoomers.*;
import il.org.spartan.spartanizer.meta.*;
import il.org.spartan.spartanizer.testing.*;
import il.org.spartan.spartanizer.tipping.*;

/** Tests for {@link LocalInitializedCollection} see github issue thus numbered
 * for more info
 * @author Niv Shalmon
 * @since 2017-05-30 */
public class Issue1421 extends BloaterTest<VariableDeclarationFragment> {
  @Override public Tipper<VariableDeclarationFragment> bloater() {
    return new LocalInitializedCollection();
  }
  @Override public Class<VariableDeclarationFragment> tipsOn() {
    return VariableDeclarationFragment.class;
  }
  @Test public void test00() {
    bloatingOf(new testUtils())//
        .givesWithBinding(
            "public void from0(){"//
                + " List<Integer> x = new ArrayList<>();"//
                + " x.addAll(ys);"//
                + "}"//
            , "from0")//
        .staysWithBinding();
  }
  @Ignore("issue 1453") @Test public void test01() {
    bloatingOf(new testUtils())// from1
        .staysWithBinding();
  }
  @Ignore("issue 1453") @Test public void test02() {
    bloatingOf(new testUtils())// from2
        .staysWithBinding();
  }
  @Test public void test03() {
    bloatingOf(new testUtils())//
        .givesWithBinding(
            "public void from3(){ "//
                + "HashSet<Integer> x = new HashSet<>();"//
                + "x.addAll(zs);"//
                + "}"//
            , "from3")//
        .staysWithBinding();
  }

  @SuppressWarnings("unused")
  class testUtils extends MetaFixture {
    List<Integer> ys = as.ilist(1, 2, 3, 4, 5);
    ArrayList<Integer> zs = new ArrayList<>();

    public testUtils() {}
    public testUtils(Collection<?> __) {
      //
    }
    /** [[SuppressWarningsSpartan]] */
    public void from0() {
      List<Integer> x = new ArrayList<>(ys);
    }
    /** [[SuppressWarningsSpartan]] */
    public void from1() {
      testUtils t = new testUtils(ys);
    }
    /** [[SuppressWarningsSpartan]] */
    public void from2() {
      Object x = new HashSet<Integer>(zs);
    }
    /** [[SuppressWarningsSpartan]] */
    public void from3() {
      HashSet<Integer> x = new HashSet<>(zs);
    }
    public void f(Integer x) {
      //
    }
    public void f(String x) {
      //
    }
    public void f(testUtils x) {
      //
    }
  }
}
