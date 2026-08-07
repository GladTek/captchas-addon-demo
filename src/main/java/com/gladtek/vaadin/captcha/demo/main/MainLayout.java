package com.gladtek.vaadin.captcha.demo.main;

import com.gladtek.vaadin.captcha.demo.friendlycaptcha.FriendlyCaptchaSubmitView;
import com.gladtek.vaadin.captcha.demo.friendlycaptcha.FriendlyCaptchaView;
import com.gladtek.vaadin.captcha.demo.hcaptcha.HCaptchaSubmitView;
import com.gladtek.vaadin.captcha.demo.hcaptcha.HCaptchaView;
import com.gladtek.vaadin.captcha.demo.turnstile.TurnstileSubmitView;
import com.gladtek.vaadin.captcha.demo.turnstile.TurnstileView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;

@Layout
public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Captchas Addon Demo");
        title.getStyle().set("font-size", "1.125rem").set("margin", "0");

        SideNav nav = new SideNav();

        SideNavItem onArrival = new SideNavItem("Verify on token arrival");
        onArrival.addItem(new SideNavItem("Cloudflare Turnstile", TurnstileView.class));
        onArrival.addItem(new SideNavItem("Friendly Captcha", FriendlyCaptchaView.class));
        onArrival.addItem(new SideNavItem("hCaptcha", HCaptchaView.class));
        onArrival.setExpanded(true);

        SideNavItem onSubmit = new SideNavItem("Verify on submit");
        onSubmit.addItem(new SideNavItem("Cloudflare Turnstile", TurnstileSubmitView.class));
        onSubmit.addItem(new SideNavItem("Friendly Captcha", FriendlyCaptchaSubmitView.class));
        onSubmit.addItem(new SideNavItem("hCaptcha", HCaptchaSubmitView.class));
        onSubmit.setExpanded(true);

        nav.addItem(new SideNavItem("Home", MainView.class), onArrival, onSubmit);

        Scroller scroller = new Scroller(nav);

        addToDrawer(scroller);
        addToNavbar(toggle, title);
    }
}
