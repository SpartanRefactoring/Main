package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.visitor.*;
import il.org.spartan.utils.*;

public class ExpressionChain {
  public static void main(final String[] args) {
    try (BufferedWriter out = system.callingClassUniqueWriter()) {
    new MasterVisitor(args) {/**/}.visitAll(new ASTTrotter() {
      {
        hookClassOnRule(ExpressionStatement.class, new Rule.Stateful<ExpressionStatement, Void>() {
          @Override public Void fire() {
            return null;
          }
          @Override public boolean ok(final ExpressionStatement ¢) {
            return compute.useSpots(¢.getExpression()).size() == 1;
          }
        });
      }

      @Override protected void record(final String summary) {
        try {
          out.write(summary);
        } catch (final IOException ¢) {
          System.err.println("Error: " + ¢.getMessage());
        }
        super.record(summary);
      }
    });
  } catch (IOException ¢) {
    note.io(¢);
  }
  }
}