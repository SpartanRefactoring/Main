package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** @author Alex Kopzon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class ModifiersTest {
  @Test public void modifierUse_01() {
    azzert.that(IExtendedModifiersOrdering.compare("public", "public"), is(0));
  }

  @Test public void modifierUse_03() {
    azzert.that(IExtendedModifiersOrdering.compare("private", "public"), greaterThan(0));
  }

  @Test public void modifierUse_04() {
    azzert.that(IExtendedModifiersOrdering.compare("public", "abstract"), lessThan(0));
  }

  @Test public void modifierUse_05() {
    azzert.that(IExtendedModifiersOrdering.compare("public", "default"), lessThan(0));
  }

  @Test public void modifierUse_06() {
    azzert.that(IExtendedModifiersOrdering.compare("public", "static"), lessThan(0));
  }

  @Test public void modifierUse_07() {
    azzert.that(IExtendedModifiersOrdering.compare("public", "final"), lessThan(0));
  }

  @Test public void modifierUse_08() {
    azzert.that(IExtendedModifiersOrdering.compare("public", "transient"), lessThan(0));
  }

  @Test public void modifierUse_09() {
    azzert.that(IExtendedModifiersOrdering.compare("public", "volatile"), lessThan(0));
  }

  @Test public void modifierUse_10() {
    azzert.that(IExtendedModifiersOrdering.compare("public", "synchronized"), lessThan(0));
  }

  @Test public void modifierUse_11() {
    azzert.that(IExtendedModifiersOrdering.compare("public", "native"), lessThan(0));
  }

  @Test public void modifierUse_12() {
    azzert.that(IExtendedModifiersOrdering.compare("public", "strictfp"), lessThan(0));
  }

  @Test public void modifierUse_13() {
    azzert.that(IExtendedModifiersOrdering.compare("strictfp", "synchronized"), greaterThan(0));
  }

  @Test public void modifierUse_14() {
    azzert.that(IExtendedModifiersOrdering.compare("transient", "abstract"), greaterThan(0));
  }

  @Test public void modifierUse_15() {
    azzert.that(IExtendedModifiersOrdering.compare("volatile", "abstract"), greaterThan(0));
  }

  @Test public void modifierUse_16() {
    azzert.that(IExtendedModifiersOrdering.compare("default", "protected"), greaterThan(0));
  }

  @Test public void modifierUse_17() {
    azzert.that(IExtendedModifiersOrdering.find("public").compareTo(IExtendedModifiersOrdering.PUBLIC), is(0));
  }

  @Test public void modifierUse_18() {
    azzert.that(IExtendedModifiersOrdering.find("protected").compareTo(IExtendedModifiersOrdering.PROTECTED), is(0));
  }

  @Test public void modifierUse_19() {
    azzert.that(IExtendedModifiersOrdering.find("private").compareTo(IExtendedModifiersOrdering.PRIVATE), is(0));
  }

  @Test public void modifierUse_2() {
    azzert.that(IExtendedModifiersOrdering.compare("protected", "public"), greaterThan(0));
  }

  @Test public void modifierUse_20() {
    azzert.that(IExtendedModifiersOrdering.find("abstract").compareTo(IExtendedModifiersOrdering.ABSTRACT), is(0));
  }

  @Test public void modifierUse_21() {
    azzert.that(IExtendedModifiersOrdering.find("default").compareTo(IExtendedModifiersOrdering.DEFAULT), is(0));
  }

  @Test public void modifierUse_22() {
    azzert.that(IExtendedModifiersOrdering.find("static").compareTo(IExtendedModifiersOrdering.STATIC), is(0));
  }

  @Test public void modifierUse_23() {
    azzert.that(IExtendedModifiersOrdering.find("final"), is(IExtendedModifiersOrdering.FINAL));
  }

  @Test public void modifierUse_24() {
    azzert.that(IExtendedModifiersOrdering.find("transient").compareTo(IExtendedModifiersOrdering.TRANSIENT), is(0));
  }

  @Test public void modifierUse_25() {
    azzert.that(IExtendedModifiersOrdering.find("volatile").compareTo(IExtendedModifiersOrdering.VOLATILE), is(0));
  }

  @Test public void modifierUse_26() {
    azzert.that(IExtendedModifiersOrdering.find("synchronized").compareTo(IExtendedModifiersOrdering.SYNCHRONIZED), is(0));
  }

  @Test public void modifierUse_27() {
    azzert.that(IExtendedModifiersOrdering.find("native").compareTo(IExtendedModifiersOrdering.NATIVE), is(0));
  }

  @Test public void modifierUse_28() {
    azzert.that(IExtendedModifiersOrdering.find("strictfp").compareTo(IExtendedModifiersOrdering.STRICTFP), is(0));
  }
}
