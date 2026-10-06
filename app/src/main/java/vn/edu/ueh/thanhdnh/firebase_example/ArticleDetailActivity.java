package vn.edu.ueh.thanhdnh.firebase_example;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class ArticleDetailActivity extends AppCompatActivity {

    ImageView imgCover;
    TextView tvTitle;
    TextView tvContent;
    TextView tvViews;

    FirebaseFirestore db;

    String documentId;

    ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_article_detail
        );

        imgCover =
                findViewById(R.id.imgDetailCover);

        tvTitle =
                findViewById(R.id.tvDetailTitle);

        tvContent =
                findViewById(R.id.tvDetailContent);

        tvViews =
                findViewById(R.id.tvDetailViews);

        db =
                FirebaseFirestore.getInstance();

        documentId =
                getIntent().getStringExtra(
                        "documentId"
                );

        if (documentId == null ||
                documentId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Không tìm thấy bài viết",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

            return;
        }

        listenToArticle();

        increaseViews();
    }

    private void listenToArticle() {

        listenerRegistration =
                db.collection("articles")
                        .document(documentId)
                        .addSnapshotListener(
                                (documentSnapshot, error) -> {

                                    if (error != null) {

                                        Toast.makeText(
                                                this,
                                                "Lỗi Firebase: "
                                                        + error.getMessage(),
                                                Toast.LENGTH_LONG
                                        ).show();

                                        return;
                                    }

                                    if (documentSnapshot == null ||
                                            !documentSnapshot.exists()) {

                                        Toast.makeText(
                                                this,
                                                "Bài viết không tồn tại",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        finish();

                                        return;
                                    }

                                    updateArticleUI(
                                            documentSnapshot
                                    );
                                }
                        );
    }

    private void updateArticleUI(
            DocumentSnapshot document) {

        String title =
                document.getString("title");

        String content =
                document.getString("content");

        String imgCoverUrl =
                document.getString("img_cover");

        Long viewsValue =
                document.getLong("views");

        if (title == null) {
            title = "";
        }

        if (content == null) {
            content = "";
        }

        if (imgCoverUrl == null) {
            imgCoverUrl = "";
        }

        long views = 0;

        if (viewsValue != null) {
            views = viewsValue;
        }

        tvTitle.setText(title);

        tvContent.setText(content);

        tvViews.setText(
                "Views: " + views
        );

        Glide.with(this)
                .load(imgCoverUrl)
                .placeholder(
                        android.R.drawable.ic_menu_gallery
                )
                .error(
                        android.R.drawable.ic_delete
                )
                .into(imgCover);
    }

    private void increaseViews() {

        db.collection("articles")
                .document(documentId)
                .update(
                        "views",
                        FieldValue.increment(1)
                )
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            this,
                            "Không thể cập nhật views: "
                                    + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    @Override
    protected void onDestroy() {

        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }

        super.onDestroy();
    }
}