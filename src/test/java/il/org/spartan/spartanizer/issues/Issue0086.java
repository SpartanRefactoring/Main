package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.trimming.*;

/** Unit tests for {@link SequencerNotLastInBlock}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0086 extends Issue____ {
  private static final String INPUT = "{   throw Something();  f(); a = 3; return 2;}";
  Statement context;
  ThrowStatement focus;
  Tipper<ThrowStatement> tipper;

  @Test public void a01_createTipper() {
    tipper = makeTipper();
    assert tipper != null;
  }

  @Test public void a02_CreateContext() {
    context = into.s(INPUT);
    assert context != null;
  }

  @Test public void a03_FindFocus() {
    a02_CreateContext();
    focus = findFirst.instanceOf(ThrowStatement.class).in(context);
    assert focus != null;
  }

  @Test public void a04_init() {
    a01_createTipper();
    a02_CreateContext();
    a03_FindFocus();
  }

  @Test public void b01init() {
    a04_init();
  }

  @Test public void b02findFirstThrow() {
    a04_init();
    azzert.that(findFirst.instanceOf(ThrowStatement.class).in(context), instanceOf(ThrowStatement.class));
  }

  @Test public void b03canSuggest() {
    a04_init();
    assert tipper.check(focus);
  }

  @Test public void b03demands() {
    a04_init();
    assert tipper.check(focus);
  }

  @Test public void b04tipNotNull() {
    a04_init();
    assert tipper.tip(focus) != null;
  }

  @Test public void b05ConfigCanFindTipper() {
    a04_init();
    assert Configurations.all().firstTipper(focus) != null;
  }

  @Test public void b06ConfigCanFindFindCorrectTipper() {
    a04_init();
    azzert.that(Configurations.all().firstTipper(focus), instanceOf(tipper.getClass()));
  }

  @Test public void b07callSuggest() {
    a04_init();
    tipper.tip(focus);
  }

  @Test public void b09descriptionNotNull() {
    a04_init();
    assert tipper.tip(focus).description != null;
  }

  @Test public void b0suggestNotNull() {
    a04_init();
    assert tipper.tip(focus) != null;
  }

  @Test public void b10descriptionContains() {
    a04_init();
    azzert.that(tipper.tip(focus).description, containsString(focus + ""));
  }

  @Test public void b12rangeNotEmpty() {
    a04_init();
    assert !tipper.tip(focus).highlight.isEmpty();
  }

  @Test public void b13applyTipper() {
    a04_init();
    tipper.tip(focus);
  }

  @Test public void b14applyTipper() {
    a04_init();
    Configurations.all().firstTipper(focus);
  }

  @Test public void doubleVanillaThrow() {
    a04_init();
    trimminKof("int f() { if (false)    i++;  else {    g(i);    throw new RuntimeException();  }  f(); a = 3; return 2;}")//
        .gives("int f(){{g(i);throw new RuntimeException();}f();a=3;return 2;}").gives("int f(){g(i);throw new RuntimeException();f();a=3;return 2;}")//
        .gives("int f(){g(i);throw new RuntimeException();a=3;return 2;}").gives("int f(){g(i);throw new RuntimeException();return 2;}")//
        .gives("int f(){g(i);throw new RuntimeException();}")//
        .stays();
  }

  private SequencerNotLastInBlock<ThrowStatement> makeTipper() {
    return new SequencerNotLastInBlock<>();
  }

  @Test public void vanilla() {
    trimminKof("{   throw Something();  f(); a = 3; return 2;}")//
        .gives("throw Something();f(); a=3; return 2;").gives("throw Something();a=3; return 2;")//
        .gives("throw Something(); return 2;")//
        .gives("throw Something();")//
        .stays();
  }

  @Test public void vanilla01() {
    trimminKof("throw Something();a=3; return 2;")//
        .gives("throw Something(); return 2;")//
        .gives("throw Something();")//
        .stays();
  }

  @Test public void vanilla02() {
    trimminKof("return Something();a=3; return 2;")//
        .gives("return Something(); return 2;")//
        .gives("return Something();")//
        .stays();
  }

  @Test public void vanilla03() {
    trimminKof("continue a;a=3; return 2;")//
        .gives("continue a; return 2;")//
        .gives("continue a;")//
        .stays();
  }

  @Test public void vanilla04() {
    trimminKof("break a;a=3; return 2;")//
        .gives("break a; return 2;")//
        .gives("break a;")//
        .stays();
  }

  @Test public void vanilla05() {
    trimminKof("continue ;a=3; return 2;")//
        .gives("continue ; return 2;")//
        .gives("continue ;")//
        .stays();
  }

  @Test public void vanilla06() {
    trimminKof("break;a=3; return 2;")//
        .gives("break; return 2;")//
        .gives("break;")//
        .stays();
  }
}
