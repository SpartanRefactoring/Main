package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;

/** Unit tests for {@link SequencerNotLastInBlock}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0086 extends Issue____ {
  private static final String INPUT = "{   throw Something();  f(); a = 3; return 2;}";
  Statement context;
  ThrowStatement focus;
  Tipper<ThrowStatement> tipper;

  @Test public void A$01_createTipper() {
    tipper = makeTipper();
    assert tipper != null;
  }

  @Test public void A$02_CreateContext() {
    context = into.s(INPUT);
    assert context != null;
  }

  @Test public void A$03_FindFocus() {
    A$02_CreateContext();
    focus = findFirst.instanceOf(ThrowStatement.class).in(context);
    assert focus != null;
  }

  @Test public void A$04_init() {
    A$01_createTipper();
    A$02_CreateContext();
    A$03_FindFocus();
    Toolbox.refresh();
  }

  @Test public void B$01init() {
    A$04_init();
  }

  @Test public void B$02findFirstThrow() {
    A$04_init();
    azzert.that(findFirst.instanceOf(ThrowStatement.class).in(context), instanceOf(ThrowStatement.class));
  }

  @Test public void B$03canSuggest() {
    A$04_init();
    assert tipper.check(focus);
  }

  @Test public void B$03demands() {
    A$04_init();
    assert tipper.check(focus);
  }

  @Test public void B$04tipNotNull() {
    A$04_init();
    assert tipper.tip(focus) != null;
  }

  @Test public void B$05toolboxCanFindTipper() {
    A$04_init();
    assert Toolbox.defaultInstance().firstTipper(focus) != null;
  }

  @Test public void B$06toolboxCanFindFindCorrectTipper() {
    A$04_init();
    azzert.that(Toolbox.defaultInstance().firstTipper(focus), instanceOf(tipper.getClass()));
  }

  @Test public void B$07callSuggest() {
    A$04_init();
    tipper.tip(focus);
  }

  @Test public void B$09descriptionNotNull() {
    A$04_init();
    assert tipper.tip(focus).description != null;
  }

  @Test public void B$0suggestNotNull() {
    A$04_init();
    assert tipper.tip(focus) != null;
  }

  @Test public void B$10descriptionContains() {
    A$04_init();
    azzert.that(tipper.tip(focus).description, containsString(focus + ""));
  }

  @Test public void B$12rangeNotEmpty() {
    A$04_init();
    assert !tipper.tip(focus).isEmpty();
  }

  @Test public void B$13applyTipper() {
    A$04_init();
    tipper.tip(focus);
  }

  @Test public void B$14applyTipper() {
    A$04_init();
    Toolbox.defaultInstance().firstTipper(focus);
  }

  @Test public void doubleVanillaThrow() {
    A$04_init();
    trimmingOf("int f() { if (false)    i++;  else {    g(i);    throw new RuntimeException();  }  f(); a = 3; return 2;}")//
        .gives("int f(){{g(i);throw new RuntimeException();}f();a=3;return 2;}").gives("int f(){g(i);throw new RuntimeException();f();a=3;return 2;}")//
        .gives("int f(){g(i);throw new RuntimeException();a=3;return 2;}").gives("int f(){g(i);throw new RuntimeException();return 2;}")//
        .gives("int f(){g(i);throw new RuntimeException();}")//
        .stays();
  }

  @NotNull private SequencerNotLastInBlock<ThrowStatement> makeTipper() {
    return new SequencerNotLastInBlock<>();
  }

  @Test public void vanilla() {
    trimmingOf("{   throw Something();  f(); a = 3; return 2;}")//
        .gives("throw Something();f(); a=3; return 2;").gives("throw Something();a=3; return 2;")//
        .gives("throw Something(); return 2;")//
        .gives("throw Something();")//
        .stays();
  }

  @Test public void vanilla01() {
    trimmingOf("throw Something();a=3; return 2;")//
        .gives("throw Something(); return 2;")//
        .gives("throw Something();")//
        .stays();
  }

  @Test public void vanilla02() {
    trimmingOf("return Something();a=3; return 2;")//
        .gives("return Something(); return 2;")//
        .gives("return Something();")//
        .stays();
  }

  @Test public void vanilla03() {
    trimmingOf("continue a;a=3; return 2;")//
        .gives("continue a; return 2;")//
        .gives("continue a;")//
        .stays();
  }

  @Test public void vanilla04() {
    trimmingOf("break a;a=3; return 2;")//
        .gives("break a; return 2;")//
        .gives("break a;")//
        .stays();
  }

  @Test public void vanilla05() {
    trimmingOf("continue ;a=3; return 2;")//
        .gives("continue ; return 2;")//
        .gives("continue ;")//
        .stays();
  }

  @Test public void vanilla06() {
    trimmingOf("break;a=3; return 2;")//
        .gives("break; return 2;")//
        .gives("break;")//
        .stays();
  }
}
