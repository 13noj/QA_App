package jp.techacademy.hirotoshi.yoshioka.qa_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoriteList extends AppCompatActivity {
    
    DatabaseReference mDatabaseReference;//*******************************added 課題
    private ListView mListView;
    private ArrayList<Question> mQuestionArrayList;
    private QuestionsListAdapter mAdapter;
    private DatabaseReference mFavoriteRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.content_main);
        super.onCreate(savedInstanceState);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.addChildEventListener(mEventListener);
        mQuestionArrayList.clear();
        mAdapter.setQuestionArrayList(mQuestionArrayList);
        mListView.setAdapter(mAdapter);
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new QuestionsListAdapter(this);
        mFavoriteRef = mDatabaseReference.child("users").child(uid).child("favorite");
        mFavoriteRef.addChildEventListener(mEventListener);
    }
    //////////////////////////////////課題//////////////////////////////////////////////////////////////
    private ChildEventListener mEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot parentDataSnapshot, String s) {
            HashMap map = (HashMap) parentDataSnapshot.getValue();
            String uid = (String) map.get("uid");
            if (!parentDataSnapshot.getKey().equals("contents")) {
                return; // 質問の情報を取りたいので、キーがcontentsではない場合、そのままリターンする
            }

            // これにより、parentDataSnapshotの子要素、つまりジャンル部分をIterableというリスト形式で取得します。
            Iterable<DataSnapshot> genreIterable = parentDataSnapshot.getChildren();
            // ジャンル部分が配列になっているので、１つずつ取り出す
            for (DataSnapshot childSnapshot : genreIterable) {
                // ジャンルに含まれる子要素、つまり質問部分をリスト形式で取得します。
                Iterable<DataSnapshot> childIterable = childSnapshot.getChildren();
                // 質問の配列になっているので、１つずつ取り出す
                for (DataSnapshot dataSnapshot : childIterable) {
                    // このdataSnapshotは質問1つ分なので、MainActivityのonChildAddedのように、質問の内容を取り出せる
                }
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
