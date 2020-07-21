package il.org.spartan.classfiles.reify;

import java.io.*;
import java.util.*;

import fluent.ly.*;
import il.org.spartan.classfiles.reify.OpCode.*;

public class SimplifiedCode {
  private static boolean isRelevant(final Instruction ¢) {
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
    if (instructionsCount == 0)
      try (BufferDataInputStream r = new BufferDataInputStream(codes)) {
        for (;;) {
          final Instruction i = OpCode.read(r);
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
      } catch (IOException e) {
        note.bug(e);
      }
  }
}
