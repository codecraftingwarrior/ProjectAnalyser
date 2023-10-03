package org.analysis.cli.processor;

import java.util.function.Predicate;

public class NonZeroIntegerInputProcessor extends IntegerInputProcessor {

    @Override
    protected String getMessage() {
        return "Saisir un entier non nul : ";
    }

    @Override
    protected Predicate<String> getValidator() {
        return str -> {
            try {
                return Integer.parseInt(str) != 0;
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
