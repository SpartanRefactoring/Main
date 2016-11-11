package il.org.spartan.spartanizer.cmdline;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public class Generic$Applicator {
  
  public Toolbox toolbox;
  public int tippersAppliedOnCurrentObject;
  protected int done;
  
  protected static List<Class<? extends ASTNode>> selectedNodeTypes = as.list(MethodDeclaration.class, 
                InfixExpression.class, //
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
  
  @SuppressWarnings({ "unused" }) public static void main(final String[] args){
    List<Class<? extends ASTNode>> l = listOfClass();
  }

  @SuppressWarnings({ "unchecked", "rawtypes", "static-access", "unused" }) private static List<Class<? extends ASTNode>> listOfClass() {
    List l = new ArrayList<>();
    Toolbox tb = new Toolbox().defaultInstance();
    int tipnum = tb.tippersCount();
    for (int i = 0; i <= tipnum; ++i){
      List<Tipper<? extends ASTNode>> b = tb.get(i);
      if(b.size()>0){
        for(Tipper<?> ¢: b){
          Class<? extends Tipper> class1 = ¢.getClass();
          ParameterizedType genericSuperclass = (ParameterizedType) class1.getGenericSuperclass();
          Type type = genericSuperclass.getActualTypeArguments()[0];
          if(!l.contains(type))
            l.add(type);
        }
      }
    }
    
    List<Class<? extends ASTNode>> l2 = as.list(l);
    System.out.println(l2);
    return l2;
  }
  
  /** Printing definition of events that occur during spartanization.
   * @author Ori Roth
   * @since 2.6 */
  private enum message {
    run_start(1, inp -> "Spartanizing " + printableAt(inp, 0)), //
    run_pass(1, inp -> "Pass #" + printableAt(inp, 0)), //
    run_pass_finish(1, inp -> "Pass #" + printableAt(inp, 0) + " finished"), //
    visit_cu(3, inp -> printableAt(inp, 0) + "/" + printableAt(inp, 1) + "\tSpartanizing " + printableAt(inp, 2)), //
    run_finish(2, inp -> "Done spartanizing " + printableAt(inp, 0) + "\nTips accepted: " + printableAt(inp, 1));
    private final int inputCount;
    private final Function<Object[], String> printing;

    message(final int inputCount, final Function<Object[], String> printing) {
      this.inputCount = inputCount;
      this.printing = printing;
    }
    public String get(final Object... ¢) {
      assert ¢.length == inputCount;
      return printing.apply(¢);
    }
    private static String printableAt(final Object[] os, final int index) {
      return Linguistic.unknownIfNull(os, xs -> xs[index]);
    }
  }
}  
