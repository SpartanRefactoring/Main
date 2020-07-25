package il.org.spartan.spartanizer.java.namespace.tables;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;

import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.cmdline.CurrentData;
import il.org.spartan.spartanizer.cmdline.GrandVisitor;
import il.org.spartan.spartanizer.cmdline.Tapper;
import il.org.spartan.utils.Int;

/**
 * Generates a table of the class fields
 * 
 * @author Dor Ma'ayan
 * @since 2017-10-16
 */
public class NonLinearTests extends NominalTables {
	static boolean isJunitAnnotation(List<String> annotations) {
		String[] anno = { "After", "AfterClass", "Before", "BeforeClass" };
		List<String> annoList = Arrays.asList(anno);
		for (String s : annotations) {
			if (annoList.contains(s))
				return true;
		}
		return false;
	}

	static boolean isIgnoredTest(List<String> annotations) {
		String[] anno = { "Ignore" };
		List<String> annoList = Arrays.asList(anno);
		for (String s : annotations) {
			if (annoList.contains(s))
				return true;
		}
		return false;
	}

	@SuppressWarnings({ "unused" })
	public static void main(final String[] args) throws Exception, UnsupportedEncodingException {
		final HashMap<String, Integer> map = new HashMap<>();
		try (PrintWriter writer = new PrintWriter("/Users/Dor/Desktop/NonLinear.txt", "UTF-8")) {
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
				}

				protected void done(final String path) {
					summarize(path);
					reset();
				}

				public void summarize(final String path) {
					writer.write("~~~~~~~~~~~~~~~~~~~ Random Samplings from " + path + "~~~~~~~~~~~~~~~~~~~");
					writer.write("\n \n \n \n \n \n");
				}
			}.visitAll(new ASTVisitor(true) {
				@Override
				public boolean visit(final CompilationUnit ¢) {
					¢.accept(new ASTVisitor() {
						@Override
						public boolean visit(final MethodDeclaration m) {
							if (m != null) {
								List<String> annotations = extract.annotations(m).stream()
										.map(a -> a.getTypeName().getFullyQualifiedName()).collect(Collectors.toList());
								if (annotations.contains("Test") || (iz.typeDeclaration(m.getParent())
										&& az.typeDeclaration(m.getParent()).getSuperclassType() != null
										&& az.typeDeclaration(m.getParent()).getSuperclassType().toString()
												.equals("TestCase"))) {
									final Int counter = new Int(); // asseerts counter
									final Int irregulars = new Int(); // asseerts counter
									final Int printed = new Int(0);
									m.accept(new ASTVisitor() {
										@Override
										public boolean visit(final ForStatement x) {
											irregulars.step();
											if (printed.get() == 0) {
												printed.step();
												writer.write("~~~~~~~New Test~~~~~~~\n \n");
												writer.write(m.toString());
												writer.write("\n \n \n \n");
											}
											return true;
										}

										@Override
										public boolean visit(final WhileStatement x) {
											irregulars.step();
											if (printed.get() == 0) {
												printed.step();
												writer.write("~~~~~~~New Test~~~~~~~\n \n");
												writer.write(m.toString());
												writer.write("\n \n \n \n");
											}
											return true;
										}

										@Override
										public boolean visit(final EnhancedForStatement x) {
											irregulars.step();
											if (printed.get() == 0) {
												printed.step();
												writer.write("~~~~~~~New Test~~~~~~~\n \n");
												writer.write(m.toString());
												writer.write("\n \n \n \n");
											}
											return true;
										}
									});
								}
							}
							return true;
						}
					});
					return super.visit(¢);
				}
			});
		}
	}
}
