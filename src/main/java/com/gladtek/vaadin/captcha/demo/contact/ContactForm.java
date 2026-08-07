package com.gladtek.vaadin.captcha.demo.contact;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;

/**
 * A required-fields contact form: the captcha field, name, and email. The captcha is
 * bound into the same {@link Binder} as any other field - since it implements
 * {@code HasValue<String>} and {@code HasValidation}, the required "Captcha is required"
 * error appears and clears exactly the way it does for Name/Email, with no extra wiring.
 */
public class ContactForm<C extends Component & HasValue<?, String>> extends FormLayout {

    private final TextField nameField = new TextField("Name");
    private final EmailField emailField = new EmailField("Email");
    private final Binder<ContactFormBean> binder = new Binder<>(ContactFormBean.class);

    public ContactForm(C captcha) {
        setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        setMaxWidth("500px");
        nameField.setWidthFull();
        emailField.setWidthFull();
        nameField.setRequiredIndicatorVisible(true);
        emailField.setRequiredIndicatorVisible(true);

        binder.forField(nameField)
                .asRequired("Name is required")
                .bind(ContactFormBean::getName, ContactFormBean::setName);
        binder.forField(emailField)
                .asRequired("Email is required")
                .withValidator(new EmailValidator("Enter a valid email address"))
                .withValidator(new WorkEmailValidator())
                .bind(ContactFormBean::getEmail, ContactFormBean::setEmail);
        binder.setBean(new ContactFormBean());

        binder.forField(captcha)
                .asRequired("Captcha is required")
                .bind(ContactFormBean::getCaptchaToken, ContactFormBean::setCaptchaToken);

        add(nameField, emailField, captcha);
    }

    /** Validates the whole form, triggering the same red errors as an unfilled TextField. */
    public boolean isValid() {
        return !binder.validate().hasErrors();
    }
}
