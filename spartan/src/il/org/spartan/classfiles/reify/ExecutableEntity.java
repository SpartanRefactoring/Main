package il.org.spartan.classfiles.reify;

import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.classfiles.reify.ClassInfo.*;
import il.org.spartan.classfiles.reify.ConstantPool.*;
import il.org.spartan.classfiles.reify.OpCode.*;

/** @author Yossi Gil
 * @since 21 November 2011 */
public class ExecutableEntity extends TypedEntity {
  @NotNull public static String signature(final String className, final String s, @NotNull final String d) {
    return className + "." + signature(s, d);
  }

  @Nullable private static String signature(final String s, @NotNull final String d) {
    return s + ":" + decode(d);
  }

  @NotNull public final ClassConstant[] exceptions;
  @Nullable CodeEntity code;
  Map<String, int[]> class2refsByComponents;
  Map<String, int[]> class2staticRefsByComponents;

  public ExecutableEntity(final ConstantPool constantPool, final int accessFlags, final String name, @NotNull final String descriptor,
      final AttributeInfo[] attributes) {
    super(constantPool, accessFlags, name, descriptor, attributes);
    exceptions = readExceptions();
    code = readCodeAttribute();
  }

  public ExecutableEntity(final ConstantPool constantPool, final int flags, final String name, final TypeInfo t, final String descriptor,
      final AttributeInfo[] attributes) {
    super(constantPool, flags, name, t, descriptor, attributes);
    exceptions = readExceptions();
  }

  public ExecutableEntity(@NotNull final TypedEntity t) {
    super(t.constantPool, t.flags, t.name, t.descriptor, t.attributes);
    exceptions = readExceptions();
    code = readCodeAttribute();
  }

  public int codeSize() {
    return code == null ? 0 : code.codes.length;
  }

  public int cyclomaticComplexity() {
    return code == null ? 0 : code.cyclomaticComplexity();
  }

  @Nullable public CodeEntity getCode() {
    return code;
  }

  @NotNull public Set<String> getReferencedMethods() {
    @NotNull final Set<String> $ = new HashSet<>();
    if (code == null)
      return $;
    for (int index = 0; index < code.simplifiedCode.instructions().size(); ++index) {
      final Instruction i = code.simplifiedCode.instructions().get(index);
      int cpIndex;
      if (!i.isInvokeInstruction())
        continue;
      cpIndex = i.args()[1] | i.args()[0] << 8;
      @NotNull final MemberReference mr = constantPool.getMemberReference(cpIndex);
      $.add(signature(mr.getClassConstant().getClassName(), mr.getNameAndType().getName(), mr.getNameAndType().getDescriptor()));
    }
    return $;
  }

  @NotNull public Set<String> instanceVariables() {
    @NotNull final Set<String> $ = new HashSet<>();
    if (code == null)
      return $;
    for (int index = 0; index < code.simplifiedCode.instructions().size(); ++index) {
      final Instruction i = code.simplifiedCode.instructions().get(index);
      if (i.opCode == OpCode.GETFIELD || i.opCode == OpCode.PUTFIELD) {
        final int cpIndex = i.args()[1] | i.args()[0] << 8;
        @NotNull final FieldReference fr = constantPool.getFieldReference(cpIndex);
        $.add(fr.getClassConstant().getClassName() + ":" + fr.getNameAndType());
      }
    }
    return $;
  }

  public int instructionCount() {
    return code == null ? 0 : code.instructionsCount();
  }

  public boolean isAccessed(@NotNull final TypedEntity e, @NotNull final String thisClassName) {
    if (code == null)
      return false;
    for (int index = 0; index < code.simplifiedCode.instructions().size(); ++index) {
      final Instruction i = code.simplifiedCode.instructions().get(index);
      if (!i.isFieldAccessInstruction() || i.isInvokeInstruction())
        continue;
      if (isAccessed(e, thisClassName, i))
        return true;
    }
    return false;
  }

  @Attribute public int referencedFieldsCount() {
    return instanceVariables().size();
  }

  public Map<String, int[]> referencesToAllClasses() {
    if (class2refsByComponents == null)
      referencesToClasses();
    return class2refsByComponents;
  }

  public int[] referencesToClass(final String className) {
    if (class2refsByComponents == null)
      referencesToClasses();
    return class2refsByComponents.get(className);
  }

  @Nullable public String signature() {
    return signature(name, descriptor);
  }

  public int throwCount() {
    return code == null ? 0 : code.throwCount();
  }

  private int[] getClassRefsByComponents(final String className) {
    int[] $ = class2refsByComponents.get(className);
    if ($ == null) {
      $ = new int[LinkComponents.values().length];
      class2refsByComponents.put(className, $);
    }
    return $;
  }

  private boolean isAccessed(@NotNull final TypedEntity e, @NotNull final String thisClassName, @NotNull final Instruction i) {
    final int cpIndex = i.args()[1] | i.args()[0] << 8;
    @NotNull final MemberReference mr = constantPool.getMemberReference(cpIndex);
    return mr.getNameAndType().getName().equals(e.name) && mr.getNameAndType().getDescriptor().equals(e.descriptor)
        && mr.getClassConstant().getClassName().endsWith(thisClassName);
  }

  private CodeEntity readCodeAttribute() {
    @Nullable final AttributeInfo $ = findAttribute("Code");
    return $ == null ? null : readCodeAttribute($);
  }

  @NotNull private CodeEntity readCodeAttribute(@NotNull final AttributeInfo i) {
    @NotNull final ConstantPoolReader r = i.reader(constantPool);
    return new CodeEntity(r.readUnsignedShort(), r.readUnsignedShort(), r.readBytesArrray());
  }

  @NotNull private ClassConstant[] readExceptions() {
    @Nullable final AttributeInfo $ = findAttribute("Exceptions");
    return $ == null ? new ClassConstant[0] : $.reader(constantPool).readClasses();
  }

  private void referencesToClasses() {
    class2refsByComponents = new HashMap<>();
    for (final TypeInfo ¢ : type.components())
      ++getClassRefsByComponents(¢ + "")[LinkComponents.MethodDeclaration.ordinal()];
    if (code != null)
      for (int index = 0; index < code.simplifiedCode.instructions().size(); ++index) {
        final Instruction i = code.simplifiedCode.instructions().get(index);
        final int cpIndex = i.args()[1] | i.args()[0] << 8;
        int component = -1;
        switch (i.opCode) {
          case NEW:
            final int[] refsByComponents = getClassRefsByComponents(constantPool.getClassName(cpIndex));
            ++refsByComponents[LinkComponents.Instantiation.ordinal()];
            continue;
          case GETSTATIC:
          case PUTSTATIC:
            component = LinkComponents.StaticFieldAccess.ordinal();
            break;
          case GETFIELD:
          case PUTFIELD:
            component = LinkComponents.FieldAccess.ordinal();
            break;
          case INVOKEVIRTUAL:
          case INVOKESPECIAL:
          case INVOKEINTERFACE:
          case INVOKEDYNAMIC:
            component = LinkComponents.MethodInvocation.ordinal();
            break;
          case INVOKESTATIC:
            component = LinkComponents.StaticMethodInvocation.ordinal();
            break;
          default:
            continue;
        }
        assert component != -1;
        @NotNull final MemberReference mr = constantPool.getMemberReference(cpIndex);
        if (!"<init>".equals(mr.getNameAndType().getName()))
          ++getClassRefsByComponents(mr.getClassConstant().getClassName())[component];
        for (final TypeInfo ¢ : decode(mr.getNameAndType().getDescriptor()).components())
          ++getClassRefsByComponents(¢ + "")[component];
      }
  }
}
