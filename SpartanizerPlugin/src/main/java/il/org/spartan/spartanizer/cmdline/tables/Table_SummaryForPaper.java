package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

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
  
  private static HashSet<String> classes = new HashSet<>();
  private static Integer methods;
  @SuppressWarnings("rawtypes") private static HashMap<String, HashSet> packageMap = new HashMap<>();
  private static HashSet<String> packages = new HashSet<>();
  
  private final Stack<CURecord> CURecords = new Stack<>();
  private static final Stack<ClassRecord> classRecords = new Stack<>();
  protected static final SortedMap<Integer, List<CURecord>> CUStatistics = new TreeMap<>(Integer::compareTo);
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
    CURecords.add(new CURecord(¢));
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
      final Integer key = Integer.valueOf(measure.statements($));
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
  
//  @Override public boolean visit(final CompilationUnit $) {
////  if (!excludeMethod($))
//    try {
//      final Integer key = Integer.valueOf(measure.statements($));
//      //
//      CUStatistics.putIfAbsent(key, new ArrayList<>());
//      classStatistics.putIfAbsent(key, new ArrayList<>());
//      //
//      final ClassRecord c = new ClassRecord($);
//      ClassRecords.push(c);
//      classStatistics.get(key).add(c);
//      final TypeDeclaration d = findFirst.instanceOf(TypeDeclaration.class)
//          .in(ast(Wrap.OUTER.off(spartanalyzer.fixedPoint(Wrap.OUTER.on($ + "")))));
//      //
////      if (d != null)
////        npDistributionStatistics.logNode(d);
//    } catch (@SuppressWarnings("unused") final AssertionError __) {
//      System.err.print("X");
//    } catch (@SuppressWarnings("unused") final NullPointerException __) {
//      System.err.print("N");
//    } catch (@SuppressWarnings("unused") final IllegalArgumentException __) {
//      System.err.print("I");
//    }
//    return true; //super.visit($);
//  }
  
//  @Override public boolean visit(final MethodDeclaration ¢) {
//    if (excludeMethod(¢))
//      return false;
//    try {
//      final Integer key = Integer.valueOf(measure.statements(¢));
//      statementsCoverageStatistics.putIfAbsent(key, new ArrayList<>());
//      final MethodRecord m = new MethodRecord(¢);
//      scope.push(m);
//      statementsCoverageStatistics.get(key).add(m);
//      final MethodDeclaration d = findFirst.instanceOf(MethodDeclaration.class)
//          .in(ast(Wrap.Method.off(spartanalyzer.fixedPoint(Wrap.Method.on(¢ + "")))));
//      if (d != null)
//        npDistributionStatistics.logNode(d);
//    } catch (final AssertionError __) {
//      ___.unused(__);
//    }
//    return true;
//  }

  
  @Override protected void done(final String path) {
    if (writer == null)
      initializeWriter();
    writer//
    .col("Project", path)//
    .col("#Packages", countPackages())//
    .col("#LOC", countLOC())//
    .col("#Classes", classRecords.size())//
    .col("#Methods", countMethods())//
    .nl();
    System.err.println("Your output is in: " + outputFolder);
  }

  @SuppressWarnings("boxing")
  private Integer countLOC() {
    Integer $ = 0;
    for(final CURecord ¢: CURecords)
      $ += ¢.getLOC();
    return $;
  }

  @SuppressWarnings({ "boxing" })
  private Integer countMethods() {
    Integer $ = 0;
    for(final CURecord ¢: CURecords)
      $ += ¢.getNumMethods();
    return $;
  }

  @SuppressWarnings("boxing")
  private Integer countPackages() {
    HashSet<String> packgSet = new HashSet<>();
    for(final CURecord ¢: CURecords)
      packgSet.add(¢.getPackage());
    return packgSet.size();
  }

  @SuppressWarnings({ "boxing", "unused", "static-method" })
  private Integer packages(final String key) {
    return packageMap.get(key).size();
  }

  @SuppressWarnings({ "unused", "static-method" })
  private Integer methods() {
    return methods;
  }

  @SuppressWarnings({ "unused", "boxing", "static-method" })
  private Integer classes() {
    return classes.size();
  }

  private static void initializeWriter() {
    writer = new Table(clazz);
  }
  
}
