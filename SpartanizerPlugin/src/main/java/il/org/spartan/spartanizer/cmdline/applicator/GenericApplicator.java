package il.org.spartan.spartanizer.cmdline.applicator;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.plugin.preferences.revision.PreferencesResources.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;

/** Generic applicator
 * @author Matteo Orru'
 * @since 2016 */
public class GenericApplicator {
  public Configuration configuration;
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
    selectedTipperGroups = tipperGroups == null ? setAllTipperGroups() : as.list(tipperGroups);
  }
  protected static List<String> setAllTipperGroups() {
    return Stream.of(TipperGroup.values()).map(Enum::name).collect(toList());
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
    final Tipper<N> $ = configuration.firstTipper(¢);
    final TipperGroup g = $.tipperGroup();
    if (!selectedTipperGroups.contains(g.name()))
      return null;
    System.out.println("selected tipper: " + g.name());
    return $;
  }
}