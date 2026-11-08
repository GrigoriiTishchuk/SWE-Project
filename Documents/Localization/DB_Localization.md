# Database Localization Strategy

## Overview

To support multilingual functionality in the EduJournal application, database localization is implemented using a
**UTF-8 encoding + user locale persistence** strategy. This approach ensures that the database can store
international content in any script (Cyrillic, Arabic, Turkish, Latin) and remembers each user's preferred
language across sessions.

---

## Design Approach

EduJournal's database localization is built on two pillars:

| Pillar | Purpose |
|---|---|
| **UTF-8 encoding (`utf8mb4`)** | Allows the database to physically store any Unicode character — Cyrillic, Arabic, Turkish special characters, and more |
| **User locale persistence** | Stores each user's chosen language in the database so the app applies it automatically on next login |

### Why not translation tables?

Translation tables (storing multiple language versions of the same content) are designed for **system-defined labels** — fixed strings like product names or category titles that the system itself defines.

In EduJournal, content such as course names, assessment titles, and grade comments is **user-entered** — a teacher types it directly in their own language. There is nothing to translate. The correct approach is to ensure the database can store whatever the user types, regardless of script.

System UI labels (button text, table headers, error messages) are localized at the **application layer** via Java `ResourceBundle` property files — not in the database.

---

## Supported Languages

| Language | Locale code | Script |
|---|---|---|
| English | `en_US` | Latin |
| Russian | `ru_RU` | Cyrillic |
| Turkish | `tr_TR` | Latin (extended) |
| Arabic | `ar_SA` | Arabic (RTL) |

---

## Character Encoding

All database tables use `utf8mb4` character set with `utf8mb4_unicode_ci` collation.

**Why `utf8mb4` and not `utf8`?**

MariaDB's `utf8` only supports 3-byte Unicode characters. `utf8mb4` supports the full 4-byte Unicode range,
which is required for Arabic script and other non-Latin languages. `utf8mb4_unicode_ci` provides correct
case-insensitive comparison and sorting across all supported languages.

---

## User Locale Persistence

A `preferred_locale` column is added to the `users` table to store each user's chosen language.

| Column | Type | Default | Example values |
|---|---|---|---|
| `preferred_locale` | `VARCHAR(10)` | `en_US` | `en_US`, `ru_RU`, `tr_TR`, `ar_SA` |

**Flow:**
1. User logs in → app reads `preferred_locale` from the database → applies it via `LocalizationManager`
2. User changes language in the UI → new locale is saved to the database immediately
3. Next login → language is restored automatically, no manual selection needed

---

## Validation

To confirm that UTF-8 encoding works end-to-end, the database seed data (`data.sql`) includes students
with names in each supported script:

| Username | First name | Last name | Script | Locale |
|---|---|---|---|---|
| `student_ru` | Алексей | Иванов | Cyrillic | `ru_RU` |
| `student_tr` | Çağrı | Öztürk | Turkish (extended Latin) | `tr_TR` |
| `student_ar` | محمد | الأحمد | Arabic | `ar_SA` |

These seed users are inserted on fresh database initialization and serve as evidence that the database
stores and retrieves international characters correctly.

---

See [DB Localization Implementation](DB_Localization_Implementation.md) for technical implementation details.

See [UI Localization](UI_Localization.md) for the application-layer localization strategy.
