package il.org.spartan.spartanizer.cmdline.tables;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.tables.*;
import il.org.spartan.utils.*;

/** Collects various reusability indices for a given folder(s)
 * @author Yossi Gil
 * @since 2016 */
public class Table_ReusabilityIndices {
  static Table writer;
  private static int maxArity;
  static final Map<String, Map<String, Integer>> usage = new LinkedHashMap<>();
  static final Collection<String> defined = new LinkedHashSet<>();

  public static void main(final String[] args) {
    new GrandVisitor(args) {
      {
        listen(new Tapper() {
          @Override public void endLocation() {
            done(CurrentData.location);
          }
        });
      }

      protected void done(final String path) {
        addMissingKeys();
        initializeWriter();
        writer.col("Project", CurrentData.relativePath);
        try (Table t = new Table("rindices")) {
          for (final String category : usage.keySet()) {
            final Map<String, Integer> map = usage.get(category);
            final Int n = new Int(), m = new Int();
            map.keySet()
                .forEach(λ -> t//
                    .col("N", ++n.inner)//
                    .col("M", ++m.inner)//
                    .col("Category", category)//
                    .col("Key", '"' + λ + '"')//
                    .col("Count", map.get(λ))//
                    .nl());
            writer.col(category, rindex(ranks(map)));
          }
          addLineToGlobalStatistcs(path);
          writer.nl();
        }
        RIndicesVisitor.clear();
      }
      void initializeWriter() {
        if (writer == null)
          writer = new Table(Table.classToNormalizedFileName(Table_ReusabilityIndices.class) + "-" + corpus, outputFolder);
      }
    }.visitAll(new RIndicesVisitor());
    writer.close();
  }

  public static class RIndicesVisitor extends ASTVisitor {
    public RIndicesVisitor() {
      super(true);
    }
    @Override public void preVisit(final ASTNode ¢) {
      increment("NODE-TYPE", Vocabulary.mangle(¢.getClass()));
    }
    @Override public boolean visit(final Assignment ¢) {
      return increment("ASSIGNMENT", Vocabulary.mangle(¢));
    }
    @Override public boolean visit(final InfixExpression ¢) {
      return increment("INFIX", key(¢));
    }
    @Override public boolean visit(final MethodDeclaration ¢) {
      return defined.add(Vocabulary.mangle(¢));
    }
    @Override public boolean visit(final MethodInvocation ¢) {
      return increment("METHOD", Vocabulary.mangle(¢));
    }
    @Override public boolean visit(final PostfixExpression ¢) {
      return increment("POSTFIX", Vocabulary.mangle(¢));
    }
    @Override public boolean visit(final PrefixExpression ¢) {
      return increment("PREFIX", Vocabulary.mangle(¢));
    }
    static void clear() {
      usage.clear();
      defined.clear();
    }
  }

  public static boolean increment(final Map<String, Integer> category, final String key) {
    category.put(key, Integer.valueOf(category.get(key).intValue() + 1));
    return true;
  }
  static int[] ranks(final Map<?, Integer> m) {
    final Int n = new Int();
    final int[] $ = new int[m.size()];
    m.values().forEach(λ -> $[n.inner++] = λ.intValue());
    return $;
  }
  public static int rindex(final int... ranks) {
    Arrays.sort(ranks);
    int $ = 0;
    for (int ¢ = 0; ¢ < ranks.length; ++¢)
      $ = Math.max($, Math.min(ranks[¢], ranks.length - ¢));
    return $;
  }
  public static Map<String, Integer> addIfNecessary(final String category, final String key) {
    usage.putIfAbsent(category, new LinkedHashMap<>());
    final Map<String, Integer> $ = usage.get(category);
    assert $ != null;
    $.putIfAbsent(key, Integer.valueOf(0));
    return $;
  }
  static void addLineToGlobalStatistcs(final String path) {
    writer.col("Project", getProjectName(path));
    if (usage.get("METHOD") == null)
      return;
    final int rExternal = rExternal(), rIntrernal = rInternal();
    writer//
        .col("External", rExternal) //
        .col("Internal", rIntrernal)//
        .col("Internal-External", rIntrernal - rExternal)//
    ;
  }
  private static String getProjectName(final String ¢) {
    return ¢.substring(¢.lastIndexOf('-') + 1);
  }
  static void addMissingKeys() {
    wizard.classToNodeType.keySet().forEach(λ -> addIfNecessary("NODE-TYPE", Vocabulary.mangle(λ)));
    as.list(op.assignment).forEach(λ -> addIfNecessary("ASSIGNMENT", Vocabulary.mangle(λ)));
    as.list(op.prefix).forEach(λ -> addIfNecessary("PREFIX", Vocabulary.mangle(λ)));
    as.list(op.postfix).forEach(λ -> addIfNecessary("POSTFIX", Vocabulary.mangle(λ)));
    for (final Operator ¢ : op.infixOperators)
      for (int arity = 2; arity <= maxArity; ++arity)
        addIfNecessary("INFIX", Vocabulary.mangle(¢, arity));
  }
  static boolean increment(final String category, final String key) {
    return increment(addIfNecessary(category, key), key);
  }
  static String key(final InfixExpression ¢) {
    return key(¢, extract.arity(¢));
  }
  private static String key(final InfixExpression ¢, final int arity) {
    maxArity = Math.max(arity, maxArity);
    return Vocabulary.mangle(¢.getOperator(), arity);
  }
  protected static int rExternal() {
    final Map<String, Integer> $ = new LinkedHashMap<>(usage.get("METHOD"));
    defined.forEach($::remove);
    return rindex(ranks($));
  }
  protected static int rInternal() {
    final Map<String, Integer> $ = new LinkedHashMap<>(usage.get("METHOD"));
    $.keySet().stream().filter(λ -> !defined.contains(λ)).forEach($::remove);
    return rindex(ranks($));
  }
  protected static int rMethod() {
    return rindex(ranks(usage.get("METHOD")));
  }
}
