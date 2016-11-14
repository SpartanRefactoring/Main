package il.org.spartan.spartanizer.research.classifier;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Ori Marcovitch
 * @since Nov 13, 2016 */
public class Classifier extends ASTVisitor {
  final Map<String, List<String>> forLoops = new HashMap<>();
  final List<ASTNode> forLoopsList = new ArrayList<>();
  static final Scanner input = new Scanner(System.in);

  @Override public boolean visit(final ForStatement node) {
    forLoopsList.add(node);
    return super.visit(node);
  }

  @Override public boolean visit(final EnhancedForStatement node) {
    forLoopsList.add(node);
    return super.visit(node);
  }

  public void analyze(final ASTNode n) {
    n.accept(this);
    Map<String, Int> awesomePatterns = new HashMap<>();
    for (boolean again = true; again;) {
      again = false;
      List<ASTNode> toRemove = new ArrayList<>();
      for (ASTNode ¢ : forLoopsList) {
        UserDefinedTipper<ASTNode> t = TipperFactory.patternTipper(format.code(generalize.code(¢ + "")), "OMG();", "");
        toRemove = new ArrayList<>();
        for (ASTNode l : forLoopsList)
          if (t.canTip(l))
            toRemove.add(l);
        if (toRemove.size() > 1) {
          awesomePatterns.putIfAbsent(format.code(generalize.code(¢ + "")), Int.valueOf(toRemove.size()));
          forLoopsList.removeAll(toRemove);
          again = true;
          break;
        }
      }
    }
    System.out.println("Well we've got " + forLoopsList.size() + " forLoop statements");
    System.out.println("From them " + awesomePatterns.size() + " are repetitive");
    System.out.println("Lets classify them together!");
    for (String k : awesomePatterns.keySet()) {
      System.out.println(k);
      System.out.println("[Matched " + awesomePatterns.get(k).inner + " times]");
      classify(k);
    }
  }

  /** @param ¢ to classify */
  private String classify(String ¢) {
    System.out.println(format.code(generalize.code(¢)));
    final String $ = input.nextLine();
    forLoops.putIfAbsent($, new ArrayList<>());
    forLoops.get($).add(¢);
    return $;
  }
}
