package il.org.spartan.spartanizer.issues;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.junit.Test;

import fluent.ly.as;
import il.org.spartan.athenizer.zoomers.LocalInitializedCollection;
import il.org.spartan.spartanizer.meta.MetaFixture;
import il.org.spartan.spartanizer.testing.BloaterTest;
import il.org.spartan.spartanizer.tipping.Tipper;

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
  @Test public void test01() {
    bloatingOf(new testUtils())
        .staysWithBinding("from1");
  }
  @Test public void test02() {
    bloatingOf(new testUtils())
        .staysWithBinding("from2");
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
    public testUtils(final Collection<?> __) {
      //
    }
    /** [[SuppressWarningsSpartan]] */
    public void from0() {
      /** [[SuppressWarningsSpartan]] */
      List<Integer> x = new ArrayList<>(ys);
    }
    /** [[SuppressWarningsSpartan]] */
    public void from1() {
      /** [[SuppressWarningsSpartan]] */
      testUtils t = new testUtils(ys);
    }
    /** [[SuppressWarningsSpartan]] */
    public void from2() {
      /** [[SuppressWarningsSpartan]] */
      Object x = new HashSet<Integer>(zs);
    }
    /** [[SuppressWarningsSpartan]] */
    public void from3() {
      /** [[SuppressWarningsSpartan]] */
      HashSet<Integer> x = new HashSet<>(zs);
    }
    public void f(final Integer x) {
      //
    }
    public void f(final String x) {
      //
    }
    public void f(final testUtils x) {
      //
    }
  }
}
