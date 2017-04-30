package il.org.spartan.Leonidas.plugin.leonidas.GenericPsiTypes;


import com.intellij.ide.util.PsiNavigationSupport;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectCoreUtil;
import com.intellij.openapi.util.Key;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;
import com.intellij.psi.impl.CheckUtil;
import com.intellij.psi.impl.ResolveScopeManager;
import com.intellij.psi.impl.source.tree.JavaElementType;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An abstract class representing generic psi element.
 * @author michalcohen
 * @since 11-01-17
 */
public abstract class GenericPsi extends LeafPsiElement implements PsiJavaToken {

    PsiElement inner;

    protected GenericPsi(PsiElement inner, String text) {
        super(JavaElementType.DUMMY_ELEMENT, text);
        this.inner = inner;
    }

    @Override
    public IElementType getTokenType() {
        return getElementType();
    }

    @Override
    public <T> T getUserData(@NotNull Key<T> k) {
        return inner.getUserData(k);
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> k, @Nullable T value) {
        inner.putUserData(k, value);
    }

    /**
     * Checks if an element conforms with the represented generic type.
     *
     * @param e - the element to be checked
     * @return true iff e is of the generic type
     */
    public abstract boolean generalizes(PsiElement e);

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public PsiFile getContainingFile() {
        return inner.getContainingFile();
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        CheckUtil.checkWritable(this);
        getTreeParent().deleteChildInternal(this);
        invalidate();
    }

    @Override
    public void checkDelete() throws IncorrectOperationException {
        CheckUtil.checkWritable(this);
    }

    @Override
    public void deleteChildRange(PsiElement first, PsiElement last) throws IncorrectOperationException {
        throw new IncorrectOperationException();
    }

    @Override
    public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
        return inner.replace(newElement);
    }

    public String toString() {
        return "PsiElement(" + getElementType().toString() + ")";
    }

    @Override
    public void accept(@NotNull PsiElementVisitor v) {
        v.visitElement(this);
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor p,
                                       @NotNull ResolveState s,
                                       PsiElement lastParent,
                                       @NotNull PsiElement place) {
        return true;
    }

    @Override
    public PsiElement getContext() {
        return getParent();
    }

    @Override
    public PsiElement getNavigationElement() {
        return this;
    }

    @Override
    public PsiElement getOriginalElement() {
        return this;
    }

    @Override
    public boolean isPhysical() {
        PsiFile file = getContainingFile();
        return file != null && file.isPhysical();
    }

    @Override
    @NotNull
    public GlobalSearchScope getResolveScope() {
        return ResolveScopeManager.getElementResolveScope(this);
    }

    @Override
    @NotNull
    public SearchScope getUseScope() {
        return ResolveScopeManager.getElementUseScope(this);
    }

    @Override
    @NotNull
    public Project getProject() {
        Project project = ProjectCoreUtil.theOnlyOpenProject();
        if (project != null)
			return project;
        final PsiManager manager = getManager();
        assert (manager != null);
        return manager.getProject();
    }

    @Override
    @NotNull
    public Language getLanguage() {
        return getElementType().getLanguage();
    }

    @Override
    public ASTNode getNode() {
        return this;
    }

    @Override
    public PsiElement getPsi() {
        return this;
    }

    @Override
    public ItemPresentation getPresentation() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void navigate(boolean requestFocus) {
        final Navigatable descriptor = PsiNavigationSupport.getInstance().getDescriptor(this);
        if (descriptor != null)
			descriptor.navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
        return PsiNavigationSupport.getInstance().canNavigate(this);
    }

    @Override
    public boolean canNavigateToSource() {
        return canNavigate();
    }

    @Override
    public boolean isEquivalentTo(final PsiElement another) {
        return another == this;
    }

    public PsiElement getInner() {
        return inner;
    }
}
