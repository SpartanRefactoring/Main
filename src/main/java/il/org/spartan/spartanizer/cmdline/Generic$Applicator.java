package il.org.spartan.spartanizer.cmdline;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Modifier;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

public class Generic$Applicator {
  public Toolbox toolbox;
  public int tippersAppliedOnCurrentObject;
  protected int done;
  private static String fqn_base = "org.eclipse.jdt.core.dom.";
  protected static List<Class<? extends ASTNode>> selectedNodeTypes = setAll();

  @SuppressWarnings("unchecked") private static List<Class<? extends ASTNode>> setSelected(String... ss) {
    List<Class<? extends ASTNode>> $ = new ArrayList<>();
    try {
      for (String ¢ : ss)
        $.add((Class<? extends ASTNode>) Class.forName(fqn_base + ¢));
    } catch (ClassNotFoundException x) {
      x.printStackTrace();
    }
    return as.list($); // useless?
  }
  
  public Generic$Applicator(){
    selectedNodeTypes = setAll();
  }
  
  public Generic$Applicator(String[] clazzes) {
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
    for (Class<? extends ASTNode> i : setSelected("MethodDeclaration", "VariableDeclarationFragment"))
      System.out.println(i);
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes", "unused" }) private static List<Class<? extends ASTNode>> listOfClass() {
    final List l = new ArrayList<>();
    new Toolbox();
    final Toolbox tb = Toolbox.defaultInstance();
    for (int tipnum = tb.tippersCount(), i = 0; i <= tipnum; ++i) {
      final List<Tipper<? extends ASTNode>> b = tb.get(i);
      if (!b.isEmpty())
        for (final Tipper<?> ¢ : b) {
          final Class<? extends Tipper> class1 = ¢.getClass();
          final ParameterizedType genericSuperclass = (ParameterizedType) class1.getGenericSuperclass();
          final Type type = genericSuperclass.getActualTypeArguments()[0];
          if (!l.contains(type))
            l.add(type);
        }
    }
    final List<Class<? extends ASTNode>> $ = as.list(l);
    System.out.println($);
    return $;
  }
}
