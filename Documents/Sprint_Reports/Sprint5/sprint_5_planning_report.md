# Sprint 5 – Sprint Planning Document

**Project:** Edujournal – Teacher's Gradebook and Report Card System  
**Sprint Duration:** 20.10.2026 – 3.11.2026  
**Team:** Bayram Erdogan, Grigorii Tishchuk, Olena Petrova, Maria Kuznetsova  
**Scrum Master:** Grigorii Tishchuk  

---

## 1. Sprint Goal

The main goal of Sprint 5 is to prepare the Edujournal application for full multilingual support by localizing the user interface (UI), externalizing all static text into `ResourceBundle` properties files, implementing dynamic language selection directly within the GUI via a `ComboBox`, and ensuring correct layout adjustments (including RTL support for Arabic) across non-Latin and Latin languages (English,  Russian, Turkish, and Arabic).

---

## 2. Sprint Objectives

The main objectives of Sprint 5 are:

- Ensure the entire user interface (UI) is ready for localization by externalizing all static text elements into resource bundles.
- Identify and document all text elements, labels, tables, dialogs, and messages requiring translation.
- Implement in-GUI language selection functionality via a dynamic `ComboBox` affecting numbers, dates, tables, and UI text elements.
- Localize the GUI to support non-Latin and Latin languages (specifically implementing English, Russian, Turkish, and Arabic).
- Assign specific language localizations to team members (Russian to Maria, Turkish to Bayram, Arabic to Olena).
- Verify that UI elements dynamically update according to the selected language in real time without restarting the application.
- Ensure layouts adjust correctly for text direction, providing full LTR/RTL support for Arabic.
- Update the Product Backlog in Trello to include localization-related user stories, estimates, and acceptance criteria.
- Update the GitHub repository `README.md` with instructions for selecting languages and running localized versions, alongside updated UML diagrams (`doc/diagrams`).
- Prepare the Sprint 6 plan focusing on database localization (schema changes, multilingual content data models).

---

## 3. Sprint Backlog

The following Product Backlog Items are planned for Sprint 5:

| Product Backlog Item | Priority | Story Points |
|---|:--------:|:------------:|
| Externalize Static UI Text & Create Resource Bundles |   High   |      5       |
| Implement In-GUI `ComboBox` Language Selector & Dynamic Re-rendering |   High   |      5       |
| Russian Language UI Localization (`ru`) |   High   |      3       |
| Turkish Language UI Localization (`tr`) & Translation Verification |   High   |      3       |
| Arabic Language UI Localization (`ar_SA`) & Acceptance Testing |   High   |      3       |
| RTL Layout Adaptation for Arabic (`NodeOrientation.RIGHT_TO_LEFT`) |   High   |      3       |
| Update Product Backlog (Trello) |  Medium  |      2       |
| Update GitHub README & UML Diagrams (`doc/diagrams`) |  Medium  |      2       |
| **Total** |          |  **26 SP**   |

---

## 4. Roles and Responsibilities

| Team Member | Responsibilities                                                                                                                                                      |
|---|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Grigorii Tishchuk**<br>*(Scrum Master)* | Sprint planning and coordination, documentation updates (README, UML diagrams), UI localization architecture, documentation management, preparation of Sprint 6 plan. |
| **Maria Kuznetsova** | Dynamic UI re-rendering, creation and implementation of the in-GUI `ComboBox` language selector, Russian language localization (`ru`).                                |
| **Bayram Erdogan** | Turkish language localization (`tr`), resource bundle setup, translation verification, unit testing.                                                                  |
| **Olena Petrova** | Arabic language localization (`ar_SA`).                                                                                                                               |

---

## 5. Expected Deliverables

At the end of Sprint 5, the team expects to deliver:

- Functional language selector `ComboBox` integrated directly into the JavaFX application interface.
- Complete localization resource properties files covering 4 languages: English, Russian, Turkish, and Arabic.
- Localized UI supporting non-Latin languages and right-to-left (RTL) text direction for Arabic.
- Dynamic UI re-rendering upon selecting a new language without requiring application restarts.
- Updated Product and Sprint Backlog in Trello reflecting localization user stories and criteria.
- Updated GitHub repository with language setup instructions in `README.md` and new UML diagrams in `doc/diagrams`.
- Sprint 6 planning report covering upcoming database localization tasks.

---

## 6. Team Capacity and Assumptions

### Team Capacity

The estimated team capacity for Sprint 5 is:

**4 members × 3 hours/day × 8 working days = approximately 96 hours**

### Assumptions and Risks

- All localized string tables and encoding issues (such as Unicode escape sequences for Arabic and Japanese) will be handled properly across environment setups.
- JavaFX scene root nodes can seamlessly toggle orientation between LTR and RTL without visual breaking.
- ResourceBundle properties files will be accessible both in IDE execution and packaged application deployments.

---

## 7. Definition of Done

A task will be considered **Done** when:

- [ ] Static UI strings are fully externalized into `.properties` resource files.
- [ ] The `ComboBox` language selector updates the active locale and re-renders the UI instantly.
- [ ] Non-Latin character sets (Russian Cyrillic, Turkish special characters, Japanese, Arabic script) display correctly.
- [ ] RTL node orientation applies automatically when the Arabic locale is selected.
- [ ] Unit tests for localization bundles pass successfully.
- [ ] Implementation has been committed and pushed to GitHub.
- [ ] Corresponding Trello tasks and project documentation (README, UML diagrams) are updated.