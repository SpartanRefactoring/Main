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
import il.org.spartan.Leonidas.plugin.leonidas.Leonidas;
import il.org.spartan.Leonidas.plugin.leonidas.Matcher;
import il.org.spartan.Leonidas.plugin.leonidas.Pruning;
import il.org.spartan.Leonidas.plugin.tipping.Tip;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * This class represents a tipper created by the leonidas language.
 *
 * @author Amir Sagiv, Sharon Kuninin, michalcohen
 * @since 26-03-2017.
 */
@SuppressWarnings("ConstantConditions")
public class LeonidasTipper implements Tipper<PsiElement> {

    private static final String LEONIDAS_ANNOTATION_NAME = Leonidas.class.getTypeName();
    private static final String SHORT_LEONIDAS_ANNOTATION_NAME = Leonidas.class.getSimpleName();
    private static final String LEONIDAS_ANNOTATION_VALUE = "value";

    private String description;
    private String name;
    private Matcher matcher;
    private Class<? extends PsiElement> rootType;
    private PsiJavaFile file;

    @SuppressWarnings("ConstantConditions")
    public LeonidasTipper(String tipperName, String fileContent) {
        file = getPsiTreeFromString("Tipper" + tipperName, fileContent);
        assert file != null;
        description = file.getClasses()[0].getDocComment().getText()
                .split("\\n")[1].trim()
                .split("\\*")[1].trim();
        name = tipperName;
        Map<Integer, List<Matcher.Constraint>> map = getStructuralConstraints();
        matcher = new Matcher(getMatcherRootTree(), map);
        injectLogicalConstraints();
        Class<? extends PsiElement> t = getPsiElementTypeFromAnnotation(getInterfaceMethod("matcher"));
        //noinspection unchecked
        rootType = t != null ? t : (Class<? extends PsiElement>) matcher.getRoot().getInner().getClass().getInterfaces()[0];
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
                    replace(node, matcher.extractInfo(node), r);
                }
            }
        };
    }

    @Override
    public Class<? extends PsiElement> getPsiClass() {
        return rootType;
    }

    //<editor-fold desc="Private Methods">

    /**
     * This method replaces the given element by the corresponding tree built by PsiTreeTipperBuilder
     *
     * @param treeToReplace - the given tree that matched the "from" tree.
     * @param r             - Rewrite object
     */
    private void replace(PsiElement treeToReplace, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        PsiElement n = getReplacingTree(m, r);
        r.replace(treeToReplace, n);
    }

    /**
     * @param m the mapping between generic element index to concrete elements of the user.
     * @param r rewrite object
     * @return the template of the replacer with the concrete elements inside it.
     */
    private PsiElement getReplacingTree(Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        Encapsulator rootCopy = getReplacerRootTree();
        if (rootCopy.isGeneric()) return m.get(az.generic(rootCopy).getId()).get(0);
        rootCopy.accept(e -> {
            if (!e.isGeneric()) return;
            GenericEncapsulator ge = az.generic(e);
            ge.replaceByRange(m.get(ge.getId()), r);
        });
        return rootCopy.getInner();
    }

    /**
     * @param x the template method
     * @return extract the first PsiElement of the type described in the Leonidas annotation
     */
    private Class<? extends PsiElement> getPsiElementTypeFromAnnotation(PsiMethod x) {
        return Arrays.stream(x.getModifierList().getAnnotations()) //
                .filter(a -> LEONIDAS_ANNOTATION_NAME.equals(a.getQualifiedName()) //
                        || SHORT_LEONIDAS_ANNOTATION_NAME.equals(a.getQualifiedName())) //
                .map(a -> getAnnotationClass(a.findDeclaredAttributeValue(LEONIDAS_ANNOTATION_VALUE) //
                        .getText().replace(".class", ""))) //
                .findFirst().orElse(null);
    }

    /**
     * @param method          the template method
     * @param rootElementType the type of the first PsiElement in the wanted tree
     * @return the first PsiElement of the type rootElementType
     */
    private PsiElement getTreeFromRoot(PsiMethod method, Class<? extends PsiElement> rootElementType) {
        PsiNewExpression ne = az.newExpression(az.expressionStatement(method.getBody().getStatements()[0]).getExpression());
        PsiElement firstElement;
        if (iz.codeBlock(((PsiLambdaExpression) ne.getArgumentList().getExpressions()[0]).getBody())) {
            firstElement = Utils.getFirstElementInsideBody((PsiCodeBlock) (((PsiLambdaExpression) ne.getArgumentList().getExpressions()[0]).getBody()));
        } else {
            firstElement = ((PsiLambdaExpression) ne.getArgumentList().getExpressions()[0]).getBody();
        }

        if (rootElementType == null)
            return firstElement;
        Wrapper<PsiElement> result = new Wrapper<>();
        Wrapper<Boolean> stop = new Wrapper<>(false);
        method.accept(new JavaRecursiveElementVisitor() {
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
    private Encapsulator getMatcherRootTree() {
        PsiMethod method = getInterfaceMethod("matcher");
        giveIdToStubElements(method);

        return Pruning.prune(Encapsulator.buildTreeFromPsi(getTreeFromRoot(method,
                getPsiElementTypeFromAnnotation(method))));
    }

    /**
     * @return the generic tree representing the "from" template
     */
    private Encapsulator getReplacerRootTree() {
        PsiMethod replacer = (PsiMethod) getInterfaceMethod("replacer").copy();
        giveIdToStubElements(replacer);
        return Pruning.prune(Encapsulator.buildTreeFromPsi(getTreeFromRoot(replacer,
                getPsiElementTypeFromAnnotation(replacer))));
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
    private Matcher.Constraint.ConstraintType extractConstraintType(PsiStatement s) {
        Wrapper<Matcher.Constraint.ConstraintType> constraintType = new Wrapper<>();

        s.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitReferenceExpression(PsiReferenceExpression expression) {
                super.visitReferenceExpression(expression);

                if (expression.getText().endsWith("is")) {
                    constraintType.set(Matcher.Constraint.ConstraintType.IS);
                } else if (expression.getText().endsWith("isNot")) {
                    constraintType.set(Matcher.Constraint.ConstraintType.IS_NOT);
                } else {
                    constraintType.set(Matcher.Constraint.ConstraintType.SPECIFIC);
                }
            }
        });

        return constraintType.get();
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
     * @return a mapping between the ID of a generic element to a list of all the constraint that apply on it.
     */
    private Map<Integer, List<Matcher.Constraint>> getStructuralConstraints() {
        Map<Integer, List<Matcher.Constraint>> map = new HashMap<>();
        PsiMethod constrainsMethod = getInterfaceMethod("constraints");
        if (!haz.body(constrainsMethod)) {
            return map;
        }

        Arrays.stream(constrainsMethod.getBody().getStatements()).forEach(s -> {
            Integer elementId = extractIdFromConstraint(s);
            Matcher.Constraint.ConstraintType constraintType = extractConstraintType(s);

            if (constraintType == Matcher.Constraint.ConstraintType.IS || constraintType == Matcher.Constraint.ConstraintType.IS_NOT) {
                PsiElement y = getLambdaExpressionBody(s);
                Optional<Class<? extends PsiElement>> q = getTypeOf(s);
                y = q.isPresent() ? getRealRootByType(y, q.get()) : y;
                // y - root, key ID
                map.putIfAbsent(elementId, new LinkedList<>());
                giveIdToStubElements(y);
                map.get(elementId).add(new Matcher.Constraint(constraintType, Pruning.prune(Encapsulator.buildTreeFromPsi(y))));
            }
        });
        return map;
    }

    /**
     * Searches for logical constraints inside the "constraints" method in a tipper, and injects the relevant
     * constraints inside the relevant encapsulators.
     */
    private void injectLogicalConstraints() {
        PsiMethod constrainsMethod = getInterfaceMethod("constraints");
        if (!haz.body(constrainsMethod)) {
            return;
        }

        Encapsulator root = matcher.getRoot();

        Arrays.stream(constrainsMethod.getBody().getStatements()).forEach(s -> {
            if (extractConstraintType(s) != Matcher.Constraint.ConstraintType.SPECIFIC) {
                return;
            }

            Encapsulator element = getElementById(root, extractIdFromConstraint(s));
            String constraintName = az.methodCallExpression(s.getFirstChild()).getMethodExpression().getReferenceName();

            Arrays.stream(element.getClass().getDeclaredMethods())
                    .filter(m -> m.getName().equals(constraintName))
                    .forEach(m -> {
                        try {
                            m.invoke(element);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
        });
    }

    /**
     * Returns the encapsulators matching the given id. This is done by starting the search from the root element, and
     * recursively searching for a child with the given id.
     *
     * @param root root element, the search begins with it
     * @param id   id of the element to be searched
     * @return element matching the given id, or <Code>null</Code> if none was found
     */
    private Encapsulator getElementById(Encapsulator root, int id) {
        if (root == null) {
            return null;
        }

        if (root.getId() != null && root.getId().equals(id)) {
            return root;
        }

        for (Encapsulator encapsulator : root.getChildren()) {
            Encapsulator $ = getElementById(encapsulator, id);

            if ($ != null) {
                return $;
            }
        }

        return null;
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
     * @param s the name of the PsiElement inherited class
     * @return the .class of s.
     */
    private Class<? extends PsiElement> getAnnotationClass(String s) {
        try {
            //noinspection unchecked
            return (Class<? extends PsiElement>) Class.forName("com.intellij.psi." + s);
        } catch (ClassNotFoundException ignore) {
        }
        return PsiElement.class;
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
