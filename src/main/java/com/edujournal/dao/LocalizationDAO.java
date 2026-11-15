package com.edujournal.dao;

import com.edujournal.config.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data access object for localization data stored in the database.
 * Provides localized labels for assessment types and user roles.
 */
public class LocalizationDAO {

    /**
     * Retrieves localized assessment type names for the specified language.
     * Falls back to English values when a translation is not available.
     *
     * @param lang language code
     * @return map where the key is the assessment type identifier
     *         and the value is the localized assessment type name
     */
    public Map<String, String> getAssessmentTypeNames(String lang) {
        EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Object[]> rows =
                    entityManager.createNativeQuery(
                                    "SELECT fb.type_key, COALESCE(t.name, fb.name) "
                                            + "FROM assessment_type_translation fb "
                                            + "LEFT JOIN assessment_type_translation t "
                                            + "    ON t.type_key = fb.type_key AND t.lang = :lang "
                                            + "WHERE fb.lang = 'en_US'")
                            .setParameter("lang", lang)
                            .getResultList();

            Map<String, String> map = new HashMap<>();
            for (Object[] row : rows) {
                map.put((String) row[0], (String) row[1]);
            }
            return map;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Retrieves a localized role label for the specified role key.
     * Falls back to the English label when a translation is not available.
     *
     * @param roleKey role identifier
     * @param lang language code
     * @return localized role label, or the role key if no translation exists
     */
    public String getRoleLabel(String roleKey, String lang) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<String> rows =
                    em.createNativeQuery(
                                    "SELECT COALESCE(t.label, fb.label) "
                                            + "FROM role_translation fb "
                                            + "LEFT JOIN role_translation t "
                                            + "    ON t.role_key = fb.role_key AND t.lang = :lang "
                                            + "WHERE fb.lang = 'en_US' AND fb.role_key = :key")
                            .setParameter("lang", lang)
                            .setParameter("key", roleKey)
                            .getResultList();

            return rows.isEmpty() ? roleKey : rows.get(0);
        } finally {
            em.close();
        }
    }
}
