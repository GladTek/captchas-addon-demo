package com.gladtek.vaadin.captcha.demo.turnstile;

import com.gladtek.vaadin.captcha.demo.contact.ContactForm;
import com.gladtek.vaadin.captcha.demo.contact.StatusMessages;
import com.gladtek.vaadin.captchas.core.CaptchaTheme;
import com.gladtek.vaadin.captchas.core.VerificationResult;
import com.gladtek.vaadin.captchas.turnstile.Turnstile;
import com.gladtek.vaadin.captchas.turnstile.TurnstileAppearance;
import com.gladtek.vaadin.captchas.turnstile.TurnstileSize;
import com.gladtek.vaadin.captchas.turnstile.TurnstileVerifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Value;

@Route("turnstile")
public class TurnstileView extends VerticalLayout {

    private final String siteKey;
    private final String secret;
    private final Div formContainer = new Div();

    private CaptchaTheme theme = CaptchaTheme.AUTO;
    private TurnstileSize size = TurnstileSize.NORMAL;
    private TurnstileAppearance appearance = TurnstileAppearance.ALWAYS;

    public TurnstileView(@Value("${captcha.turnstile.site-key}") String siteKey,
            @Value("${captcha.turnstile.secret}") String secret) {
        this.siteKey = siteKey;
        this.secret = secret;
        setSizeFull();
        setPadding(false);

        VerticalLayout content = new VerticalLayout(new H1("Cloudflare Turnstile"), formContainer);
        content.setPadding(true);

        RadioButtonGroup<CaptchaTheme> themeSelector = new RadioButtonGroup<>();
        themeSelector.setLabel("Widget theme");
        themeSelector.setItems(CaptchaTheme.values());
        themeSelector.setValue(theme);
        themeSelector.setHelperText("Applies on next render");
        themeSelector.addValueChangeListener(event -> {
            theme = event.getValue();
            buildForm();
        });

        RadioButtonGroup<TurnstileSize> sizeSelector = new RadioButtonGroup<>();
        sizeSelector.setLabel("Widget size");
        sizeSelector.setItems(TurnstileSize.values());
        sizeSelector.setValue(size);
        sizeSelector.setHelperText("Applies on next render");
        sizeSelector.addValueChangeListener(event -> {
            size = event.getValue();
            buildForm();
        });

        RadioButtonGroup<TurnstileAppearance> appearanceSelector = new RadioButtonGroup<>();
        appearanceSelector.setLabel("Widget appearance");
        appearanceSelector.setItems(TurnstileAppearance.values());
        appearanceSelector.setValue(appearance);
        appearanceSelector.setHelperText("Applies on next render");
        appearanceSelector.addValueChangeListener(event -> {
            appearance = event.getValue();
            buildForm();
        });

        VerticalLayout optionsPanel = new VerticalLayout(new H3("Options"), themeSelector, sizeSelector,
                appearanceSelector);
        optionsPanel.setWidth("280px");
        optionsPanel.setHeightFull();
        optionsPanel.getStyle().set("border-inline-start", "1px solid var(--vaadin-border-color)");

        HorizontalLayout mainRow = new HorizontalLayout(content, optionsPanel);
        mainRow.setSizeFull();
        mainRow.setSpacing(false);
        mainRow.setFlexGrow(1, content);
        mainRow.setFlexShrink(0, optionsPanel);

        add(mainRow);
        buildForm();
    }

    private void buildForm() {
        formContainer.removeAll();

        Turnstile captcha = new Turnstile(siteKey);
        captcha.setTheme(theme);
        captcha.setSize(size);
        captcha.setAppearance(appearance);
        ContactForm<Turnstile> form = new ContactForm<>(captcha);
        Paragraph status = new Paragraph();
        Button submit = new Button("Submit");

        captcha.addTokenListener(event -> {
            VerificationResult result = new TurnstileVerifier(secret).verify(event.getToken());
            if (result.success()) {
                StatusMessages.ok(status, "Captcha verified.");
            } else {
                StatusMessages.error(status, "Captcha verification failed: " + result.errorCodes());
            }
        });

        submit.addClickListener(event -> {
            if (!form.isValid()) {
                return;
            }
            StatusMessages.ok(status, "Form submitted!");
            Notification.show("Form submitted!");
        });

        formContainer.add(form, status, submit);
    }
}
