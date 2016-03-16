package il.org.spartan.refactoring.utils;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

public class PlantStatement {
  private final Statement inner;
  public PlantStatement(final Statement inner) {
    this.inner = inner;
  }
  public void intoThen(final IfStatement s) {
    final IfStatement plant = Funcs.asIfStatement(inner);
    s.setThenStatement(plant == null || plant.getElseStatement() != null ? inner : Subject.statements(inner).toBlock());
  }
}