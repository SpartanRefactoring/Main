package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Unit tests for {@link ThrowNotLastInBlock}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public final class Issue086 extends Issue___ {
  private static final String INPUT = "{" + "   throw Something(); " + " f();" + " a = 3;" + " return 2;" + "}";
  Tipper<ThrowStatement> tipper;
  Statement context;
  ThrowStatement focus;

  @Test public void A$01_createWring() {
    tipper = makeWring();
    assert tipper != null;
  }

  @Test public void A$02_CreateContext() {
    context = into.s(INPUT);
    assert context != null;
  }

  @Test public void A$03_FindFocus() {
    A$02_CreateContext();
    focus = findFirst.throwStatement(context);
    assert focus != null;
  }

  @Test public void A$04_init() {
    A$01_createWring();
    A$02_CreateContext();
    A$03_FindFocus();
    Toolbox.refresh();
  }

  @Test public void B$01init() {
    A$04_init();
  }

  @Test public void B$02findFirstThrow() {
    A$04_init();
    azzert.that(findFirst.throwStatement(context), instanceOf(ThrowStatement.class));
  }

  @Test public void B$03canSuggest() {
    A$04_init();
    assert tipper.canTip(focus);
  }

  @Test public void B$03demands() {
    A$04_init();
    assert tipper.canTip(focus);
  }

  @Test public void B$04tipNotNull() throws TipperFailure {
    A$04_init();
    assert tipper.tip(focus) != null;
  }

  @Test public void B$05toolboxCanFindWring() {
    A$04_init();
    assert Toolbox.defaultInstance().find(focus) != null;
  }

  @Test public void B$06toolboxCanFindFindCorrectWring() {
    A$04_init();
    azzert.that(Toolbox.defaultInstance().find(focus), instanceOf(tipper.getClass()));
  }

  @Test public void B$07callSuggest() throws TipperFailure {
    A$04_init();
    tipper.tip(focus);
  }

  @Test public void B$09descriptionNotNull() throws TipperFailure {
    A$04_init();
    assert tipper.tip(focus).description != null;
  }

  @Test public void B$0suggestNotNull() throws TipperFailure {
    A$04_init();
    assert tipper.tip(focus) != null;
  }

  @Test public void B$10descriptionContains() throws TipperFailure {
    A$04_init();
    azzert.that(tipper.tip(focus).description, containsString(focus + ""));
  }

  @Test public void B$12rangeNotEmpty() throws TipperFailure {
    A$04_init();
    assert !tipper.tip(focus).isEmpty();
  }

  @Test public void B$13applyWring() throws TipperFailure {
    A$04_init();
    tipper.tip(focus);
  }

  @Test public void B$14applyWring() {
    A$04_init();
    Toolbox.defaultInstance().find(focus);
  }

  @Test public void doubleVanillaThrow() {
    A$04_init();
    trimmingOf("int f() {" + " if (false) " + "   i++; " + " else { " + "   g(i); " + "   throw new RuntimeException(); " + " } " + " f();"
        + " a = 3;" + " return 2;" + "}").gives("int f(){{g(i);throw new RuntimeException();}f();a=3;return 2;}")
            .gives("int f(){g(i);throw new RuntimeException();f();a=3;return 2;}").gives("int f(){g(i);throw new RuntimeException();a=3;return 2;}")
            .gives("int f(){g(i);throw new RuntimeException();return 2;}").gives("int f(){g(i);throw new RuntimeException();}").stays();
  }

  @Test public void vanilla() {
    trimmingOf("{" + "   throw Something(); " + " f();" + " a = 3;" + " return 2;" + "}").gives("throw Something();f(); a=3; return 2;")
        .gives("throw Something();a=3; return 2;").gives("throw Something(); return 2;").gives("throw Something();").stays();
  }

  @Test public void vanilla01() {
    trimmingOf("throw Something();a=3; return 2;").gives("throw Something(); return 2;").gives("throw Something();").stays();
  }

  private ThrowNotLastInBlock makeWring() {
    return new ThrowNotLastInBlock();
  }
}
