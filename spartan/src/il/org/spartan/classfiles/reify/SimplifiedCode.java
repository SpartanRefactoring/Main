package il.org.spartan.classfiles.reify;

import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.classfiles.reify.OpCode.*;

public class SimplifiedCode {
  private static boolean isRelevant(@NotNull final Instruction ¢) {
    switch (¢.opCode) {
      case GETFIELD:
      case GETSTATIC:
      case INVOKEDYNAMIC:
      case INVOKEINTERFACE:
      case INVOKESPECIAL:
      case INVOKESTATIC:
      case INVOKEVIRTUAL:
      case NEW:
      case PUTFIELD:
      case PUTSTATIC:
        return true;
      default:
        return false;
    }
  }

  final List<Instruction> instructions = new ArrayList<>();
  int instructionsCount;
  int throwCount;
  private final byte[] codes;

  public SimplifiedCode(final byte[] codes) {
    this.codes = codes;
  }
  public int cyclomaticComplexity() {
    return new CFG(codes).cyclomaticComplexity();
  }
  @NotNull public List<Instruction> instructions() {
    parse();
    return instructions;
  }
  public int instructionsCount() {
    parse();
    return instructionsCount;
  }
  public int throwCount() {
    parse();
    return throwCount;
  }
  private void parse() {
    if (instructionsCount == 0)
      for (@NotNull final BufferDataInputStream r = new BufferDataInputStream(codes);;) {
        @Nullable final Instruction i = OpCode.read(r);
        if (i == null)
          return;
        if (i.invalid())
          throw new RuntimeException();
        if (i.opCode == OpCode.ATHROW)
          ++throwCount;
        if (isRelevant(i))
          instructions.add(i);
        ++instructionsCount;
      }
  }
}
