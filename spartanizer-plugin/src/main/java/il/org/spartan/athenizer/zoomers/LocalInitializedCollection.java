package il.org.spartan.athenizer.zoomers;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tippers.LocalInitialized;
import il.org.spartan.utils.Examples;

/** replaces {@code List<Integer> x = new ArrayList<>(ys)} with
 * {@code List<Integer> x = new ArrayList<>(); x.addAll(ys)}
 * @author Niv Shalmon
 * @since 2017-05-30 */
public class LocalInitializedCollection extends LocalInitialized {
  private static final long serialVersionUID = 0x92A37F3E7049082L;
  ClassInstanceCreation newExpression;
  private Type type;
  Expression argument;

  public LocalInitializedCollection() {
    needs("Initializer is a 'new Type(...)' expression", //
        () -> newExpression = az.classInstanceCreation(initializer));
    andAlso("Initializer has a single operand", //
        () -> step.arguments(newExpression).size() == 1);
    needs("Initializer argument", //
        () -> argument = step.arguments(newExpression).get(0));
    andAlso("Arguemnt is a collection", //
        () -> isCollection(argument.resolveTypeBinding()));
    needs("The initialized type", //
        () -> type = newExpression.getType());
    andAlso("type has an empty constructor", //
        () -> hasEmptyConstructor(type.resolveBinding()));
    andAlso("type has an addAll method that accepts a collection", //
        () -> hasAddAll(type.resolveBinding()));
  }
  private static boolean isCollection(final ITypeBinding tb) {
    return tb != null && ("java.util.Collection".equals(tb.getQualifiedName().split("<")[0])
        || as.list(tb.getInterfaces()).stream().filter(LocalInitializedCollection::isCollection).findAny().isPresent());
  }
  private static boolean hasEmptyConstructor(final ITypeBinding tb) {
    return tb != null && as.list(tb.getDeclaredMethods()).stream()//
        .filter(IMethodBinding::isConstructor)//
        .filter(λ -> λ.getParameterTypes().length == 0)//
        .findAny()//
        .isPresent();
  }
  private static boolean hasAddAll(final ITypeBinding tb) {
    return tb != null && (as.list(tb.getDeclaredMethods()).stream().filter(λ -> "addAll".equals(λ.getName()))
        .filter(λ -> λ.getParameterTypes().length == 1).filter(λ -> isCollection(λ.getParameterTypes()[0])).findAny().isPresent()
        || as.list(tb.getSuperclass()).stream().filter(LocalInitializedCollection::hasAddAll).findAny().isPresent()
        || as.list(tb.getInterfaces()).stream().filter(LocalInitializedCollection::hasAddAll).findAny().isPresent());
  }
  @Override public Examples examples() {
    // bloater requires binding which causes test to fail
    // return convert("List<Integer> x = new List<>(ys);").to("List<Integer>x =
    // new List<>(); x.addAll(ys);");
    return null;
  }
  @Override protected ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    if (!iz.block(declaration.getParent()))
      return $;
    final ClassInstanceCreation e = copy.of(newExpression);
    e.arguments().remove(0);
    final AST ast = current.getAST();
    final MethodInvocation mi = ast.newMethodInvocation();
    mi.setName(ast.newSimpleName("addAll"));
    step.arguments(mi).add(copy.of(argument));
    mi.setExpression(copy.of(name));
    $.replace(newExpression, e, g);
    $.getListRewrite(declaration.getParent(), Block.STATEMENTS_PROPERTY).insertAfter(ast.newExpressionStatement(mi), declaration, g);
    return $;
  }
}
