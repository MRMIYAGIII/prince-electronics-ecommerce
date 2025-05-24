package com.ecommerce.beta.configuration;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;

public class CustomBigDecimalEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.trim().isEmpty()) {
            setValue(null);
            return;
        }
        String cleanedText = text.replace(",", "").trim();
        try {
            setValue(new BigDecimal(cleanedText));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + text, e);
        }
    }
}