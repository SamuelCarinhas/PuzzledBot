package dev.puzzled.com.judge.errors;

public enum JudgeErrors {

    COMPILE_ERROR("Compile Time Error"),
    RUN_TIME_ERROR("Run Time Error"),
    TIME_LIMIT_EXCEED("Time Limit Exceed"),
    SYSTEM_ERROR("System Error"),
    ACCEPTED("Accepted"),
    WRONG_ANSWER("Wrong Answer");

    String message;

    JudgeErrors(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
