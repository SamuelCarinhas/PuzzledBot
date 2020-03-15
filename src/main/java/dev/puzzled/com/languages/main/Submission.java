package dev.puzzled.com.languages.main;

import dev.puzzled.com.judge.errors.JudgeErrors;

import java.util.List;

public interface Submission {

    List<String> getOutput();
    List<String> getErrors();
    JudgeErrors start(List<String> inputs, List<String> outputs);
    long getExecutionTime();


}
