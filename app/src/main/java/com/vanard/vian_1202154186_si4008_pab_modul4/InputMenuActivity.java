package com.vanard.vian_1202154186_si4008_pab_modul4;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vanard.vian_1202154186_si4008_pab_modul4.model.ItemMenu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InputMenuActivity extends AppCompatActivity {

    private final int REQUEST_GET_SINGLE_FILE = 101;
    private final int REQUEST_CAPTURE_IMAGE = 102;

    Button btnChoose, btnTambah;
    EditText etName, etHarga, etDesc;
    ImageView img;
    Boolean a,b,c;
    Bitmap bitmap;
    Uri uri;
    String imagePath, mName, mHarga, mDesc, imgUrl;
    byte[] selectedImageBytes;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_menu);

        bindView();

        checkPermissionAndroid();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Tunggu sebentar");
        dialog.setCancelable(false);
        dialog.setProgress(0);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                a = s.toString().length() > 1;
                if (a && b && c){
                    btnTambah.setVisibility(View.VISIBLE);
                }else{
                    btnTambah.setVisibility(View.GONE);
                }
            }
        });

        etHarga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                b = s.toString().length() > 1;
                if (a && b && c){
                    btnTambah.setVisibility(View.VISIBLE);
                }else{
                    btnTambah.setVisibility(View.GONE);
                }
            }
        });

        etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                c = s.toString().length() > 1;
                if (a && b && c){
                    btnTambah.setVisibility(View.VISIBLE);
                }else{
                    btnTambah.setVisibility(View.GONE);
                }
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalery();
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
    }

    private void verify() {
        mName = etName.getText().toString().trim();
        mHarga = etHarga.getText().toString().trim();
        mDesc = etDesc.getText().toString().trim();

        if (mName.isEmpty()){
            pesan("Nama harus diisi");
            return;
        }
        if (mHarga.isEmpty()){
            pesan("Harga harus diisi");
            return;
        }
        if (mDesc.isEmpty()){
            pesan("Deskripsi harus diisi");
            return;
        }

        saveData();
    }

    private void saveData() {
        dialog.show();

        final StorageReference up = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()+""+System.currentTimeMillis());

        up.putBytes(selectedImageBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                up.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imgUrl = uri.toString();

                        saveDb();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                dialog.setProgress((int) progress);
            }
        });

    }

    private void saveDb() {
        FirebaseFirestore.getInstance().collection("ItemMenu").add(new ItemMenu(mName, mHarga, mDesc, imgUrl)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();

                    backToMain();
                }
            }
        });
    }

    private void bindView() {
        a = b = c = false;

        btnChoose = findViewById(R.id.choose_img_input);
        img = findViewById(R.id.img_input);
        etName = findViewById(R.id.nama_menu_input);
        etHarga = findViewById(R.id.harga_menu_input);
        etDesc = findViewById(R.id.deskripsi_menu_input);
        btnTambah = findViewById(R.id.tambah_btn_input);

        btnTambah.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAPTURE_IMAGE) {
                if (data != null && data.getExtras() != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    img.setImageBitmap(bitmap);
                }
            } else if (requestCode == REQUEST_GET_SINGLE_FILE) {
                uri = data.getData();
                imagePath = uri != null ? uri.getPath() : null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                            uri);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                    img.setImageBitmap(bitmap);

                    selectedImageBytes = outputStream.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void openGalery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                REQUEST_GET_SINGLE_FILE);

    }

    private void checkPermissionAndroid() {
        if (ContextCompat.checkSelfPermission(InputMenuActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("MAINACTIVITY", "checkPermissionAndroid: Read: You have already granted this permission!");
        } else {
            backToMain();
        }

        if (ContextCompat.checkSelfPermission(InputMenuActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("MAINACTIVITY", "checkPermissionAndroid: Write : You have already granted this permission!");
        } else {
            backToMain();
        }
    }

    private void backToMain(){
        Intent i = new Intent(InputMenuActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    private void pesan(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
