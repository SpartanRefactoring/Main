package il.org.spartan.Leonidas.plugin.tippers;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
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
import il.org.spartan.Leonidas.plugin.tipping.Tip;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

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
    }

    @Override
    public boolean canTip(PsiElement e) {
        return matcher.match(e);
    }

    @Override
    public String description(PsiElement element) {
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
                if (canTip(node)) {
                    Wrapper<Integer> i = new Wrapper<>(0);
                    Map<Integer, List<PsiElement>> map = matcher.extractInfo(node, i);
                    replace(node, map, i.get(), r);
                }
            }
        };
    }

    @Override
    public Class<? extends PsiElement> getPsiClass() {
        return rootType;
    }

    /**
     * This method replaces the given element by the corresponding tree built by PsiTreeTipperBuilder
     *
     * @param treeToReplace - the given tree that matched the "from" tree.
     * @param r             - Rewrite object
     */
    private void replace(PsiElement treeToReplace, Map<Integer, List<PsiElement>> m, Integer numberOfRoots, PsiRewrite r) {
        List<PsiElement> elements = getReplacingForest(m, r);
        PsiElement prev = treeToReplace.getPrevSibling();
        while(prev != null && iz.whiteSpace(prev)){
            prev = prev.getPrevSibling();
        }
        PsiElement last = treeToReplace;
        for (int i = 1; i < numberOfRoots; i++){
            last = last.getNextSibling();
        }
        PsiElement parent = treeToReplace.getParent();
        r.deleteByRange(parent, treeToReplace, last);
        //parent.deleteChildRange(treeToReplace, last);
        if (prev == null){
            prev = parent.getFirstChild();
            for (PsiElement element : elements){
                r.addBefore(parent, prev, element);
            }
        } else {
            for (PsiElement element : elements){
                r.addAfter(parent, prev, element);
            }
        }

    }

    /**
     * @param m the mapping between generic element index to concrete elements of the user.
     * @param r rewrite object
     * @return the template of the replacer with the concrete elements inside it.
     */
    private List<PsiElement> getReplacingForest(Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        List<Encapsulator> rootsCopy = getReplacerRootTree();
        List<PsiElement> elements = new LinkedList<>();
        rootsCopy.forEach(rootCopy -> {
            if (rootCopy.isGeneric())
                elements.addAll(m.get(az.generic(rootCopy).getId())); //TODO
            else {
                rootCopy.accept(e -> {
                    if (!e.isGeneric()) return;
                    GenericEncapsulator ge = az.generic(e);
                    ge.replaceByRange(m.get(ge.getId()), r);
                });
                elements.add(rootCopy.getInner());
            }
        });
        return elements;
    }

    /**
     * @param method the template method
     * @return the first PsiElement of the type rootElementType
     */
    private List<Encapsulator> getForestFromMethod(PsiMethod method) {
        PsiNewExpression ne = az.newExpression(az.expressionStatement(method.getBody().getStatements()[0]).getExpression());
        PsiElement current;
        if (iz.codeBlock(((PsiLambdaExpression) ne.getArgumentList().getExpressions()[0]).getBody())) {
            current = Utils.getFirstElementInsideBody((PsiCodeBlock) (((PsiLambdaExpression) ne.getArgumentList().getExpressions()[0]).getBody()));
        } else {
            current = ne.getArgumentList().getExpressions()[0].getFirstChild();
            current = Utils.getNextActualSibling(current);
            current = Utils.getNextActualSibling(current);
        }
        while (current != null && (!iz.javadoc(current) || !az.javadoc(current).getText().contains("start"))) {
            current = current.getNextSibling();
        }
        current = Utils.getNextActualSibling(current);
        List<Encapsulator> roots = new ArrayList<>();
        rootType = current.getClass();
        while (current != null && (!iz.javadoc(current) || !az.javadoc(current).getText().contains("end"))) {
            roots.add(Pruning.prune(Encapsulator.buildTreeFromPsi(current), map));
            current = Utils.getNextActualSibling(current);
        }
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
            public void visitElement(PsiElement element) {
                super.visitElement(element);
                Toolbox.getInstance().getGenericsBasicBlocks().stream().filter(g -> g.conforms(element)).findFirst().ifPresent(g -> element.putUserData(KeyDescriptionParameters.ID, g.extractId(element)));
            }
        });
    }

    /**
     * @return the generic tree representing the "from" template
     */
    private List<Encapsulator> getMatcherRootsTree() {
        PsiMethod method = getInterfaceMethod("matcher");
        giveIdToStubElements(method);

        return getForestFromMethod(method);
    }

    /**
     * @return the generic tree representing the "from" template
     */
    private List<Encapsulator> getReplacerRootTree() {
        PsiMethod replacer = (PsiMethod) getInterfaceMethod("replacer").copy();
        giveIdToStubElements(replacer);
        return getForestFromMethod(replacer);
    }

    /**
     * @param s the statement of the constraint. For example: the(3).isNot(!booleanExpression(4))
     * @return the ID on which the constraint applies. The previous example will return 3.
     */
    private Integer extractIdFromConstraint(PsiStatement s) {
        Wrapper<PsiMethodCallExpression> x = new Wrapper<>();
        s.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                super.visitMethodCallExpression(expression);
                if (expression.getMethodExpression().getText().equals("element")) {
                    x.set(expression);
                }
            }
        });
        PsiMethodCallExpression m = x.get();
        return Integer.parseInt(step.arguments(m).get(0).getText());
    }

    /**
     * @param s constraint statement.
     * @return "is" if the(index).is(constraint) and "isNot" if the(index).isNot(constraint).
     */
    private Constraint.ConstraintType extractConstraintType(PsiStatement s) {
        PsiMethodCallExpression method = az.methodCallExpression(s.getFirstChild());
        String constraintName = method.getMethodExpression().getReferenceName();
        try {
            return Constraint.ConstraintType.valueOf(constraintName.toUpperCase());
        } catch (Exception ignore) {
        }
        return Constraint.ConstraintType.SPECIFIC;
    }

    /**
     * @param s the statement of the constraint. For example: the(booleanExpression(3)).isNot(!booleanExpression(4))
     * @return the body of the lambda expression that defines the constraint. The previous example will return !booleanExpression(4).
     */
    private PsiElement getLambdaExpressionBody(PsiStatement s) {
        Wrapper<PsiLambdaExpression> l = new Wrapper<>();
        s.accept(new JavaRecursiveElementVisitor() {

            @Override
            public void visitLambdaExpression(PsiLambdaExpression expression) {
                super.visitLambdaExpression(expression);
                l.set(expression);
            }
        });
        return l.get().getBody();
    }

    /**
     * @param s the statement of the constraint. For example: the(booleanExpression(3)).isNot(!booleanExpression(4)).ofType(PsiExpression.class)
     * @return the type inside "ofType" expression. The previous example will return Optional(PsiExpression.class).
     * if no "ofType" expression is found, Optional.empty() is returned.
     */
    private Optional<Class<? extends PsiElement>> getTypeOf(PsiStatement s) {
        Wrapper<Optional<Class<? extends PsiElement>>> wq = new Wrapper<>(Optional.empty());
        s.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                super.visitMethodCallExpression(expression);
                if (expression.getMethodExpression().getText().endsWith("ofType")) {
                    wq.set(Optional.of(getPsiClass(expression.getArgumentList().getExpressions()[0].getText().replace(".class", ""))));
                }
            }
        });
        return wq.get();
    }

    /**
     * TODO assuming constraints has only one tree
     * Searches for all the structural constraints found in the "constraints" method and returns a mapping between
     * element ids and their structural constraints. For example, if the following is present in the constraints method:
     * <p>
     * <code>element(1).is(() -> return null;);</code>
     * <p>
     * It will search for the element with id 1, and put a structural constraint on it, requiring it look like:
     * <p>
     * <code>return null;</code>.
     *
     * @return a mapping between the ID of generic elements to a list of all the constraint that apply on them.
     */
    private Map<Integer, List<Constraint>> getConstraints() {
        Map<Integer, List<Constraint>> map = new HashMap<>();
        PsiMethod constrainsMethod = getInterfaceMethod("constraints");
        if (!haz.body(constrainsMethod)) {
            return map;
        }
        Arrays.stream(constrainsMethod.getBody().getStatements()).forEach(s -> {
            Integer elementId = extractIdFromConstraint(s);
            Constraint.ConstraintType constraintType = extractConstraintType(s);

            if (constraintType == Constraint.ConstraintType.IS || constraintType == Constraint.ConstraintType.ISNOT) {
                PsiElement y = getLambdaExpressionBody(s);
                Optional<Class<? extends PsiElement>> q = getTypeOf(s);
                y = q.isPresent() ? getRealRootByType(y, q.get()) : y;
                giveIdToStubElements(y);
                map.putIfAbsent(elementId, new LinkedList<>());
                List<Encapsulator> l = new LinkedList<>();
                l.add(Pruning.prune(Encapsulator.buildTreeFromPsi(y), map));
                map.get(elementId).add(new Matcher.StructuralConstraint(constraintType, l));
            } else {
                PsiMethodCallExpression method = az.methodCallExpression(s.getFirstChild());
                List<Object> arguments = step.arguments(method).stream().map(e -> az.literal(e).getValue()).collect(Collectors.toList());
                map.putIfAbsent(elementId, new LinkedList<>());
                map.get(elementId).add(new Matcher.NonStructuralConstraint(method.getMethodExpression().getReferenceName(), arguments.toArray()));
            }
        });
        return map;
    }

    /**
     * @param s a string representing Psi element class. For example "PsiIfStatement".
     * @return a class object of the received class.
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
     * @param element         the root of the template.
     * @param rootElementType the type of the inner element to extract
     * @return the inner element derived by its type
     */
    private PsiElement getRealRootByType(PsiElement element, Class<? extends PsiElement> rootElementType) {
        Wrapper<PsiElement> result = new Wrapper<>();
        Wrapper<Boolean> stop = new Wrapper<>(false);
        element.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement element) {
                super.visitElement(element);
                if (!stop.get() && iz.ofType(element, rootElementType)) {
                    result.set(element);
                    stop.set(true);
                }
            }
        });
        return result.get();
    }

    /**
     * @param name the name of the method of the interface LeonidasTipperDefinition.
     * @return the body of the method
     */
    private PsiMethod getInterfaceMethod(String name) {
        Wrapper<PsiMethod> x = new Wrapper<>();
        file.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethod(PsiMethod method) {
                super.visitMethod(method);
                if (method.getName().equals(name)) {
                    x.set(method);
                }
            }
        });
        return x.get();
    }

    /**
     * @param name    the name of the tipper
     * @param content the definition file of the tipper
     * @return PsiFile representing the file of the tipper
     */
    private PsiJavaFile getPsiTreeFromString(String name, String content) {
        Language language = JavaLanguage.INSTANCE;
        LightVirtualFile virtualFile = new LightVirtualFile(name, language, content);
        SingleRootFileViewProvider.doNotCheckFileSizeLimit(virtualFile);
        final FileViewProviderFactory factory = LanguageFileViewProviders.INSTANCE.forLanguage(language);
        FileViewProvider viewProvider = factory != null ? factory.createFileViewProvider(virtualFile, language, Utils.getPsiManager(Utils.getProject()), true) : null;
        if (viewProvider == null)
            viewProvider = new SingleRootFileViewProvider(
                    Utils.getPsiManager(ProjectManager.getInstance().getDefaultProject()),
                    virtualFile,
                    true
            );
        language = viewProvider.getBaseLanguage();
        final ParserDefinition parserDefinition = LanguageParserDefinitions.INSTANCE.forLanguage(language);
        if (parserDefinition != null) {
            return (PsiJavaFile) viewProvider.getPsi(language);
        }
        return null;
    }
}
