package com.example.hp.in_a_click.frg_user;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import com.example.hp.in_a_click.model.ImageUploadInfo;
import com.example.hp.in_a_click.signinout.DriverSignInOutActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;

import com.example.hp.in_a_click.R;
import com.example.hp.in_a_click.model.UserNormal;
import com.example.hp.in_a_click.residemenu.MenuActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.irozon.sneaker.Sneaker;
import com.maksim88.passwordedittext.PasswordEditText;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.special.ResideMenu.ResideMenu;

import dmax.dialog.SpotsDialog;

public class FrgUserProfile extends Fragment {

    public RequestManager mGlideRequestManager;
    FirebaseUser firebaseUser = null;
    Context context = null;
    FloatingActionButton fabSaveChanges = null, fabTakePhoto = null;
    SpotsDialog dialogSaving = null;
    MaterialEditText etEmail, etCity, etName, etPhone;
    PasswordEditText etPass;
    Uri selectedUri;
    int PLACE_PICKER_REQUEST = 1;
    PlacePicker.IntentBuilder builderPlacePicker = null;
    CircleImageView ivProfile = null;
    String placeName = "";
    boolean imageChanged = false;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage = null;
    private View parentView;
    private ResideMenu resideMenu;
    private ViewGroup mSelectedImagesContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getDataIfExist();
        accessFields();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void accessFields() {
        etPass.addTextChangedListener(addTextWatcherForEditText());
        etEmail.addTextChangedListener(addTextWatcherForEditText());
        etPhone.addTextChangedListener(addTextWatcherForEditText());
        etName.addTextChangedListener(addTextWatcherForEditText());
        etCity.addTextChangedListener(addTextWatcherForEditText());
        etCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    try {
                        startActivityForResult(builderPlacePicker.build(getActivity()),
                                PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //--------------------
        //fabSaveChanges.hide();//hide af first time
        fabSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePic();
            }


        });

    }

    private void takePic() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                TedBottomPicker bottomSheetDialogFragment = new
                        TedBottomPicker.Builder(getActivity())
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(final Uri uri) {
                                // Log.d("ted", "uri: " + uri);
                                // Log.d("ted", "uri.getPath(): " + uri.getPath());
                                selectedUri = uri;
                                imageChanged = true;
                                mSelectedImagesContainer.setVisibility(View.GONE);
                                fabSaveChanges.show();
                                ivProfile.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mGlideRequestManager
                                                .load(uri)
                                                .into(ivProfile);
                                    }
                                });
                                        /*
                                        Glide.with(MainActivity.this)
                                                //.load(uri.toString())
                                                .load(uri)
                                                .into(iv_image);
                                         */
                            }
                        })
                        .setPeekHeight(getResources().getDisplayMetrics().heightPixels / 2)
                        .setSelectedUri(selectedUri)
                        //.showVideoMedia()
                        //.setPeekHeight(1200)
                        .create();
                bottomSheetDialogFragment.show(new MenuActivity().getSupportFragmentManager());
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(),
                        "Permission Denied\n" +
                                deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        new TedPermission(FrgUserProfile.this.getContext())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == MenuActivity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                placeName = place.getName().toString();
                if (etCity != null) etCity.setText(placeName);

            }
        }
    }

    private void saveChanges() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return;
        dialogSaving.show();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    UserNormal userNormal = dataSnapshot.getValue(UserNormal.class);
                    HashMap<String, Object> map
                            = new HashMap<>();
                    map.put("userName", etName.getText().toString());
                    //map.put("userPass", etPass.getText().toString());
                    //map.put("userEmail", etEmail.getText().toString());
                    //map.put("userPhone", etPhone.getText().toString());
                    map.put("userCity", etCity.getText().toString());
                    reference.setValue(map);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //upload the image photo to firebase storage

        if (imageChanged) {
            uploadPhoto(selectedUri);
        }

    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadPhoto(Uri selectedUri) {


        //creating reference to firebase storage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference =
                firebaseStorage
                        .getReferenceFromUrl("gs://whatsapp-38bc2.appspot.com/");    //change the url according to your firebase app

        // Checking whether FilePathUri Is empty or not.
        if (selectedUri != null) {

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String fileName = "image_" + dateFormat.format(date) + ".jpg";

            StorageReference childRef = storageReference.child(fileName);

            //uploading the image
            UploadTask uploadTask = childRef.putFile(selectedUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String imgDownloadedUrl = taskSnapshot.getDownloadUrl().toString();
                    if (firebaseUser != null) {
                        FirebaseDatabase.getInstance()
                                .getReference("Users")
                                .child(firebaseUser.getUid())
                                .setValue(new
                                        HashMap<String, Object>()
                                        .put("userPhotoUrl", imgDownloadedUrl));


                        fabSaveChanges.hide();
                        dialogSaving.dismiss();
                        Sneaker.with(getActivity())
                                .setTitle("Changes saved")
                                .sneakSuccess();


                    }

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                }
            });
        }
    }

    private void getDataIfExist() {

        if (firebaseUser == null) {
            Toast.makeText(context, "null user", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    UserNormal userNormal = dataSnapshot.getValue(UserNormal.class);
                    setData(userNormal);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private TextWatcher addTextWatcherForEditText() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().trim().length() != 0) {
                    if (fabSaveChanges != null) {
                        if (!fabSaveChanges.isShown()) {
                            fabSaveChanges.show();
                        }
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void setData(UserNormal userNormal) {
        if (userNormal.getUserPass() != null && userNormal.getUserPass() != "")
            etPass.setText(userNormal.getUserPass());
        if (userNormal.getCity() != null && userNormal.getCity() != "")
            etCity.setText(userNormal.getCity());
        if (userNormal.getUserName() != null && userNormal.getUserName() != "")
            etName.setText(userNormal.getUserName());
        if (userNormal.getUserEmail() != null && userNormal.getUserEmail() != "")
            etEmail.setText(userNormal.getUserEmail());
        if (userNormal.getUserPhone() != null && userNormal.getUserPhone() != "")
            etPhone.setText(userNormal.getUserPhone());


    }

    private void init(View parentView) {
        mGlideRequestManager = Glide.with(getActivity());
        //--------------------------
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //-------------------------------
        context = FrgUserProfile.this.getActivity();
        //----------------------------
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
        //----------------------------
        fabSaveChanges = parentView.findViewById(R.id.fabSave);
        fabTakePhoto = parentView.findViewById(R.id.fabTakePhoto);
        //-------------------------
        dialogSaving = new SpotsDialog(context, "Saving data ... ");
        //-------------------------
        etPass = parentView.findViewById(R.id.etPassword);
        etName = parentView.findViewById(R.id.etName);
        etEmail = parentView.findViewById(R.id.etEmail);
        etCity = parentView.findViewById(R.id.etCity);
        etPhone = parentView.findViewById(R.id.etPhone);
        //--------------------------------------------
        builderPlacePicker = new PlacePicker.IntentBuilder();
        ///------------------------
        ivProfile = parentView.findViewById(R.id.ivProfile);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



}
