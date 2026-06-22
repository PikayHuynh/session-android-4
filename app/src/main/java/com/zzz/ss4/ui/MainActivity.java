package com.zzz.ss4.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zzz.ss4.R;
import com.zzz.ss4.adapter.TeamAdapter;
import com.zzz.ss4.constant.Constant;
import com.zzz.ss4.model.Team;
import com.zzz.ss4.repository.impl.TeamRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private ListView listView;
  private Button btnAdd;
  private List<Team> teams;
  private SQLiteDatabase db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    init();
    makeOrOpenDB();
    showData();
    act();
    ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });
  }

  private void init() {
    listView = findViewById(R.id.lv_crud);
    btnAdd = findViewById(R.id.btn_add_team);
    btnAdd.setText("Add team");
    TextView tvTitle = findViewById(R.id.tv_crud);
    tvTitle.setText("CRUD Team");
  }

  private void act() {
    btnAdd.setOnClickListener(
        v -> {
          openIUDActivity();
        });

    listView.setOnItemClickListener(
        (parent, view, position, id) -> {
          Team team = teams.get(position);
          Intent intent = new Intent(MainActivity.this, ActivityIUD.class);
          intent.putExtra(Constant.ACTION, Constant.UPDATE);
          intent.putExtra(Constant.ID, team.getId());
          startActivity(intent);
        });
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (db != null) {
      showData();
    }
  }

  private void openIUDActivity() {
    Intent intent = new Intent(this, ActivityIUD.class);
    intent.putExtra(Constant.ACTION, Constant.ADD);
    startActivity(intent);
  }

  private void showData() {
    teams = new ArrayList<>();
    try (Cursor cursor = TeamRepository.findAll()) {
      while (cursor.moveToNext()) {
        teams.add(
            new Team(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getBlob(2),
                cursor.getString(4),
                cursor.getString(3)));
      }
    } catch (Exception e) {
      e.fillInStackTrace();
    }
    TeamAdapter adapter = new TeamAdapter(this, R.layout.row_team, teams);
    listView.setAdapter(adapter);
  }

  public void makeOrOpenDB() {
    TeamRepository.init(getApplicationContext());
    db = TeamRepository.getDb();
    String sql =
        "CREATE TABLE IF NOT EXISTS "
            + Constant.FC_TEAM_TABLE
            + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "name VARCHAR(255), "
            + "logo BLOB, "
            + "stadium VARCHAR(255), "
            + "capacity VARCHAR(255))";
    db.execSQL(sql);
  }
}
