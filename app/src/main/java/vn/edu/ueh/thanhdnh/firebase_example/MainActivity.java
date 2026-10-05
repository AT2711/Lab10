package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  FirebaseFirestore db;
  Button btAdd, btShow;
  EditText etName, etPhone;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);

    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

      v.setPadding(
              systemBars.left,
              systemBars.top,
              systemBars.right,
              systemBars.bottom
      );

      return insets;
    });

    FirebaseApp.initializeApp(this);

    db = FirebaseFirestore.getInstance();

    btAdd = findViewById(R.id.btAdd);
    btShow = findViewById(R.id.btShow);

    etName = findViewById(R.id.etName);
    etPhone = findViewById(R.id.etPhone);

    btAdd.setOnClickListener(this);
    btShow.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {

    if (view.getId() == R.id.btAdd) {

      String title = etName.getText().toString().trim();
      String content = etPhone.getText().toString().trim();

      if (title.isEmpty()) {
        etName.setError("Vui lòng nhập tiêu đề bài viết");
        etName.requestFocus();
        return;
      }

      if (content.isEmpty()) {
        etPhone.setError("Vui lòng nhập nội dung bài viết");
        etPhone.requestFocus();
        return;
      }

      Map<String, Object> article = new HashMap<>();

      article.put("title", title);
      article.put("content", content);

      btAdd.setEnabled(false);

      db.collection("articles")
              .add(article)
              .addOnSuccessListener(documentReference -> {

                btAdd.setEnabled(true);

                Toast.makeText(
                        MainActivity.this,
                        "Đã lưu bài viết lên Firebase",
                        Toast.LENGTH_SHORT
                ).show();

                etName.setText("");
                etPhone.setText("");
              })
              .addOnFailureListener(e -> {

                btAdd.setEnabled(true);

                Toast.makeText(
                        MainActivity.this,
                        "Lỗi Firebase: " + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
              });

    } else if (view.getId() == R.id.btShow) {

      Intent intent = new Intent(
              MainActivity.this,
              ShowDataActivity.class
      );

      startActivity(intent);
    }
  }
}