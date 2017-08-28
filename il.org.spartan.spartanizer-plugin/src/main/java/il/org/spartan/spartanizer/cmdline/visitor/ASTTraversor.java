package il.org.spartan.spartanizer.cmdline.visitor;

import java.io.*;
import java.nio.file.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-08-24 */
public class ASTTraversor extends Traverse.Execution {
  private final ASTVisitorsAggregate visitors = new ASTVisitorsAggregate();
  void visitFile() {
    try {
      Object relativePath = Paths.get(f.getCanonicalPath()).subpath(Paths.get(inputFolder).getNameCount(),
          Paths.get(file().getCanonicalPath()).getNameCount()) + "";
      absolutePath = Paths.get(f.getCanonicalPath()).subpath(Paths.get(inputFolder).getNameCount(), Paths.get(f.getCanonicalPath()).getNameCount())
          + "";
    } catch (IOException ¢) {
      ¢.printStackTrace();
    }
  }
  public final ASTTraversor withVisitor(ASTVisitor ¢) {
    visitors.push(¢);
    return this;
  }
  @Override final void visitFile() {
    try {
      String relativePath = Paths.get(file().getCanonicalPath()).subpath(Paths.get(inputFolder).getNameCount(),
          Paths.get(file().getCanonicalPath()).getNameCount()) + "";
      String absolutePath = Paths.get(file.getCanonicalPath()).subpath(Paths.get(inputFolder).getNameCount(),
          Paths.get(f.getCanonicalPath()).getNameCount()) + "";
    } catch (IOException ¢) {
      ¢.printStackTrace();
    }
  }
  public ASTTraversor(String[] arguments) {
    super(arguments);
    withTapper(new I.IdleTapper() {
      @Override @SuppressWarnings("synthetic-access") public void beginFile() {
        ((CompilationUnit) makeAST.COMPILATION_UNIT.from(content)).accept(visitors);
      }
    });
  }

  public abstract class Visitor extends ASTVisitor implements Traverse.Delegator {
    @Override public final Traverse inner() {
      return ASTTraversor.this;
    }
  }
}
