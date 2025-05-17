package com.flashcards.FlashcardQuizApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView questionTextView, correctAnswerTextView;
    private TextView answerTextView;
    private EditText userAnswerEditText;
    private Button checkAnswerButton;
    private Button nextQuestionButton;
    private List<Flashcard> flashcards;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.question_text_view);
        answerTextView = findViewById(R.id.answer_text_view);
        userAnswerEditText = findViewById(R.id.user_answer_edit_text);
        checkAnswerButton = findViewById(R.id.check_answer_button);
        nextQuestionButton = findViewById(R.id.next_question_button);
        correctAnswerTextView = findViewById(R.id.correct_answer);


        ConstraintLayout layout = findViewById(R.id.main);
        layout.setOnClickListener(v -> {
            hideKeyboardAndClearFocus();
        });

        flashcards = MainActivity.getFlashcards();
        Collections.shuffle(flashcards);

        nextQuestionButton.setVisibility(View.GONE);


        if (flashcards.isEmpty()) {
            questionTextView.setText("No flashcards added yet");
            checkAnswerButton.setEnabled(false);
            nextQuestionButton.setEnabled(false);
        } else {
            showQuestion();
        }

        checkAnswerButton.setOnClickListener(v -> checkAnswer());

        nextQuestionButton.setOnClickListener(v -> showNextQuestion());
    }

    private void showQuestion() {
        Flashcard currentFlashcard = flashcards.get(currentQuestionIndex);
        questionTextView.setText(currentFlashcard.getQuestion());
        answerTextView.setText(currentFlashcard.getAnswer());
        answerTextView.setVisibility(View.GONE);
        correctAnswerTextView.setVisibility(View.GONE);
        userAnswerEditText.getText().clear();
        checkAnswerButton.setEnabled(true);
        nextQuestionButton.setEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    private void checkAnswer() {
        String userAnswer = userAnswerEditText.getText().toString().trim();
        if (userAnswer.isEmpty()) {
            Toast.makeText(QuizActivity.this, "Please enter the answer !", Toast.LENGTH_SHORT).show();
            return;
        }
        Flashcard currentFlashcard = flashcards.get(currentQuestionIndex);
        String correctAnswer = currentFlashcard.getAnswer().trim();
        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            correctAnswers++;
            Toast.makeText(QuizActivity.this, "Correct answer!", Toast.LENGTH_SHORT).show();
            showNextQuestion();
            checkAnswerButton.setEnabled(true);
        } else {
            incorrectAnswers++;
            Toast.makeText(QuizActivity.this, "Incorrect answer!", Toast.LENGTH_SHORT).show();
            checkAnswerButton.setEnabled(false);
            checkAnswerButton.setVisibility(View.GONE);
            nextQuestionButton.setVisibility(View.VISIBLE);
        }

        if (!userAnswer.equalsIgnoreCase(correctAnswer)) {
            answerTextView.setVisibility(View.VISIBLE);
            userAnswerEditText.setText("            Incorrect answer!");
            userAnswerEditText.setEnabled(false);
            userAnswerEditText.setTextColor(ContextCompat.getColor(this, R.color.red));
            userAnswerEditText.setBackground(ContextCompat.getDrawable(this, R.drawable.incorrect_answer));
            correctAnswerTextView.setVisibility(View.VISIBLE);
        }
        nextQuestionButton.setEnabled(true);
    }

    private void showNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < flashcards.size()) {
            showQuestion();
        } else {
            endQuiz();
        }
        nextQuestionButton.setVisibility(View.GONE);
        checkAnswerButton.setVisibility(View.VISIBLE);
        userAnswerEditText.setEnabled(true);
        userAnswerEditText.setTextColor(ContextCompat.getColor(this, R.color.white));
        userAnswerEditText.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_border));
    }

    private void endQuiz() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("correctAnswers", correctAnswers);
        intent.putExtra("incorrectAnswers", incorrectAnswers);
        startActivity(intent);
        finish();
    }

    private void hideKeyboardAndClearFocus() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.clearFocus();
        }
    }
}