# Software Engineering Project 1 - Sprint 5 Review Report

## Sprint 5 Review – Edujournal (UI Localization)

- **Project:** Edujournal – Teacher's Gradebook and Report Card System
- **Sprint Duration:** 20.10.2026 – 3.11.2026
- **Team Members:** Bayram Erdogan, Grigorii Tishchuk, Olena Petrova, Maria Kuznetsova
- **Scrum Master:** Grigorii Tishchuk

---

## 1. Sprint 5 Goal

The goal of Sprint 5 was to prepare the Edujournal application for full multilingual support by localizing the user interface (UI), externalizing all static text into `ResourceBundle` properties files, implementing dynamic language selection directly within the GUI via a `ComboBox`, and ensuring correct layout adjustments (including RTL support for Arabic) across 4 target languages (English, Russian, Turkish, and Arabic).

---

## 2. Sprint Outcome Summary

- Sprint 5 was successfully completed, and all planned localization tasks and backlog items were delivered.
- All static text elements, labels, buttons, dialogs, and table headers across the Edujournal UI were externalized into language resource bundles (`MessagesBundle`).
- A dynamic language selector (`ComboBox`) was created and integrated directly into the JavaFX graphical interface by Maria Kuznetsova, enabling real-time scene re-rendering and locale switching on the fly.
- Language localization assignments were successfully completed:
  - **Maria Kuznetsova** completed Russian (`ru`) UI localization.
  - **Bayram Erdogan** completed Turkish (`tr`) UI localization and translation verification.
  - **Olena Petrova** completed Arabic (`ar_SA`) UI localization.
- Automatic layout direction switching (**RTL / LTR**) was implemented and verified for Arabic locale support by Grigorii Tishchuk.
- Full multi-language support covering 4 languages (English, Russian, Turkish, and Arabic) was validated.
- A total of **26 Story Points** planned for the sprint were completed.

---

## 3. Completed User Stories / Tasks

### User Interface Localization Preparation & Architecture
- Audited the entire application codebase to identify hardcoded strings, labels, table columns, messages, and dialog prompts.
- Established the `ResourceBundle` architecture and structure for properties files.

### In-GUI Language Selection & Dynamic UI Rendering
- Created and integrated a `ComboBox<String>` language selector directly into the top area of the main JavaFX window.
- Configured event handlers to switch `Locale` and dynamically trigger scene re-rendering when a new language is selected.

### Multi-Language Support Implementation
- **Russian (`ru`):** Implemented by Maria Kuznetsova, including Cyrillic string mapping for all UI controls, headers, and validation messages.
- **Turkish (`tr`):** Implemented by Bayram Erdogan.
- **Arabic (`ar_SA`):** Implemented by Olena Petrova using Unicode-escaped string bundles to ensure error-free rendering.
- **RTL Layout Orientation:** Configured by Olena Petrova using JavaFX `NodeOrientation.RIGHT_TO_LEFT` whenever Arabic is active.
- **English:** Integrated into the standard bundle selection table.

### Project Management & Documentation
- Updated Product and Sprint Backlogs in Trello to reflect localization user stories, story point estimates, and acceptance criteria.
- Updated `README.md` with instructions on running localized versions and selecting languages.
- Updated system UML diagrams in `doc/diagrams` to reflect the localization architecture.

---

## 4. Sprint 5 Demo Summary

During the Sprint 5 Review, the team demonstrated:

- Instant UI language switching via the in-GUI `ComboBox` selector.
- Seamless re-rendering of labels, table headers, form buttons, and dialog messages across all 4 supported languages.
- Correct rendering of non-Latin characters (Russian Cyrillic, Turkish, Japanese, and Arabic script).
- Automatic LTR to RTL layout mirroring when switching to Arabic (`ar_SA`).
- Stable application execution and data display across localized views.

---

## 5. What Went Well

- Clear division of language responsibilities allowed the team to work efficiently in parallel.
- The in-GUI `ComboBox` implementation allowed instant testing of language switches without application restarts.
- RTL layout mirroring for Arabic worked cleanly using native JavaFX node orientation methods.
- All 4 planned languages (English, Russian, Turkish, and Arabic) were integrated smoothly.

---

## 6. What Could Be Improved

- Initial encoding issues with non-Latin characters in properties files required converting strings to Unicode escape sequences (`\uXXXX`).
- Layout padding and button widths required minor CSS adjustments to accommodate varying string lengths between languages.

---

## 7. Daily Scrum (Stand-up Meetings)

The team conducted regular stand-up syncs to track progress on bundle key mappings, `ComboBox` UI integration, and layout adjustments for right-to-left text.

---

## 8. Team Contributions

| Team Member Name | Assigned Tasks                                                                                                                                                                         | Time Spent (hrs) | In-class tasks |
| :--- |:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| :---: | :---: |
| **Grigorii Tishchuk**<br>*(Scrum Master)* | Sprint planning, Scrum coordination, UI localization architecture, documentation management, documentation updates (README, UML diagrams)                                              | 12 h 0 min | Submitted |
| **Maria Kuznetsova** | Dynamic UI re-rendering, created and implemented the in-GUI `ComboBox` language selector, Russian language localization (`ru`), Login page localization, resource bundle setup, translation verification        | 16 h 0 min | Submitted |
| **Bayram Erdogan** | Turkish language localization (`tr`), resource bundle setup, translation verification                                                                                                  | 10 h 30 min | Submitted |
| **Olena Petrova** | Arabic language localization (`ar_SA`), resource bundle setup, translation verification                                                                                                | 11 h 30 min | Submitted |

---

## Submission Summary

| Deliverable | Status |
| :--- | :--- |
| **Sprint Report (GitHub)** | Submitted |
| **User Interface Localization (Commit)** | Submitted |
| **Product/Sprint Backlog Update (Trello)** | Submitted |
| **GitHub Update (README & Diagrams)** | Submitted |