package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.tables.*;

/**
 * TODO Matteo Orru': document class {@link }
 * @author Matteo Orru' <tt>matteo.orru@cs.technion.ac.il</tt>
 * @since 2017-02-10
 */
public class Table_SummaryForPaper extends FolderASTVisitor {
  
  private static Table writer;
  
  private static HashMap<String, HashSet<String>> packageMap = new HashMap<>();
  private static HashSet<String> packages = new HashSet<>();
  
  private final Stack<CompilationUnitRecord> CopilationUnitRecords = new Stack<>();
  private final Stack<ClassRecord> classRecords = new Stack<>();

  protected static final SortedMap<Integer, List<CompilationUnitRecord>> CUStatistics = new TreeMap<>(Integer::compareTo);
  protected static final SortedMap<Integer, List<ClassRecord>> classStatistics = new TreeMap<>(Integer::compareTo);
  
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  
  static {
    clazz = Table_SummaryForPaper.class;
    // Logger.subscribe(Table_SummaryForPaper::logNanoContainingMethodInfo);
  }
  
  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
    writer.close();
    System.err.println("Your output is in: " + Table.temporariesFolder);
  }
  
  @Override public boolean visit(final CompilationUnit ¢) {
    CopilationUnitRecords.add(new CompilationUnitRecord(¢));
    count.lines(¢);
    ¢.accept(new CleanerVisitor());
    return true;
  }
  
  @Override public boolean visit(final PackageDeclaration ¢) {
    packages.add(¢.getName().getFullyQualifiedName());
    return true;
  }
  
  @SuppressWarnings("unused")
  @Override public boolean visit(final TypeDeclaration $) {
//  if (!excludeMethod($))
    try {
      final Integer key = box.it(measure.statements($));
      //
      CUStatistics.putIfAbsent(key, new ArrayList<>());
      classStatistics.putIfAbsent(key, new ArrayList<>());
      //
      final ClassRecord c = new ClassRecord($);
      classRecords.push(c);
      classStatistics.get(key).add(c);
      final TypeDeclaration d = findFirst.instanceOf(TypeDeclaration.class)
          .in(ast(Wrap.OUTER.off(spartanalyzer.fixedPoint(Wrap.OUTER.on($ + "")))));
      //
//      if (d != null)
//        npDistributionStatistics.logNode(d);
    } catch (final AssertionError __) {
      System.err.print("X");
    } catch (final NullPointerException __) {
      System.err.print("N");
    } catch (final IllegalArgumentException __) {
      System.err.print("I");
    }
    return true; //super.visit($);
  }
  
  @Override protected void done(final String path) {
    if (writer == null)
      initializeWriter();
    writer//
    .col("Project", path)//
    .col("#Packages", countPackages())//
    .col("#LOC", countLOC())//
    .col("#Classes", countClasses())//
    .col("#Methods", countMethods())//
    .nl();
    System.err.println("Your output is in: " + outputFolder);
  }

  private int countClasses() {
    int $ = 0;
    for(final CompilationUnitRecord ¢: CopilationUnitRecords)
      $ += ¢.numClasses;
    return ($);
  }

  private int countLOC() {
    int $ = 0;
    for(final CompilationUnitRecord ¢: CopilationUnitRecords)
      $ += ¢.linesOfCode;
    return $;
  }

  private int countMethods() {
    int $ = 0;
    for(final CompilationUnitRecord ¢: CopilationUnitRecords)
      $ += ¢.numMethods;
    return $;
  }

  private int countPackages() {
    HashSet<String> packgSet = new HashSet<>();
    for(final CompilationUnitRecord ¢: CopilationUnitRecords)
      packgSet.add(¢.pakcage);
    return packgSet.size();
  }

  @SuppressWarnings({ "unused", "static-method" })
  private int packages(final String key) {
    return packageMap.get(key).size();
  }

  private static void initializeWriter() {
    writer = new Table(clazz);
  }
  
}
