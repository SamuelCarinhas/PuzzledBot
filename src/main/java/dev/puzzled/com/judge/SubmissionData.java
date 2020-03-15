package dev.puzzled.com.judge;

import dev.puzzled.com.Main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SubmissionData {

    private String fileName, username;
    private int problemId;

    public SubmissionData(String username, String fileName, int problemId) {
        this.username = username;
        this.fileName = fileName;
        this.problemId = problemId;

        Main.getDatabase().updateSubmission(fileName, "status", "running");
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getDebugName() {
        String debugName = "";
        boolean add = false;
        for(char c : fileName.toCharArray()) {
            if(add) debugName += c;
            if(c == '-' && !add) add = true;
        }
        try {
            System.out.println("Debug file...");
            Files.copy(new File(fileName).toPath(), new File(debugName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception exc) {
            System.err.println("Error while debug file...");
            System.err.println(exc.toString());
        }
        return debugName;
    }

    public void setResult(String result) {
        Main.getDatabase().updateSubmission(fileName, "status", result);
    }

    public int getProblemId() {
        return this.problemId;
    }

    @Override
    public String toString() {
        return "{fileName='" + fileName + '\'' + ", username='" + username + '\'' + ", problemId=" + problemId + '}';
    }
}
