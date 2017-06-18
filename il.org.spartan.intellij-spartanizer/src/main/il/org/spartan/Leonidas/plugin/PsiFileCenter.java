package il.org.spartan.Leonidas.plugin;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.Wrapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Used to create PsiFiles from user code and track it after arbitrary changes to the former.
 * Provides Useful operations Used by the automatic testing and playground.
 *
 * @author Roey Maor
 * @since 15-06-2016
 */
public class PsiFileCenter {

    private static String marker = "/*X_CENTER_MARKER*/";
    private static String markerRegex = "/\\*X_CENTER_MARKER\\*/";
    private HashMap<CodeType, String> wrappingPrefixes;
    private HashMap<CodeType, String> wrappingPostfixes;
    public PsiFileCenter() {
        wrappingPrefixes = new HashMap<>();
        wrappingPrefixes.put(CodeType.CLASS_BOUND, "public class XCLASS{\n");
        wrappingPrefixes.put(CodeType.METHOD_BOUND, wrappingPrefixes.get(CodeType.CLASS_BOUND) + "public void XMETHOD(){\n");
        wrappingPrefixes.put(CodeType.EXPRESSION, wrappingPrefixes.get(CodeType.METHOD_BOUND) + "XMETHOD(");
        wrappingPrefixes.put(CodeType.ENUM_BOUND, wrappingPrefixes.get(CodeType.CLASS_BOUND) + "public enum XENUM{\n");
        wrappingPrefixes.put(CodeType.FILE_BOUND, "");

        wrappingPostfixes = new HashMap<>();
        wrappingPostfixes.put(CodeType.CLASS_BOUND, "\n}");
        wrappingPostfixes.put(CodeType.METHOD_BOUND, "\n}" + wrappingPostfixes.get(CodeType.CLASS_BOUND));
        wrappingPostfixes.put(CodeType.EXPRESSION, ");" + wrappingPostfixes.get(CodeType.METHOD_BOUND));
        wrappingPostfixes.put(CodeType.ENUM_BOUND, "\n}" + wrappingPostfixes.get(CodeType.CLASS_BOUND));
        wrappingPostfixes.put(CodeType.FILE_BOUND, "");
    }

    public PsiFileWrapper createFileFromString(String s) {
        if (s == null) {
            return null;
        }
        List<CodeType> codeTypesByGenerality = Arrays.asList(CodeType.FILE_BOUND, CodeType.CLASS_BOUND, CodeType.METHOD_BOUND, CodeType.EXPRESSION, CodeType.ENUM_BOUND);
        PsiFile file;
        for (CodeType type : codeTypesByGenerality) {
            String currText = wrappingPrefixes.get(type) + marker + "\n\n" + s + "\n\n" + marker + wrappingPostfixes.get(type);
            file = PsiFileFactory.getInstance(Utils.getProject())
                    .createFileFromText(JavaLanguage.INSTANCE, currText);
            Wrapper<Boolean> isValid = new Wrapper(true);
            file.accept(new JavaRecursiveElementVisitor() {
                @Override
                public void visitErrorElement(PsiErrorElement element) {
                    isValid.set(false);
                    super.visitErrorElement(element);
                }
            });
            if (isValid.get()) {
                return new PsiFileWrapper(file, type);
            }
        }

        return new PsiFileWrapper(null, CodeType.ILLEGAL);
    }

    public enum CodeType {
        EXPRESSION, METHOD_BOUND, CLASS_BOUND, ENUM_BOUND, FILE_BOUND, ILLEGAL
    }

    /*
     * This is a PsiFile, that it's sole purpose is to hold within it some
     * sub-tree that was given by the user.
     */
    public class PsiFileWrapper {

        private CodeType codeType;
        private PsiFile file;

        public PsiFileWrapper(PsiFile file, CodeType type) {
            this.file = file;
            this.codeType = type;
        }

        public PsiFile getFile() {
            return this.file;
        }

        public CodeType getCodeType() {
            return this.codeType;
        }

        public String extractCanonicalSubtreeString() {
            String raw = extractRelevantSubtreeString();
            raw = raw.replaceAll("\t", " ");
            raw = raw.trim().replaceAll(" +", " ");
            raw = raw.replaceAll(" ,", ",");
            raw = raw.replaceAll(", ", ",");
            raw = raw.replaceAll("\n+", "\n");
            return raw;
        }

        private String extractRelevantSubtreeString() {
            return (file.getText().split(PsiFileCenter.markerRegex))[1];
        }
    }
}
