package com.example.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.quiz.QuizContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Quiz2.db";
    private static final int DATABASE_VERSION = 1;
    private static QuizDbHelper instance;

    private SQLiteDatabase db;//referenta la baza de date actuala

    private QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized  QuizDbHelper getInstance(Context context){
        if (instance==null){
            instance=new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//crearea initiala a bazei de date
        this.db = db;


        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " +
                CategoryTable.TABLE_NAME + "( " +
                CategoryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoryTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " (" +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoryTable.TABLE_NAME + "(" + CategoryTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORY_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoryTable();
        fillQuestionsTable();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoryTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoryTable(){
        Category c1= new Category("Geografie");
        addCategory(c1);
        Category c2= new Category("Limba romana");
        addCategory(c2);
        Category c3= new Category("Cultura generala");
        addCategory(c3);
    }

    private void addCategory(Category category){
        ContentValues cv= new ContentValues();
        cv.put(CategoryTable.COLUMN_NAME, category.getName());
        db.insert(CategoryTable.TABLE_NAME,null, cv);

    }

    private void fillQuestionsTable(){
        Question q1 = new Question(" Care este cel mai lung fluviu din Europa?", "Volga", "Rin", "Nil", "Sena",1, Category.GEOGRAFIE);
        addQuestion(q1);
        Question q2 = new Question(" Cate strofe are poezia 'LUCEAFARUL' de Mihai Eminescu?", "100", "99", "50", "98",4, Category.LIMBA_ROMANA);
        addQuestion(q2);
        Question q3 = new Question(" Ce tara din America de Sud are cea mai mare suprafata?", "Peru", "Venezuela", "Argentina", "Brazilia",4, Category.GEOGRAFIE);
        addQuestion(q3);
        Question q4 = new Question(" Cate zile are un an bisect?", "366", "365", "364", "367",1, Category.CULTURA_GENERALA);
        addQuestion(q4);
        Question q5 = new Question(" In ce parc din Bucuresti se afla Muzeul Satului?", "Parcul Cismigiu", "Parcul Herastrau", "Parcul Tineretului", "Parcul IOR",2, Category.CULTURA_GENERALA);
        addQuestion(q5);
        Question q6 = new Question(" Unde s-a nascut Ion Creanga?", "Bucuresti", "Humulesti", "Botosani", "Pascani",2, 2);
        addQuestion(q6);
        Question q7 = new Question(" In ce an s-a scufundat Titanicul?", "1997", "1912", "1915", "1990",2, 3);
        addQuestion(q7);
        Question q8 = new Question("Din ce tara izvoraste Dunarea?", "Romania", "Germania", "Ucraina", "Grecia",2, 1);
        addQuestion(q8);
        Question q9 = new Question("Care este capitala Islandei?", "Dublin", "Varsovia", "Reykjavik", "Atena",3, 1);
        addQuestion(q9);
        Question q10 = new Question("Care este cel mai inalt munte de pe glob?", "Mont Blanc", "Muntele Everest", "Elbrus", "Muntele K2",2, 1);
        addQuestion(q10);
        Question q11 = new Question("Ce poet a ocupat functia de Ministru al Culturii?", "Marin Sorescu", "George Bacovia", "Tudor Arghezi", "Vasile Voiculescu",1, 2);
        addQuestion(q11);
        Question q12 = new Question("Cine a scris 'Enigma Otiliei'?", "Ion Creanga", "Mihail Sadoveanu", "George Calinescu", "Liviu Rebreanu",3, 2);
        addQuestion(q12);
        Question q13 = new Question("In ce oras a murit Nichita Stanescu?", "Bucuresti", "Brasov", "Iasi", "Bacau",1, 2);
        addQuestion(q13);
        Question q14 = new Question("Ce culoare are cutia neagra a unui avion?", "negru", "rosu", "portocaliu", "alb",3, 3);
        addQuestion(q14);
        Question q15 = new Question("Cati ani a durat Razboiul de 100 de ani?", "100", "99", "115", "116",4, 3);
        addQuestion(q15);



    }

    private void addQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionTable.TABLE_NAME, null,cv);
    }


    public List<Category> getAllCategories(){
        List<Category> categoryList =new ArrayList<>();
        db=getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM "+ CategoryTable.TABLE_NAME, null);

        if (c.moveToFirst()){
            do{
                Category category=new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoryTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoryTable.COLUMN_NAME)));
                categoryList.add(category);
            } while(c.moveToNext());

        }
        c.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ QuestionTable.TABLE_NAME, null);

        if(c.moveToFirst()){
            do{
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryID) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        String selection=QuestionTable.COLUMN_CATEGORY_ID+ " = ? ";
        String[] selectionArgs= new String[]{ String.valueOf(categoryID)};

        Cursor c = db.query(
                QuestionTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if(c.moveToFirst()){
            do{
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}
