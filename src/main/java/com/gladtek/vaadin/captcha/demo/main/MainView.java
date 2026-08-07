package com.gladtek.vaadin.captcha.demo.main;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        add(new H1("Captchas Addon Demo"));
        add(new Paragraph("Select a captcha demo from the side navigation."));
    }
}
