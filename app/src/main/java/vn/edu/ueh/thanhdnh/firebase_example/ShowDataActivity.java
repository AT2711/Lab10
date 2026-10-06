package vn.edu.ueh.thanhdnh.firebase_example;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowDataActivity extends AppCompatActivity {

    FirebaseFirestore db;

    RecyclerView recyclerView;

    List<Article> articles = new ArrayList<>();

    ArticleViewAdapter adapter;

    ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_show_data);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        FirebaseApp.initializeApp(this);

        db = FirebaseFirestore.getInstance();

        recyclerView =
                findViewById(R.id.reclyclerview);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter =
                new ArticleViewAdapter(
                        this,
                        articles
                );

        recyclerView.setAdapter(adapter);

        listenToArticles();
    }

    private void listenToArticles() {

        listenerRegistration =
                db.collection("articles")
                        .addSnapshotListener(
                                (QuerySnapshot result,
                                 FirebaseFirestoreException error) -> {

                                    if (error != null) {

                                        Toast.makeText(
                                                this,
                                                "Lỗi Firebase: "
                                                        + error.getMessage(),
                                                Toast.LENGTH_LONG
                                        ).show();

                                        return;
                                    }

                                    if (result == null) {
                                        return;
                                    }

                                    articles.clear();

                                    for (QueryDocumentSnapshot document : result) {

                                        String title =
                                                document.getString("title");

                                        String content =
                                                document.getString("content");

                                        String imgCover =
                                                document.getString("img_cover");

                                        Long viewsValue =
                                                document.getLong("views");

                                        if (title == null) {
                                            title = "";
                                        }

                                        if (content == null) {
                                            content = "";
                                        }

                                        if (imgCover == null) {
                                            imgCover = "";
                                        }

                                        long views = 0;

                                        if (viewsValue != null) {
                                            views = viewsValue;
                                        }

                                        Article article =
                                                new Article(
                                                        document.getId(),
                                                        title,
                                                        content,
                                                        imgCover,
                                                        views
                                                );

                                        articles.add(article);
                                    }

                                    adapter.update(articles);

                                    adapter.notifyDataSetChanged();

                                    if (articles.isEmpty()) {

                                        Toast.makeText(
                                                this,
                                                "Firebase chưa có bài viết",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                }
                        );
    }

    @Override
    protected void onDestroy() {

        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }

        super.onDestroy();
    }
}