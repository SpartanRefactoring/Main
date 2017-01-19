package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.methods.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.tables.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-03 */
public class TableNanosStatistics extends FolderASTVisitor {
  private static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  private static Table pWriter;
  private static final NanoPatternsStatistics npStatistics = new NanoPatternsStatistics();
  private static final Set<JavadocMarkerNanoPattern> excluded = new HashSet<JavadocMarkerNanoPattern>() {
    static final long serialVersionUID = 1L;
    {
      add(new HashCodeMethod());
      add(new ToStringMethod());
    }
  };
  static {
    clazz = TableNanosStatistics.class;
    Logger.subscribe(npStatistics::logNPInfo);
  }

  private static void initializeWriter() {
    pWriter = new Table(outputFileName());
  }

  private static String outputFileName() {
    return TableNanosStatistics.class.getSimpleName();
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
    pWriter.close();
    System.err.println("Your output is in: " + Table.temporariesFolder + outputFileName());
  }

  @Override public boolean visit(final MethodDeclaration $) {
    System.out.println($.getName());
    if (!excludeMethod($))
      try {
        spartanalyzer.fixedPoint(Wrap.Method.on($ + ""));
      } catch (@SuppressWarnings("unused") final AssertionError __) {
        System.err.print("X");
      } catch (@SuppressWarnings("unused") final IllegalArgumentException __) {
        System.err.print("I");
      }
    return super.visit($);
  }

  @Override public boolean visit(final CompilationUnit ¢) {
    ¢.accept(new CleanerVisitor());
    return true;
  }

  @Override protected void done(final String path) {
    summarizeNPStatistics(path);
    System.err.println(" " + path + " Done");
  }

  private static boolean excludeMethod(final MethodDeclaration ¢) {
    return iz.constructor(¢) || body(¢) == null || anyTips(excluded, ¢);
  }

  public static void summarizeNPStatistics(final String path) {
    if (pWriter == null)
      initializeWriter();
    pWriter.col("Project", path);
    npStatistics.keySet().stream()//
        .sorted((k1, k2) -> npStatistics.get(k1).name.compareTo(npStatistics.get(k2).name))//
        .map(npStatistics::get)//
        .forEach(n -> pWriter.col(n.name, n.occurences));
    fillAbsents();
    pWriter.nl();
    npStatistics.clear();
  }

  private static void fillAbsents() {
    spartanalyzer.getAllPatterns().stream()//
        .map(p -> p.getClass().getSimpleName())//
        .filter(n -> !npStatistics.keySet().contains(n))//
        .forEach(n -> pWriter.col(n, 0));
  }

  private static boolean anyTips(final Collection<JavadocMarkerNanoPattern> ps, final MethodDeclaration d) {
    return d != null && ps.stream().anyMatch(t -> t.canTip(d));
  }
}
