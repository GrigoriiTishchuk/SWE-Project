# Database Localization Strategy

## Overview

To support multilingual functionality in the EduJournal application, database localization is implemented using
three complementary approaches: **UTF-8 encoding**, **user locale persistence**, and **DB-driven translations**
for system-defined content. Together they ensure the database stores any script correctly, remembers each user's
language, and delivers localized labels for fixed application data directly from the database.

---

## Design Approach

EduJournal's database localization is built on three layers:

| Layer | Approach | Purpose |
|---|---|---|
| **UTF-8 encoding (`utf8mb4`)** | Schema-level | Stores any Unicode character — Cyrillic, Arabic, Turkish, Latin |
| **User locale persistence** | Column per user | Remembers each user's chosen language across sessions |
| **Assessment type names** | Translation table | Localized names for fixed system types (Exam, Homework, etc.) read from DB |
| **Role labels** | Row approach | Localized role labels (Teacher, Student, Admin) read from DB |

### What is translated in the database vs. the application layer

| Content | Where localized | Reason |
|---|---|---|
| Course names, assessment titles, comments | Not translated | User-entered — teachers type in their own language, nothing to translate |
| Button text, table headers, error messages | `ResourceBundle` `.properties` files | Static UI strings, no DB needed |
| Assessment type names (Exam, Homework…) | `assessment_type_translation` table | System-defined fixed set, meaningful to translate |
| Role labels (Teacher, Student, Admin) | `role_translation` table | System-defined fixed set, meaningful to translate |

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

## DB-Driven Translations

### Assessment Type Names — Translation Table Approach

Assessment types (EXAM1, HOMETASK, PROJECT, etc.) are system-defined and shown in the GUI. Their display names
are stored in a dedicated translation table and queried for the active language, with English as fallback.

**Schema:**
```sql
CREATE TABLE assessment_type_translation (
    type_key VARCHAR(30) NOT NULL,
    lang     VARCHAR(10) NOT NULL,
    name     VARCHAR(100) NOT NULL,
    PRIMARY KEY (type_key, lang)
);
```

**Example data:**

| type_key | lang | name |
|---|---|---|
| `HOMETASK` | `en_US` | Homework |
| `HOMETASK` | `ru_RU` | Домашнее задание |
| `HOMETASK` | `tr_TR` | Ev Ödevi |
| `HOMETASK` | `ar_SA` | واجب منزلي |

The GUI reads localized names via `LocalizationDAO.getAssessmentTypeNames(lang)`, which uses a `LEFT JOIN`
with `COALESCE` so any missing translation automatically falls back to English.

---

### Role Labels — Row Approach

Role labels (ADMINISTRATOR, TEACHER, STUDENT) are stored using the **row approach**: one row per role and
language in a single table, identified by a `role_key` column.

**Schema:**
```sql
CREATE TABLE role_translation (
    role_key VARCHAR(30) NOT NULL,
    lang     VARCHAR(10) NOT NULL,
    label    VARCHAR(50) NOT NULL,
    PRIMARY KEY (role_key, lang)
);
```

**Example data:**

| role_key | lang | label |
|---|---|---|
| `TEACHER` | `en_US` | Teacher |
| `TEACHER` | `ru_RU` | Преподаватель |
| `TEACHER` | `tr_TR` | Öğretmen |
| `TEACHER` | `ar_SA` | معلم |

The top bar and profile page read the role label via `LocalizationDAO.getRoleLabel(roleKey, lang)` with English
fallback. No schema change is needed to add a new language — only new rows.

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
