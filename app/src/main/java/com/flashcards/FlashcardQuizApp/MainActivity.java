package com.flashcards.FlashcardQuizApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button addFlashcardButton, startQuizButton, clearButton;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private static List<Flashcard> flashcards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("FlashcardPrefs", Context.MODE_PRIVATE);
        gson = new Gson();
        loadFlashcards();
        addFlashcardButton = findViewById(R.id.add_flashcard_button);
        startQuizButton = findViewById(R.id.start_quiz_button);
        clearButton = findViewById(R.id.clear_flashcards_button);

        addFlashcardButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddFlashcardActivity.class);
            startActivity(intent);
        });

        startQuizButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            startActivity(intent);
        });

        clearButton.setOnClickListener(v -> {
            clearFlashcards();
        });
    }

    private void loadFlashcards() {
        String json = sharedPreferences.getString("flashcards", null);
        Type type = new TypeToken<ArrayList<Flashcard>>() {
        }.getType();
        if (json != null) {
            flashcards = gson.fromJson(json, type);
        } else {
            flashcards = new ArrayList<>();
        }
    }

    private void clearFlashcards() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("flashcards");
        editor.apply();
        MainActivity.setFlashcards(new ArrayList<>());
        Toast.makeText(this, "Flashcards cleared", Toast.LENGTH_SHORT).show();
    }

    public static void setFlashcards(List<Flashcard> flashcards) {
        MainActivity.flashcards = flashcards;
    }

    public static List<Flashcard> getFlashcards() {
        return flashcards;
    }
}