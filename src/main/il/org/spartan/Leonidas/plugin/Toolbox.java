package il.org.spartan.Leonidas.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.type;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericEncapsulator;
import il.org.spartan.Leonidas.plugin.tippers.*;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;
import il.org.spartan.Leonidas.plugin.utils.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isAbstract;

/**
 * @author Oren Afek, michalcohen, Amir Sagiv
 * @since 01-12-2016
 */
public class Toolbox implements ApplicationComponent {


    private static final Logger logger = new Logger(Toolbox.class);
    private final Map<Class<? extends PsiElement>, List<Tipper>> tipperMap = new ConcurrentHashMap<>();
    private final Set<VirtualFile> excludedFiles = new HashSet<>();
    private final Set<Class<? extends PsiElement>> operableTypes = new HashSet<>();
    public boolean playground = false;
    public boolean replaced = false;
    private Map<Class<? extends PsiElement>, List<Tipper>> allTipperMap = new ConcurrentHashMap<>();
    private List<GenericEncapsulator> blocks = new ArrayList<>();

    public static Toolbox getInstance() {
        return (Toolbox) ApplicationManager.getApplication().getComponent(Toolbox.auxGetComponentName());
    }

    private static String auxGetComponentName() {
        return Toolbox.class.getSimpleName();
    }

    public List<Tipper> getAllTippers() {
        List<Tipper> list = new ArrayList<>();
        this.allTipperMap.values().forEach(element -> element.forEach(list::add));
        return list;
    }

    public List<Tipper> getCurrentTippers() {
        List<Tipper> list = new ArrayList<>();
        this.tipperMap.values().forEach(element -> element.forEach(list::add));
        return list;
    }

    private void initializeInstance() {
        this
                .add(new SafeReference())
                .add(new Unless())
                .add(new LambdaExpressionRemoveRedundantCurlyBraces()) //
                .add(new LispLastElement())
                .add(new DefaultsTo())
                .add(new MethodDeclarationRenameSingleParameterToCent())//
                .add(new Delegator());
        initBasicBlocks();
        createLeonidasTippers();
    }

    private void initBasicBlocks() {
        blocks.addAll(getAllSubTypes().stream()
                .filter(c -> !isAbstract(c.getModifiers()))
                .map(c -> {
                    try {
                        Constructor<? extends GenericEncapsulator> cc = c.getDeclaredConstructor();
                        cc.setAccessible(true);
                        return cc.newInstance();
                    } catch (Exception ignored) { /**/ }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(i -> (GenericEncapsulator) i)
                .collect(Collectors.toList()));
    }

    private Set<Class<? extends GenericEncapsulator>> getAllSubTypes() {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks"))));
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);


        Reflections r = new Reflections(GenericEncapsulator.class.getPackage().getName());
        return classes.stream().filter(c -> GenericEncapsulator.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())).map(c -> (Class<? extends GenericEncapsulator>) c).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    public void updateTipperList(List<String> list) {
        this.tipperMap.values().forEach(element -> element.forEach(tipper -> {
            if (!list.contains(tipper.name())) {
                element.remove(tipper);
            }
        }));

        this.allTipperMap.values().forEach(element -> element.forEach(tipper -> {
            if (list.contains(tipper.name())) {
                tipperMap.putIfAbsent(tipper.getPsiClass(), new CopyOnWriteArrayList<>());
                operableTypes.add(tipper.getPsiClass());
                tipperMap.get(tipper.getPsiClass()).add(tipper);
            }
        }));

    }

    private void createLeonidasTippers() {
        (new Reflections(LeonidasTipperDefinition.class)).getSubTypesOf(LeonidasTipperDefinition.class)
                .forEach(c -> {
                    String source = Utils.getSourceCode(c);
                    if (!source.equals(""))
                        add(new LeonidasTipper(c.getSimpleName(), source));
                });
    }

    private Toolbox add(Tipper<? extends PsiElement> t) {
        tipperMap.putIfAbsent(t.getPsiClass(), new CopyOnWriteArrayList<>());
        operableTypes.add(t.getPsiClass());
        tipperMap.get(t.getPsiClass()).add(t);
        allTipperMap.putIfAbsent(t.getPsiClass(), new CopyOnWriteArrayList<>());
        allTipperMap.get(t.getPsiClass()).add(t);
        return this;
    }

    public boolean isElementOfOperableType(PsiElement e) {
        return operableTypes.stream().anyMatch(t -> t.isAssignableFrom(e.getClass()));
    }

    @SuppressWarnings("unchecked")
    public void executeAllTippers(PsiElement e) {
        if (checkExcluded(e.getContainingFile()) || !isElementOfOperableType(e))
            return;
        tipperMap.get(type.of(e)).stream().filter(tipper -> tipper.canTip(e)).findFirst()
                .ifPresent(t -> t.tip(e).go(new PsiRewrite().psiFile(e.getContainingFile()).project(e.getProject())));
    }

    /**
     * Can element by spartanized
     *
     * @param e JD
     * @return true iff there exists a tip that tip.canTip(element) is true
     */
    public boolean canTip(PsiElement e) {
        return (!checkExcluded(e.getContainingFile()) && canTipType(type.of(e)) && tipperMap.get(type.of(e)).stream().anyMatch(tip -> tip.canTip(e)));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Tipper getTipper(PsiElement e) {
        try {
            if (!checkExcluded(e.getContainingFile()) && canTipType(type.of(e)) &&
                    tipperMap.get(type.of(e)).stream().anyMatch(tip -> tip.canTip(e)))
                return tipperMap.get(type.of(e)).stream().filter(tip -> tip.canTip(e)).findFirst().get();
        } catch (Exception ignore) {
        }
        return new NoTip<>();
    }

    public boolean checkExcluded(PsiFile f) {
        return f == null || excludedFiles.contains(f.getVirtualFile());
    }

    public void excludeFile(PsiFile f) {
        excludedFiles.add(f.getVirtualFile());
    }

    public void includeFile(PsiFile f) {
        excludedFiles.remove(f.getVirtualFile());
    }

    private boolean canTipType(Class<? extends PsiElement> c) {
        return tipperMap.keySet().stream().anyMatch(x -> x.equals(c));
    }

    /**
     * Called on INTELLIJ initialization
     */
    @Override
    public void initComponent() {
        initializeInstance();
        logger.info("Initialized toolbox component");
    }

    @Override
    public void disposeComponent() {
        logger.info("Disposed toolbox component");
    }

    @NotNull
    @Override
    public String getComponentName() {
        return auxGetComponentName();
    }

    public List<GenericEncapsulator> getGenericsBasicBlocks() {
        return this.blocks;
    }

    public Optional<GenericEncapsulator> getGeneric(PsiElement e) {
        return getGenericsBasicBlocks().stream().filter(g -> g.conforms(e)).findFirst();
    }
}
