package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore";
    public static final long COUNTDOWN_IN_MILLIS = 30000;

    public static final String KEY_SCORE = "keyScore";
    public static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    public static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    public static final String KEY_ANSWERED = "keyAnswered";
    public static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView text_view_score;
    private TextView text_view_question_count;
    private TextView textViewCategory;
    private TextView text_view_difficulty;
    private TextView text_view_countdown;
    private TextView text_view_question;
    private RadioGroup radio_group;
    private RadioButton radio_button1;
    private RadioButton radio_button2;
    private RadioButton radio_button3;
    private Button button_confirm_next;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;


    protected ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        text_view_score = findViewById(R.id.text_view_score);
        text_view_question_count = findViewById(R.id.text_view_question_count);
        textViewCategory = findViewById(R.id.text_view_category);
        text_view_difficulty = findViewById(R.id.text_view_difficulty);
        text_view_countdown = findViewById(R.id.text_view_countdown);
        text_view_question = findViewById(R.id.text_view_question);
        radio_group = findViewById(R.id.radio_group);
        radio_button1 = findViewById(R.id.radio_button1);
        radio_button2 = findViewById(R.id.radio_button2);
        radio_button3 = findViewById(R.id.radio_button3);
        button_confirm_next = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = radio_button1.getTextColors();
        textColorDefaultCd = text_view_countdown.getTextColors();

        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(MainActivity.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);
        String difficulty = intent.getStringExtra(MainActivity.EXTRA_DIFFICULTY);

        textViewCategory.setText("Category: " + categoryName);
        text_view_difficulty.setText("Difficulty: " + difficulty);

        if (savedInstanceState == null) {
            QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
            questionList = dbHelper.getQuestions(categoryID, difficulty);
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if (!answered) {
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }

        button_confirm_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (radio_button1.isChecked() || radio_button2.isChecked() || radio_button3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select the answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        radio_button1.setTextColor(textColorDefaultRb);
        radio_button2.setTextColor(textColorDefaultRb);
        radio_button3.setTextColor(textColorDefaultRb);
        radio_group.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            text_view_question.setText(currentQuestion.getQuestion());
            radio_button1.setText(currentQuestion.getOption1());
            radio_button2.setText(currentQuestion.getOption2());
            radio_button3.setText(currentQuestion.getOption3());

            questionCounter++;
            text_view_question_count.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            button_confirm_next.setText("Confirm");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        text_view_countdown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            text_view_countdown.setTextColor(Color.RED);
        } else {
            text_view_countdown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        answered = true;

        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(radio_group.getCheckedRadioButtonId());
        int answerNr = radio_group.indexOfChild(rbSelected) + 1;

        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            text_view_score.setText("Score: " + score);
        }

        showSolution();
    }

    private void showSolution() {
        radio_button1.setTextColor(Color.RED);
        radio_button2.setTextColor(Color.RED);
        radio_button3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNr()) {
            case 1:
                radio_button1.setTextColor(Color.GREEN);
                text_view_question.setText("Answer 1 is correct");
                break;
            case 2:
                radio_button2.setTextColor(Color.GREEN);
                text_view_question.setText("Answer 2 is correct");
                break;
            case 3:
                radio_button3.setTextColor(Color.GREEN);
                text_view_question.setText("Answer 3 is correct");
                break;
        }

        if (questionCounter < questionCountTotal) {
            button_confirm_next.setText("Next");
        } else {
            button_confirm_next.setText("Finish");
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}