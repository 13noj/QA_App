package jp.techacademy.hirotoshi.yoshioka.qa_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private int mGenre = 0;

    // --- ここから ---
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mFavoriteRef; //*****************************************課題追加
    private DatabaseReference mGenreRef;
    private ListView mListView;
    private ArrayList<Question> mQuestionArrayList;
    private QuestionsListAdapter mAdapter;
    private Question mQuestion;
    String title;
    String body;
    String name;
    String uid;
    String imageString;

    /*
    //////////////////////////////////課題//////////////////////////////////////////////////////////////
    private ChildEventListener mQuestionEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                HashMap<String, Object> mQuestion = (HashMap<String, Object>) entry.getValue();
                String title       = (String) mQuestion.get("title");
                String body        = (String) mQuestion.get("body");
                String name        = (String) mQuestion.get("name");
                String uid         = (String) mQuestion.get("uid");
                String imageString = (String) mQuestion.get("image"); }

            Bitmap image = null;
            byte[] bytes;
            if (imageString != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                bytes = Base64.decode(imageString, Base64.DEFAULT);
            } else {
                bytes = new byte[0];
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
*/
    //////////////////////////////////課題//////////////////////////////////////////////////////////////
    private ChildEventListener mFavoriteEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();
            String genre = (String)map.get("genre");
            String qid = dataSnapshot.getKey();

            /*
            final DatabaseReference qRef = mDatabaseReference.child(Const.ContentsPATH).child(String.valueOf(genre)).child(qid);


            String title = (String) map.get("title");
            String body = (String) map.get("body");
            String name = (String) map.get("name");
            String uid = (String) map.get("uid");
            String imageString = (String) map.get("image");
            Bitmap image = null;
            byte[] bytes;
            if (imageString != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                bytes = Base64.decode(imageString, Base64.DEFAULT);
            } else {
                bytes = new byte[0];
            }

            ArrayList<Answer> answerArrayList = new ArrayList<Answer>();
            HashMap answerMap = (HashMap) map.get("answers");
            if (answerMap != null) {
                for (Object key : answerMap.keySet()) {
                    HashMap temp = (HashMap) answerMap.get((String) key);
                    String answerBody = (String) temp.get("body");
                    String answerName = (String) temp.get("name");
                    String answerUid = (String) temp.get("uid");
                    Answer answer = new Answer(answerBody, answerName, answerUid, (String) key);
                    answerArrayList.add(answer);
                }
            }

            Question question = new Question(title, body, name, uid, dataSnapshot.getKey(), mGenre, bytes, answerArrayList);
            mQuestionArrayList.add(question);
            mAdapter.notifyDataSetChanged();
*/
            final DatabaseReference qRef = mDatabaseReference.
                    child(Const.ContentsPATH).
                    child(String.valueOf(genre)).
                    child(qid);

            qRef.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (!snapshot.exists()) return;
                            HashMap map = (HashMap)snapshot.getValue();
                            String title = (String) map.get("title");

                            String body = (String) map.get("body");
                            String name = (String) map.get("name");
                            String uid = (String) map.get("uid");
                            String imageString = (String) map.get("image");
                            Bitmap image = null;
                            byte[] bytes;
                            if (imageString != null) {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                bytes = Base64.decode(imageString, Base64.DEFAULT);
                            } else {
                                bytes = new byte[0];
                            }

                            ArrayList<Answer> answerArrayList = new ArrayList<Answer>();
                            HashMap answerMap = (HashMap) map.get("answers");
                            if (answerMap != null) {
                                for (Object key : answerMap.keySet()) {
                                    HashMap temp = (HashMap) answerMap.get((String) key);
                                    String answerBody = (String) temp.get("body");
                                    String answerName = (String) temp.get("name");
                                    String answerUid = (String) temp.get("uid");
                                    Answer answer = new Answer(answerBody, answerName, answerUid, (String) key);
                                    answerArrayList.add(answer);
                                }
                            }

                            Question question = new Question(title, body, name, uid, snapshot.getKey(), mGenre, bytes, answerArrayList);
                            mQuestionArrayList.add(question);
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void
                        onCancelled(DatabaseError
                                            firebaseError) { }
                    });


        }

        @Override
        public void onChildChanged(DataSnapshot
        dataSnapshot, String s) {
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
            String title = (String) map.get("title");
            String body = (String) map.get("body");
            String name = (String) map.get("name");
            String uid = (String) map.get("uid");
            String imageString = (String) map.get("image");
            Bitmap image = null;
            byte[] bytes;
            if (imageString != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                bytes = Base64.decode(imageString, Base64.DEFAULT);
            } else {
                bytes = new byte[0];
            }

            ArrayList<Answer> answerArrayList = new ArrayList<Answer>();
            HashMap answerMap = (HashMap) map.get("answers");
            if (answerMap != null) {
                for (Object key : answerMap.keySet()) {
                    HashMap temp = (HashMap) answerMap.get((String) key);
                    String answerBody = (String) temp.get("body");
                    String answerName = (String) temp.get("name");
                    String answerUid = (String) temp.get("uid");
                    Answer answer = new Answer(answerBody, answerName, answerUid, (String) key);
                    answerArrayList.add(answer);
                }
            }

            Question question = new Question(title, body, name, uid, dataSnapshot.getKey(), mGenre, bytes, answerArrayList);
            mQuestionArrayList.add(question);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();

            // 変更があったQuestionを探す
            for (Question question: mQuestionArrayList) {
                if (dataSnapshot.getKey().equals(question.getQuestionUid())) {
                    // このアプリで変更がある可能性があるのは回答(Answer)のみ
                    question.getAnswers().clear();
                    HashMap answerMap = (HashMap) map.get("answers");
                    if (answerMap != null) {
                        for (Object key : answerMap.keySet()) {
                            HashMap temp = (HashMap) answerMap.get((String) key);
                            String answerBody = (String) temp.get("body");
                            String answerName = (String) temp.get("name");
                            String answerUid = (String) temp.get("uid");
                            Answer answer = new Answer(answerBody, answerName, answerUid, (String) key);
                            question.getAnswers().add(answer);
                        }
                    }

                    mAdapter.notifyDataSetChanged();
                }
            }
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
    @Override
    protected void onResume() {
        super.onResume();  // Always call the superclass method first
        // ナビゲーションドロワーの設定
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            navigationView.getMenu().clear();//*********************************remove the list once and then reload depending on login condition
            navigationView.inflateMenu(R.menu.activity_main_drawer_not_login); //お気に入りがないやつ

        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer); //お気に入りがあるやつ
        }
    }
    //////////////////////////////////課題//////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Bundle extras = getIntent().getExtras();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ジャンルを選択していない場合（mGenre == 0）はエラーを表示するだけ
                if (mGenre == 0) {
                    Snackbar.make(view, "ジャンルを選択して下さい", Snackbar.LENGTH_LONG).show();
                    return;
                }

                // ログイン済みのユーザーを収録する
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user == null) {
                    // ログインしていなければログイン画面に遷移させる
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    // ジャンルを渡して質問作成画面を起動する
                    Intent intent = new Intent(getApplicationContext(), QuestionSendActivity.class);
                    intent.putExtra("genre", mGenre);
                    startActivity(intent);
                }

            }
        });




        // ナビゲーションドロワーの設定
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            navigationView.getMenu().clear();//*********************************remove the list once and then reload depending on login condition
            navigationView.inflateMenu(R.menu.activity_main_drawer_not_login); //お気に入りがないやつ

        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_main_drawer); //お気に入りがあるやつ
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                boolean favorite = false;           ///////////////////////////////////////////20170108追加
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab); //質問作成ボタン
                fab.setVisibility(VISIBLE);
                if (id == R.id.nav_hobby) {
                    mToolbar.setTitle("趣味");
                    mGenre = 1;
                } else if (id == R.id.nav_life) {
                    mToolbar.setTitle("生活");
                    mGenre = 2;
                } else if (id == R.id.nav_health) {
                    mToolbar.setTitle("健康");
                    mGenre = 3;
                } else if (id == R.id.nav_compter) {
                    mToolbar.setTitle("コンピューター");
                    mGenre = 4;
                }
                /////////////課題//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                else if (id == R.id.nav_favorite) {
                    mToolbar.setTitle("お気に入り");
                    fab.setVisibility(INVISIBLE); //課題お気に入りを選択した場合は質問作成ボタンを見えなくする
                    favorite = true;
                }
                /////////////課題//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                // 質問のリストをクリアしてから再度Adapterにセットし、AdapterをListViewにセットし直す
                mQuestionArrayList.clear();
                mAdapter.setQuestionArrayList(mQuestionArrayList);
                mListView.setAdapter(mAdapter);

                // 選択したジャンルにリスナーを登録する
                if (mGenreRef != null) {
                    mGenreRef.removeEventListener(mEventListener);
                }

                if (id != R.id.nav_favorite) {
                    mGenreRef = mDatabaseReference.child(Const.ContentsPATH).child(String.valueOf(mGenre));
                    mGenreRef.addChildEventListener(mEventListener);

                }
                else{
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //mFavoriteRef = mDatabaseReference.child(Const.ContentsPATH);
                    mGenreRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid()).child("favorites");
                    //mFavoriteRef = mDatabaseReference.child(Const.UsersPATH);
                    //mFavoriteRef = mDatabaseReference.child(Const.UsersPATH).child(user.getUid()).child("favorite");
                    //mFavoriteRef = mDatabaseReference.child(Const.ContentsPATH).child(String.valueOf(4));
                    //mGenreRef.addChildEventListener(mFavoriteEventListener);
                    mGenreRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(
                                DataSnapshot dataSnapshot, String s) {

                            HashMap map = (HashMap) dataSnapshot.getValue();
                            String genre = (String)map.get("genre");
                            String qid = dataSnapshot.getKey();

                            final DatabaseReference qRef = mDatabaseReference.
                                    child(Const.ContentsPATH).
                                    child(String.valueOf(genre)).
                                    child(qid);

                            qRef.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            if (!snapshot.exists()) return;
                                            HashMap map = (HashMap)snapshot.getValue();
                                            String title = (String) map.get("title");

                                            String body = (String) map.get("body");
                                            String name = (String) map.get("name");
                                            String uid = (String) map.get("uid");
                                            String imageString = (String) map.get("image");
                                            Bitmap image = null;
                                            byte[] bytes;
                                            if (imageString != null) {
                                                BitmapFactory.Options options = new BitmapFactory.Options();
                                                bytes = Base64.decode(imageString, Base64.DEFAULT);
                                            } else {
                                                bytes = new byte[0];
                                            }

                                            ArrayList<Answer> answerArrayList = new ArrayList<Answer>();
                                            HashMap answerMap = (HashMap) map.get("answers");
                                            if (answerMap != null) {
                                                for (Object key : answerMap.keySet()) {
                                                    HashMap temp = (HashMap) answerMap.get((String) key);
                                                    String answerBody = (String) temp.get("body");
                                                    String answerName = (String) temp.get("name");
                                                    String answerUid = (String) temp.get("uid");
                                                    Answer answer = new Answer(answerBody, answerName, answerUid, (String) key);
                                                    answerArrayList.add(answer);
                                                }
                                            }

                                            Question question = new Question(title, body, name, uid, snapshot.getKey(), mGenre, bytes, answerArrayList);
                                            mQuestionArrayList.add(question);
                                            mAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void
                                        onCancelled(DatabaseError
                                                            firebaseError) { }
                                    });
                        }
                        @Override
                        public void onChildChanged(DataSnapshot
                                                           dataSnapshot, String s) {
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
                    });
                }
                return true;
            }
        });























        // Firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // ListViewの準備
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new QuestionsListAdapter(this);
        mQuestionArrayList = new ArrayList<Question>();
        mAdapter.notifyDataSetChanged();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Questionのインスタンスを渡して質問詳細画面を起動する
                Intent intent = new Intent(getApplicationContext(), QuestionDetailActivity.class);
                intent.putExtra("question", mQuestionArrayList.get(position));
                Question q = mQuestionArrayList.get(position);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}