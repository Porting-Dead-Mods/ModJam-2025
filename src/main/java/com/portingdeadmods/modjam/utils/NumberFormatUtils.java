package com.portingdeadmods.modjam.utils;

import java.text.NumberFormat;

public class NumberFormatUtils {
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getIntegerInstance();

    public static String formatEnergy(int value) {
        if (value >= 1_000_000) {
            return String.format("%.1fM", value / 1_000_000.0);
        } else if (value >= 1_000) {
            return String.format("%.1fK", value / 1_000.0);
        } else {
            return String.valueOf(value);
        }
    }

    public static String formatFluid(int value) {
        return NUMBER_FORMAT.format(value);
    }
}
