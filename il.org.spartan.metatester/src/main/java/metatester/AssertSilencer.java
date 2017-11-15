package metatester;

import il.org.spartan.spartanizer.ast.navigate.compute;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import metatester.aux_layer.mutables.Wrapper;
import org.eclipse.jdt.core.dom.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static metatester.AssertJTransformator.transformations;
import static metatester.aux_layer.MetaTesterStringUtils.getTemplatedValues;

/**
 * @author Oren Afek
 * @since 5/25/2017.
 */
public class AssertSilencer {
    /**
     * Is the string in an AssertJ format
     *
     * @param ¢ the string
     * @return true iff ¢ is AssertJ formated and false otherwise.
     */
    private static boolean isAssertJFormatted(final String ¢) {
        return ¢.trim().startsWith("assertThat");
    }

    private static Optional<String> sideEffect(final String s) {
        ASTNode node = null;
        try {
            node = wizard.ast(s);
        } catch (Throwable t) {
            try {
                node = wizard.ast(s + ";");
            } catch (Throwable ignore) {
            }
        }

        if (node == null)
            return Optional.empty();

        Wrapper<ASTNode> w = new Wrapper<>(null);
        Wrapper<Boolean> visited = new Wrapper<>(false);

        node.accept(new ASTVisitor() {

            private boolean _visit(ASTNode cent) {
                if (visited.get())
                    return false;
                visited.set(true);
                w.set(cent);
                return true;
            }

            @Override
            public boolean visit(MethodInvocation cent) {
                return _visit(cent);
            }

            @Override
            public boolean visit(PrefixExpression cent) {
                return _visit(cent);
            }

            @Override
            public boolean visit(PostfixExpression cent) {
                return _visit(cent);
            }

        });

        if (iz.expression(node)) {
            List<Statement> l = compute.decompose(az.expression(node));
            return !l.isEmpty() ?
                    Optional.of(l.stream().map(ASTNode::toString).reduce("", (s1, s2) -> s1 + " " + s2))
                    : Optional.empty();

        }

        return Optional.empty();
        /*return iz.methodInvocation(node) || iz.postfixExpression(node) || iz.prefixExpression(node) ?
                Optional.of(w.get().toString()) : Optional.empty();*/

    }

    /**
     * Is the string can be a Java Statement
     *
     * @param ¢ a string (should be a rightfully Java expression)
     * @return truee if "¢;" is a statement and false otherwise.
     */
    private static boolean isStatement(final String ¢) {
        ASTNode ast;
        try {
            ast = wizard.ast(¢ + ";");
        } catch (Throwable e) {
            return false;
        }
        Wrapper<ASTNode> w = new Wrapper<>(ast);
        ast.accept(new ASTVisitor() {
            @Override
            public boolean visit(ExpressionStatement node) {
                w.set(node);
                return true;
            }
        });
        return iz.expressionStatement(w.get());
    }

    /**
     * Removing an AssertJ test idiom, but keeping its side effects.
     * e.g.: "assertThat(f(x)).isEqualTo(1)" -> "f(x)"
     * "assertThat(1+2).isEqualTo(3)"  -> ""
     *
     * @param ¢ test idiom
     * @return the extraction of the side effects from the idiom, or "" if there aren't any.
     */
    private static String shutDownAssertJ(final String ¢) {
        assert isAssertJFormatted(¢);
        final List<String> $ = getTemplatedValues(¢, Arrays.stream(transformations)
                .map(Transformation::to)
                .map(s -> s.replace("%s", "(.*)"))
                .collect(Collectors.toList()));

        List<String> sideEffects = $
                .stream()
                .map(s -> sideEffect(s).orElse(";"))
                .filter(s -> !s.trim().equals(";") && !"\n".equals(s) && !";\n".equals(s) && !"\n;".equals(s))
                .collect(Collectors.toList());

        return sideEffects.stream().reduce("", (s1, s2) -> s1 + " " + s2);

    }

    /**
     * Shuts down all statements but the one in activePosition (iff they are AssertJ formated).
     *
     * @param statements     list of test statements
     * @param activePosition of active statement.
     * @return
     */
    public static List<String> shutDown(List<String> statements, int activePosition) {
        for (int i = 0; i < statements.size(); i++) {
            if (i == activePosition) {
                continue;
            }
            statements.set(i, shutDown(statements.get(i)));
        }
        return statements;
    }

    /**
     * Removing an test idiom, but keeping its side effects.
     * e.g.: "assertThat(f(x)).isEqualTo(1)" -> "f(x)"
     * "assertThat(1+2).isEqualTo(3)"  -> ""
     *
     * @param ¢ test idiom
     * @return the extraction of the side effects from the idiom, or "" if there aren't any.
     */
    public static String shutDown(final String ¢) {
        return !isAssertJFormatted(¢) ? ¢ : shutDownAssertJ(¢);
    }
}
