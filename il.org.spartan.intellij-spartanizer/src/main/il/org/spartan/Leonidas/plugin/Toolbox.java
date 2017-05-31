package il.org.spartan.Leonidas.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericEncapsulator;
import il.org.spartan.Leonidas.plugin.tippers.*;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.TipperUnderConstruction;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;
import il.org.spartan.Leonidas.plugin.utils.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isAbstract;

/**
 * @author Oren Afek, michalcohen, Amir Sagiv, Roey Maor
 * @since 01-12-2016
 */
public class Toolbox implements ApplicationComponent {


    private static final Logger logger = new Logger(Toolbox.class);
    private final Map<Class<? extends PsiElement>, List<Tipper>> tipperMap = new ConcurrentHashMap<>();
    private final Set<VirtualFile> excludedFiles = new HashSet<>();
    private final Set<Class<? extends PsiElement>> operableTypes = new HashSet<>();
    public boolean playground = false;
    public boolean testing = true;
    public boolean replaced = false;
    private Map<Class<? extends PsiElement>, List<Tipper>> allTipperMap = new ConcurrentHashMap<>();
    private List<GenericEncapsulator> blocks = new ArrayList<>();
    private List<LeonidasTipperDefinition> tipperInstances = new ArrayList<>();

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
        initializeAllTipperClassesInstances();
    }

    private void initializeAllTipperClassesInstances() {
        (new Reflections(LeonidasTipperDefinition.class)).getSubTypesOf(LeonidasTipperDefinition.class)
                .forEach(c -> {
                    try {
                        tipperInstances.add(c.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

    public List<LeonidasTipperDefinition> getAllTipperInstances(){
        return tipperInstances;
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
        Set<Class<? extends GenericEncapsulator>> allClasses =
                new Reflections(GenericEncapsulator.class.getPackage().getName()).getSubTypesOf(GenericEncapsulator.class);
        //noinspection unchecked
        return allClasses.stream().filter(c -> GenericEncapsulator.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())).map(c -> (Class<? extends GenericEncapsulator>) c).collect(Collectors.toSet());
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
                .stream()
                .filter(c -> !c.isAnnotationPresent(TipperUnderConstruction.class))
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

        tipperMap.get(e.getClass())
                .stream()
                .filter(tipper -> tipper.canTip(e))
                .findFirst()
                .ifPresent(t -> t.tip(e).go(new PsiRewrite().psiFile(e.getContainingFile()).project(e.getProject())));
    }
    /*This should work on any tree!
    * Returns true if the tipper changed anything.
    * false otherwise.
    * */
    public boolean executeSingleTipper(PsiElement e, String tipperName){
        Tipper tipper = getTipperByName(tipperName);
        if(tipper == null) {System.out.println("\nNull tipper!\n"); return false;}
        if(e == null) {System.out.println("\nNull element!\n"); return false;}

        Wrapper<PsiElement> toReplace = new Wrapper<>(null);
        Wrapper<Boolean> modified = new Wrapper<>(false);
        e.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitElement(PsiElement el) {
                super.visitElement(el);
                if(modified.get()){return;}
                if(tipper.canTip(el)){
                    toReplace.set(el);
                    modified.set(true);
                }
            }
        });
        if(!modified.get()) {return false;}
        tipper.tip(toReplace.get()).go(new PsiRewrite().psiFile(e.getContainingFile()).project(e.getProject()));
        return true;
    }

    /**
     * Can element by spartanized
     *
     * @param e JD
     * @return true iff there exists a tip that tip.canTip(element) is true
     */
    public boolean canTip(PsiElement e) {
        return (!checkExcluded(e.getContainingFile()) && canTipType(e.getClass()) && tipperMap.get(e.getClass()).stream().anyMatch(tip -> tip.canTip(e)));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Tipper getTipper(PsiElement e) {
        try {
            if (!checkExcluded(e.getContainingFile()) && canTipType(e.getClass()) &&
                    tipperMap.get(e.getClass()).stream().anyMatch(tip -> tip.canTip(e)))
                return tipperMap.get(e.getClass()).stream().filter(tip -> tip.canTip(e)).findFirst().get();
        } catch (Exception ignore) {
        }
        return new NoTip<>();
    }

    public Tipper getTipperByName(String name){
        Optional<Tipper> res = getAllTippers().stream().filter(tipper -> tipper.name().equals(name)).findFirst();
        if(res.isPresent()){
            return res.get();
        }
        return null;
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
