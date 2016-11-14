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
  int forLoopsAmount;
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
    forLoopsAmount = forLoopsList.size();
    Map<String, Int> patterns = new HashMap<>();
    for (boolean again = true; again;) {
      again = false;
      List<ASTNode> toRemove = new ArrayList<>();
      for (ASTNode ¢ : forLoopsList) {
        UserDefinedTipper<ASTNode> t = TipperFactory.tipper(format.code(generalize.code(¢ + "")), "FOR();", "");
        toRemove = new ArrayList<>();
        for (ASTNode l : forLoopsList)
          if (t.canTip(l))
            toRemove.add(l);
        if (toRemove.size() > 1) {
          patterns.putIfAbsent(format.code(generalize.code(¢ + "")), Int.valueOf(toRemove.size()));
          forLoopsList.removeAll(toRemove);
          again = true;
          break;
        }
      }
    }
    System.out.println("Well we've got " + forLoopsAmount + " forLoop statements");
    System.out.println("From them " + patterns.size() + " are repetitive which cover a total of " + forLoopsList.size() + " forLoops");
    System.out.println("Lets classify them together!");
    for (String k : patterns.keySet()) {
      System.out.println(k);
      System.out.println("[Matched " + patterns.get(k).inner + " times]");
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
