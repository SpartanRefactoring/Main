package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.tables.*;

/** Collects various reusability indices for a given folder(s)
 * @author Yossi Gil
 * @since 2016 */
public class TableReusabilityIndices extends FolderASTVisitor {
  static {
    clazz = TableReusabilityIndices.class;
  }
  private static Table writer;

  public static boolean increment(final Map<String, Integer> category, final String key) {
    category.put(key, Integer.valueOf(category.get(key).intValue() + 1));
    return true;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
    writer.close();
    System.out.println("Your output is in " + writer.description());
  }

  public static int rindex(final int[] ranks) {
    Arrays.sort(ranks);
    int $ = 0;
    for (int ¢ = 0; ¢ < ranks.length; ++¢)
      $ = Math.max($, Math.min(ranks[¢], ranks.length - ¢));
    return $;
  }

  static String key(final SimpleName n, final int arity) {
    return n + "/" + arity;
  }

  static int[] ranks(final Map<?, Integer> i) {
    int n = 0;
    final int $[] = new int[i.size()];
    for (final Integer ¢ : i.values())
      $[n++] = ¢.intValue();
    return $;
  }

  private static String key(final Assignment ¢) {
    return key(¢.getOperator());
  }

  private static String key(final Assignment.Operator key) {
    return key + "";
  }

  private static String key(final Class<? extends ASTNode> key) {
    return key.getSimpleName();
  }

  private static String key(final InfixExpression.Operator o, final int arity) {
    return o + "/" + arity;
  }

  private static String key(final MethodInvocation ¢) {
    return key(¢.getName(), step.arguments(¢).size());
  }

  private static String key(final PostfixExpression ¢) {
    return key(¢.getOperator());
  }

  private static String key(final PostfixExpression.Operator ¢) {
    return ¢ + (PrefixExpression.Operator.toOperator(¢ + "") == null ? "" : "(post)");
  }

  private static String key(final PrefixExpression ¢) {
    return key(¢.getOperator());
  }

  private static String key(final PrefixExpression.Operator ¢) {
    return ¢ + (PostfixExpression.Operator.toOperator(¢ + "") == null ? "" : "(pre)");
  }

  private int maxArity;
  private final Map<String, Map<String, Integer>> usage = new LinkedHashMap<>();
  private final Set<String> defined = new LinkedHashSet<>();

  public Map<String, Integer> addIfNecessary(final String category, final String key) {
    if (usage.get(category) == null)
      usage.put(category, new LinkedHashMap<>());
    final Map<String, Integer> $ = usage.get(category);
    assert $ != null;
    $.putIfAbsent(key, Integer.valueOf(0));
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

  @Override public boolean visit(final MethodDeclaration ¢) {
    return defined.add(key(¢.getName(), step.parameters(¢).size()));
  }

  @Override public boolean visit(final MethodInvocation ¢) {
    return increment("METHOD", key(¢));
  }

  @Override public boolean visit(final PostfixExpression ¢) {
    return increment("POSTFIX", key(¢));
  }

  @Override public boolean visit(final PrefixExpression ¢) {
    return increment("PREFIX", key(¢));
  }

  @Override protected void done(@SuppressWarnings("unused") final String path) {
    dotter.end();
    addMissingKeys();
    summarize();
  }

  void addMissingKeys() {
    for (final Class<? extends ASTNode> ¢ : wizard.classToNodeType.keySet())
      addIfNecessary("NODE-TYPE", key(¢));
    for (final Assignment.Operator ¢ : wizard.assignmentOperators)
      addIfNecessary("ASSIGNMENT", key(¢));
    for (final PrefixExpression.Operator ¢ : wizard.prefixOperators)
      addIfNecessary("PREFIX", key(¢));
    for (final PostfixExpression.Operator ¢ : wizard.postfixOperators)
      addIfNecessary("POSTFIX", key(¢));
    for (final Operator ¢ : wizard.infixOperators)
      for (int arity = 2; arity <= maxArity; ++arity)
        addIfNecessary("INFIX", key(¢, arity));
  }

  boolean increment(final String category, final String key) {
    return increment(addIfNecessary(category, key), key);
  }

  void summarize() {
    if (writer == null)
      writer = new Table(this);
    try (Table t = new Table("rindices")) {
      for (final String category : usage.keySet()) {
        final Map<String, Integer> map = usage.get(category);
        int n = 0;
        int m = 0;
        for (final String key : map.keySet())
          t//
              .col("N", ++n)//
              .col("M", ++m)//
              .col("Category", category)//
              .col("Key", '"' + key + '"')//
              .col("Count", map.get(key)) //
              .nl();
        writer.col(category, rindex(ranks(map)));
        System.err.println("Your output is in: " + t.description());
        addLineToGlobalStatistcs();
      }
    }
  }

  private void addLineToGlobalStatistcs() {
    int N = 0;
    writer.col("$\\#$", ++N);
    writer.col("Project", presentSourceName);
    if (usage.get("METHOD") == null)
      return;
    final Map<String, Integer> adopted = new LinkedHashMap<>(usage.get("METHOD"));
    for (final String m : defined)
      adopted.remove(m);
    final Map<String, Integer> born = new LinkedHashMap<>(usage.get("METHOD"));
    for (final String k : new ArrayList<>(born.keySet()))
      if (!defined.contains(k))
        born.remove(k);
    int rindexBorn = rindex(ranks(born));
    int rindexAdopted = rindex(ranks(adopted));
    writer//
        .col("Adoption", rindexAdopted) //
        .col("Reuse", rindexBorn)//
        .col("$\\Delta$", rindexBorn - rindexAdopted)//
        .nl();
  }

  protected int methodRIndex() {
    return rindex(ranks(usage.get("METHOD")));
  }

  private String key(final InfixExpression ¢) {
    return key(¢, wizard.arity(¢));
  }

  private String key(final InfixExpression ¢, final int arity) {
    maxArity = Math.max(arity, maxArity);
    return key(¢.getOperator(), arity);
  }
}
