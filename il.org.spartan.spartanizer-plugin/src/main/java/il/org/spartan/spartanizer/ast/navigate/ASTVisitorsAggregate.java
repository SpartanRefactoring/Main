package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;
import org.eclipse.jdt.core.dom.*;

/** Makes it possible to use multiple AST traversor in the same traversal.
 * @author Yossi Gil
 * @since 2017-08-24 [[SuppressWarningsSpartan]] */
public final class ASTVisitorsAggregate extends ASTVisitor {
  public final List<ASTVisitor> inner = new ArrayList<>();

  //@formatter:off
@Override  public  void  endVisit(AnnotationTypeDeclaration        n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(AnnotationTypeMemberDeclaration  n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(AnonymousClassDeclaration        n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ArrayAccess                      n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ArrayCreation                    n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ArrayInitializer                 n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ArrayType                        n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(AssertStatement                  n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(Assignment                       n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(Block                            n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(BlockComment                     n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(BooleanLiteral                   n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(BreakStatement                   n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(CastExpression                   n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(CatchClause                      n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(CharacterLiteral                 n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ClassInstanceCreation            n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(CompilationUnit                  n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ConditionalExpression            n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ConstructorInvocation            n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ContinueStatement                n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(CreationReference                n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(Dimension                        n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(DoStatement                      n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(EmptyStatement                   n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(EnhancedForStatement             n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(EnumConstantDeclaration          n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(EnumDeclaration                  n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ExpressionMethodReference        n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ExpressionStatement              n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(FieldAccess                      n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(FieldDeclaration                 n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ForStatement                     n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(IfStatement                      n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ImportDeclaration                n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(InfixExpression                  n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(Initializer                      n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(InstanceofExpression             n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(IntersectionType                 n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(Javadoc                          n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(LabeledStatement                 n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(LambdaExpression                 n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(LineComment                      n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(MarkerAnnotation                 n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(MemberRef                        n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(MemberValuePair                  n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(MethodDeclaration                n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(MethodInvocation                 n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(MethodRef                        n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(MethodRefParameter               n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(Modifier                         n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(NameQualifiedType                n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(NormalAnnotation                 n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(NullLiteral                      n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(NumberLiteral                    n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(PackageDeclaration               n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ParameterizedType                n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ParenthesizedExpression          n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(PostfixExpression                n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(PrefixExpression                 n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(PrimitiveType                    n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(QualifiedName                    n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(QualifiedType                    n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ReturnStatement                  n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(SimpleName                       n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(SimpleType                       n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(SingleMemberAnnotation           n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(SingleVariableDeclaration        n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(StringLiteral                    n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(SuperConstructorInvocation       n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(SuperFieldAccess                 n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(SuperMethodInvocation            n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(SuperMethodReference             n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(SwitchCase                       n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(SwitchStatement                  n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(SynchronizedStatement            n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(TagElement                       n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(TextElement                      n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ThisExpression                   n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(ThrowStatement                   n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(TryStatement                     n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(TypeDeclaration                  n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(TypeDeclarationStatement         n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(TypeLiteral                      n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(TypeMethodReference              n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(TypeParameter                    n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(UnionType                        n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(VariableDeclarationExpression    n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(VariableDeclarationFragment      n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(VariableDeclarationStatement     n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(WhileStatement                   n)  {  inner.forEach(λ->λ.visit(n));  }
@Override  public  void  endVisit(WildcardType                     n)  {  inner.forEach(λ->λ.visit(n));  }
//@formatter:on
  public boolean live() {
    return !inner.isEmpty();
  }
  @Override public void postVisit(ASTNode n) {
    inner.forEach(x -> x.postVisit(n));
  }
// @formatter:off
@Override  public  void  preVisit(ASTNode                          n)  {  inner.forEach(λ->λ.preVisit(n));  }
@Override  public  boolean  preVisit2(ASTNode                      n)  {  inner.forEach(λ->λ.preVisit2(n));  return  live();  }
// @formatter:on
// @formatter:off
@Override  public  boolean  visit(AnnotationTypeDeclaration        n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(AnnotationTypeMemberDeclaration  n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(AnonymousClassDeclaration        n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ArrayAccess                      n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ArrayCreation                    n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ArrayInitializer                 n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ArrayType                        n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(AssertStatement                  n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(Assignment                       n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(Block                            n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(BlockComment                     n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(BooleanLiteral                   n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(BreakStatement                   n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(CastExpression                   n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(CatchClause                      n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(CharacterLiteral                 n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ClassInstanceCreation            n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(CompilationUnit                  n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ConditionalExpression            n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ConstructorInvocation            n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ContinueStatement                n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(CreationReference                n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(Dimension                        n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(DoStatement                      n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(EmptyStatement                   n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(EnhancedForStatement             n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(EnumConstantDeclaration          n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(EnumDeclaration                  n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ExpressionMethodReference        n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ExpressionStatement              n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(FieldAccess                      n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(FieldDeclaration                 n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ForStatement                     n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(IfStatement                      n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ImportDeclaration                n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(InfixExpression                  n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(Initializer                      n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(InstanceofExpression             n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(IntersectionType                 n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(Javadoc                          n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(LabeledStatement                 n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(LambdaExpression                 n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(LineComment                      n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(MarkerAnnotation                 n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(MemberRef                        n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(MemberValuePair                  n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(MethodDeclaration                n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(MethodInvocation                 n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(MethodRef                        n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(MethodRefParameter               n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(Modifier                         n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(NameQualifiedType                n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(NormalAnnotation                 n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(NullLiteral                      n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(NumberLiteral                    n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(PackageDeclaration               n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ParameterizedType                n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ParenthesizedExpression          n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(PostfixExpression                n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(PrefixExpression                 n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(PrimitiveType                    n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(QualifiedName                    n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(QualifiedType                    n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ReturnStatement                  n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(SimpleName                       n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(SimpleType                       n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(SingleMemberAnnotation           n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(SingleVariableDeclaration        n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(StringLiteral                    n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(SuperConstructorInvocation       n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(SuperFieldAccess                 n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(SuperMethodInvocation            n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(SuperMethodReference             n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(SwitchCase                       n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(SwitchStatement                  n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(SynchronizedStatement            n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(TagElement                       n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(TextElement                      n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ThisExpression                   n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(ThrowStatement                   n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(TryStatement                     n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(TypeDeclaration                  n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(TypeDeclarationStatement         n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(TypeLiteral                      n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(TypeMethodReference              n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(TypeParameter                    n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(UnionType                        n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(VariableDeclarationExpression    n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(VariableDeclarationFragment      n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(VariableDeclarationStatement     n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(WhileStatement                   n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
@Override  public  boolean  visit(WildcardType                     n)  {  inner.forEach(λ->λ.visit(n));      return  live();  }
//@formatter:on
  public void push(ASTVisitor visitor) {
    inner.add(visitor);
  }
}
