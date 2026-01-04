package com.example.musicplayerr;

// Section 3.3: Model class for JSON parsing
public class post {

        private int id;
        private String title;
        private String body;

        // Requirement: Empty constructor for Retrofit/GSON parsing
        public post() {
        }

        // Requirement: Parameterized constructor for Database/RecyclerView
        public post(int id, String title, String body) {
                this.id = id;
                this.title = title;
                this.body = body;
        }

        // Getters: Required for the Adapter and SQLite to read data
        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getBody() { return body; }
}