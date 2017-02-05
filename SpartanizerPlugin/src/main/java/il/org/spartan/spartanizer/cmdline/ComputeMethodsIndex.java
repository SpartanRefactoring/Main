package il.org.spartan.spartanizer.cmdline;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;


/** Compute reusabilty index for methods Meanwhile just computes the index of
 * tokens for methods...
 * @author Ori Marcovitch
 * @since Dec 14, 2016 */
public class ComputeMethodsIndex extends FolderASTVisitor {
  static {
    clazz = ComputeMethodsIndex.class;
  }

  public static void main( final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
  }

  @Override @SuppressWarnings("boxing") public boolean visit(final MethodDeclaration ¢) {
    if (step.statements(¢) == null || step.statements(¢).isEmpty())
      return false;
    methods.put(identifier(name(¢)), metrics.tokens(¢ + ""));
    return true;
  }

  private final Map<String, Integer> methods = new LinkedHashMap<>();

  @Override protected void init(final String path) {
    System.err.println("Processing: " + path);
  }

  @Override protected void done(final String path) {
    dotter.end();
    System.err.println("Done processing: " + path);
    final CSVLineWriter writer = new CSVLineWriter(makeFile("node-types"));
    int n = 0;
    for (final String key : methods.keySet()) {
      writer//
          .put("N", ++n)//
          .put("Key", '"' + key + '"')//
          .put("tokens", methods.get(key)) //
      ;
      writer.nl();
    }
    System.err.println("Your output is in: " + writer.close());
  }
}
