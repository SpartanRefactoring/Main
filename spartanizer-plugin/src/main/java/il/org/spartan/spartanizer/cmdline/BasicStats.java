package il.org.spartan.spartanizer.cmdline;

import java.io.IOException;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import fluent.ly.system;
import il.org.spartan.utils.Accumulator.Counter;

/** TODO Matteo Orru': document class
 * @author Matteo Orru'
 * @since 2017-06-30 */
public class BasicStats extends ASTInFilesVisitor {
  static final Counter classes = new Counter("Classes");
  static final Counter methods = new Counter("Methods");

  public static void main(String[] args) {
    out = system.callingClassUniqueWriter();
    try {
      out.write("project\tClassesNum\tMethodsNum\n");
    } catch (IOException x1) {
      x1.printStackTrace();
    }
    BasicStats stats = new BasicStats(args);
    stats.visitAll(new ASTTrotter() {
      @Override public boolean preVisit2(final ASTNode ¢) {
        return true;
      }
      @Override public boolean visit(final MethodDeclaration ¢) {
        methods.inc();
        System.err.printf(getCurrentLocation() + " Visiting Method: %s\t(%d)\n", ¢.getName(), methods.val());
        return true;
      }
      @Override public boolean visit(final ReturnStatement ¢) {
        return true;
      }
      @Override public boolean visit(final TypeDeclaration ¢) {
        classes.inc();
        System.err.printf(getCurrentLocation() + " Visiting Type: %s\t(%d)\n", ¢.getName(), classes);
        return true;
      }
      @Override protected void record(final String summary) {}
    });
    try {
      out.close();
    } catch (IOException ¢) {
      ¢.printStackTrace();
    }
    System.err.printf("Num Classes: %d\t Num Methods: %d\n", classes, methods);
  }
  public BasicStats(String[] args) {
    super(args);
  }
  @Override public void visitAll(final ASTVisitor v) {
    notify.beginBatch();
    astVisitor = v;
    locations.forEach(λ -> {
      setCurrentLocation(λ);
      visitLocation();
      try {
        out.write(getCurrentLocation() + "\t" + classes + "\t" + methods + "\n");
      } catch (IOException ¢) {
        ¢.printStackTrace();
      }
    });
  }
}
