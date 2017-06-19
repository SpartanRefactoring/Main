package il.org.spartan.java.cfg;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-06-15 */
public class VolatileCFG extends CFG<VolatileCFG> {
  private static final int PRINT_THRESHOLD = 20;
  public final Map<ASTNode, List<ASTNode>> ins = new HashMap<>();
  public final Map<ASTNode, List<ASTNode>> outs = new HashMap<>();
  public final Collection<ASTNode> roots = new LinkedList<>();

  @Override public List<ASTNode> in(final ASTNode ¢) {
    if (!ins.containsKey(¢))
      ins.put(¢, new LinkedList<>());
    return ins.get(¢);
  }
  @Override public List<ASTNode> out(final ASTNode ¢) {
    if (!outs.containsKey(¢))
      outs.put(¢, new LinkedList<>());
    return outs.get(¢);
  }
  @Override public void acknowledgeRoot(final ASTNode ¢) {
    roots.add(¢);
  }
  @Override public String toString() {
    final StringBuilder $ = new StringBuilder();
    final Comparator<ASTNode> c = (o1, o2) -> o1.getStartPosition() - o2.getStartPosition();
    final SortedMap<ASTNode, List<ASTNode>> sins = new TreeMap<>(c), souts = new TreeMap<>(c);
    sins.putAll(ins);
    souts.putAll(outs);
    $.append("* Roots *\n");
    $.append(shorten(roots) + "\n");
    $.append("* In *\n");
    for (final ASTNode n : sins.keySet())
      $.append(shorten(n) + " <-\n\t" + shorten(sins.get(n)) + "\n");
    $.append("* Out *\n");
    for (final ASTNode n : souts.keySet())
      $.append(shorten(n) + " ->\n\t" + shorten(souts.get(n)) + "\n");
    return $ + "";
  }
  private static String shorten(final ASTNode ¢) {
    return ¢ == null ? String.valueOf(null) : English.trimAbsolute((¢ + "").replaceAll("\\s+", " "), PRINT_THRESHOLD, "...");
  }
  private static String shorten(final Collection<ASTNode> list) {
    return list == null ? String.valueOf(null) : list.stream().map(λ -> shorten(λ)).collect(Collectors.toList()) + "";
  }
}
