package jp.techacademy.hirotoshi.yoshioka.qa_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import android.widget.ImageButton; //*******************************added 課題
import static android.view.View.INVISIBLE; //*******************************added 課題
import static android.view.View.VISIBLE; //*******************************added 課題

public class QuestionDetailActivity extends AppCompatActivity {
    private ListView mListView;
    private Question mQuestion;
    private QuestionDetailListAdapter mAdapter;

    private DatabaseReference mAnswerRef;

    private ImageButton imageButton; //*******************************added 課題
    private DatabaseReference mDatabaseReference;//*******************************added 課題
    private DatabaseReference mFavoriteRef;//*******************************added 課題


    private ChildEventListener mEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();

            String answerUid = dataSnapshot.getKey();

            for(Answer answer : mQuestion.getAnswers()) {
                // 同じAnswerUidのものが存在しているときは何もしない
                if (answerUid.equals(answer.getAnswerUid())) {
                    return;
                }
            }

            String body = (String) map.get("body");
            String name = (String) map.get("name");
            String uid = (String) map.get("uid");

            Answer answer = new Answer(body, name, uid, answerUid);
            mQuestion.getAnswers().add(answer);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        // 渡ってきたQuestionのオブジェクトを保持する
        Bundle extras = getIntent().getExtras();
        mQuestion = (Question) extras.get("question");

        setTitle(mQuestion.getTitle());

        // ListViewの準備
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new QuestionDetailListAdapter(this, mQuestion);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ログイン済みのユーザーを収録する
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user == null) {
                    // ログインしていなければログイン画面に遷移させる
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    // Questionを渡して回答作成画面を起動する

                    Intent intent = new Intent(getApplicationContext(), AnswerSendActivity.class);
                    intent.putExtra("question", mQuestion);
                    startActivity(intent);
                    // --- ここまで ---
                }
            }
        });

        //********************************************************************課題
        imageButton = (ImageButton) findViewById(R.id.favoriteButton); //お気に入りボタン
        //ログインしていなければ見えない

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            imageButton.setVisibility(INVISIBLE);         //デフォルトではお気に入りボタンが見えない。ログインしているとお気に入りに登録ボタンが現れる。
        }
        else{imageButton.setVisibility(VISIBLE);
        }


        if (user != null) {
            mDatabaseReference = FirebaseDatabase.getInstance().getReference();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mFavoriteRef = mDatabaseReference.child(Const.UsersPATH).child(uid).child("favorites");
            mFavoriteRef.addChildEventListener(mFavoriteEventListener);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButton.setImageResource(R.drawable.after);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mFavoriteRef = mDatabaseReference;
                mFavoriteRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid()).child("favorites");
                mFavoriteRef.addChildEventListener(mFavoriteEventListener);
                Map<String, String> favoriteData = new HashMap<String, String>();
                favoriteData.put("genre", String.valueOf(mQuestion.getGenre())); ////////////////user name
                favoriteData.put(mQuestion.getQuestionUid(), mQuestion.getQuestionUid());
                favoriteData.put("questionUid", mQuestion.getQuestionUid()); // ここです
                FirebaseDatabase.getInstance().getReference().child(Const.UsersPATH).child(user.getUid()).child("favorites").push().setValue(favoriteData);
            }
        });
        //**********************************************************************
    }

    //////////////////////////////////課題//////////////////////////////////////////////////////////////
    private ChildEventListener mFavoriteEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();
            String questionUid = (String) map.get("questionUid"); // ここです
            Log.d("test", "questionUid is:" + questionUid);
            if (questionUid.equals(mQuestion.getQuestionUid())){
                imageButton.setImageResource(R.drawable.after);
                String answerUid = dataSnapshot.getKey();
                String body = (String) map.get("body");
                String name = (String) map.get("name");
                String uid = (String) map.get("uid");
                Answer answer = new Answer(body, name, uid, answerUid);
                mQuestion.getAnswers().add(answer);
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };
//////////////////////////////////課題//////////////////////////////////////////////////////////////




}