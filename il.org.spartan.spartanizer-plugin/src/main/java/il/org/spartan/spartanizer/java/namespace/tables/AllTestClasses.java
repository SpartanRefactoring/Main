package il.org.spartan.spartanizer.java.namespace.tables;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Filter projects which does not contain any Junit test (Dor Msc.)
 * @author Dor Ma'ayan
 * @since 2019-01-10 */
public class AllTestClasses extends NominalTables {
  @SuppressWarnings({ "boxing", "unused" }) public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
    final HashMap<String, Integer> map = new HashMap<>();
    map.put("#Tests", 0);
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(CurrentData.location);
          }
        });
      }

      void reset() {
        map.put("#Tests", 0);
      }
      protected void done(final String path) {
        summarize(path);
        reset();
      }
      public void summarize(final String path) {
        initializeWriter();
//        if (map.get("#Tests") != 0) {
//          table.col("Project", path).nl();
//        }
      }
      void initializeWriter() {
        if (table == null)
          table = new Table(corpus, outputFolder);
      }
    }.visitAll(new ASTVisitor(true) {
      @Override public boolean visit(final CompilationUnit ¢) {
        ¢.accept(new ASTVisitor() {
          @Override public boolean visit(final MethodDeclaration x) {
            if (x != null) {
              List<String> annotations = extract.annotations(x).stream().map(a -> a.getTypeName().getFullyQualifiedName())
                  .collect(Collectors.toList());
              if (annotations.contains("Test") || (iz.typeDeclaration(x.getParent()) && az.typeDeclaration(x.getParent()).getSuperclassType() != null
                  && az.typeDeclaration(x.getParent()).getSuperclassType().toString().equals("TestCase"))) {
                map.put("#Tests", map.get("#Tests") + 1);
              }
            }
            return true;
          }
        });
        return super.visit(¢);
      }
    });
    table.close();
    System.err.println(table.description());
  }
}
