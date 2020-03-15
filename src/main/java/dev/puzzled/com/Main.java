package dev.puzzled.com;

import dev.puzzled.com.database.Database;
import dev.puzzled.com.judge.Dispatcher;
import dev.puzzled.com.judge.Judge;
import dev.puzzled.com.judge.SubmissionData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    static Database database;
    static Dispatcher dispatcher;
    static boolean busy;

    public static void main(String[] args) {
        database = new Database();
        dispatcher = new Dispatcher();
        busy = false;
        ScheduledExecutorService loop = Executors.newSingleThreadScheduledExecutor();
        loop.scheduleAtFixedRate(() -> {
            if(dispatcher.getSubmissionsTotal() == 0) {
                database.getSubmissions();
                if(dispatcher.getSubmissionsTotal() > 0) System.out.println("New submissions found.");
            } else {
                if(!isBusy()) {
                    SubmissionData submissionData = dispatcher.getSubmission();
                    System.out.println("Judge submission: " + submissionData);
                    new Judge(submissionData);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static Database getDatabase() {
        return database;
    }

    public static Dispatcher getDispatcher() {
        return dispatcher;
    }

    public static boolean isBusy() {
        return busy;
    }

    public static void setBusy(boolean state) {
        busy = state;
    }

}
