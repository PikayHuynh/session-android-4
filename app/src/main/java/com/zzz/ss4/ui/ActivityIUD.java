package com.zzz.ss4.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.zzz.ss4.R;
import com.zzz.ss4.constant.Constant;
import com.zzz.ss4.repository.impl.TeamRepository;
import com.zzz.ss4.util.BitmapUtil;
import com.zzz.ss4.util.StringUtil;


import java.io.FileNotFoundException;
import java.io.InputStream;

public class ActivityIUD extends AppCompatActivity {
  private static final int REQUEST_CODE = 111;
  private static final String NAME = "name";
  private static final String STADIUM = "stadium";
  private static final String CAPACITY = "capacity";
  private static final String LOGO = "logo";
  private Integer id;
  private ImageView imgLogo;
  private EditText edtName, edtStadium, edtCapacity;
  private Button btnExit, btnAdd, btnDelete, btnChoose;
    private Bitmap bitmap;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_iud);
    TeamRepository.init(getApplicationContext());
    init();
    switchButtonReq();
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
      TextView tvTitle = findViewById(R.id.tv_title_uid);
    tvTitle.setText("Chức năng");
    imgLogo = findViewById(R.id.img_logo_iud);
    Glide.with(this).load(R.drawable.java).circleCrop().into(imgLogo);
    edtName = findViewById(R.id.edt_name_iud);
    edtCapacity = findViewById(R.id.edt_capacity_iud);
    edtStadium = findViewById(R.id.edt_stadium_iud);
    btnAdd = findViewById(R.id.btn_save_iud);
    btnAdd.setText(Constant.ADD);
    btnChoose = findViewById(R.id.btn_choose_iud);
    btnChoose.setText(Constant.CHOOSE);
    btnExit = findViewById(R.id.btn_exit_iud);
    btnExit.setText(Constant.EXIT);
    btnDelete = findViewById(R.id.btn_delete_iud);
    btnDelete.setText(Constant.DELETE);
  }

  private void act() {
    btnExit.setOnClickListener(v -> finish());

    btnChoose.setOnClickListener(v -> chooseImage());

    btnAdd.setOnClickListener(v -> saveTeam());

    btnDelete.setOnClickListener(v -> deleteTeam());
  }

  private void chooseImage() {
    Intent intent = new Intent(Intent.ACTION_PICK);
    intent.setType("image/*");
    startActivityForResult(intent, REQUEST_CODE);
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
      Uri uri = data.getData();
      try {
        if (uri != null) {
          InputStream inputStream = getContentResolver().openInputStream(uri);
          bitmap = BitmapFactory.decodeStream(inputStream);
          Glide.with(this).load(bitmap).circleCrop().into(imgLogo);
        }
      } catch (FileNotFoundException e) {
        e.fillInStackTrace();
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void saveTeam() {
    String name = StringUtil.cast(edtName.getText());
    String capacity = StringUtil.cast(edtCapacity.getText());
    String stadium = StringUtil.cast(edtStadium.getText());
    byte[] logo = bitmap != null ? BitmapUtil.getBytes(bitmap) : null;
    ContentValues values = new ContentValues();
    values.put(NAME, name);
    values.put(CAPACITY, capacity);
    values.put(STADIUM, stadium);
    values.put(LOGO, logo);

    String action = getIntent().getStringExtra(Constant.ACTION);
    if (action == null) action = Constant.ADD;

    switch (action) {
      case Constant.ADD -> {
        if (!isEmptyData()) {
          Toast.makeText(this, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
        } else {
          long code = TeamRepository.save(values);
          if (code == -1) {
            Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            finish();
          }
        }
      }
      case Constant.UPDATE -> {
        if (!isEmptyData()) {
          Toast.makeText(this, "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
        } else {
          long code = TeamRepository.save(values, id);
          if (code <= 0) {
            Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish();
          }
        }
      }
    }
  }

  private void deleteTeam() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setIcon(android.R.drawable.ic_delete);
    builder.setTitle("Xóa Đội bóng");
    builder.setMessage("Bạn có muốn xóa?");
    builder.setPositiveButton(
        "Có",
        (dialog, which) -> {
          int code = TeamRepository.delete(id);
          if (code < 1) {
            Toast.makeText(ActivityIUD.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(ActivityIUD.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            finish();
          }
        });

    // khong xoa
    builder.setNegativeButton("không", (dialog, which) -> {});
    AlertDialog dialog = builder.create();
    dialog.show();
  }

  private boolean isEmptyData() {
    String name = StringUtil.cast(edtName.getText());
    String capacity = StringUtil.cast(edtCapacity.getText());
    String stadium = StringUtil.cast(edtStadium.getText());
    return !name.isEmpty() && !capacity.isEmpty() && !stadium.isEmpty() && bitmap != null;
  }

  private void switchButtonReq() {
    Intent intent = this.getIntent();
    String action = intent.getStringExtra(Constant.ACTION);
    if (action == null) return;
    switch (action) {
      case Constant.ADD -> {
        btnDelete.setEnabled(false);
        btnAdd.setText(Constant.ADD);
      }
      case Constant.UPDATE -> {
        btnAdd.setText(Constant.UPDATE);
        btnDelete.setEnabled(true);
        id = intent.getIntExtra(Constant.ID, -1);
        if (id == -1) {
          finish();
        } else {
          Cursor cursor = TeamRepository.findById(id);
          if (cursor.moveToFirst()) {
            edtName.setText(cursor.getString(1));
            byte[] logoBlob = cursor.getBlob(2);
            if (logoBlob != null) {
              bitmap = BitmapUtil.getBitmap(logoBlob);
              Glide.with(this).load(bitmap).circleCrop().into(imgLogo);
            }
            edtStadium.setText(cursor.getString(3));
            edtCapacity.setText(cursor.getString(4));
            cursor.close();
          }
        }
      }
    }
  }
}
