package com.gladtek.vaadin.captcha.demo.friendlycaptcha;

import com.gladtek.vaadin.captcha.demo.contact.ContactForm;
import com.gladtek.vaadin.captcha.demo.contact.StatusMessages;
import com.gladtek.vaadin.captchas.core.CaptchaTheme;
import com.gladtek.vaadin.captchas.core.VerificationResult;
import com.gladtek.vaadin.captchas.friendlycaptcha.FriendlyCaptcha;
import com.gladtek.vaadin.captchas.friendlycaptcha.FriendlyCaptchaStartMode;
import com.gladtek.vaadin.captchas.friendlycaptcha.FriendlyCaptchaVerifier;
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

@Route("friendly-captcha-submit")
public class FriendlyCaptchaSubmitView extends VerticalLayout {

    private final String siteKey;
    private final String secret;
    private final Div formContainer = new Div();

    private CaptchaTheme theme = CaptchaTheme.AUTO;
    private FriendlyCaptchaStartMode startMode = FriendlyCaptchaStartMode.FOCUS;

    public FriendlyCaptchaSubmitView(@Value("${captcha.friendlycaptcha.site-key}") String siteKey,
            @Value("${captcha.friendlycaptcha.secret}") String secret) {
        this.siteKey = siteKey;
        this.secret = secret;
        setSizeFull();
        setPadding(false);

        VerticalLayout content = new VerticalLayout(new H1("Friendly Captcha - verify on submit"), formContainer);
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

        RadioButtonGroup<FriendlyCaptchaStartMode> startModeSelector = new RadioButtonGroup<>();
        startModeSelector.setLabel("Start mode");
        startModeSelector.setItems(FriendlyCaptchaStartMode.values());
        startModeSelector.setValue(startMode);
        startModeSelector.setHelperText("Applies on next render");
        startModeSelector.addValueChangeListener(event -> {
            startMode = event.getValue();
            buildForm();
        });

        VerticalLayout optionsPanel = new VerticalLayout(new H3("Options"), themeSelector, startModeSelector);
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

        FriendlyCaptcha captcha = new FriendlyCaptcha(siteKey);
        captcha.setTheme(theme);
        captcha.setStartMode(startMode);
        ContactForm<FriendlyCaptcha> form = new ContactForm<>(captcha);
        Paragraph status = new Paragraph();
        Button submit = new Button("Submit");

        submit.addClickListener(event -> {
            if (!form.isValid()) {
                return;
            }
            VerificationResult result = new FriendlyCaptchaVerifier(secret, siteKey).verify(captcha.getValue());
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
