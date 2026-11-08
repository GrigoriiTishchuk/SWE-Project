# Database Localization Implementation

## Implementation Details (Hibernate/JPA + MariaDB)

The database localization strategy is implemented using Java Persistence API (JPA) with Hibernate ORM
and MariaDB. It consists of three parts: schema-level encoding, JDBC connection configuration, and
user locale persistence.

---

## 1. Schema — UTF-8 Encoding

All tables are created with `utf8mb4` character set and `utf8mb4_unicode_ci` collation.

Example from `schema.sql`:

```sql
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    phone VARCHAR(30),
    email VARCHAR(255),
    preferred_locale VARCHAR(10) DEFAULT 'en_US'
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

The same `DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci` declaration is applied to all
seven tables: `users`, `academic_groups`, `students`, `courses`, `enrollments`, `assessments`, `grades`.

**Migration for existing databases:**

An idempotent `ALTER TABLE` statement runs every startup to add the `preferred_locale` column to
databases created before this sprint, without affecting existing data:

```sql
ALTER TABLE users ADD COLUMN IF NOT EXISTS preferred_locale VARCHAR(10) DEFAULT 'en_US';
```

---

## 2. Database Creation — Default Charset

The database itself is created with `utf8mb4` as the default character set so that any new tables
created without an explicit charset declaration also use it:

```java
statement.executeUpdate(
    "CREATE DATABASE IF NOT EXISTS " + databaseName
    + " DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
);
```

---

## 3. JDBC Connection — Encoding Parameters

The JDBC connection URL includes encoding parameters to ensure correct character handling between
the Java application and the database:

`.env`:
```
DB_URL=jdbc:mariadb://localhost:3306/edujournal?useUnicode=true&characterEncoding=UTF-8
```

`DatabaseInitializer.java` builds the same parameters into its internal connection URL:

```java
String databaseUrl =
    "jdbc:mariadb://" + databaseHost + ":" + databasePort + "/" + databaseName
    + "?useUnicode=true&characterEncoding=UTF-8";
```

---

## 4. User Entity — Locale Field

The `preferred_locale` column is mapped in the `User` JPA entity:

```java
@Column(name = "preferred_locale")
private String preferredLocale = "en_US";

public String getPreferredLocale() {
    return preferredLocale != null ? preferredLocale : "en_US";
}

public void setPreferredLocale(String preferredLocale) {
    this.preferredLocale = preferredLocale;
}
```

---

## 5. UserDAO — Locale Update

A dedicated method updates only the locale column, avoiding a full entity merge:

```java
public void updateLocale(Integer userId, String locale) {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
    try {
        entityManager.getTransaction().begin();
        entityManager.createQuery(
            "UPDATE User u SET u.preferredLocale = :locale WHERE u.id = :id"
        )
        .setParameter("locale", locale)
        .setParameter("id", userId)
        .executeUpdate();
        entityManager.getTransaction().commit();
    } catch (Exception e) {
        if (entityManager.getTransaction().isActive()) entityManager.getTransaction().rollback();
        throw e;
    } finally {
        entityManager.close();
    }
}
```

---

## 6. AuthService — Locale Applied on Login

When a user logs in successfully, their saved locale is read from the database and applied to the
application's `LocalizationManager`:

```java
public boolean login(String username, String password) {
    User user = userDao.findByUsername(username);
    if (user != null && checkPassword(password, user.getPasswordHash())) {
        UserSession.getInstance().setCurrentUser(userMapper.toDTO(user));
        applyUserLocale(user.getPreferredLocale());
        return true;
    }
    return false;
}

private void applyUserLocale(String localeTag) {
    if (localeTag == null || localeTag.isBlank()) return;
    String[] parts = localeTag.split("_");
    Locale locale = parts.length == 2
            ? new Locale(parts[0], parts[1])
            : new Locale(parts[0]);
    LocalizationManager.setLocale(locale);
}
```

---

## 7. TopBar — Locale Saved on Language Switch

When the user picks a different language from the dropdown, the new locale is saved to the database:

```java
languageSelector.valueProperty().addListener((obs, old, val) -> {
    Locale locale = switch (val) {
        case "Русский" -> new Locale("ru", "RU");
        case "Türkçe"  -> new Locale("tr", "TR");
        case "العربية"  -> new Locale("ar", "SA");
        default        -> new Locale("en", "US");
    };
    LocalizationManager.setLocale(locale);
    UserDTO currentUser = UserSession.getInstance().getCurrentUser();
    if (currentUser != null) {
        new UserDAO().updateLocale(currentUser.getId(), locale.toString());
    }
    Main.reloadPage();
});
```

---

## 8. Username Generation — Latin-only Enforcement

When a student or teacher is created with a non-Latin name (e.g. Cyrillic or Arabic), the username
must remain Latin-only since it is used for login. The `normalize()` method strips all non-ASCII
characters after Finnish character substitutions:

```java
public static String normalize(String s) {
    if (s == null) return "";
    return s.toLowerCase()
            .replace("ä", "a")
            .replace("ö", "o")
            .replace("å", "a")
            .replaceAll("[^a-z0-9]", "");
}
```

If both name parts normalize to an empty string (e.g. a fully Arabic name), a role-based prefix is
used as fallback:

```java
String combined = GeneratorUtil.normalize(firstName) + GeneratorUtil.normalize(lastName);
String base = candidates.isEmpty()
        ? (combined.isEmpty() ? fallbackPrefix : combined)
        : candidates.get(0).substring(0, 7);
```

Where `fallbackPrefix` is `"teacher"` for teachers and `"student"` for students.

---

## Summary

| Component | Change | Purpose |
|---|---|---|
| `schema.sql` | `utf8mb4` on all tables + `preferred_locale` column | Store any Unicode script; persist user language |
| `schema.sql` | `ALTER TABLE ... ADD COLUMN IF NOT EXISTS` | Migrate existing databases safely |
| `.env` | `useUnicode=true&characterEncoding=UTF-8` | Correct encoding over JDBC |
| `DatabaseInitializer.java` | `utf8mb4` on `CREATE DATABASE` + encoding in URL | Full UTF-8 from DB creation to connection |
| `User.java` | `preferredLocale` field | Map the locale column in JPA |
| `UserDAO.java` | `updateLocale()` method | Save locale to DB on language switch |
| `AuthService.java` | `applyUserLocale()` on login | Restore user's language automatically |
| `TopBar.java` | `updateLocale()` call on dropdown change | Persist language choice immediately |
| `GeneratorUtil.java` | Strip non-ASCII in `normalize()` | Keep usernames Latin-only |
| `data.sql` | Cyrillic, Arabic, Turkish seed users | Validate UTF-8 end-to-end |
