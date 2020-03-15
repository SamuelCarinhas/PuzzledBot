package dev.puzzled.com.judge;

import java.util.LinkedList;
import java.util.Queue;

public class Dispatcher {

    private Queue<SubmissionData> queue;

    public Dispatcher() {
        this.queue = new LinkedList<>();
    }

    public int getSubmissionsTotal() {
        return queue.size();
    }

    public void addSubmission(SubmissionData submissionData) {
        this.queue.add(submissionData);
    }

    public SubmissionData getSubmission() {
        return queue.remove();
    }

}
