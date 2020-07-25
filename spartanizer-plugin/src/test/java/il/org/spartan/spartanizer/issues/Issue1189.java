package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.is;
import static il.org.spartan.utils.Proposition.AND;
import static il.org.spartan.utils.Proposition.F;
import static il.org.spartan.utils.Proposition.OR;
import static il.org.spartan.utils.Proposition.T;
import static il.org.spartan.utils.Proposition.X;
import static il.org.spartan.utils.Proposition.that;

import java.util.function.BooleanSupplier;

import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.utils.Proposition;
import il.org.spartan.utils.Proposition.And;
import il.org.spartan.utils.Proposition.Or;
import il.org.spartan.utils.PropositionReducer;
import il.org.spartan.utils.ReduceStringConcatenate;
import il.org.spartan.utils.Tab;

/** pretty print of proposition and testing
 * @author oran1248 <tt>oran.gilboa1@gmail.com</tt>
 * @since 2017-03-30 */
@SuppressWarnings({ "static-method", "javadoc", "unused" })
public class Issue1189 {
  private static final String TAB = "\t";
  private static final String NL = "\n";

  @Test public void a() {
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
