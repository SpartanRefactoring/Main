package il.org.spartan.spartanizer.cmdline.collector;

import static il.org.spartan.spartanizer.research.analyses.util.Files.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.analyses.util.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since Dec 14, 2016 */
public class SortedSpartanizedMethodsCollector extends FolderASTVisitor {
  static SpartAnalyzer spartanizer = new SpartAnalyzer();
  SortedMap<Integer, List<MethodDeclaration>> methods = new TreeMap<>((o1, o2) -> o1.compareTo(o2));
  static {
    clazz = SortedSpartanizedMethodsCollector.class;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
  }

  @Override public boolean visit(final MethodDeclaration ¢) {
    if (excludeMethod(¢))
      return false;
    Count.before(¢);
    try {
      final MethodDeclaration after = findFirst.methodDeclaration(wizard.ast(Wrap.Method.off(spartanizer.fixedPoint(Wrap.Method.on(¢ + "")))));
      Count.after(after);
      final Integer key = Integer.valueOf(count.statements(after));
      methods.putIfAbsent(key, new ArrayList<>());
      methods.get(key).add(after);
    } catch (@SuppressWarnings("unused") final AssertionError __) {
      //
    }
    return true;
  }

  @Override public void endVisit(final TypeDeclaration ¢) {
    if (haz.methods(¢))
      Logger.finishedType();
  }

  @Override public boolean visit(final CompilationUnit ¢) {
    Logger.logCompilationUnit(¢);
    ¢.accept(new CleanerVisitor());
    return true;
  }

  @Override public boolean visit(final TypeDeclaration ¢) {
    if (!haz.methods(¢))
      return false;
    Logger.logType(¢);
    return true;
  }

  @Override protected void init(final String path) {
    System.err.println("Processing: " + path);
  }

  @Override protected void done(final String path) {
    dotter.end();
    System.err.println("Done processing: " + path);
    System.err.println("Wait for output files...");
    writeFile(new File(makeFile("after.java")),
        methods.values().stream().map(li -> li.stream().map(x -> format.code(x + "")).reduce("", (x, y) -> x + y)).reduce("", (x, y) -> x + y));
    writeFile(new File(makeFile("notTagged.java")),
        methods.values().stream()
            .map(li -> li.stream().filter(m -> !(javadoc(m) + "").contains("[[")).map(x -> format.code(x + "")).reduce("", (x, y) -> x + y))
            .reduce("", (x, y) -> x + y));
    Logger.summarizeSortedMethodStatistics(outputFolder);
    Logger.summarizeNPStatistics(outputFolder);
    System.err.println("Your output is in: " + outputFolder);
  }

  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null;
  }
}
