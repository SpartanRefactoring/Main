package il.org.spartan.spartanizer.cmdline;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public class Generic$Applicator {
  public Toolbox toolbox;
  public int tippersAppliedOnCurrentObject;
  protected int done;
  private static List<String> selectedTipperGroups;
  private static String fqn_base = "org.eclipse.jdt.core.dom.";
  protected static List<Class<? extends ASTNode>> selectedNodeTypes = setAllNodeTypes();

  @SuppressWarnings("unchecked") private static List<Class<? extends ASTNode>> setSelectedNodeTypes(String... ss) {
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
    selectedNodeTypes = setAllNodeTypes();
  }
  
  public Generic$Applicator(String[] clazzes) {
    if (clazzes == null)
      selectedNodeTypes = setAllNodeTypes();
    else {
      selectedNodeTypes = setSelectedNodeTypes(clazzes);
      System.out.println("selected: " + selectedNodeTypes.size());
    }
  }
  
  public Generic$Applicator(String[] clazzes, String[] tipperGroups) {
      this(clazzes);
      selectedTipperGroups = as.list(tipperGroups);
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
    for (Class<? extends ASTNode> i : setSelectedNodeTypes("MethodDeclaration", "VariableDeclarationFragment"))
      System.out.println(i);
    for (String i : setSelectedTipperGroups("Abbreviation", "Centification"))
      System.out.println(i);
  }
  
  private static List<String> setSelectedTipperGroups(String ... ss) {
    List<String> l = new ArrayList<>();
    for(String s: ss){
      l.add(s);
    }
    return l;
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
  
  <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    Tipper<N> t = toolbox.firstTipper(¢);
    TipperGroup g = t.tipperGroup();
//  Toolbox.get(g);
    if(selectedTipperGroups.contains(g.name())){
      System.out.println("selected tipper: " + g.name());
      return t;
    }
    return null;
  }
  
}
