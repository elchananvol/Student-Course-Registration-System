package com.system.exceptions;

import org.springframework.http.HttpStatus;

public class UserExceptions {

    public static class InvalidInput extends UserBaseException {
        public InvalidInput() {
            super("Invalid input data", HttpStatus.BAD_REQUEST);
        }
        public InvalidInput(String message) {
            super(message, HttpStatus.BAD_REQUEST);
        }
    }

    public static class UnauthorizedAccess extends UserBaseException {
        public UnauthorizedAccess() {
            super("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
    }

    public static class NotFound extends UserBaseException {
        public NotFound() {
            super("Resource not found", HttpStatus.NOT_FOUND);
        }
        public NotFound(String message) {
            super(message, HttpStatus.NOT_FOUND);
        }
    }
    public static class CourseRegistration extends UserBaseException {
        public CourseRegistration(String message) {
            super(message, HttpStatus.CONFLICT);
        }
    }
    public static class StudentRegistration extends UserBaseException {
        public StudentRegistration(String message) {
            super(message, HttpStatus.CONFLICT);
        }
    }
}
