package il.org.spartan.spartanizer.cmdline.applicators;

import static java.util.stream.Collectors.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.spartanizer.traversal.*;

/** Generic applicator
 * @author Matteo Orru'
 * @since 2016 */
public class GenericApplicator {
  public Toolbox toolbox;
  public int tippersAppliedOnCurrentObject;
  protected int done;
  private static List<String> selectedTipperGroups;
  protected static List<Class<? extends ASTNode>> selectedNodeTypes = setAllNodeTypes();

  @SuppressWarnings("unchecked") private static List<Class<? extends ASTNode>> setSelectedNodeTypes(final String... ss) {
    final Collection<Class<? extends ASTNode>> ret = an.empty.list();
    try {
      for (final String ¢ : ss) // NANO - but throws...
        ret.add((Class<? extends ASTNode>) Class.forName("org.eclipse.jdt.core.dom." + ¢));
    } catch (final ClassNotFoundException ¢) {
      note.bug(¢);
    }
    return as.list(ret); // useless?
  }
  public GenericApplicator() {
    selectedNodeTypes = setAllNodeTypes();
  }
  public GenericApplicator(final String... classes) {
    System.out.println("classes:" + Arrays.toString(classes));
    if (classes == null) {
      selectedNodeTypes = setAllNodeTypes();
      selectedNodeTypes.forEach(System.out::println);
    } else {
      selectedNodeTypes = setSelectedNodeTypes(classes);
      System.out.println("selected: " + selectedNodeTypes.size());
    }
  }
  public GenericApplicator(final String[] classes, final String... tipperGroups) {
    this(classes);
    selectedTipperGroups = tipperGroups == null ? allLabels() : as.list(tipperGroups);
  }
  protected static List<String> allLabels() {
    return Taxa.hierarchy.nodes().stream().map(λ -> λ.label).collect(toList());
  }
  private static List<Class<? extends ASTNode>> setAllNodeTypes() {
    return as.list(MethodDeclaration.class, InfixExpression.class, //
        VariableDeclarationFragment.class, //
        EnhancedForStatement.class, //
        Modifier.class, //
        VariableDeclarationExpression.class, //
        ThrowStatement.class, //
        CastExpression.class, //
        ClassInstanceCreation.class, //
        SuperConstructorInvocation.class, //
        SingleVariableDeclaration.class, //
        ForStatement.class, //
        WhileStatement.class, //
        Assignment.class, //
        Block.class, //
        PostfixExpression.class, //
        InfixExpression.class, //
        InstanceofExpression.class, //
        MethodDeclaration.class, //
        MethodInvocation.class, //
        IfStatement.class, //
        PrefixExpression.class, //
        ConditionalExpression.class, //
        TypeDeclaration.class, //
        EnumDeclaration.class, //
        FieldDeclaration.class, //
        CastExpression.class, //
        EnumConstantDeclaration.class, //
        NormalAnnotation.class, //
        Initializer.class, //
        VariableDeclarationFragment.class //
    );
  }
  public static void main(final String[] args) {
    setSelectedNodeTypes("MethodDeclaration", "VariableDeclarationFragment").forEach(System.out::println);
    setSelectedTipperGroups("Abbreviation", "Centification").forEach(System.out::println);
  }
  private static Iterable<String> setSelectedTipperGroups(final String... ¢) {
    final Collection<String> ret = an.empty.list();
    Collections.addAll(ret, ¢);
    return ret;
  }
  <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    final Tipper<N> ret = toolbox.firstTipper(¢);
    return selectedTipperGroups.contains(ret.tipperGroup().label()) ? ret : null;
  }
}