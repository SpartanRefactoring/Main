package metatester.aux_layer;

import il.org.spartan.Wrapper;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.iz;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import java.util.function.Function;

import static il.org.spartan.spartanizer.ast.factory.makeAST.COMPILATION_UNIT;
import static org.eclipse.jdt.core.dom.CompilationUnit.IMPORTS_PROPERTY;

/**
 * AST modification utilities.
 *
 * @author Oren Afek
 * @since 27-Jun-17.
 */
public class ASTUtils {

	private Document document;
	private ASTNode node;
	private ASTRewrite rw;

	private ASTUtils(ASTNode node, Document document) {
		this.node = node;
		this.document = document;
	}

	public static ASTUtils createRemover(ASTNode node, Document document) {
		return new ASTUtils(node, document);
	}

	private ASTNode compUnit(ASTNode removee) {
		ASTNode parent = removee.getParent();
		for (; parent != null;) {
			if (iz.compilationUnit(parent))
				return parent;
			parent = parent.getParent();
		}

		return removee.getRoot();
	}

	public static ASTNode makeASTNode(final String s) {
		ASTNode node = null;
		try {
			node = wizard.ast(s);
		} catch (Throwable t) {
			try {
				node = wizard.ast(s);
			} catch (Throwable ignore) {
				/**/}
		}

		return node;
	}

	public ASTNode commit() {
		if (this.rw == null)
			return node;
		TextEdit edit = rw.rewriteAST(document, null);
		return commit(edit, node, document);
	}

	public static ASTNode commit(TextEdit edit, ASTNode defaultNode, Document document) {
		try {

			edit.apply(document);
			return makeAST.COMPILATION_UNIT.from(document.get());
		} catch (BadLocationException ignore) {
		}

		return defaultNode;
	}

	public static <T> T tripleMethodInvocation(ASTNode node, Function<ASTNode, T> handler) {
		Wrapper<T> w = new Wrapper<>();
		node.accept(new ASTVisitor() {
			@Override
			public boolean visit(MethodInvocation node) {
				node.accept(new ASTVisitor() {
					@Override
					public boolean visit(MethodInvocation node) {
						node.accept(new ASTVisitor() {
							@Override
							public boolean visit(MethodInvocation node) {
								w.set(handler.apply(node));
								return true;
							}
						});

						return false;
					}
				});

				return false;
			}
		});

		return w.get();
	}

	public static ASTNode addStaticImports(ASTNode root, Document document, String importName, String simpleName) {
		if (!iz.compilationUnit(root))
			return root;
		metatester.aux_layer.mutables.Wrapper<ASTNode> w = new metatester.aux_layer.mutables.Wrapper<>(root);
		root.accept(new ASTVisitor() {
			@Override
			public boolean visit(ImportDeclaration ¢) {
				w.set(¢);
				return true;
			}
		});

		ASTNode newRoot = root;
		ListRewrite lr = ASTRewrite.create(newRoot.getAST()).getListRewrite(newRoot, IMPORTS_PROPERTY);
		ImportDeclaration id = newRoot.getAST().newImportDeclaration();
		id.setStatic(true);
		id.setName(newRoot.getAST().newName(importName.split("\\.")));
		lr.insertFirst(id, null);
		try {
			TextEdit te = lr.getASTRewrite().rewriteAST(document, null);
			te.apply(document);
			return COMPILATION_UNIT.from(document.get());
		} catch (BadLocationException e) {
			/**/}
		return root;

	}

	public ASTUtils remove(ASTNode removee) {
		if (this.rw == null)
			this.rw = ASTRewrite.create(compUnit(removee).getAST());
		this.rw.remove(removee, null);
		return this;
	}
}
