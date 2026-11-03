# EduJournal – UI Localization Documentation

## Overview

EduJournal supports full multilingual user interface (UI) localization using Java's built-in `ResourceBundle` mechanism. All static text elements — labels, buttons, table headers, dialog messages, and validation prompts — are externalized into `.properties` files and loaded at runtime based on the active locale.

---

## Supported Languages

| Language | Locale Code | Script   | Direction |
|----------|-------------|----------|-----------|
| English  | `en_US`     | Latin    | LTR (default) |
| Russian  | `ru_RU`     | Cyrillic | LTR |
| Turkish  | `tr_TR`     | Latin    | LTR |
| Arabic   | `ar_SA`     | Arabic   | RTL |

---

## Localization Framework

### Library Used
Java standard library — `java.util.ResourceBundle` (no external dependencies required).

### Resource Bundle Files

All translation keys are stored in `src/main/resources/`:

| File | Language |
|------|----------|
| `MessagesBundle_en_US.properties` | English (default) |
| `MessagesBundle_ru_RU.properties` | Russian |
| `MessagesBundle_tr_TR.properties` | Turkish |
| `MessagesBundle_ar_SA.properties` | Arabic |

### LocalizationManager

The central class `com.edujournal.config.LocalizationManager` manages the active locale and provides utility methods used across all views:

```java
// Get a localized string by key (falls back to English if key is missing)
LocalizationManager.get("key")

// Switch language at runtime
LocalizationManager.setLocale(new Locale("ru", "RU"))

// Get the current locale
LocalizationManager.getCurrentLocale()

// RTL helpers (used for Arabic layout)
LocalizationManager.isRTL()
LocalizationManager.getOrientation()   // NodeOrientation.RIGHT_TO_LEFT / LEFT_TO_RIGHT
LocalizationManager.getStartAlignment() // Pos.CENTER_RIGHT (RTL) or Pos.CENTER_LEFT (LTR)
LocalizationManager.getEndAlignment()   // Pos.CENTER_LEFT (RTL) or Pos.CENTER_RIGHT (LTR)
```

**English fallback:** if a key is missing in the active bundle, `LocalizationManager.get()` automatically returns the English value.

---

## Language Selector (In-GUI ComboBox)

A `ComboBox<String>` language selector is embedded directly in the application UI:

- **Main application pages:** located in the top-right header bar (`TopBar.java`)
- **Login page:** overlaid top-right using `StackPane` so it does not affect the page layout

When a new language is selected, the selector:
1. Creates the corresponding `Locale`
2. Calls `LocalizationManager.setLocale(locale)`
3. Calls `Main.reloadPage()` — which re-instantiates the current page using the stored `Supplier<Parent>`

This produces instant full UI re-rendering without restarting the application.

### Page Reload Mechanism

Each page registers itself for reload in its constructor:

```java
Main.setCurrentPageSupplier(DashboardPage::new);
```

`Main.reloadPage()` then calls `currentPageSupplier.get()` to create a fresh instance with the new locale and replaces the scene root.

---

## RTL Layout Support (Arabic)

When Arabic (`ar_SA`) is selected, the application automatically adjusts layout direction:

- All JavaFX containers that call `LocalizationManager.getOrientation()` switch to `NodeOrientation.RIGHT_TO_LEFT`
- Alignment helpers (`getStartAlignment()`, `getEndAlignment()`) ensure text and elements align correctly for RTL reading order

---

## Filter ComboBox Localization Pattern

For ComboBoxes that drive filter logic (e.g., filter by Name, ID, Group), a key-display pattern is used to keep internal values stable across language switches:

- Items list stores **English keys** (e.g., `"All"`, `"Name"`) — used by filter logic
- `setCellFactory` + `setButtonCell` override the **displayed text** with the localized value via `LocalizationManager.get(key)`

This ensures filter logic never breaks when the user switches language.

---

## Adding a New Language

1. Create a new properties file: `src/main/resources/MessagesBundle_<lang>_<COUNTRY>.properties`
2. Copy all keys from `MessagesBundle_en_US.properties` and translate the values
3. Add the language option to the `ComboBox` in `TopBar.java` and `LoginPage.java`
4. Add a matching `case` in the locale switch block:
   ```java
   case "Français" -> new Locale("fr", "FR");
   ```
5. Add a matching `case` in the `localeToLanguage()` helper method

---

## Translation Reference

The full translation table with all keys across all 4 languages is available in:
[`Documents/Localization/table with translations.csv`](table%20with%20translations.csv)
