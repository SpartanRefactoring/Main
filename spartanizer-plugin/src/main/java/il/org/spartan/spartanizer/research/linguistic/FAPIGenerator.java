package il.org.spartan.spartanizer.research.linguistic;

import static il.org.spartan.spartanizer.research.linguistic.FAPI.BINDING_PROPERTY;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotatableType;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.ltk.core.refactoring.TextFileChange;

import fluent.ly.as;
import fluent.ly.lisp;
import fluent.ly.note;
import fluent.ly.separate;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.ast.safety.property;
import il.org.spartan.spartanizer.plugin.Eclipse;
import il.org.spartan.utils.UnderConstruction;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-05-11 */
@UnderConstruction
public class FAPIGenerator {
  public static final String NAME = "Fulent-API generator";
  final FAPI fapi;
  private IPath path;
  private IProject project;
  private IProgressMonitor monitor;
  private IFile classFile;
  private AST ast;
  private CompilationUnit cu;
  private ASTRewrite r;
  private ImportRewrite ir;
  private TypeDeclaration baseType;
  private TypeDeclaration lastKnownType;

  protected FAPIGenerator(final FAPI fapi) {
    this.fapi = Objects.requireNonNull(fapi);
  }
  public static FAPIGenerator by(final FAPI i) {
    return new FAPIGenerator(i);
  }
  public FAPIGenerator in(final IJavaProject p) {
    if (p == null)
      return this;
    try {
      for (final IPackageFragmentRoot fr : p.getAllPackageFragmentRoots())
        if (fr.getKind() == IPackageFragmentRoot.K_SOURCE) {
          path = fr.getPath().removeFirstSegments(1);
          project = p.getProject();
          return this;
        }
    } catch (final JavaModelException x) {
      note.bug(x);
    }
    return this;
  }
  public FAPIGenerator with(final IProgressMonitor m) {
    monitor = m;
    return this;
  }
  public boolean generateAll() {
    return generateFile() && generateCode() && generateImports() && commit();
  }
  private boolean generateFile() {
    if (path == null || project == null)
      return false;
    final IPath classPath = path.append(separate.these(fapi.names).by('/')).addFileExtension("java");
    if (!Eclipse.recursiveCreateFolder(project.getFolder(classPath.removeLastSegments(1).toString()), monitor))
      return false;
    try {
      classFile = project.getFile(classPath.toString());
      if (!classFile.exists())
        classFile.create(new ByteArrayInputStream("".getBytes()), IResource.NONE, monitor);
    } catch (final CoreException x) {
      note.bug(x);
      return false;
    }
    return true;
  }
  private void generatePackageDeclaration() {
    if (cu.getPackage() != null)
      return;
    final PackageDeclaration $ = ast.newPackageDeclaration();
    $.setName(ast.newName(separate.these(lisp.chopLast(fapi.names)).by('.')));
    r.set(cu, CompilationUnit.PACKAGE_PROPERTY, $, null);
  }
  private boolean generateCode() {
    cu = az.compilationUnit(makeAST.COMPILATION_UNIT.fromWithBinding(classFile));
    if (cu == null)
      return false;
    ast = cu.getAST();
    r = ASTRewrite.create(cu.getAST());
    ir = ImportRewrite.create(cu, true);
    ir.setUseContextToFilterImplicitImports(true);
    ir.setFilterImplicitImports(true);
    generatePackageDeclaration();
    generateBaseType();
    generateDeclarations();
    return true;
  }
  private boolean generateImports() {
    try {
      ir.rewriteImports(new NullProgressMonitor());
    } catch (final CoreException ¢) {
      note.bug(¢);
      return false;
    }
    final ListRewrite lr = r.getListRewrite(cu, CompilationUnit.IMPORTS_PROPERTY);
    final Collection<String> idns = an.empty.list();
    if (ir.getCreatedImports() != null)
      idns.addAll(as.list(ir.getCreatedImports()));
    if (ir.getCreatedStaticImports() != null)
      idns.addAll(as.list(ir.getCreatedStaticImports()));
    for (final String idn : idns) {
      final ImportDeclaration id = ast.newImportDeclaration();
      id.setName(ast.newName(idn));
      lr.insertLast(id, null);
    }
    return true;
  }
  @SuppressWarnings("unchecked") private void generateBaseType() {
    final List<AbstractTypeDeclaration> ds = cu.types();
    final String baseName = fapi.className.getIdentifier();
    for (final AbstractTypeDeclaration d : ds)
      if (iz.typeDeclaration(d) && d.getName().getIdentifier().equals(baseName)) {
        baseType = az.typeDeclaration(d);
        return;
      }
    baseType = ast.newTypeDeclaration();
    baseType.setName(ast.newSimpleName(baseName));
    baseType.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC));
    r.getListRewrite(cu, CompilationUnit.TYPES_PROPERTY).insertFirst(baseType, null);
  }
  private void generateDeclarations() {
    lastKnownType = baseType;
    int i = 0;
    outer: for (; i < fapi.invocations.size(); ++i) {
      if (!property.has(fapi.invocations.get(i), BINDING_PROPERTY))
        break;
      final ITypeBinding b = property.get(fapi.invocations.get(i), BINDING_PROPERTY);
      for (final TypeDeclaration t : baseType.getTypes())
        if (t.getName().getIdentifier().equals(b.getName())) {
          lastKnownType = t;
          continue outer;
        }
      note.bug("FAPIGenerator#generateDeclarations: API not in class: " + b.getName());
    }
    if (i == fapi.invocations.size())
      return;
    final Expression firstExpression = fapi.invocations.get(i);
    final String firstTypeName = i == fapi.invocations.size() - 1 ? null : getAbleName(fapi.invocations.get(i + 1));
    final BodyDeclaration addedDeclaration = iz.fieldAccess(firstExpression) ? getField(az.fieldAccess(firstExpression), firstTypeName)
        : getMethod(az.methodInvocation(firstExpression), lastKnownType, firstTypeName);
    final List<TypeDeclaration> addedTypes = an.empty.list();
    for (++i; i < fapi.invocations.size(); ++i) {
      final Expression e = fapi.invocations.get(i);
      final String curTypeName = getAbleName(e);
      final String nextTypeName = i == fapi.invocations.size() - 1 ? null : getAbleName(fapi.invocations.get(i + 1));
      if (iz.fieldAccess(e))
        addField(az.fieldAccess(e), curTypeName, nextTypeName, addedTypes);
      else
        addMethod(az.methodInvocation(e), curTypeName, nextTypeName, addedTypes);
    }
    // TODO Roth: smart insert
    r.getListRewrite(lastKnownType, TypeDeclaration.BODY_DECLARATIONS_PROPERTY).insertFirst(addedDeclaration, null);
    for (final TypeDeclaration t : addedTypes)
      r.getListRewrite(baseType, TypeDeclaration.BODY_DECLARATIONS_PROPERTY).insertLast(t, null);
  }
  @SuppressWarnings("unchecked") private TypeDeclaration addField(final FieldAccess a, final String curTypeName, final String nextTypeName,
      final List<TypeDeclaration> addedTypes) {
    final TypeDeclaration $ = ast.newTypeDeclaration();
    $.setName(ast.newSimpleName(curTypeName));
    $.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC | Modifier.STATIC));
    if (a != null)
      $.bodyDeclarations().add(getField(a, nextTypeName));
    addedTypes.add($);
    return $;
  }
  @SuppressWarnings("unchecked") private TypeDeclaration addMethod(final MethodInvocation i, final String curTypeName, final String nextTypeName,
      final List<TypeDeclaration> addedTypes) {
    final TypeDeclaration $ = ast.newTypeDeclaration();
    $.setName(ast.newSimpleName(curTypeName));
    $.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC | Modifier.STATIC));
    if (i != null)
      $.bodyDeclarations().add(getMethod(i, $, nextTypeName));
    addedTypes.add($);
    return $;
  }
  @SuppressWarnings("unchecked") private FieldDeclaration getField(final FieldAccess a, final String nextTypeName) {
    final VariableDeclarationFragment df = ast.newVariableDeclarationFragment();
    df.setName(ast.newSimpleName(a.getName().getIdentifier()));
    final AnnotatableType t = ast.newSimpleType(ast.newName(nextTypeName != null ? nextTypeName : "Object"));
    final ClassInstanceCreation c = ast.newClassInstanceCreation();
    c.setType(copy.of(t));
    df.setInitializer(c);
    final FieldDeclaration fd = ast.newFieldDeclaration(df);
    fd.setType(t);
    fd.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC));
    return fd;
  }
  @SuppressWarnings("unchecked") private MethodDeclaration getMethod(final MethodInvocation i, final TypeDeclaration lastType,
      final String nextTypeName) {
    final MethodDeclaration md = ast.newMethodDeclaration();
    md.setName(ast.newSimpleName(i.getName().getIdentifier()));
    int xx = 1; // TODO Roth: variables generation
    for (final Expression e : (List<Expression>) i.arguments()) {
      final SingleVariableDeclaration d = ast.newSingleVariableDeclaration();
      d.setName(ast.newSimpleName("arg" + xx++));
      final Type t = property.has(e, BINDING_PROPERTY) ? getType(property.get(e, BINDING_PROPERTY)) : ast.newSimpleType(ast.newSimpleName("Object"));
      d.setType(t);
      md.parameters().add(d);
    }
    final AnnotatableType t = nextTypeName == null ? ast.newPrimitiveType(PrimitiveType.VOID) : ast.newSimpleType(ast.newName(nextTypeName));
    md.setReturnType2(t);
    md.modifiers().addAll(ast.newModifiers( //
        Modifier.PUBLIC | (lastType != baseType ? Modifier.NONE : Modifier.STATIC)));
    final Block b = ast.newBlock();
    if (!t.isPrimitiveType() || !PrimitiveType.VOID.equals(((PrimitiveType) t).getPrimitiveTypeCode())) {
      final ClassInstanceCreation c = ast.newClassInstanceCreation();
      c.setType(copy.of(t));
      final ReturnStatement s = ast.newReturnStatement();
      s.setExpression(c);
      b.statements().add(s);
    }
    md.setBody(b);
    return md;
  }
  private static String getAbleName(final Expression s) {
    return getAbleName(iz.methodInvocation(s) ? az.methodInvocation(s).getName().getIdentifier() : az.fieldAccess(s).getName().getIdentifier());
  }
  private static String getAbleName(final String s) {
    return s + "Able";
  }
  private Type getType(final ITypeBinding b) {
    final Type $ = getTypeInner(b);
    if ($ != null)
      ir.addImport(b, ast);
    return $;
  }
  private Type getTypeInner(final ITypeBinding b) {
    if (b.isNullType())
      return ast.newSimpleType(ast.newSimpleName("Object"));
    if (b.isPrimitive())
      return ast.newPrimitiveType(PrimitiveType.toCode(b.getName()));
    if (b.isArray())
      return ast.newArrayType(getType(b.getElementType()), b.getDimensions());
    // TODO Roth: complete all cases
    return ast.newSimpleType(ast.newSimpleName(b.getName()));
  }
  private boolean commit() {
    try {
      final TextFileChange $ = new TextFileChange(NAME, classFile);
      $.setTextType("java");
      $.setEdit(r.rewriteAST());
      $.perform(monitor != null ? monitor : new NullProgressMonitor());
      return true;
    } catch (IllegalArgumentException | CoreException x) {
      note.bug(x);
    }
    return false;
  }
}
