package com.gladtek.vaadin.captcha.demo.contact;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

import java.util.Locale;
import java.util.Set;

/**
 * Rejects addresses from well-known free/consumer email providers, requiring a work email.
 */
public class WorkEmailValidator implements Validator<String> {

    private static final Set<String> FREE_EMAIL_DOMAINS = Set.of(
            "gmail.com", "googlemail.com", "yahoo.com", "yahoo.co.uk", "ymail.com",
            "outlook.com", "hotmail.com", "hotmail.co.uk", "live.com", "msn.com",
            "icloud.com", "me.com", "mac.com", "aol.com", "protonmail.com", "proton.me",
            "gmx.com", "gmx.net", "zoho.com", "mail.com", "yandex.com", "yandex.ru");

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        if (value == null || value.isBlank()) {
            return ValidationResult.ok();
        }

        String domain = extractDomain(value);
        if (domain != null && FREE_EMAIL_DOMAINS.contains(domain)) {
            return ValidationResult.error("Please use your work email address, not a personal one");
        }

        return ValidationResult.ok();
    }

    private static String extractDomain(String email) {
        int at = email.lastIndexOf('@');
        if (at < 0 || at == email.length() - 1) {
            return null;
        }
        return email.substring(at + 1).toLowerCase(Locale.ROOT);
    }
}
