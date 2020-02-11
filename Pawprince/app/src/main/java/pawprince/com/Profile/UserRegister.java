package pawprince.com.Profile;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import pawprince.com.R;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {

    EditText mUsername, mEmail, mAddress, mPassword, mBirthdate, mAge, mGender;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    ConstraintLayout first, second;
    ImageView imageButton;
    Button next;
    EditText mResultEt;
    ImageView mPreviewIv;
    TextView errorT;
    Uri image_uri=null;
    private StorageTask uploadTask;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;
    public static final String TAG = "TAG";
    String userID;
    String cameraPermission[];
    String storagePermission[];






    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        mResultEt = findViewById(R.id.resultEt);
        mPreviewIv = findViewById(R.id.imageIv2);
        mUsername = findViewById(R.id.etFullname);
        mEmail = findViewById(R.id.etEmail);
        mAddress = findViewById(R.id.etAddress);
        mPassword = findViewById(R.id.etPassword);
        mBirthdate = findViewById(R.id.etBirthdate);
        mAge = findViewById(R.id.etAge);
        mGender = findViewById((R.id.etGender));
        mLoginBtn = findViewById(R.id.CreateText);
        first = findViewById(R.id.first_step);
        second = findViewById(R.id.secondStep);
        next = findViewById(R.id.next);
        first.setVisibility(View.VISIBLE);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("validimage");
        imageButton =  findViewById(R.id.Kodak);



        cameraPermission = new String[]{ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //storage permission
        storagePermission = new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE};

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageImportDialog();
            }

        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String username = mUsername.getText().toString().trim();
                final String address = mAddress.getText().toString().trim();
                final String birthdate = mBirthdate.getText().toString().trim();
                final String age = mAge.getText().toString().trim();
                final String gender = mGender.getText().toString().trim();
                final String resultT = mResultEt.getText().toString().trim();
                String imagebuttonerror = "Upload image first";
                String imagebuttonnoterror = "Not Upload image first";

                if (next.getText().equals("Confirm")) {


                    if (TextUtils.isEmpty(email)) {
                        mEmail.setError("Email is required.");
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        mPassword.setError("Password is required.");
                        return;
                    }
                    if (TextUtils.isEmpty(username)) {
                        mUsername.setError(("Full name is required."));
                        return;
                    }
                    if (TextUtils.isEmpty(address)) {
                        mAddress.setError(("Address is required."));
                        return;
                    }
                    if (TextUtils.isEmpty(birthdate)) {
                        mBirthdate.setError(("Birth date is required."));
                        return;
                    }
                    if (TextUtils.isEmpty(age)) {
                        mAge.setError(("Age is required."));
                        return;
                    }
                    if (TextUtils.isEmpty(gender)) {
                        mGender.setError(("Gender is required."));
                        return;
                    }

                    if (password.length() < 6) {
                        mPassword.setError("Password must be equal to 6 and above Characters");
                        return;
                    }
                    next.setText("Complete");
                    first.setVisibility(View.GONE);
                    second.setVisibility(View.VISIBLE);

                } else if (next.getText().equals("Complete")) {

                    if (TextUtils.isEmpty(resultT)){
                        Toast.makeText(getApplicationContext(),imagebuttonerror,Toast.LENGTH_LONG).show();

                    } else {
                          fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull final Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final StorageReference Imagename = storageReference.child("image" + image_uri.getLastPathSegment());
                                    Imagename.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Toast.makeText(UserRegister.this, "User Created Enjoy Pawprince application", Toast.LENGTH_SHORT).show();
                                                    userID = fAuth.getCurrentUser().getUid();
                                                    DocumentReference documentReference = fStore.collection("users").document(userID);
                                                    Map<String, Object> user = new HashMap<>();
                                                    user.put("fName", username);
                                                    user.put("email", email);
                                                    user.put("address", address);
                                                    user.put("birthdate", birthdate);
                                                    user.put("age", age);
                                                    user.put("gender", gender);
                                                    user.put("Result Valid Id", resultT);
                                                    user.put("Image Valid Id", String.valueOf(uri));
                                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "onSuccess: user Profile is created for" + userID);
                                                        }
                                                    });
                                                    startActivity(new Intent(getApplicationContext(), Login.class));
                                                }
                                            });
                                        }
                                    });


                                } else {
                                    Toast.makeText(UserRegister.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            }
        });
    }

    //handle actionbar item clicks
    private void showImageImportDialog() {
        //items to display in dialog
        String [] items = {"Camera", "Gallery"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //set title
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    //camera option clicked
                    if(!checkCameraPermission()){
                        //camera permission not allowed, request it
                        requestCameraPermission();
                    }
                    else{
                        //permission allowed, take picture
                        pickCamera();
                    }

                }
                if(i == 1){
                    //gallery option clicked
                    if(!checkStoragePermission()){
                        //Storage permission not allowed, request it
                        requestStoragePermission();
                    }
                    else{
                        //permission allowed, take picture
                        pickGallery();
                    }
                }
            }
        });
        dialog.create().show(); //show dialog

    }
    private void pickGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        //set intent type to image
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickCamera() {
        //intent to take image from camera, it will also be save to storage to get high quality image
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "VALID ID"); // title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text"); // description
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);


    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean resultl = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && resultl;
    }

    //handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        pickCamera();
                    }
                    else{
                        Toast.makeText(this,"permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if(grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickGallery();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    //handle image result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //got image from camera
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                //got image from now crop it]
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON) //enable image guidelines
                        .start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                image_uri = data.getData();
                //got image from now crop it]
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON) //enable image guidelines
                        .start(this);
            }
        }
        //get cropped image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //set image to image view
                mPreviewIv.setImageURI(resultUri);
                //get drawble bitmap for text recognition
                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

                if (!recognizer.isOperational()) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    //get text from sb until there is no text
                    for (int it = 0; it < items.size(); it++) {
                        TextBlock myItem = items.valueAt(it);
                        sb.append(myItem.getValue());
                        sb.append("\n\n");
                        if (sb.toString().contains("TIN")){
                            Toast.makeText(this, "VERIFIED ID", Toast.LENGTH_LONG).show();
                        }else if(sb.toString().contains("DRIVER`S LICENSE")){
                            Toast.makeText(this, "VERIFIED ID", Toast.LENGTH_LONG).show();
                        }else if(sb.toString().contains("GSIS")){
                            Toast.makeText(this, "VERIFIED ID", Toast.LENGTH_LONG).show();
                        }else if(sb.toString().contains("Unified Multi-Purpose ID")){
                            Toast.makeText(this, "VERIFIED ID", Toast.LENGTH_LONG).show();
                        }else if(sb.toString().contains("Social Security System")){
                            Toast.makeText(this, "VERIFIED ID", Toast.LENGTH_LONG).show();
                        }else if(sb.toString().contains("PROFESSIONAL REGULATION COMMISSION")){
                            Toast.makeText(this, "VERIFIED ID", Toast.LENGTH_LONG).show();
                        }else if(sb.toString().contains("OFW")){
                            Toast.makeText(this, "VERIFIED ID", Toast.LENGTH_LONG).show();
                        }else if(sb.toString().contains("DRIVER'S LICENSE")){
                            Toast.makeText(this, "VERIFIED ID", Toast.LENGTH_LONG).show();
                        }else if(sb.toString().contains("REPUBLIKA NG PILIPINAS")){
                            Toast.makeText(this, "VERIFIED ID", Toast.LENGTH_LONG).show();
                        }else if(sb.toString().contains("Republic of the Philippines")){
                            Toast.makeText(this, "VERIFIED ID", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "INVALID ID", Toast.LENGTH_LONG).show();
                            return;
                        }


                    }
                    //set text to edit text
                    mResultEt.setText(sb.toString());

                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //if there is any error show it
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }

        }
    }


}







