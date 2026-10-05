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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

public class ShowDataActivity extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView recyclerView;
    List<User> users = new ArrayList<>();
    UserViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_data);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(this);

        if (firebaseApp == null) {
            Toast.makeText(this, "Firebase chưa được cấu hình", Toast.LENGTH_LONG).show();
            return;
        }

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.reclyclerview);
        adapter = new UserViewAdapter(this, users);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadDataFromServer();
    }

    private void loadDataFromServer() {
        db.collection("users")
                .get(Source.SERVER)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        QuerySnapshot result = task.getResult();

                        users.clear();

                        for (QueryDocumentSnapshot document : result) {
                            String name = document.getString("name");
                            String phone = document.getString("phone");

                            if (name == null) {
                                name = "";
                            }

                            if (phone == null) {
                                phone = "";
                            }

                            users.add(new User(name, phone));
                        }

                        adapter.update(users);
                        adapter.notifyDataSetChanged();

                        if (users.isEmpty()) {
                            Toast.makeText(
                                    this,
                                    "Firebase chưa có dữ liệu",
                                    Toast.LENGTH_SHORT
                            ).show();
                        } else {
                            Toast.makeText(
                                    this,
                                    "Đã tải " + users.size() + " dữ liệu từ Firebase",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    } else {
                        Exception e = task.getException();

                        String message = e != null
                                ? e.getMessage()
                                : "Không thể đọc dữ liệu";

                        Toast.makeText(
                                this,
                                "Lỗi Firebase: " + message,
                                Toast.LENGTH_LONG
                        ).show();
                    }
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

