package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.*;
import com.intellij.testFramework.LightVirtualFile;
import il.org.spartan.Leonidas.auxilary_layer.*;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericEncapsulator;
import il.org.spartan.Leonidas.plugin.leonidas.KeyDescriptionParameters;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher.Constraint;
import il.org.spartan.Leonidas.plugin.leonidas.Pruning;
import il.org.spartan.Leonidas.plugin.leonidas.Replacer;
import il.org.spartan.Leonidas.plugin.tipping.Tip;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a tipper created by the leonidas language.
 *
 * @author Amir Sagiv, Sharon Kuninin, michalcohen
 * @since 26-03-2017.
 */
@SuppressWarnings("ConstantConditions")
public class LeonidasTipper implements Tipper<PsiElement> {

    private String description;
    private String name;
    private Matcher matcher;
    private Replacer replacer;
    private Class<? extends PsiElement> rootType;
    private PsiJavaFile file;
    private Map<Integer, List<Constraint>> map;

    @SuppressWarnings("ConstantConditions")
    public LeonidasTipper(String tipperName, String fileContent) {
        file = getPsiTreeFromString("Tipper" + tipperName, fileContent);
        assert file != null;
        description = file.getClasses()[0].getDocComment().getText()
                .split("\\n")[1].trim()
                .split("\\*")[1].trim();
        name = tipperName;
        map = getConstraints();
        matcher = new Matcher(getMatcherRootsTree(), map);
        rootType = matcher.getAllRoots().get(0).getInner().getClass();
        replacer = new Replacer(initializeReplacerRoots(getReplacingRules()));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~ Tipper methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * @param e an element inside a template body
     * @return each and every generic element in the sub tree of e, including 'hidden' generic elements
     * that are not part of the tree, rather part of the properties of other generic elements in the tree.
     */
    public static List<GenericEncapsulator> getGenericElements(Encapsulator e) {
        List<GenericEncapsulator> lge = new LinkedList<>();
        e.accept(n -> {
            if (!iz.generic(n)) return;
            lge.add(az.generic(n));
            lge.addAll(az.generic(n).getGenericElements().values());
        });
        return lge;
    }

    @Override
    public boolean canTip(PsiElement e) {
        return matcher.match(e);
    }

    @Override
    public String description(PsiElement e) {
        return description;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Tip tip(PsiElement node) {
        return new Tip(description(node), node, this.getClass()) {
            @Override
            public void go(PsiRewrite r) {
                if (!canTip(node))
					return;
				Wrapper<Integer> i = new Wrapper<>(0);
				getReplacerCopy().replace(node, matcher.extractInfo(node, i), i.get(), r);
            }
        };
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~ Auxiliary methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~ Extracting template body ~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Class<? extends PsiElement> getOperableType() {
        return rootType;
    }

    /**
     * @param m the template method
     * @return The list of template roots inside 'start - and' section
     */
    private List<Encapsulator> getForestFromMethod(PsiMethod m) {
        PsiNewExpression ne = az.newExpression(az.expressionStatement(m.getBody().getStatements()[0]).getExpression());
        PsiElement current;
        Wrapper<PsiElement> we = new Wrapper<>(ne);
        ne.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitComment(PsiComment c) {
                super.visitComment(c);
                if (c.getText().contains("start"))
                    we.set(c);
            }
        });
        current = Utils.getNextActualSiblingWithComments(we.get());
        List<Encapsulator> roots = new ArrayList<>();
        if (iz.declarationStatement(current) && iz.classDeclaration(current.getFirstChild()))
            current = current.getFirstChild();
        for (; current != null
                && (!iz.comment(current) || !az.comment(current).getText().contains("end")); current = Utils
                .getNextActualSiblingWithComments(current))
            roots.add(Pruning.prune(Encapsulator.buildTreeFromPsi(current), map));
        return roots;
    }

    /**
     * Inserts the ID numbers into the user data of the generic method call expressions.
     * For example 5 will be inserted to booleanExpression(5).
     *
     * @param tree the root of the tree for which we insert IDs.
     */
    private void giveIdToStubElements(PsiElement tree) {
        tree.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement e) {
                super.visitElement(e);
                Toolbox.getInstance().getGenericsBasicBlocks().stream().filter(g -> g.conforms(e)).findFirst().ifPresent(g -> e.putUserData(KeyDescriptionParameters.ID, g.extractId(e)));
            }
        });
    }

    /**
     * @param name the name of a method of the interface LeonidasTipperDefinition.
     * @return the body of the method
     */
    private PsiMethod getInterfaceMethod(String name) {
        Wrapper<PsiMethod> x = new Wrapper<>();
        file.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethod(PsiMethod m) {
                super.visitMethod(m);
                if (m.getName().equals(name))
                    x.set(m);
            }
        });
        if (x.get() == null) return null;
        giveIdToStubElements(x.get());
        return x.get();
    }

    /**
     * @param name    the name of the tipper
     * @param content the content of the definition file of the tipper
     * @return PsiFile representing the file of the tipper
     */
    private PsiJavaFile getPsiTreeFromString(String name, String content) {
        Language language = JavaLanguage.INSTANCE;
        LightVirtualFile virtualFile = new LightVirtualFile(name, language, content);
        SingleRootFileViewProvider.doNotCheckFileSizeLimit(virtualFile);
        final FileViewProviderFactory factory = LanguageFileViewProviders.INSTANCE.forLanguage(language);
        FileViewProvider viewProvider = factory == null ? null
                : factory.createFileViewProvider(virtualFile, language, Utils.getPsiManager(Utils.getProject()), true);
        if (viewProvider == null)
            viewProvider = new SingleRootFileViewProvider(
                    Utils.getPsiManager(ProjectManager.getInstance().getDefaultProject()), virtualFile, true);
        language = viewProvider.getBaseLanguage();
        return LanguageParserDefinitions.INSTANCE.forLanguage(language) == null ? null
                : (PsiJavaFile) viewProvider.getPsi(language);
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Build Matcher ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * @return the generic forest representing the "matcher" template
     */
    private List<Encapsulator> getMatcherRootsTree() {
        return getForestFromMethod(getInterfaceMethod("matcher"));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Build Replacer ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * @return the generic forest representing the replacer template
     */
    private List<Encapsulator> initializeReplacerRoots(Map<Integer, List<PsiMethodCallExpression>> m) {
        List<Encapsulator> l = getForestFromMethod((PsiMethod) getInterfaceMethod("replacer").copy());
        l.forEach(root -> getGenericElements(root).forEach(n -> m.getOrDefault(n.getId(), new LinkedList<>()).forEach(mce -> {
            List<Object> arguments = step.arguments(mce).stream().map(e -> az.literal(e).getValue()).collect(Collectors.toList());
            Encapsulator ie = !iz.quantifier(n) ? n : az.quantifier(n).getInternal();
            try {
                Utils.getDeclaredMethod(ie.getClass(), mce.getMethodExpression().getReferenceName(), Arrays.stream(arguments.toArray()).map(Object::getClass).collect(Collectors.toList()).toArray(new Class<?>[] {})).invoke(ie, arguments.toArray());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                e1.printStackTrace();
            }

        })));
        return l;
    }

    /**
     * @return a copy of the replacer, since on each activation, the current replacer is corrupted.
     */
    private Replacer getReplacerCopy(){
        return new Replacer(replacer, getForestFromMethod((PsiMethod) getInterfaceMethod("replacer").copy()));
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~ Build additional rules ~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * @param s the statement of the rule. For example: element(3).isNot(!booleanExpression(4))
     * @return the ID on which the rule applies. The previous example will return 3.
     */
    private Integer extractIdFromRule(PsiStatement s) {
        Wrapper<PsiMethodCallExpression> x = new Wrapper<>();
        s.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                super.visitMethodCallExpression(expression);
                if ("element".equals(expression.getMethodExpression().getText()))
                    x.set(expression);
            }
        });
        return Integer.parseInt(step.arguments(x.get()).get(0).getText());
    }

    /**
     * @param s a rule statement.
     * @return "is" if s is of the form 'element(index).is(constraint)', "isNot" if
     * s is of the form 'element(index).isNot(constraint)' and "specific" if s is not structural rule.
     */
    private Constraint.ConstraintType extractConstraintType(PsiStatement s) {
        String constraintName = az.methodCallExpression(s.getFirstChild()).getMethodExpression().getReferenceName();
        try {
            return Constraint.ConstraintType.valueOf(constraintName.toUpperCase());
        } catch (Exception ignore) {
        }
        return Constraint.ConstraintType.SPECIFIC;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~ Build Structural rules ~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * @param s a string representing Psi element class. For example "PsiIfStatement".
     * @return a class object of the received class. For example "PsiIfStatement.class"
     */
    private Class<? extends PsiElement> getPsiClass(String s) {
        try {
            //noinspection unchecked
            return (Class<? extends PsiElement>) Class.forName("com.intellij.psi." + s);
        } catch (ClassNotFoundException ignore) {
        }
        return PsiElement.class;
    }

    /**
     * @param e         the root of the template.
     * @param rootElementType the type of the inner element to extract
     * @return the inner element derived by its type
     */
    private PsiElement getRealRootByType(PsiElement e, Class<? extends PsiElement> rootElementType) {
        Wrapper<PsiElement> result = new Wrapper<>();
        Wrapper<Boolean> stop = new Wrapper<>(false);
        e.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);
                if (stop.get() || !iz.ofType(element, rootElementType))
                    return;
                result.set(element);
                stop.set(true);
            }
        });
        return result.get();
    }

    /**
     * @param s the statement of the constraint. For example: element(booleanExpression(3)).isNot(!booleanExpression(4))
     * @return the body of the lambda expression that defines the constraint. The previous example will return !booleanExpression(4).
     */
    private PsiElement getLambdaExpressionBody(PsiStatement s) {
        Wrapper<PsiLambdaExpression> l = new Wrapper<>();
        s.accept(new JavaRecursiveElementVisitor() {

            @Override
            public void visitLambdaExpression(PsiLambdaExpression x) {
                super.visitLambdaExpression(x);
                l.set(x);
            }
        });
        return l.get().getBody();
    }

    /**
     * @param s the statement of the constraint. For example: element(booleanExpression(3)).isNot(!booleanExpression(4)).ofType(PsiExpression.class)
     * @return the type inside "ofType" expression. The previous example will return Optional(PsiExpression.class).
     * if no "ofType" expression is found, Optional.empty() is returned.
     */
    private Optional<Class<? extends PsiElement>> getTypeOf(PsiStatement s) {
        Wrapper<Optional<Class<? extends PsiElement>>> wq = new Wrapper<>(Optional.empty());
        s.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression x) {
                super.visitMethodCallExpression(x);
                if (x.getMethodExpression().getText().endsWith("ofType"))
                    wq.set(Optional
                            .of(getPsiClass(x.getArgumentList().getExpressions()[0].getText().replace(".class", ""))));
            }
        });
        return wq.get();
    }

    // ~~~~~~~~~~~~~~~~~~ Build Constraints and Replacing rules ~~~~~~~~~~~~~~~~~~

    /**
     * Searches for all the structural constraints found in the "constraints" method and returns a mapping between
     * element ids and their structural constraints. For example, if the following is present in the constraints method:
     * <p>
     * <code>element(1).is(() -> return null;);</code>
     * <p>
     * It will map id 1 to a structural constraint on it, requiring it look like:
     * <p>
     * <code>return null;</code>.
     *
     * @return a mapping between the ID of generic elements to a list of all the constraint that apply on them.
     */
    private Map<Integer, List<Constraint>> getConstraints() {
        Map<Integer, List<Constraint>> map = new HashMap<>();
        PsiMethod constrainsMethod = getInterfaceMethod("constraints");
        if (constrainsMethod != null && haz.body(constrainsMethod))
            Arrays.stream(constrainsMethod.getBody().getStatements()).forEach(s -> {
                Integer elementId = extractIdFromRule(s);
                Constraint.ConstraintType constraintType = extractConstraintType(s);
                if (constraintType != Constraint.ConstraintType.IS
                        && constraintType != Constraint.ConstraintType.ISNOT) {
                    PsiMethodCallExpression method = az.methodCallExpression(s.getFirstChild());
                    List<Object> arguments = step.arguments(method).stream().map(e -> az.literal(e).getValue())
                            .collect(Collectors.toList());
                    map.putIfAbsent(elementId, new LinkedList<>());
                    map.get(elementId).add(new Matcher.NonStructuralConstraint(
                            method.getMethodExpression().getReferenceName(), arguments.toArray()));
                } else {
                    PsiElement y = getLambdaExpressionBody(s);
                    Optional<Class<? extends PsiElement>> q = getTypeOf(s);
                    y = !q.isPresent() ? y : getRealRootByType(y, q.get());
                    map.putIfAbsent(elementId, new LinkedList<>());
                    List<Encapsulator> l = new LinkedList<>();
                    l.add(Pruning.prune(Encapsulator.buildTreeFromPsi(y), map));
                    map.get(elementId).add(new Matcher.StructuralConstraint(constraintType, l));
                }
            });
        return map;
    }

    /**
     * Searches for all the replacing rules found in the "replacing rules" method and returns a mapping between
     * elements id and their replacing rules. For example, if the following is present in the replacing rules method:
     * <p>
     * <code>element(1).asStatement.replaceIdentifier(3, "cent");</code>
     * <p>
     * It will map id 1 to the replacing rule "replace identifiers"
     * @return a mapping between the ID of generic elements to a list of all the replacing rules that apply on them.
     */
    private Map<Integer, List<PsiMethodCallExpression>> getReplacingRules() {
        Map<Integer, List<PsiMethodCallExpression>> map = new HashMap<>();
        PsiMethod replacingRulesMethod = getInterfaceMethod("replacingRules");
        if (replacingRulesMethod != null && haz.body(replacingRulesMethod))
            Arrays.stream(replacingRulesMethod.getBody().getStatements()).forEach(s -> {
                Integer elementId = extractIdFromRule(s);
                PsiMethodCallExpression method = az.methodCallExpression(s.getFirstChild());
                map.putIfAbsent(elementId, new LinkedList<>());
                map.get(elementId).add(method);
            });
        return map;
    }

    public Matcher getMatcher() {
        return matcher;
    }

    public Replacer getReplacer() {
        return replacer;
    }
}
