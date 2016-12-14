package il.org.spartan.spartanizer.cmdline.collector;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;

/** Collects various reusability indices for a given folder(s)
 * @author Yossi Gil
 * @since 2016 */
public class TableReusabilityIndices extends FolderASTVisitor {
  static {
    clazz = TableReusabilityIndices.class;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
    System.err.println("Your output is in: " + writer.close());
  }

  private static String key(final Assignment ¢) {
    return key(¢.getOperator());
  }

  private static String key(final Class<? extends ASTNode> key) {
    return key.getSimpleName();
  }

  private static String key(final InfixExpression.Operator o, final int arity) {
    return arity == 3 ? o + "(ternary)" : arity == 2 ? o + "(binary)" : //
        o + "(" + arity + "ary)";
  }

  private static String key(final org.eclipse.jdt.core.dom.Assignment.Operator key) {
    return key + "(assignment)";
  }

  private static String key(final PrefixExpression ¢) {
    return key(¢.getOperator());
  }

  private static String key(final PrefixExpression.Operator key) {
    return key + "(pre)";
  }

  private int maxArity;
  private final Map<String, Map<String, Integer>> usage = new LinkedHashMap<>();

  public Map<String, Integer> addIfNecessary(final String category, final String key) {
    if (usage.get(category) == null)
      usage.put(category, new LinkedHashMap<>());
    final Map<String, Integer> $ = usage.get(category);
    assert $ != null;
    $.putIfAbsent(key, Integer.valueOf(0));
    return $;
  }

  public static int rindex(int[] ranks) {
    Arrays.sort(ranks);
    int $ = 0;
    for (int ¢ = 0; ¢ < ranks.length; ++¢)
      $ = Math.max($, Math.min(ranks[¢], ranks.length - ¢));
    return $;
  }

  @Override public void preVisit(final ASTNode ¢) {
    increment("NODE-TYPE", key(¢.getClass()));
  }

  @Override public boolean visit(final Assignment ¢) {
    return increment("ASSIGNMENT", key(¢));
  }

  @Override public boolean visit(final InfixExpression ¢) {
    return increment("INFIX", key(¢));
  }

  @Override public boolean visit(final PostfixExpression ¢) {
    return increment("POSTFIX", key(¢));
  }

  private static String key(final PostfixExpression ¢) {
    return key(¢.getOperator());
  }

  private static String key(final PostfixExpression.Operator ¢) {
    return ¢ + "(post)";
  }

  @Override public boolean visit(final MethodInvocation ¢) {
    return increment("METHOD", key(¢));
  }

  private static String key(MethodInvocation ¢) {
    return ¢.getName() + "/" + step.arguments(¢).size();
  }

  @Override public boolean visit(final PrefixExpression ¢) {
    return increment("PREFIX", key(¢));
  }

  @Override protected void init(String path) {
    System.err.println("Processing: " + path);
  }

  @Override protected void done(String path) {
    dotter.end();
    System.err.println("Done processing: " + path);
    addMissingKeys();
    reporRawUsage();
  }

  void addMissingKeys() {
    for (final Class<? extends ASTNode> ¢ : wizard.classToNodeType.keySet())
      addIfNecessary("NODE-TYPE", key(¢));
    for (final Assignment.Operator ¢ : wizard.assignmentOperators)
      addIfNecessary("ASSIGN",key(¢));
    for (final PrefixExpression.Operator ¢ : wizard.prefixOperators)
      addIfNecessary("PREFIX", key(¢));
    for (final PostfixExpression.Operator ¢ : wizard.postfixOperators)
      addIfNecessary("POSTFIX", key(¢));
    for (final Operator ¢ : wizard.infixOperators)
      for (int arity = 2; arity <= maxArity; ++arity)
        addIfNecessary("INFIX", key(¢, arity));
  }
  static final CSVLineWriter writer = new CSVLineWriter(outputFolder + "/rindex");

  void reporRawUsage() {
    int n = 0;
    final CSVLineWriter w = new CSVLineWriter(makeFile("raw-reuse-ranks"));
    for (final String category : usage.keySet()) {
      int m = 0;
      final Map<String, Integer> map = usage.get(category);
      int[] ranks = new int[map.size()];
      for (final String key : map.keySet()) {
        ranks[m++] = map.get(key).intValue();
        w//
            .put("N", ++n)//
            .put("M", m)//
            .put("Category", category)//
            .put("Key", '"' + key + '"')//
            .put("Count", map.get(key)) //
        ;
        w.nl();
      }
        writer.put("Name", presentSourceName);
        writer.put("Category", category);
        writer.put("R-Index", rindex(ranks));
        writer.nl();
    }
    System.err.println("Your output is in: " + w.close());
  }

  boolean increment(final String category, String key) {
    return increment(addIfNecessary(category, key), key);
  }

  public static boolean increment(Map<String, Integer> category, String key) {
    category.put(key, Integer.valueOf(category.get(key).intValue() + 1));
    return true;
  }

  private String key(final InfixExpression ¢) {
    return key(¢, wizard.arity(¢));
  }

  private String key(final InfixExpression ¢, final int arity) {
    maxArity = Math.max(arity, maxArity);
    return key(¢.getOperator(), arity);
  }
}
