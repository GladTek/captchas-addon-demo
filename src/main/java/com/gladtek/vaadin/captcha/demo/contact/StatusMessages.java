package com.gladtek.vaadin.captcha.demo.contact;

import com.vaadin.flow.component.html.Paragraph;

/** Small helper for showing red/normal status text consistently across the demo views. */
public final class StatusMessages {

    private StatusMessages() {
    }

    public static void ok(Paragraph status, String message) {
        status.setText(message);
        status.getStyle().remove("color");
    }

    public static void error(Paragraph status, String message) {
        status.setText(message);
        status.getStyle().set("color", "red");
    }
}
