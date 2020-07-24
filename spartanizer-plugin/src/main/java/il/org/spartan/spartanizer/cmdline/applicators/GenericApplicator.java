package il.org.spartan.spartanizer.cmdline.applicators;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import fluent.ly.as;
import fluent.ly.note;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.spartanizer.tipping.categories.Taxa;
import il.org.spartan.spartanizer.traversal.Toolbox;

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
    final Collection<Class<? extends ASTNode>> $ = an.empty.list();
    try {
      for (final String ¢ : ss) // NANO - but throws...
        $.add((Class<? extends ASTNode>) Class.forName("org.eclipse.jdt.core.dom." + ¢));
    } catch (final ClassNotFoundException ¢) {
      note.bug(¢);
    }
    return as.list($); // useless?
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
    final Collection<String> $ = an.empty.list();
    Collections.addAll($, ¢);
    return $;
  }
  <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    final Tipper<N> $ = toolbox.firstTipper(¢);
    return selectedTipperGroups.contains($.tipperGroup().label()) ? $ : null;
  }
}