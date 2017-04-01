package il.org.spartan.spartanizer.issues;

import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.Proposition.*;

import java.util.function.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.utils.*;

/** pretty print of proposition and testing
 * @author oran1248 <tt>oran.gilboa1@gmail.com</tt>
 * @since 2017-03-30 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1189 {
  private static final String TAB = "\t";
  private static final String NL = "\n";

  @Test @SuppressWarnings("unused") public void a() {
    azzert.that(AND("A:", that("B1", () -> {
      throw new AssertionError();
    }), OR("B2:", that("C1", T), that("C2", X), AND("C3:", that("D1", X), that("D2", F))))
        .reduce(new PropositionReducer<String>(new ReduceStringConcatenate()) {
          Tab tab = new Tab();

          @Override protected String map(final BooleanSupplier ¢) {
            return ¢ + "";
          }

          @Override protected String ante(final Proposition.Some ¢) {
            return tab.begin() + ¢ + " " + (¢ instanceof And ? "AND" : ¢ instanceof Or ? "OR" : "UNKOWN") + NL;
          }

          @Override protected String ante(final Proposition.Singleton ¢) {
            return tab.begin();
          }

          @Override protected String post(final Proposition.Some ¢) {
            tab.less();
            return "";
          }

          @Override protected String post(final Proposition.Singleton ¢) {
            tab.less();
            return "";
          }

          @Override protected String inter(final Proposition.And ¢) {
            return NL;
          }

          @Override protected String inter(final Proposition.Or ¢) {
            return NL;
          }
        }), is("A: AND" + NL + TAB + "B1" + NL + TAB + "B2: OR" + NL + TAB + TAB + "C1" + NL + TAB + TAB + "C2" + NL + TAB + TAB + "C3: AND" + NL
            + TAB + TAB + TAB + "D1" + NL + TAB + TAB + TAB + "D2"));
  }
}
