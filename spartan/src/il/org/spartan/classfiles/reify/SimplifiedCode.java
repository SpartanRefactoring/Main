package il.org.spartan.classfiles.reify;

import java.util.*;

import il.org.spartan.classfiles.reify.OpCode.*;

public class SimplifiedCode {
  static private boolean isRelevant(final Instruction i) {
    switch (i.opCode) {
      case GETSTATIC:
      case PUTSTATIC:
      case GETFIELD:
      case PUTFIELD:
      case INVOKEVIRTUAL:
      case INVOKESPECIAL:
      case INVOKESTATIC:
      case INVOKEINTERFACE:
      case INVOKEDYNAMIC:
      case NEW:
        return true;
      default:
        return false;
    }
  }

  final List<Instruction> instructions = new ArrayList<>();
  int instructionsCount = 0;
  int throwCount = 0;
  private final byte[] codes;

  public SimplifiedCode(final byte[] codes) {
    this.codes = codes;
  }

  public int cyclomaticComplexity() {
    return new CFG(codes).cyclomaticComplexity();
  }

  public List<Instruction> instructions() {
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
    if (instructionsCount != 0)
      return;
    for (final BufferDataInputStream r = new BufferDataInputStream(codes);;) {
      final Instruction i = OpCode.read(r);
      if (i == null)
        return;
      if (i.invalid())
        throw new RuntimeException();
      if (i.opCode == OpCode.ATHROW)
        throwCount++;
      if (isRelevant(i))
        instructions.add(i);
      instructionsCount++;
    }
  }
}
