# Captchas Addon Demo

A Vaadin 25 (Flow) demo app showcasing the [`captchas-addon`](https://central.sonatype.com/artifact/com.gladtek.vaadin.addons/captchas-addon) - Cloudflare Turnstile, hCaptcha, and Friendly Captcha as Java components.

## What it shows

- All three captcha providers, each in two flavors:
  - **Verify on token arrival** - the form validates as soon as the captcha token comes back.
  - **Verify on submit** - the token is checked when the user clicks Submit.
- Per-widget option panels (theme, size, appearance/start-mode - whatever each provider exposes) on a right-hand side panel, applied on next render.
- An `AppLayout` + `SideNav` shell using Vaadin's Aura theme.
- A shared contact form (`Name` + `Email` + captcha) with:
  - Standard email format validation.
  - A `WorkEmailValidator` rejecting common free/consumer email domains (Gmail, Yahoo, Outlook, iCloud, etc.), requiring a work email.

## Running locally

1. Copy `src/main/resources/application.properties.example` to `src/main/resources/application.properties` and fill in your own captcha site keys/secrets (or use the public "always passes" test keys noted in the comments for Turnstile/hCaptcha - Friendly Captcha requires a real sandbox account from [app.friendlycaptcha.com](https://app.friendlycaptcha.com)).
2. Run:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Open http://localhost:8090.

## Project structure

Package base: `com.gladtek.vaadin.captcha.demo`

| Package | Contents |
|---|---|
| *(root)* | `Application` - Spring Boot entry point, Aura theme setup |
| `main` | `MainLayout` (app shell/side nav), `MainView` (home page) |
| `contact` | `ContactForm`, `ContactFormBean`, `WorkEmailValidator`, `StatusMessages` - shared across all captcha views |
| `turnstile` | Cloudflare Turnstile views |
| `hcaptcha` | hCaptcha views |
| `friendlycaptcha` | Friendly Captcha views |

## Build

```bash
./mvnw install
```

A GitHub Actions workflow (`.github/workflows/build.yml`) runs the same build on every push/PR to `main`.
