package il.org.spartan.spartanizer.research.classifier;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since Nov 13, 2016 */
public class Classifier extends ASTVisitor {
  final Map<String, List<String>> forLoops = new HashMap<>();
  final List<ASTNode> forLoopsList = new ArrayList<>();
  int forLoopsAmount;
  static final Scanner input = new Scanner(System.in);
  static List<Tipper<EnhancedForStatement>> enhancedForKnownPatterns = new ArrayList<Tipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(new AnyMatches());
      add(new Contains());
      add(new ForEach());
      add(new FindFirst());
      add(new Reduce());
    }
  };
  static List<Tipper<ForStatement>> forKnownPatterns = new ArrayList<Tipper<ForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(new CopyArray());
      add(new ForEach2());
      add(new InitArray());
    }
  };
  private Map<String, Int> patterns;

  @Override public boolean visit(final ForStatement node) {
    if (!anyTips(node))
      forLoopsList.add(node);
    return super.visit(node);
  }

  @Override public boolean visit(final EnhancedForStatement node) {
    if (!anyTips(node))
      forLoopsList.add(node);
    return super.visit(node);
  }

  /** @param ¢ */
  public void analyze(final ASTNode ¢) {
    ¢.accept(this);
    forLoopsAmount = forLoopsList.size();
    patterns = filterAllIntrestingPatterns();
    displayInteractive();
    classifyPatterns();
    summarize();
  }

  /**
   * 
   */
  private void summarize() {
    for (String k : forLoops.keySet()) {
      System.out.println("****" + k + "****");
      for (String p : forLoops.get(k))
        System.out.println(tipperize(p, k));
    }
  }

  private void classifyPatterns() {
    for (final String k : patterns.keySet()) {
      System.out.println(k);
      System.out.println("[Matched " + patterns.get(k).inner + " times]");
      if (!classify(k))
        break;
    }
  }

  private void displayInteractive() {
    System.out.println("Well we've got " + forLoopsAmount + " forLoop statements");
    System.out.println("From them " + patterns.size() + " are repetitive");
    System.out.println("Lets classify them together!");
  }

  private Map<String, Int> filterAllIntrestingPatterns() {
    final Map<String, Int> $ = new HashMap<>();
    for (boolean again = true; again;) {
      again = false;
      List<ASTNode> toRemove = new ArrayList<>();
      for (final ASTNode ¢ : forLoopsList) {
        final UserDefinedTipper<ASTNode> t = TipperFactory.patternTipper(format.code(generalize.code(¢ + "")), "FOR();", "");
        toRemove = new ArrayList<>();
        for (final ASTNode l : forLoopsList)
          if (t.canTip(l))
            toRemove.add(l);
        if (toRemove.size() > 1) {
          $.putIfAbsent(¢ + "", Int.valueOf(toRemove.size()));
          forLoopsList.removeAll(toRemove);
          again = true;
          break;
        }
        forLoopsList.remove(¢);
        again = true;
        break;
      }
    }
    return $;
  }

  private static boolean anyTips(final EnhancedForStatement ¢) {
    for (Tipper<EnhancedForStatement> p : enhancedForKnownPatterns)
      if (p.canTip(¢))
        return true;
    return false;
  }

  private static boolean anyTips(final ForStatement ¢) {
    for (Tipper<ForStatement> p : forKnownPatterns)
      if (p.canTip(¢))
        return true;
    return false;
  }

  /** @param ¢ to classify */
  private boolean classify(final String ¢) {
    String code = format.code(generalize.code(¢));
    System.out.println(code);
    final String classification = input.nextLine();
    if ("q".equals(classification) || "Q".equals(classification))
      return false;
    System.out.println(tipperize(code, classification));
    forLoops.putIfAbsent(classification, new ArrayList<>());
    forLoops.get(classification).add(¢);
    return true;
  }

  /** @param code
   * @return */
  private static String tipperize(String code, String classification) {
    return "TipperFactory.patternTipper(\"" + format.code(generalize.code(code)).replace("\n", "").replace("\r", "") + "\", \"" + classification
        + "();\", \"" + classification + "\")";
  }
}
