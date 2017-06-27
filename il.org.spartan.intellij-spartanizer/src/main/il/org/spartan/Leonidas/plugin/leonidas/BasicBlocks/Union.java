package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.step;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A basic block representing 'Or' notion between elements.
 * @author Sharon LK
 * @since 19-05-17
 */
public class Union extends GenericMethodCallBasedBlock {
    public static final String TEMPLATE = "union";

    protected List<GenericEncapsulator> encapsulators = new ArrayList<>();

    public Union(PsiElement e) {
        super(e, TEMPLATE);

        calcUnionParticipants(e);
    }

    public Union(Encapsulator n) {
        super(n, TEMPLATE);

        calcUnionParticipants(n.inner);
    }

    /**
     * For reflection use DO NOT REMOVE!
     */
    @SuppressWarnings("unused")
    protected Union() {
        super(TEMPLATE);
    }

    @Override
    protected boolean goUpwards(Encapsulator prev, Encapsulator next) {
        return false;
    }

    @Override
    public GenericEncapsulator create(Encapsulator e, Map<Integer, List<Matcher.Constraint>> map) {
        return new Union(e);
    }

    @Override
    public MatchingResult generalizes(Encapsulator e, Map<Integer, List<PsiElement>> m) {
        return encapsulators.stream().map(encapsulator -> encapsulator.generalizes(e, m)).filter(MatchingResult::matches).findFirst().orElse(new MatchingResult(false));
    }

    /**
     * Calculates the participants of this union, so the generalizes method can be implemented as
     * expected.
     *
     * @param e PSI element representing the union declaration
     */
    private void calcUnionParticipants(PsiElement e) {
        // The given PSI element is of the form union(1, method(), statement()), we want to extract
        // the arguments and parse them into basic blocks
        List<PsiExpression> arguments = step.arguments(az.methodCallExpression(e));

        // First argument is the index, we want to skip it when parsing the union structure
        arguments.remove(0);

        arguments.forEach(arg -> {
            GenericEncapsulator encapsulator = null;

            if (arg.getText().startsWith("method"))
				encapsulator = new Method();
			else if (arg.getText().startsWith("statement"))
				encapsulator = new Statement();
			else if (arg.getText().startsWith("anyBlock"))
				encapsulator = new Block();
			else if (arg.getText().startsWith("booleanExpression"))
				encapsulator = new BooleanExpression();
			else if (arg.getText().startsWith("booleanLiteral"))
				encapsulator = new BooleanLiteral();
			else if (arg.getText().startsWith("expression"))
				encapsulator = new Expression();

            if (encapsulator != null) {
                int id = az.integer(step.firstParameterExpression(az.methodCallExpression(arg)));
                encapsulators.add(encapsulator);
            }
        });
    }
}
