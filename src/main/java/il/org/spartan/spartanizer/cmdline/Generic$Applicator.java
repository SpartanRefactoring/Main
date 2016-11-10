package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;

public class Generic$Applicator {
  protected static List<Class<? extends ASTNode>> selectedNodeTypes = as.list(MethodDeclaration.class, InfixExpression.class, //
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
  public Toolbox toolbox;
  public int tippersAppliedOnCurrentObject;
  protected int done;
}
