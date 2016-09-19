package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** @author Alex Kopakzon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public final class ModifiersTest {
  @Test public void modifierUse_01() {
    azzert.that(ModifiersOrdering.compare("public", "public"), is(0));
  }

  @Test public void modifierUse_03() {
    azzert.that(ModifiersOrdering.compare("private", "public"), greaterThan(0));
  }

  @Test public void modifierUse_04() {
    azzert.that(ModifiersOrdering.compare("public", "abstract"), lessThan(0));
  }

  @Test public void modifierUse_05() {
    azzert.that(ModifiersOrdering.compare("public", "default"), lessThan(0));
  }

  @Test public void modifierUse_06() {
    azzert.that(ModifiersOrdering.compare("public", "static"), lessThan(0));
  }

  @Test public void modifierUse_07() {
    azzert.that(ModifiersOrdering.compare("public", "final"), lessThan(0));
  }

  @Test public void modifierUse_08() {
    azzert.that(ModifiersOrdering.compare("public", "transient"), lessThan(0));
  }

  @Test public void modifierUse_09() {
    azzert.that(ModifiersOrdering.compare("public", "volatile"), lessThan(0));
  }

  @Test public void modifierUse_10() {
    azzert.that(ModifiersOrdering.compare("public", "synchronized"), lessThan(0));
  }

  @Test public void modifierUse_11() {
    azzert.that(ModifiersOrdering.compare("public", "native"), lessThan(0));
  }

  @Test public void modifierUse_12() {
    azzert.that(ModifiersOrdering.compare("public", "strictfp"), lessThan(0));
  }

  @Test public void modifierUse_13() {
    azzert.that(ModifiersOrdering.compare("strictfp", "synchronized"), greaterThan(0));
  }

  @Test public void modifierUse_14() {
    azzert.that(ModifiersOrdering.compare("transient", "abstract"), greaterThan(0));
  }

  @Test public void modifierUse_15() {
    azzert.that(ModifiersOrdering.compare("volatile", "abstract"), greaterThan(0));
  }

  @Test public void modifierUse_16() {
    azzert.that(ModifiersOrdering.compare("default", "protected"), greaterThan(0));
  }

  @Test public void modifierUse_17() {
    azzert.that(ModifiersOrdering.find("public").compareTo(ModifiersOrdering.PUBLIC), is(0));
  }

  @Test public void modifierUse_18() {
    azzert.that(ModifiersOrdering.find("protected").compareTo(ModifiersOrdering.PROTECTED), is(0));
  }

  @Test public void modifierUse_19() {
    azzert.that(ModifiersOrdering.find("private").compareTo(ModifiersOrdering.PRIVATE), is(0));
  }

  @Test public void modifierUse_2() {
    azzert.that(ModifiersOrdering.compare("protected", "public"), greaterThan(0));
  }

  @Test public void modifierUse_20() {
    azzert.that(ModifiersOrdering.find("abstract").compareTo(ModifiersOrdering.ABSTRACT), is(0));
  }

  @Test public void modifierUse_21() {
    azzert.that(ModifiersOrdering.find("default").compareTo(ModifiersOrdering.DEFAULT), is(0));
  }

  @Test public void modifierUse_22() {
    azzert.that(ModifiersOrdering.find("static").compareTo(ModifiersOrdering.STATIC), is(0));
  }

  @Test public void modifierUse_23() {
    azzert.that(ModifiersOrdering.find("final"), is(ModifiersOrdering.FINAL));
  }

  @Test public void modifierUse_24() {
    azzert.that(ModifiersOrdering.find("transient").compareTo(ModifiersOrdering.TRANSIENT), is(0));
  }

  @Test public void modifierUse_25() {
    azzert.that(ModifiersOrdering.find("volatile").compareTo(ModifiersOrdering.VOLATILE), is(0));
  }

  @Test public void modifierUse_26() {
    azzert.that(ModifiersOrdering.find("synchronized").compareTo(ModifiersOrdering.SYNCHRONIZED), is(0));
  }

  @Test public void modifierUse_27() {
    azzert.that(ModifiersOrdering.find("native").compareTo(ModifiersOrdering.NATIVE), is(0));
  }

  @Test public void modifierUse_28() {
    azzert.that(ModifiersOrdering.find("strictfp").compareTo(ModifiersOrdering.STRICTFP), is(0));
  }
}
