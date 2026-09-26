package com.edujournal.security.authorization;

import com.edujournal.backend.service.CourseService;
import com.edujournal.backend.utils.UserSession;
import com.edujournal.dao.CourseDAO;
import com.edujournal.entity.Course;
import com.edujournal.entity.Role;
import com.edujournal.model.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TeacherAuthorizationSecurityTest {

    private UserSession session;

    @BeforeEach
    void setUp() {
        session = UserSession.getInstance();
        session.cleanUserSession();
    }

    @AfterEach
    void tearDown() {
        session.cleanUserSession();
    }

    private void loginAsTeacher(int teacherId) {
        UserDTO user = mock(UserDTO.class);
        when(user.getId()).thenReturn(teacherId);
        when(user.getRole()).thenReturn(Role.TEACHER);

        session.setCurrentUser(user);
    }


    @Test
    void teacherShouldOnlyQueryCoursesByOwnUserId() {
        int loggedInTeacherId = 100;

        loginAsTeacher(loggedInTeacherId);

        try (MockedConstruction<CourseDAO> mocked =
                     mockConstruction(CourseDAO.class, (mock, context) -> {
                         when(mock.findByUserId(loggedInTeacherId))
                                 .thenReturn(java.util.List.of());
                     })) {

            CourseService service = new CourseService();

            service.findByUserId(loggedInTeacherId);

            verify(mocked.constructed().get(0))
                    .findByUserId(loggedInTeacherId);

            verify(mocked.constructed().get(0), never())
                    .findAll();
        }
    }
}
