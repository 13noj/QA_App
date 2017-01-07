package jp.techacademy.hirotoshi.yoshioka.qa_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class QuestionDetailActivity extends AppCompatActivity {
    private ImageButton imageButton; //*******************************added 課題
    private ListView mListView;
    private Question mQuestion;
    private QuestionDetailListAdapter mAdapter;
    private DatabaseReference mDatabaseReference;//*******************************added 課題
    private DatabaseReference mFavoriteRef;//*******************************added 課題
    private DatabaseReference mAnswerRef;
    private String imageString;
//////////////////////////////////課題//////////////////////////////////////////////////////////////
  private ChildEventListener mFavoriteEventListener = new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot dataSnapshot, String s) {
          HashMap map = (HashMap) dataSnapshot.getValue();

          String questionUid = (String) map.get("questionUid"); // ここです

          if (questionUid.equals(mQuestion.getQuestionUid())){
              imageButton.setImageResource(R.drawable.after);
              String answerUid = dataSnapshot.getKey();
              String body = (String) map.get("body");
              String name = (String) map.get("name");
              String uid = (String) map.get("uid");
              imageString = (String) map.get("image");
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
            mFavoriteRef = mDatabaseReference.child("users").child(uid).child("favorite");
            mFavoriteRef.addChildEventListener(mFavoriteEventListener);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButton.setImageResource(R.drawable.after);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //////////////////UID 課題

                Map<String, String> favoriteData = new HashMap<String, String>();
                favoriteData.put(mQuestion.getQuestionUid(), mQuestion.getQuestionUid());
                favoriteData.put("questionUid", mQuestion.getQuestionUid()); // ここです

                byte[] bytes;
                //favoriteData.put("image", mQuestion.getImageBytes().Base64.encodeToString());


                favoriteData.put("title", mQuestion.getTitle());           //////////////////////タイトル取得課題。
                favoriteData.put("name", mQuestion.getName());           //////////////////////表示名取得課題。
                favoriteData.put("uid", uid); ////////////////user name
                favoriteData.put("image", imageString); ////////////////user name
                FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("favorite").push().setValue(favoriteData);
            }
        });
        //**********************************************************************
        }

}