package il.org.spartan.spartanizer.cmdline.collector;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;

/** Collects the language diversity index for a given folder(s)
 * @author Yossi Gil
 * @since 2016 */
public class ComputeLanguageIndex extends FolderASTVisitor {
  static {
    clazz = ComputeLanguageIndex.class;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    FolderASTVisitor.main(args);
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

  private final Map<String, String> keyToCategory = new HashMap<>();
  private int maxArity;
  private final Map<String, Integer> usage = new LinkedHashMap<>();
  private CSVLineWriter writer;

  public void addIfNecessary(final String key, final String category) {
    keyToCategory.put(key, category);
    addIfNecessary(key);
  }

  public void addIfNecessary(final String key) {
    usage.putIfAbsent(key, Integer.valueOf(0));
  }

  @Override public void preVisit(final ASTNode ¢) {
    increment(key(¢.getClass()));
  }

  @Override public boolean visit(final Assignment ¢) {
    return increment(key(¢));
  }

  @Override public boolean visit(final InfixExpression ¢) {
    return increment(key(¢));
  }

  @Override public boolean visit(final PostfixExpression ¢) {
    increment(¢.getOperator() + "(post)");
    return true;
  }

  @Override public boolean visit(final PrefixExpression ¢) {
    increment(key(¢));
    return true;
  }

  @Override protected void init() {
    writer = new CSVLineWriter(makeFile("node-types"));
  }

  @Override protected void done() {
    dotter.end();
    for (final Class<? extends ASTNode> ¢ : wizard.classToNodeType.keySet())
      addIfNecessary(key(¢), "TYPE");
    for (final Assignment.Operator ¢ : wizard.assignmentOperators)
      addIfNecessary(key(¢), "ASSIGN");
    for (final PrefixExpression.Operator ¢ : wizard.prefixOperators)
      addIfNecessary(key(¢), "PRE");
    for (final Operator ¢ : wizard.infixOperators)
      for (int arity = 2; arity <= maxArity; ++arity)
        addIfNecessary(key(¢, arity), "INFIX");
    int n = 0;
    for (final String key : usage.keySet()) {
      writer//
          .put("N", ++n)//
          .put("Key", '"' + key + '"')//
          .put("Count", usage.get(key)) //
          .put("Category", keyToCategory.get(key));
      writer.nl();
    }
    System.err.println("Your output is in: " + writer.close());
    writer = null;
  }

  boolean increment(final String key) {
    addIfNecessary(key);
    usage.put(key, Integer.valueOf(usage.get(key).intValue() + 1));
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
