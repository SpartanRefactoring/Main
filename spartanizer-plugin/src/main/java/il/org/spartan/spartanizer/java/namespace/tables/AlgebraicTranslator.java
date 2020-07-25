package il.org.spartan.spartanizer.java.namespace.tables;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.cmdline.CurrentData;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.tables.Table;
import il.org.spartan.utils.Int;

/**
 * Generates a table of the class fields
 * 
 * @author Dor Ma'ayan
 * @since 2017-10-16
 */
public class AlgebraicTranslator extends NominalTables {

	public boolean isLinearTest(MethodDeclaration m) {
		Boolean check = Boolean.FALSE;
		m.accept(new ASTVisitor() {

		});
		return check;
	}

	@SuppressWarnings({ "boxing", "unused" })
	public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
		final HashMap<String, Integer> map = new HashMap<>();
		map.put("#Tests", 0);
		try (PrintWriter writer = new PrintWriter("/Users/Dor/Desktop/paper_results/raw.txt", "UTF-8")) {
			new GrandVisitor(args) {
				{
					listen(new Tapper() {
						@Override
						public void endLocation() {
							done(CurrentData.location);
						}
					});
				}

				void reset() {
					map.put("#Tests", 0);
				}

				protected void done(final String path) {
					summarize(path);
					reset();
				}

				public void summarize(final String path) {
					initializeWriter();
					if (map.get("#Tests") != 0) {
						writer.write("~~~~~~~~~~~~~~~~~~~ Random Samplings from " + path + "~~~~~~~~~~~~~~~~~~~");
						writer.write("\n \n \n \n \n \n");
					}
				}

				void initializeWriter() {
					if (table == null)
						table = new Table(corpus, outputFolder);
				}
			}.visitAll(new ASTVisitor(true) {
				@Override
				public boolean visit(final CompilationUnit ¢) {
					String algebraicRep = "";
					Int testCounter = new Int();
					¢.accept(new ASTVisitor() {
						@Override
						public boolean visit(final MethodDeclaration x) {
							if (x != null) {
								List<String> annotations = extract.annotations(x).stream()
										.map(a -> a.getTypeName().getFullyQualifiedName()).collect(Collectors.toList());
								if (annotations.contains("Test") || (iz.typeDeclaration(x.getParent())
										&& az.typeDeclaration(x.getParent()).getSuperclassType() != null
										&& az.typeDeclaration(x.getParent()).getSuperclassType().toString()
												.equals("TestCase")))
									testCounter.step();
							}
							return true;
						}
					});
					return super.visit(¢);
				}
			});
			table.close();
		}
		System.err.println(table.description());
	}
}
