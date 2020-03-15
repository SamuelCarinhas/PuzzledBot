package dev.puzzled.com.judge;

import dev.puzzled.com.Main;
import dev.puzzled.com.judge.errors.JudgeErrors;
import dev.puzzled.com.languages.C;
import dev.puzzled.com.languages.CPP;
import dev.puzzled.com.languages.Java;
import dev.puzzled.com.languages.Python;
import dev.puzzled.com.languages.main.Submission;

import java.util.List;

public class Judge {

    private SubmissionData submissionData;

    public Judge(SubmissionData submissionData) {
        Main.setBusy(true);
        this.submissionData = submissionData;
        lookup();
    }

    public void lookup() {
        Submission submission = getSubmission(submissionData.getDebugName(), submissionData.getFileName());
        if(submission == null) {
            Main.setBusy(false);
            return;
        }
        System.out.println("Looking for input...");
        List<List<String>> inputData = Main.getDatabase().getInput(submissionData.getProblemId());
        System.out.println("Starting judge...");
        JudgeErrors rep = submission.start(inputData.get(0), inputData.get(1));
        System.out.println("Result: " + rep);
        submissionData.setResult(rep.toString());

        Main.setBusy(false);
    }

    private Submission getSubmission(String name, String realName) {
        if(name.endsWith(".java")) return new Java(name, realName);
        if(name.endsWith(".cpp")) return new CPP(name, realName);
        if(name.endsWith(".c")) return new C(name, realName);
        if(name.endsWith(".py")) return new Python(name, realName);
        return null;
    }

}
