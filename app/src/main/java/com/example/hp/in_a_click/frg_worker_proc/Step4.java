package com.example.hp.in_a_click.frg_worker_proc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.in_a_click.R;
import com.example.hp.in_a_click.residemenu.MenuActivity;
//import com.github.fcannizzaro.materialstepper.AbstractStep;
//import com.github.fcannizzaro.materialstepper.style.BaseStyle;
import com.special.ResideMenu.ResideMenu;
import com.theartofdev.edmodo.cropper.CropImage;

/**
 * User: special
 * Date: 13-12-22
 * Time: 下午1:33
 * Mail: specialcyci@gmail.com
 */
public class Step4 extends Fragment {

    Button btnFileUpload, btnFileUploadNow;
    ImageView ivFileShow;
    LinearLayout llFileUpload;
    TextView tvEditImage;
    boolean uploadedSuccess = false;
    Uri filePath = null;
    private View parentView;
    private ResideMenu resideMenu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.step4, container, false);
        //setUpViews();
        init();
        return parentView;
    }

//    private void setUpViews() {
//        MenuActivity parentActivity = (MenuActivity) getActivity();
//        resideMenu = parentActivity.getResideMenu();
//
////        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
////            }
////        });
////
////        // add gesture operation's ignored views
////        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
////        resideMenu.addIgnoredView(ignored_view);
//    }

    private void init() {

        llFileUpload = parentView.findViewById(R.id.llStep1);
        tvEditImage = parentView.findViewById(R.id.tvStep1Edit);
        btnFileUpload = parentView.findViewById(R.id.btnStep1FileUpload);
        btnFileUploadNow = parentView.findViewById(R.id.btnStep1UploadNow);
        ivFileShow = parentView.findViewById(R.id.ivStep1);
        btnFileUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .start(getContext(), Step4.this);
            }
        });
        tvEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .start(getContext(), Step4.this);
            }
        });

        btnFileUploadNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void setUpViews() {
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();
//        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
//            }
//        });
//
//        // add gesture operation's ignored views
//        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
//        resideMenu.addIgnoredView(ignored_view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleResult(requestCode, resultCode, data);

    }

    public boolean isUploadedSuccess() {
        return uploadedSuccess;
    }

    public void setUploadedSuccess(boolean uploadedSuccess) {
        this.uploadedSuccess = uploadedSuccess;
    }

    public Uri getFilePath() {
        return filePath;
    }

    public void setFilePath(Uri filePath) {
        this.filePath = filePath;
    }

    private void handleResult(int requestCode, int resultCode, Intent data) {
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == MenuActivity.RESULT_OK) {

                llFileUpload.setVisibility(View.VISIBLE);
                ivFileShow.setImageURI(result.getUri());
                btnFileUpload.setVisibility(View.GONE);

//                btnFileUploadNow.setVisibility(View.VISIBLE);
//                btnFileUpload.setVisibility(View.GONE);

                uploadedSuccess = true;
                filePath = result.getUri();


                //new AddingNewItem(getActivity(), llImagesForUploading, filePath);
                //llNoSelfiesFound.setVisibility(LinearLayout.GONE);
//                try {
//                    Bitmap mImageBitmap = null;
//                    InputStream imageStream = null;
//                    imageStream = getActivity().getContentResolver().openInputStream(result.getUri());
//                    mImageBitmap = BitmapFactory.decodeStream(imageStream);
//                    new AddingNewItem(getActivity(), llImagesForUploading,);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } finally {
//
//                }
            }
            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
            if (resultCode == MenuActivity.RESULT_CANCELED) {
                uploadedSuccess = filePath != null;
            }
        }
    }


}
