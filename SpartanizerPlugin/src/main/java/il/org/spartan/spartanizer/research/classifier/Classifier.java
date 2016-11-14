package il.org.spartan.spartanizer.research.classifier;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.util.*;

/** @author Ori Marcovitch
 * @since Nov 13, 2016 */
public class Classifier extends ASTVisitor {
  final Map<String, List<ASTNode>> forLoops = new HashMap<>();
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
    System.out.println("Well we've got " + forLoopsList.size() + " forLoop statements");
    System.out.println("lets classify them together");
    System.out.println("enter classification for each for loop, [p] to skip example");
    for (final ASTNode ¢ : forLoopsList) {
      final UserDefinedTipper<ASTNode> t = TipperFactory.tipper(format.code(generalize.code(¢ + "")), "OMG();", "");
      int count = 0;
      for (final ASTNode l : forLoopsList)
        if (t.canTip(l) && !l.equals(¢))
          ++count;
      if (count != 0) {
        System.out.println("WOW: pattern [" + format.code(generalize.code(¢ + "")) + "]\nmatched " + count + " times.");
        classify(¢);
      }
    }
  }

  /** @param ¢ to classify */
  private String classify(final ASTNode ¢) {
    System.out.println(format.code(generalize.code(¢ + "")));
    final String $ = input.nextLine();
    forLoops.putIfAbsent($, new ArrayList<>());
    forLoops.get($).add(¢);
    return $;
  }
}
