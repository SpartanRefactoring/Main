package il.org.spartan.spartanizer.cmdline;

import java.util.function.*;

public class NamedFunction<ASTNode, T> {
  final String name;
  final Function<ASTNode, T> function;

  NamedFunction(final String name, final Function<ASTNode, T> function) {
    this.name = name;
    this.function = function;
  }
  public String name() {
    return name;
  }
  public Function<ASTNode, T> function() {
    return function;
  }
}