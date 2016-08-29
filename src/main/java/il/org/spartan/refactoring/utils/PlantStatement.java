package il.org.spartan.refactoring.utils;

import org.eclipse.jdt.core.dom.*;

public class PlantStatement {
  private final Statement inner;

  public PlantStatement(final Statement inner) {
    this.inner = inner;
  }

  public void intoThen(final IfStatement s) {
    final IfStatement plant = az.ifStatement(inner);
    s.setThenStatement(plant == null || plant.getElseStatement() != null ? inner : subject.statements(inner).toBlock());
  }
}