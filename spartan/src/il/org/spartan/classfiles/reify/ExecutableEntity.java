package il.org.spartan.classfiles.reify;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import il.org.spartan.classfiles.reify.ClassInfo.*;
import il.org.spartan.classfiles.reify.ConstantPool.*;
import il.org.spartan.classfiles.reify.OpCode.*;

/** @author Yossi Gil
 * @since 21 November 2011 */
public class ExecutableEntity extends TypedEntity {
   public static String signature(final String className, final String s,  final String d) {
    return className + "." + signature(s, d);
  }
   private static String signature(final String s,  final String d) {
    return s + ":" + decode(d);
  }

   public final ClassConstant[] exceptions;
   CodeEntity code;
  Map<String, int[]> class2refsByComponents;
  Map<String, int[]> class2staticRefsByComponents;

  public ExecutableEntity(final ConstantPool constantPool, final int accessFlags, final String name,  final String descriptor,
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
  public ExecutableEntity( final TypedEntity t) {
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
   public CodeEntity getCode() {
    return code;
  }
   public Set<String> getReferencedMethods() {
     final Set<String> $ = new HashSet<>();
    if (code == null)
      return $;
    for (int index = 0; index < code.simplifiedCode.instructions().size(); ++index) {
      final Instruction i = code.simplifiedCode.instructions().get(index);
      int cpIndex;
      if (!i.isInvokeInstruction())
        continue;
      cpIndex = i.args()[1] | i.args()[0] << 8;
       final MemberReference mr = constantPool.getMemberReference(cpIndex);
      $.add(signature(mr.getClassConstant().getClassName(), mr.getNameAndType().getName(), mr.getNameAndType().getDescriptor()));
    }
    return $;
  }
   public Set<String> instanceVariables() {
     final Set<String> $ = new HashSet<>();
    if (code == null)
      return $;
    for (int index = 0; index < code.simplifiedCode.instructions().size(); ++index) {
      final Instruction i = code.simplifiedCode.instructions().get(index);
      if (i.opCode == OpCode.GETFIELD || i.opCode == OpCode.PUTFIELD) {
         final FieldReference fr = constantPool.getFieldReference(i.args()[1] | i.args()[0] << 8);
        $.add(fr.getClassConstant().getClassName() + ":" + fr.getNameAndType());
      }
    }
    return $;
  }
  public int instructionCount() {
    return code == null ? 0 : code.instructionsCount();
  }
  public boolean isAccessed( final TypedEntity e,  final String thisClassName) {
    if (code == null)
      return false;
    for (int index = 0; index < code.simplifiedCode.instructions().size(); ++index) {
      final Instruction i = code.simplifiedCode.instructions().get(index);
      if (i.isFieldAccessInstruction() && !i.isInvokeInstruction() && isAccessed(e, thisClassName, i))
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
   public String signature() {
    return signature(name, descriptor);
  }
  public int throwCount() {
    return code == null ? 0 : code.throwCount();
  }
  private int[] getClassRefsByComponents(final String className) {
    int[] $ = class2refsByComponents.get(className);
    if ($ != null)
      return $;
    $ = new int[LinkComponents.values().length];
    class2refsByComponents.put(className, $);
    return $;
  }
  private boolean isAccessed( final TypedEntity e,  final String thisClassName,  final Instruction i) {
     final MemberReference $ = constantPool.getMemberReference(i.args()[1] | i.args()[0] << 8);
    return $.getNameAndType().getName().equals(e.name) && $.getNameAndType().getDescriptor().equals(e.descriptor)
        && $.getClassConstant().getClassName().endsWith(thisClassName);
  }
  private CodeEntity readCodeAttribute() {
     final AttributeInfo $ = findAttribute("Code");
    return $ == null ? null : readCodeAttribute($);
  }
   private CodeEntity readCodeAttribute( final AttributeInfo ¢) {
     final ConstantPoolReader $ = ¢.reader(constantPool);
    return new CodeEntity($.readUnsignedShort(), $.readUnsignedShort(), $.readBytesArrray());
  }
   private ClassConstant[] readExceptions() {
     final AttributeInfo $ = findAttribute("Exceptions");
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
          case INVOKEDYNAMIC:
          case INVOKEINTERFACE:
          case INVOKESPECIAL:
          case INVOKEVIRTUAL:
            component = LinkComponents.MethodInvocation.ordinal();
            break;
          case INVOKESTATIC:
            component = LinkComponents.StaticMethodInvocation.ordinal();
            break;
          default:
            continue;
        }
        assert component != -1;
         final MemberReference mr = constantPool.getMemberReference(cpIndex);
        if (!"<init>".equals(mr.getNameAndType().getName()))
          ++getClassRefsByComponents(mr.getClassConstant().getClassName())[component];
        for (final TypeInfo ¢ : decode(mr.getNameAndType().getDescriptor()).components())
          ++getClassRefsByComponents(¢ + "")[component];
      }
  }
}
