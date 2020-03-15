package dev.puzzled.com.database;

import com.mongodb.*;
import dev.puzzled.com.Main;
import dev.puzzled.com.judge.SubmissionData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Database {

    private MongoClient mongoClient;
    private DB database;

    public Database() {
        try {
            this.mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            this.database = this.mongoClient.getDB("puzzledJudge");
            System.out.println("MongoDB connected");
        } catch (Exception exc) {
            System.out.println("Database not found.");
        }
    }

    public void getSubmissions() {
        DBCollection collection = this.database.getCollection("submissions");
        DBCursor submission = collection.find();
        while(submission.hasNext()) {
            DBObject object = submission.next();

            String status = (String) object.get("status");

            if(status.equals("pending")) {
                String username = (String) object.get("username");
                String fileName = (String) object.get("fileName");
                int problemId = (int) object.get("problem");

                Main.getDispatcher().addSubmission(new SubmissionData(username, fileName, problemId));
            }
        }
    }

    public void updateSubmission(String fileName, String key, String value) {
        DBCollection collection = this.database.getCollection("submissions");
        BasicDBObject query = new BasicDBObject();
        query.put("fileName", fileName);

        BasicDBObject update = new BasicDBObject();
        update.put(key, value);

        BasicDBObject updateObject = new BasicDBObject();
        updateObject.put("$set", update);

        collection.update(query, updateObject);
    }

    public List<List<String>> getInput(int id) {
        DBCollection collection = this.database.getCollection("problems");
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        List<List<String>> sol = new LinkedList<>();
        DBCursor problem = collection.find(query);
        while(problem.hasNext()) {
            DBObject object = problem.next();

            List<String> inputs = (ArrayList<String>) object.get("inputs");
            List<String> outputs = (ArrayList<String>) object.get("outputs");

            sol.add(inputs);
            sol.add(outputs);
            break;
        }
        return (sol.size() > 0) ? sol : null;
    }

}
