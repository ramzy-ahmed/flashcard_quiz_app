package com.flashcards.FlashcardQuizApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import com.google.gson.Gson;
import java.util.List;

public class AddFlashcardActivity extends AppCompatActivity {
    private EditText questionEditText;
    private EditText answerEditText;
    private Button saveButton;
    private SharedPreferences sharedPreferences;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flashcard);

        questionEditText = findViewById(R.id.question_edit_text);
        answerEditText = findViewById(R.id.answer_edit_text);
        saveButton = findViewById(R.id.save_flashcard_button);

        sharedPreferences = getSharedPreferences("FlashcardPrefs", Context.MODE_PRIVATE);
        gson = new Gson();

        saveButton.setOnClickListener(v -> {
            String question = questionEditText.getText().toString();
            String answer = answerEditText.getText().toString();

            if (question.isEmpty() || answer.isEmpty()) {
                Toast.makeText(AddFlashcardActivity.this, "Please fill in both question and answer", Toast.LENGTH_SHORT).show();
                return;
            }

            Flashcard newFlashcard = new Flashcard(question, answer);
            List<Flashcard> flashcards = MainActivity.getFlashcards();
            flashcards.add(newFlashcard);
            saveFlashcards(flashcards);
            questionEditText.getText().clear();
            answerEditText.getText().clear();
            Toast.makeText(AddFlashcardActivity.this, "Flashcard added", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveFlashcards(List<Flashcard> flashcards) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(flashcards);
        editor.putString("flashcards", json);
        editor.apply();
        MainActivity.setFlashcards(flashcards);
    }
}