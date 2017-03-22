package il.org.spartan.spartanizer.cmdline.applicator;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.plugin.preferences.revision.PreferencesResources.*;
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

  @SuppressWarnings("unchecked") private static List<Class<? extends ASTNode>> setSelectedNodeTypes(@NotNull final String... ss) {
    final Collection<Class<? extends ASTNode>> $ = new ArrayList<>();
    try {
      for (final String ¢ : ss) // NANO - but throws...
        $.add((Class<? extends ASTNode>) Class.forName("org.eclipse.jdt.core.dom." + ¢));
    } catch (@NotNull final ClassNotFoundException ¢) {
      ¢.printStackTrace();
    }
    return as.list($); // useless?
  }

  public Generic$Applicator() {
    selectedNodeTypes = setAllNodeTypes();
  }

  public Generic$Applicator(@Nullable final String... classes) {
    System.out.println("classes:" + Arrays.toString(classes));
    if (classes == null) {
      selectedNodeTypes = setAllNodeTypes();
      selectedNodeTypes.forEach(System.out::println);
    } else {
      selectedNodeTypes = setSelectedNodeTypes(classes);
      System.out.println("selected: " + selectedNodeTypes.size());
    }
  }

  public Generic$Applicator(final String[] classes, @Nullable final String... tipperGroups) {
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

  @NotNull private static Iterable<String> setSelectedTipperGroups(final String... ¢) {
    final Collection<String> $ = new ArrayList<>();
    Collections.addAll($, ¢);
    return $;
  }

  @Nullable <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    final Tipper<N> $ = toolbox.firstTipper(¢);
    final TipperGroup g = $.tipperGroup();
    if (!selectedTipperGroups.contains(g.name()))
      return null;
    System.out.println("selected tipper: " + g.name());
    return $;
  }
}