package il.org.spartan.spartanizer.cmdline.good;

import static il.org.spartan.spartanizer.ast.navigate.step.parameters;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.tables.Table;

public class TableMethodProperties {
  public static void main(final String[] args) {
    try (Table t = new Table(TableMethodProperties.class)) {
      new GrandVisitor(args).visitAll(new ASTVisitor(true) {
        @Override public boolean visit(final MethodDeclaration ¢) {
          t//
              .col("Name", ¢.getName()) //
              .col("#Parameters", parameters(¢).size()) //
              .col("Private", java.lang.reflect.Modifier.isStatic(¢.getModifiers())) //
              .nl();
          return true;
        }
      });
      System.out.println("Output is in " + t.description());
    }
  }
}