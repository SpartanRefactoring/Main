package il.org.spartan.spartanizer.cmdline.applicator;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.plugin.preferences.PreferencesResources.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Generic applicator
 * @author Matteo Orru'
 * @since 2016 */
public class Generic$Applicator {
  public Toolbox toolbox;
  public int tippersAppliedOnCurrentObject;
  protected int done;
  private static List<String> selectedTipperGroups;
  protected static List<Class<? extends ASTNode>> selectedNodeTypes = setAllNodeTypes();

  @SuppressWarnings("unchecked") private static List<Class<? extends ASTNode>> setSelectedNodeTypes(final String... ss) {
    final List<Class<? extends ASTNode>> $ = new ArrayList<>();
    try {
      for (final String ¢ : ss)
        $.add((Class<? extends ASTNode>) Class.forName("org.eclipse.jdt.core.dom." + ¢));
    } catch (final ClassNotFoundException ¢) {
      ¢.printStackTrace();
    }
    return as.list($); // useless?
  }

  public Generic$Applicator() {
    selectedNodeTypes = setAllNodeTypes();
  }

  public Generic$Applicator(final String[] classes) {
    System.out.println("classes:" + classes);
    if (classes != null) {
      selectedNodeTypes = setSelectedNodeTypes(classes);
      System.out.println("selected: " + selectedNodeTypes.size());
    } else {
      selectedNodeTypes = setAllNodeTypes();
      for (final Class<? extends ASTNode> ¢ : selectedNodeTypes)
        System.out.println(¢);
    }
  }

  public Generic$Applicator(final String[] classes, final String[] tipperGroups) {
    this(classes);
    selectedTipperGroups = tipperGroups == null ? setAllTipperGroups() : as.list(tipperGroups);
  }

  protected static List<String> setAllTipperGroups() {
    final List<String> $ = new ArrayList<>();
    for (final TipperGroup ¢ : TipperGroup.values())
      $.add(¢.name());
    return $;
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

  private static List<String> setSelectedTipperGroups(final String... ¢) {
    final List<String> $ = new ArrayList<>();
    Collections.addAll($, ¢);
    return $;
  }

  <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    final Tipper<N> $ = toolbox.firstTipper(¢);
    final TipperGroup g = $.tipperGroup();
    if (!selectedTipperGroups.contains(g.name()))
      return null;
    System.out.println("selected tipper: " + g.name());
    return $;
  }
}