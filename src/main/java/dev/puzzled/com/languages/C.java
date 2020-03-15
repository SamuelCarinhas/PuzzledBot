package dev.puzzled.com.languages;

import dev.puzzled.com.Main;
import dev.puzzled.com.judge.errors.JudgeErrors;
import dev.puzzled.com.languages.main.Submission;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class C implements Submission {

    private String fileName, realFileName;
    private List<String> output;
    private List<String> errors;
    private long executionTime = -1;

    public C(String fileName, String realFileName) {
        this.realFileName = realFileName;
        this.fileName = fileName;
        this.output = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public List<String> getOutput() {
        return this.output;
    }

    public long getExecutionTime() {
        return this.executionTime;
    }

    public int compile() {
        String command = "gcc " + fileName;
        try {
            Process compileProcess = Runtime.getRuntime().exec(command);

            String line;
            BufferedReader bre = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));

            while((line = bre.readLine()) != null) {
                errors.add(line);
            }
            bre.close();

            compileProcess.destroy();
            return errors.size() == 0 ? 1 : 0;
        } catch (Exception exc) {
            return -1;
        }
    }

    public JudgeErrors start(List<String> inputs, List<String> outputs) {
        int r = compile();
        if(r == -1) return JudgeErrors.SYSTEM_ERROR;
        else if(r == 0) return JudgeErrors.COMPILE_ERROR;
        for(int i = 0; i < inputs.size(); i++) {
            Main.getDatabase().updateSubmission(realFileName, "status", "running case #" + i);
            output.clear();
            errors.clear();
            try {
                String command = "./a.out";
                Process runProcess = Runtime.getRuntime().exec(command);
                try {
                    int finalI = i;
                    int type = CompletableFuture.supplyAsync(() -> run(runProcess, inputs.get(finalI))).get(1000, TimeUnit.MILLISECONDS);
                    if (type == -1) return JudgeErrors.SYSTEM_ERROR;
                    else if (type == 0) return JudgeErrors.RUN_TIME_ERROR;
                    else {
                        String currentOutput = "";
                        for (String line : output) {
                            currentOutput += line;
                        }
                        if (!currentOutput.equals(outputs.get(i))) return JudgeErrors.WRONG_ANSWER;
                    }
                } catch (Exception timeout) {
                    runProcess.destroy();
                    return JudgeErrors.TIME_LIMIT_EXCEED;
                }
            } catch (Exception exc) {
                return JudgeErrors.SYSTEM_ERROR;
            }
        }
        return JudgeErrors.ACCEPTED;
    }

    public int run(Process runProcess, String inputText) {
        try {
            long start = System.currentTimeMillis();

            String line;
            BufferedWriter brw = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
            brw.write(inputText);
            brw.close();
            BufferedReader bri = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            BufferedReader bre = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));

            while((line = bri.readLine()) != null) {
                output.add(line + "\r\n");
            }
            bri.close();

            while((line = bre.readLine()) != null) {
                errors.add(line);
            }
            bre.close();

            executionTime = System.currentTimeMillis()-start;

            runProcess.destroy();
            return errors.size() == 0 ? 1 : 0;
        } catch(Exception exc) {
            runProcess.destroy();
            return -1;
        }
    }

}
