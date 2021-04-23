package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizapp.QuizContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyQuiz.db";
    public static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    private QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + " ( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                " ) ";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                " ) ";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("Programming");
        insertCategory(c1);
        Category c2 = new Category("Geography");
        insertCategory(c2);
        Category c3 = new Category("Math");
        insertCategory(c3);
    }

    public void addCategory(Category category) {
        db = getWritableDatabase();
        insertCategory(category);
    }

    public void addCategories(List<Category> categories) {
        db = getWritableDatabase();

        for(Category category : categories) {
            insertCategory(category);
        }
    }

    private void insertCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("Which one of the following cannot be used with the virtual keyword?",
                "Destructor", "Constructor", "None of the above", 2,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q1);
        Question q2 = new Question("Which of the following is the address operator?",
                "@", "#", "&", 3,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q2);
        Question q3 = new Question("Which of the following features must be supported by any programming language to become a pure object-oriented programming language?",
                "Inheritance", "Polymorphism", "All of the above", 3,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q3);
        Question q4 = new Question("The programming language that has the ability to create new data types is called___",
                "Overloaded", "Extensible", "Encapsulated", 2,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q4);
        Question q5 = new Question("C++ is a ___ type of language.",
                "Middle-level language", "Low-level language", "High-level language", 1,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q5);
        Question q6 = new Question("Which of the following statements is correct about the class?",
                "An object is an instance of its class", "A class is an instance of its object", "both", 1,
                Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        insertQuestion(q6);
        Question q7 = new Question("Which of the following is the correct syntax for declaring the array?",
                "init array []", "int array [5];", "Array[5];", 2,
                Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        insertQuestion(q7);
        Question q8 = new Question("Which of the following gives the 4th element of the array?",
                "Array[0];", "Array[4];", "Array[3];", 3,
                Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        insertQuestion(q8);
        Question q9 = new Question("Which of the following is the correct syntax for printing the address of the first element?",
                "array[0];", "array[1];", "array[2];", 1,
                Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        insertQuestion(q9);
        Question q10 = new Question("Which type of memory is used by an Array in C++ programming language?",
                "Contiguous", "None-contiguous", "Both A and B", 1,
                Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        insertQuestion(q10);
        Question q11 = new Question("In C++, for what purpose the \"rank()\" is used?",
                "It returns the size of each dimension", "It returns the dimension of the specified array", "None of the above", 2,
                Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        insertQuestion(q11);
        Question q12 = new Question("Which types of arrays are always considered as linear arrays?",
                "Single Dimensional array", "Multi-Dimensional array", "Both A and B", 1,
                Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        insertQuestion(q12);
        Question q13 = new Question("Which of the following can be considered as the object of an array?",
                "Index of an array", "Elements of the Array", "All of the above", 2,
                Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        insertQuestion(q13);
        Question q14 = new Question("How many types of elements can an array store?",
                "Same types of elements", "Char and int type", "Only char types", 1,
                Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        insertQuestion(q14);
        Question q15 = new Question("Which of the following can be considered as the members that can be inherited but not accessible in any class?",
                "Public", "Protected", "Private", 3,
                Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        insertQuestion(q15);
        Question g1 = new Question("Which of the following is studied under Physical geography?",
                "Human", "Soils", "Water", 2,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        insertQuestion(g1);
        Question g2 = new Question("The logo of World Wildlife Fund (WWF) is depicts which among the following animals?",
                "Panda", "Tiger", "Lion", 1,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        insertQuestion(g2);
        Question g3 = new Question("Which among the following is Brightest star?",
                "Sirius", "Alpha centauri", "Proxima Centauri", 1,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        insertQuestion(g3);
        Question g4 = new Question("In which type of rocks the Fossil Fuel can be found?",
                "Igneous", "Sedimenatry", "Metamorphic", 2,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        insertQuestion(g4);
        Question g5 = new Question("Capital of which of the following states is called “Scotland of the east”?",
                "Manipur", "Sikkim", "Meghalaya", 3,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        insertQuestion(g5);
        Question g6 = new Question("Which among the following is a landlocked sea?",
                "Timor Sea", "Arafura Sea", "Aral Sea", 3,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(g6);
        Question g7 = new Question("After Brazil, which among the following is the second largest country in South America?",
                "Columbia", "Argentina", "Peru", 2,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(g7);
        Question g8 = new Question("Which of the following archipelago is located in Indian Ocean?",
                "Seto Inland Sea", "Maldive Islands", "Dalmatia", 2,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(g8);
        Question g9 = new Question("Which among the following celestial bodies does not have their own light?",
                "Galaxy", "Sun", "Planets", 3,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(g9);
        Question g10 = new Question("What is the approximate distance between the Earth and the Sun ?",
                "350 million km", "250 million km", "150 million km", 3,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(g10);
        Question g11 = new Question("What of the following device is used to measure the diameter of stars?",
                "Barometer", "Interferometer", "Photometer", 2,
                Question.DIFFICULTY_HARD, Category.GEOGRAPHY);
        insertQuestion(g11);
        Question g12 = new Question("What is the temperature at the outer surface of the Sun?",
                "6000 degrees C", "7000 degrees C", "4000 degrees C", 1,
                Question.DIFFICULTY_HARD, Category.GEOGRAPHY);
        insertQuestion(g12);
        Question g13 = new Question("What is the largest division of the geologic time scale?",
                "Eon", "Era", "Period", 1,
                Question.DIFFICULTY_HARD, Category.GEOGRAPHY);
        insertQuestion(g13);
        Question g14 = new Question("Which star is directly above North Pole?",
                "Sirius", "Polaris", "Vega", 2,
                Question.DIFFICULTY_HARD, Category.GEOGRAPHY);
        insertQuestion(g14);
        Question g15 = new Question("Which planet is referred to as Earth’s ‘sister planet’?",
                "Mercury", "Mars", "Venus", 3,
                Question.DIFFICULTY_HARD, Category.GEOGRAPHY);
        insertQuestion(g15);
        Question m1 = new Question("2+2 is?",
                "22", "12", "4", 3,
                Question.DIFFICULTY_EASY, Category.MATH);
        insertQuestion(m1);
        Question m2 = new Question("2-2 is?",
                "7", "0", "8", 2,
                Question.DIFFICULTY_EASY, Category.MATH);
        insertQuestion(m2);
        Question m3 = new Question("7*9 is?",
                "76", "89", "63", 3,
                Question.DIFFICULTY_EASY, Category.MATH);
        insertQuestion(m3);
        Question m4 = new Question("30/3 ?",
                "11", "10", "8", 2,
                Question.DIFFICULTY_EASY, Category.MATH);
        insertQuestion(m4);
        Question m5 = new Question("2%5",
                "2", "5", "0", 1,
                Question.DIFFICULTY_EASY, Category.MATH);
        insertQuestion(m5);
        Question m6 = new Question("The average weight of 16 boys in a class is 50.25 kg and that of the remaining 8 boys is 45.15 kg. Find the average weights of all the boys in the class.",
                "68.55 kg", "48.55 kg", "58.55 kg", 2,
                Question.DIFFICULTY_MEDIUM, Category.MATH);
        insertQuestion(m6);
        Question m7 = new Question("The average weight of A, B and C is 45 kg. If the average weight of A and B be 40 kg and that of B and C be 43 kg, then the weight of B is___________?",
                "31 kg", "28 kg", "21 kg", 1,
                Question.DIFFICULTY_MEDIUM, Category.MATH);
        insertQuestion(m7);
        Question m8 = new Question("A person travels from x to y at a speed of 40Km/h and returns by increasing his speed 50%. What is his average speed for both the trips?",
                "44km/h", "50km/h", "48km/h", 3,
                Question.DIFFICULTY_MEDIUM, Category.MATH);
        insertQuestion(m8);
        Question m9 = new Question("The mean marks of 30 students in a class is 58.5. Later on it was found that 75 was wrongly recorded as 57. Find the correct them.",
                "59.7", "59.1", "59.5", 2,
                Question.DIFFICULTY_MEDIUM, Category.MATH);
        insertQuestion(m9);
        Question m10 = new Question("The average of five consecutive odd numbers is 61. What is the difference between the highest and lowest numbers?",
                "8", "12", "4", 1,
                Question.DIFFICULTY_MEDIUM, Category.MATH);
        insertQuestion(m10);
        Question m11 = new Question("The angle of elevation of the sun, when the length of the shadow of a tree is √3 times the height of the tree, is :________?",
                "35°", "40°", "30°", 3,
                Question.DIFFICULTY_HARD, Category.MATH);
        insertQuestion(m11);
        Question m12 = new Question("If the height of a pole is 2√3 metres and the length of its shadow is 2 metres, find the angle of elevation of the sun.",
                "50°", "60°", "70°", 2,
                Question.DIFFICULTY_HARD, Category.MATH);
        insertQuestion(m12);
        Question m13 = new Question("The angle of elevation of a ladder leaning against a wall is 60° and the foot of the ladder is 4.6 m away from the wall. The length of the ladder is :_________?",
                "8.2 m", "7.2 m", "9.2 m", 3,
                Question.DIFFICULTY_HARD, Category.MATH);
        insertQuestion(m13);
        Question m14 = new Question("18 men can eat 20 kg of rice in 3 days. How long will 6 men take to eat 40 kg of rice?",
                "22", "18", "20", 2,
                Question.DIFFICULTY_HARD, Category.MATH);
        insertQuestion(m14);
        Question m15 = new Question("In a fort there is sufficient food for 600 men for a month. If 400 more men arrive the fort then how long the food is sufficient for now?",
                "18 days", "20 days", "20 days", 1,
                Question.DIFFICULTY_HARD, Category.MATH);
        insertQuestion(m15);

    }

    public void addQuestion(Question question) {
        db = getWritableDatabase();
        insertQuestion(question);
    }

    public void addQuestions(List<Question> questions) {
        db = getWritableDatabase();

        for (Question question : questions) {
            insertQuestion(question);
        }
    }

    private void insertQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionTable.TABLE_NAME, null, cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }

        c.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();

        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = db.query(
                QuestionTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
