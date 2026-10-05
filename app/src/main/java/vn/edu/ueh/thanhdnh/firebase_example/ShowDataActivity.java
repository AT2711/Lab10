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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

public class ShowDataActivity extends AppCompatActivity {

    FirebaseFirestore db;

    RecyclerView recyclerView;

    List<Article> articles = new ArrayList<>();

    ArticleViewAdapter adapter;

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

        recyclerView = findViewById(R.id.reclyclerview);

        adapter = new ArticleViewAdapter(
                this,
                articles
        );

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerView.setAdapter(adapter);

        loadDataFromServer();
    }

    private void loadDataFromServer() {

        db.collection("articles")
                .get(Source.SERVER)
                .addOnSuccessListener(result -> {

                    articles.clear();

                    for (QueryDocumentSnapshot document : result) {

                        String title = document.getString("title");
                        String content = document.getString("content");

                        if (title == null) {
                            title = "";
                        }

                        if (content == null) {
                            content = "";
                        }

                        Article article = new Article(
                                title,
                                content
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

                    } else {

                        Toast.makeText(
                                this,
                                "Đã tải "
                                        + articles.size()
                                        + " bài viết từ Firebase",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            "Lỗi Firebase: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null && adapter != null) {
            loadDataFromServer();
        }
    }
}