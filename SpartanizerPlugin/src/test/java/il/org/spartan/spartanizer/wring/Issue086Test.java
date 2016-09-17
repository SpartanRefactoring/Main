package il.org.spartan.spartanizer.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.strategies.*;
import il.org.spartan.spartanizer.wrings.*;

/** Unit tests for {@link ThrowNotLastInBlock}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue086Test extends Issue___TestTemplate {
  private static final String INPUT = "{"//
      + "   throw Something; "//
      + " f();" //
      + " a = 3;" //
      + " return 2;" //
      + "}";
  Wring<ThrowStatement> wring;
  Statement context;
  ThrowStatement focus;

  @Test public void A$01_createWring() {
    wring = makeWring();
    assert wring != null;
  }

  @Test public void A$02_CreateContext() {
    context = into.s(INPUT);//
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
    assert wring.canSuggest(focus);
  }

  @Test public void B$03demands() {
    A$04_init();
    assert wring.demandsToSuggestButPerhapsCant(focus);
  }

  @Test public void B$04suggestionNotNull() {
    A$04_init();
    assert wring.suggest(focus) != null;
  }

  @Ignore @Test public void B$05toolboxCanFindWring() {
    A$04_init();
    final Wring<?> w = Toolbox.defaultInstance().find(focus);
    assert w != null;
  }

  @Ignore @Test public void B$06toolboxCanFindFindCorrectWring() {
    A$04_init();
    final Wring<?> w = Toolbox.defaultInstance().find(focus);
    azzert.that(w, instanceOf(wring.getClass()));
  }

  @Test public void B$07callSuggest() {
    A$04_init();
    wring.suggest(focus);
  }

  @Test public void B$09descriptionNotNull() {
    A$04_init();
    assert wring.suggest(focus).description != null;
  }

  @Test public void B$0suggestNotNull() {
    A$04_init();
    assert wring.suggest(focus) != null;
  }

  @Test public void B$10descriptionContains() {
    A$04_init();
    azzert.that(wring.suggest(focus).description, //
        containsString(focus + ""));
  }

  @Test public void B$12rangeNotEmpty() {
    A$04_init();
    assert !wring.suggest(focus).isEmpty();
  }

  @Test public void B$13applyWring() {
    A$04_init();
    wring.suggest(focus);
  }

  @Test public void B$14applyWring() {
    A$04_init();
    Toolbox.defaultInstance().find(focus);
  }

  @Ignore @Test public void doubleVanillaThrow() {
    A$04_init();
    trimming("int f() {"//
        + " if (false) "//
        + "   i++; "//
        + " else { "//
        + "   g(i); "//
        + "   throw new RuntimeException(); "//
        + " } "//
        + " f();" //
        + " a = 3;" //
        + " return 2;" + "}"//
    )//
        .to("int f(){{g(i);throw new RuntimeException();}f();a=3;return 2;}") //
        .to("int f(){g(i);throw new RuntimeException();f();a=3;return 2;}") //
        .to("int f(){g(i);throw new RuntimeException();a=3;return 2;}") //
        .to("int f(){g(i);throw new RuntimeException();}") //
        .stays();
  }

  @Ignore @Test public void vanilla() {
    trimming(INPUT)//
        .to("throw Something();a=3; return 2;") //
        .to("throw Something(); return 2;") //
        .to("throw Something()") //
        .stays();
  }

  private ThrowNotLastInBlock makeWring() {
    return new ThrowNotLastInBlock();
  }
}
