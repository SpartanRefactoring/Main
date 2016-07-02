package il.org.spartan.refactoring.preferences;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

@SuppressWarnings("javadoc") public interface Kind {
  public interface RENAME_RETURN_VARIABLE extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.RENAME_RETURN_VARIABLE;
    }
  }

  interface SWITCH_IF_CONVERTION extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.SWITCH_IF_CONVERTION;
    }
  }

  interface ConsolidateStatements extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
    };
  }

  interface DiscardRedundant extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.DISCARD_METHOD_INVOCATION;
    }
  }

  interface InlineVariable extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.ELIMINATE_TEMP;
    }
  }

  interface OPTIMIZE_ANNOTATIONS extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.OPTIMIZE_ANNOTATIONS;
    }
  }

  interface REMOVE_REDUNDANT_PUNCTUATION extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.REMOVE_REDUNDANT_PUNCTUATION;
    }
  }

  interface RENAME_PARAMETERS extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.RENAME_PARAMETERS;
    }
  }

  interface ReorganizeExpression extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.REORDER_EXPRESSIONS;
    }
  }

  interface REPLACE_CLASS_INSTANCE_CREATION extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.REPLACE_CLASS_INSTANCE_CREATION;
    }
  }

  interface Simplify extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.REFACTOR_INEFFECTIVE;
    }
  }

  interface SIMPLIFY_NESTED_BLOCK extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.SIMPLIFY_NESTED_BLOCKS;
    }
  }

  interface Ternarize extends Kind {
    @Override default WringGroup kind() {
      return WringGroup.Ternarize;
    }
  }

  /**
   * Returns the preference group to which the wring belongs to. This method
   * should be overridden for each wring and should return one of the values of
   * {@link WringGroup}
   *
   * @return the preference group this wring belongs to
   */
  abstract WringGroup kind();
}