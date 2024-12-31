package com.system.util;

public class Exceptions {

    public static class InvalidInput extends BaseException {
        public InvalidInput() {
            super("Invalid input data", 400);
        }
        public InvalidInput(String message) {
            super(message, 400);
        }
    }

    public static class UnauthorizedAccess extends BaseException {
        public UnauthorizedAccess() {
            super("Unauthorized access", 401);
        }
    }

    public static class NotFound extends BaseException {
        public NotFound() {
            super("Resource not found", 404);
        }
        public NotFound(String message) {
            super(message, 404);
        }
    }
    public static class CourseRegistration extends BaseException {
        public CourseRegistration(String message) {
            super(message, 409);
        }
    }
    public static class StudentRegistration extends BaseException {
        public StudentRegistration(String message) {
            super(message, 409);
        }
    }
}
