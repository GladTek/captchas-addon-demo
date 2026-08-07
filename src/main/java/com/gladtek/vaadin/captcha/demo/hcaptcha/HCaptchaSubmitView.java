package com.gladtek.vaadin.captcha.demo.hcaptcha;

import com.gladtek.vaadin.captcha.demo.contact.ContactForm;
import com.gladtek.vaadin.captcha.demo.contact.StatusMessages;
import com.gladtek.vaadin.captchas.core.CaptchaTheme;
import com.gladtek.vaadin.captchas.core.VerificationResult;
import com.gladtek.vaadin.captchas.hcaptcha.HCaptcha;
import com.gladtek.vaadin.captchas.hcaptcha.HCaptchaSize;
import com.gladtek.vaadin.captchas.hcaptcha.HCaptchaVerifier;
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

@Route("hcaptcha-submit")
public class HCaptchaSubmitView extends VerticalLayout {

    private final String siteKey;
    private final String secret;
    private final Div formContainer = new Div();

    private CaptchaTheme theme = CaptchaTheme.LIGHT;
    private HCaptchaSize size = HCaptchaSize.NORMAL;

    public HCaptchaSubmitView(@Value("${captcha.hcaptcha.site-key}") String siteKey,
            @Value("${captcha.hcaptcha.secret}") String secret) {
        this.siteKey = siteKey;
        this.secret = secret;
        setSizeFull();
        setPadding(false);

        VerticalLayout content = new VerticalLayout(new H1("hCaptcha - verify on submit"), formContainer);
        content.setPadding(true);

        RadioButtonGroup<CaptchaTheme> themeSelector = new RadioButtonGroup<>();
        themeSelector.setLabel("Widget theme");
        themeSelector.setItems(CaptchaTheme.LIGHT, CaptchaTheme.DARK);
        themeSelector.setValue(theme);
        themeSelector.setHelperText("Applies on next render");
        themeSelector.addValueChangeListener(event -> {
            theme = event.getValue();
            buildForm();
        });

        RadioButtonGroup<HCaptchaSize> sizeSelector = new RadioButtonGroup<>();
        sizeSelector.setLabel("Widget size");
        sizeSelector.setItems(HCaptchaSize.values());
        sizeSelector.setValue(size);
        sizeSelector.setHelperText("Applies on next render");
        sizeSelector.addValueChangeListener(event -> {
            size = event.getValue();
            buildForm();
        });

        VerticalLayout optionsPanel = new VerticalLayout(new H3("Options"), themeSelector, sizeSelector);
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

        HCaptcha captcha = new HCaptcha(siteKey);
        captcha.setTheme(theme);
        captcha.setSize(size);
        ContactForm<HCaptcha> form = new ContactForm<>(captcha);
        Paragraph status = new Paragraph();
        Button submit = new Button("Submit");

        submit.addClickListener(event -> {
            if (!form.isValid()) {
                return;
            }
            VerificationResult result = new HCaptchaVerifier(secret).verify(captcha.getValue());
            if (result.success()) {
                StatusMessages.ok(status, "Verified on submit - form accepted.");
                Notification.show("Form submitted!");
            } else {
                StatusMessages.error(status, "Verification failed: " + result.errorCodes());
                captcha.reset();
            }
        });

        formContainer.add(form, status, submit);
    }
}
