package il.org.spartan.refactoring.utils;

import org.eclipse.jdt.core.dom.*;

/**
 * @author Yossi Gil
 *
 * @since 2016`
 */
public class PlantStatement {
  private final Statement inner;

  /**
   * instantiates this class
   *
   * @param inner JD
   */
  public PlantStatement(final Statement inner) {
    this.inner = inner;
  }
  /**
   * TODO Javadoc(2016): automatically generated for method
   * <code>intoThen</code>
   *
   * @param s JD
   */
  public void intoThen(final IfStatement s) {
    final IfStatement plant = Funcs.asIfStatement(inner);
    s.setThenStatement(plant == null || plant.getElseStatement() != null ? inner : Subject.statements(inner).toBlock());
  }
}