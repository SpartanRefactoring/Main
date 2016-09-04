package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** @author Alex Kopakzon
 * @year 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class ModifiersTest {
  @Test public void modifierUse_01() {
    azzert.that(Modifiers.gt("public", "public"), is(false));
  }

  @Test public void modifierUse_03() {
    azzert.that(Modifiers.gt("private", "public"), is(true));
  }

  @Test public void modifierUse_04() {
    azzert.that(Modifiers.gt("public", "abstract"), is(false));
  }

  @Test public void modifierUse_05() {
    azzert.that(Modifiers.gt("public", "default"), is(false));
  }

  @Test public void modifierUse_06() {
    azzert.that(Modifiers.gt("public", "static"), is(false));
  }

  @Test public void modifierUse_07() {
    azzert.that(Modifiers.gt("public", "final"), is(false));
  }

  @Test public void modifierUse_08() {
    azzert.that(Modifiers.gt("public", "transient"), is(false));
  }

  @Test public void modifierUse_09() {
    azzert.that(Modifiers.gt("public", "volatile"), is(false));
  }

  @Test public void modifierUse_10() {
    azzert.that(Modifiers.gt("public", "synchronized"), is(false));
  }

  @Test public void modifierUse_11() {
    azzert.that(Modifiers.gt("public", "native"), is(false));
  }

  @Test public void modifierUse_12() {
    azzert.that(Modifiers.gt("public", "strictfp"), is(false));
  }

  @Test public void modifierUse_13() {
    azzert.that(Modifiers.gt("strictfp", "synchronized"), is(true));
  }

  @Test public void modifierUse_14() {
    azzert.that(Modifiers.gt("transient", "abstract"), is(true));
  }

  @Test public void modifierUse_15() {
    azzert.that(Modifiers.gt("volatile", "abstract"), is(true));
  }

  @Test public void modifierUse_16() {
    azzert.that(Modifiers.gt("default", "protected"), is(true));
  }

  @Test public void modifierUse_17() {
    azzert.that(Modifiers.find("public").compareTo(Modifiers.PUBLIC), is(0));
  }

  @Test public void modifierUse_18() {
    azzert.that(Modifiers.find("protected").compareTo(Modifiers.PROTECTED), is(0));
  }

  @Test public void modifierUse_19() {
    azzert.that(Modifiers.find("private").compareTo(Modifiers.PRIVATE), is(0));
  }

  @Test public void modifierUse_2() {
    azzert.that(Modifiers.gt("protected", "public"), is(true));
  }

  @Test public void modifierUse_20() {
    azzert.that(Modifiers.find("abstract").compareTo(Modifiers.ABSTRACT), is(0));
  }

  @Test public void modifierUse_21() {
    azzert.that(Modifiers.find("default").compareTo(Modifiers.DEFAULT), is(0));
  }

  @Test public void modifierUse_22() {
    azzert.that(Modifiers.find("static").compareTo(Modifiers.STATIC), is(0));
  }

  @Test public void modifierUse_23() {
    azzert.that(Modifiers.find("final"), is(Modifiers.FINAL));
  }

  @Test public void modifierUse_24() {
    azzert.that(Modifiers.find("transient").compareTo(Modifiers.TRANSIENT), is(0));
  }

  @Test public void modifierUse_25() {
    azzert.that(Modifiers.find("volatile").compareTo(Modifiers.VOLATILE), is(0));
  }

  @Test public void modifierUse_26() {
    azzert.that(Modifiers.find("synchronized").compareTo(Modifiers.SYNCHRONIZED), is(0));
  }

  @Test public void modifierUse_27() {
    azzert.that(Modifiers.find("native").compareTo(Modifiers.NATIVE), is(0));
  }

  @Test public void modifierUse_28() {
    azzert.that(Modifiers.find("strictfp").compareTo(Modifiers.STRICTFP), is(0));
  }
}
