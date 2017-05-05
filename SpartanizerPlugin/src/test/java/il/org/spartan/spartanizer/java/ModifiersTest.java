package il.org.spartan.spartanizer.java;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;

/** Tests {@link IExtendedModifiersRank}
 * @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class ModifiersTest {
  @Test public void modifierUse_01() {
    azzert.that(IExtendedModifiersRank.compare("public", "public"), is(0));
  }
  @Test public void modifierUse_03() {
    azzert.that(IExtendedModifiersRank.compare("private", "public"), greaterThan(0));
  }
  @Test public void modifierUse_04() {
    azzert.that(IExtendedModifiersRank.compare("public", "abstract"), lessThan(0));
  }
  @Test public void modifierUse_05() {
    azzert.that(IExtendedModifiersRank.compare("public", "default"), lessThan(0));
  }
  @Test public void modifierUse_06() {
    azzert.that(IExtendedModifiersRank.compare("public", "static"), lessThan(0));
  }
  @Test public void modifierUse_07() {
    azzert.that(IExtendedModifiersRank.compare("public", "final"), lessThan(0));
  }
  @Test public void modifierUse_08() {
    azzert.that(IExtendedModifiersRank.compare("public", "transient"), lessThan(0));
  }
  @Test public void modifierUse_09() {
    azzert.that(IExtendedModifiersRank.compare("public", "volatile"), lessThan(0));
  }
  @Test public void modifierUse_10() {
    azzert.that(IExtendedModifiersRank.compare("public", "synchronized"), lessThan(0));
  }
  @Test public void modifierUse_11() {
    azzert.that(IExtendedModifiersRank.compare("public", "native"), lessThan(0));
  }
  @Test public void modifierUse_12() {
    azzert.that(IExtendedModifiersRank.compare("public", "strictfp"), lessThan(0));
  }
  @Test public void modifierUse_13() {
    azzert.that(IExtendedModifiersRank.compare("strictfp", "synchronized"), greaterThan(0));
  }
  @Test public void modifierUse_14() {
    azzert.that(IExtendedModifiersRank.compare("transient", "abstract"), greaterThan(0));
  }
  @Test public void modifierUse_15() {
    azzert.that(IExtendedModifiersRank.compare("volatile", "abstract"), greaterThan(0));
  }
  @Test public void modifierUse_16() {
    azzert.that(IExtendedModifiersRank.compare("default", "protected"), greaterThan(0));
  }
  @Test public void modifierUse_17() {
    azzert.that(IExtendedModifiersRank.find("public").compareTo(IExtendedModifiersRank.PUBLIC), is(0));
  }
  @Test public void modifierUse_18() {
    azzert.that(IExtendedModifiersRank.find("protected").compareTo(IExtendedModifiersRank.PROTECTED), is(0));
  }
  @Test public void modifierUse_19() {
    azzert.that(IExtendedModifiersRank.find("private").compareTo(IExtendedModifiersRank.PRIVATE), is(0));
  }
  @Test public void modifierUse_2() {
    azzert.that(IExtendedModifiersRank.compare("protected", "public"), greaterThan(0));
  }
  @Test public void modifierUse_20() {
    azzert.that(IExtendedModifiersRank.find("abstract").compareTo(IExtendedModifiersRank.ABSTRACT), is(0));
  }
  @Test public void modifierUse_21() {
    azzert.that(IExtendedModifiersRank.find("default").compareTo(IExtendedModifiersRank.DEFAULT), is(0));
  }
  @Test public void modifierUse_22() {
    azzert.that(IExtendedModifiersRank.find("static").compareTo(IExtendedModifiersRank.STATIC), is(0));
  }
  @Test public void modifierUse_23() {
    azzert.that(IExtendedModifiersRank.find("final"), is(IExtendedModifiersRank.FINAL));
  }
  @Test public void modifierUse_24() {
    azzert.that(IExtendedModifiersRank.find("transient").compareTo(IExtendedModifiersRank.TRANSIENT), is(0));
  }
  @Test public void modifierUse_25() {
    azzert.that(IExtendedModifiersRank.find("volatile").compareTo(IExtendedModifiersRank.VOLATILE), is(0));
  }
  @Test public void modifierUse_26() {
    azzert.that(IExtendedModifiersRank.find("synchronized").compareTo(IExtendedModifiersRank.SYNCHRONIZED), is(0));
  }
  @Test public void modifierUse_27() {
    azzert.that(IExtendedModifiersRank.find("native").compareTo(IExtendedModifiersRank.NATIVE), is(0));
  }
  @Test public void modifierUse_28() {
    azzert.that(IExtendedModifiersRank.find("strictfp").compareTo(IExtendedModifiersRank.STRICTFP), is(0));
  }
}
