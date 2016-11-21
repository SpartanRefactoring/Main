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
  static List<Tipper<EnhancedForStatement>> knownPatterns = new ArrayList<Tipper<EnhancedForStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(new ApplyToEach());
      add(new FindFirst());
    }
  };
  private Map<String, Int> patterns;

  @Override public boolean visit(final ForStatement node) {
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
    System.out.println("hmmmmmmmmmmm");
    forLoopsAmount = forLoopsList.size();
    patterns = filterAllIntrestingPatterns();
    System.out.println("hmmmmmmmmmmm");
    displayInteractive();
    classifyPatterns();
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
    System.out.println("From them " + patterns.size() + " are repetitive which cover a total of " + forLoopsList.size() + " forLoops");
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
          $.putIfAbsent(format.code(generalize.code(¢ + "")), Int.valueOf(toRemove.size()));
          forLoopsList.removeAll(toRemove);
          System.out.println("again");
          again = true;
          break;
        }
      }
    }
    return $;
  }

  private static boolean anyTips(final EnhancedForStatement ¢) {
    for (Tipper<EnhancedForStatement> p : knownPatterns)
      if (p.canTip(¢))
        return true;
    return false;
  }

  /** @param ¢ to classify */
  private boolean classify(final String ¢) {
    String code = format.code(generalize.code(¢));
    System.out.println(code);
    final String classification = input.nextLine();
    if ("".equals(classification))
      return false;
    System.out.println(tiperize(code, classification));
    forLoops.putIfAbsent(classification, new ArrayList<>());
    forLoops.get(classification).add(¢);
    return true;
  }

  /** @param code
   * @return */
  private static String tiperize(String code, String classification) {
    return "TipperFactory.newTipper(\"" + code.replace("\n", "").replace("\r", "") + "\", \"" + classification + "();\", \"" + classification + "\")";
  }
}
