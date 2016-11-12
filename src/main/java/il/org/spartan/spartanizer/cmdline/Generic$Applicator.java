package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;

public class Generic$Applicator {
  public Toolbox toolbox;
  public int tippersAppliedOnCurrentObject;
  protected int done;
  private static String fqn_base = "org.eclipse.jdt.core.dom.";
  protected static List<Class<? extends ASTNode>> selectedNodeTypes = setAll();

  @SuppressWarnings("unchecked") private static List<Class<? extends ASTNode>> setSelected(final String... ss) {
    final List<Class<? extends ASTNode>> $ = new ArrayList<>();
    try {
      for (final String ¢ : ss)
        $.add((Class<? extends ASTNode>) Class.forName(fqn_base + ¢));
    } catch (final ClassNotFoundException x) {
      x.printStackTrace();
    }
    return as.list($); // useless?
  }
  public Generic$Applicator() {
    selectedNodeTypes = setAll();
  }
  public Generic$Applicator(final String[] clazzes) {
    if (clazzes == null)
      selectedNodeTypes = setAll();
    else {
      selectedNodeTypes = setSelected(clazzes);
      System.out.println("selected: " + selectedNodeTypes.size());
    }
  }
  private static List<Class<? extends ASTNode>> setAll() {
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
    for (final Class<? extends ASTNode> i : setSelected("MethodDeclaration", "VariableDeclarationFragment"))
      System.out.println(i);
  }
}
