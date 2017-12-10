package com.example.hp.in_a_click.frg_driver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hp.in_a_click.R;
import com.example.hp.in_a_click.frg_worker_proc.Step1;
import com.example.hp.in_a_click.frg_worker_proc.Step2;
import com.example.hp.in_a_click.frg_worker_proc.Step3;
import com.example.hp.in_a_click.frg_worker_proc.Step4;
import com.example.hp.in_a_click.frg_worker_proc.Step5AddCat;
import com.example.hp.in_a_click.model.UserDriver;
import com.example.hp.in_a_click.residemenu.MenuActivity;
import com.example.hp.in_a_click.signinout.DriverSignInOutActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rey.material.widget.FloatingActionButton;
import com.special.ResideMenu.ResideMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;

//import com.github.fcannizzaro.materialstepper.AbstractStep;
//import com.github.fcannizzaro.materialstepper.style.ProgressStepper;

import dmax.dialog.SpotsDialog;
import io.fabric.sdk.android.Fabric;
import me.drozdzynski.library.steppers.SteppersItem;
import me.drozdzynski.library.steppers.SteppersView;
import me.drozdzynski.library.steppers.interfaces.OnCancelAction;
import me.drozdzynski.library.steppers.interfaces.OnChangeStepAction;
import me.drozdzynski.library.steppers.interfaces.OnClickCancelButton;
import me.drozdzynski.library.steppers.interfaces.OnClickContinue;
import me.drozdzynski.library.steppers.interfaces.OnFinishAction;


public class FrgDriverVehicles extends Fragment {

    public final static String CAT_FAMILY = "CAT_FAMILY";
    public final static String CAT_HIGH = "CAT_HIGH";
    public final static String CAT_CHEAP = "CAT_CHEAP";
    public final static String CAT_NEEDS = "CAT_NEEDS";
    public final static String CAT_WENSH = "CAT_WENSH";
    public final static String CAT_TRANSPORT = "CAT_TRANSPORT";
    Context context = null;
    boolean b = false;
    SteppersItem item1, item2, item3, item4, item5;
    FirebaseStorage storage;
    StorageReference storageRef;
    SpotsDialog dialogUpload = null;
    Uri filePath1, filePath2, filePath3, filePath4;
    String fileName1, fileName2, fileName3, fileName4;
    String imgDownloadedUrl1, imgDownloadedUrl2, imgDownloadedUrl3, imgDownloadedUrl14;
    Date date;
    SimpleDateFormat dateFormat;
    String fileName, imgDownloadedUrl;
    AlertDialog alertDialogPrev;
    ArrayList<String> listDocsUrls = new ArrayList<>();
    android.support.design.widget.FloatingActionButton fabAddNewVeh;
    int count = 0;
    Uri filePaths[];
    boolean locker = true;
    int index = 1;
    Button btnOpenMenu;
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
    private View parentView;
    private ResideMenu resideMenu;
    //    public class ProgressSample extends ProgressStepper {
//
//        private int i = 1;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//
//            setErrorTimeout(1500);
//            setTitle("Progress Stepper");
//            setStateAdapter();
//            //setStartPreviousButton();
//
//            addStep(createFragment(new Step1()));
//            addStep(createFragment(new Step2()));
//            addStep(createFragment(new Step3()));
//            addStep(createFragment(new Step4()));
//
//            super.onCreate(savedInstanceState);
//        }
//
//        private AbstractStep createFragment(AbstractStep fragment) {
//            Bundle b = new Bundle();
//            b.putInt("position", i++);
//            fragment.setArguments(b);
//            return fragment;
//        }
//    }
    private SteppersView steppersView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.frg_driver_vechic, container, false);
        init();
        addIndicatorView();
        initFabAddNewVeh();
        initBtnOpenMenu();
        //activateCarOwner();

        return parentView;
    }

    private void initBtnOpenMenu() {

        //init resideMenu
        MenuActivity parentActivity = (MenuActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        //init btn open menu
        btnOpenMenu = parentView.findViewById(R.id.btnOpenMenu);
        btnOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });


    }

    private void initFabAddNewVeh() {
        fabAddNewVeh = parentView.findViewById(R.id.fabAddNewVeh);
        fabAddNewVeh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //fabAddNewVeh.hide();
                Toast.makeText(context, "Add new vehicle + ", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void addIndicatorView() {


    }

    private void init() {
        context = getContext();

        steppersView = parentView.findViewById(R.id.steppersView);

        SteppersView.Config steppersViewConfig = new SteppersView.Config();
        steppersViewConfig.setOnFinishAction(new OnFinishAction() {
            @Override
            public void onFinish() {
                if (((Step5AddCat) item5.getFragment()).getCountSpinnerModelNumber() == 1) {
                    //steppersView.nextStep();
                    //showPreviewBeforeUpload();
                    showMessage("Please, Select car model :D ");
                    //uploadAllPhotos();
                    return;
                }
                if (((Step5AddCat) item5.getFragment()).getCountSpinnerCatName() == 1) {
                    //steppersView.nextStep();
                    //showPreviewBeforeUpload();
                    showMessage("Please, Select car category :D ");
                    //uploadAllPhotos();
                    return;
                }
                if (((Step5AddCat) item5.getFragment()).getListChips().isEmpty()) {
                    //steppersView.nextStep();
                    //showPreviewBeforeUpload();
                    showMessage("Please, Add your car categories");
                    //uploadAllPhotos();
                    return;
                }
                //otherwise
                //showPreviewBeforeUpload();


            }
        });

        steppersViewConfig.setOnCancelAction(new OnCancelAction() {
            @Override
            public void onCancel() {
                //steppersView.prevStep();
                //Toast.makeText(context, "setOnCancelAction", Toast.LENGTH_SHORT).show();
                //steppersView.removeAllViews();
                //init();
                getActivity().finish();
                startActivity(new Intent(getActivity(), MenuActivity.class));
            }
        });

        steppersViewConfig.setOnChangeStepAction(new OnChangeStepAction() {
            @Override
            public void onChangeStep(int position, SteppersItem activeStep) {
                //Toast.makeText(context, "setOnChangeStepAction", Toast.LENGTH_SHORT).show();

            }
        });

        steppersViewConfig.setFragmentManager(getActivity().getSupportFragmentManager());
        ArrayList<SteppersItem> steps = new ArrayList<>();

        //add doc 1
        item1 = new SteppersItem();
        item1.setLabel("Step1");
        item1.setFragment(new Step1());
        item1.setOnClickContinue(new OnClickContinue() {
            @Override
            public void onClick() {
                if (((Step1) item1.getFragment()).isUploadedSuccess()) {
                    steppersView.nextStep();
                } else {
                    showMessage("Please, upload photo then continue");
                }
            }
        });
        item1.setPositiveButtonEnable(true);
        //item1.setSubLabel("رخصة القياده");
        item1.setSubLabel("قم بتحميل مستنداتك الشخصيه  / رخصة القياده");
        //item1.setButtonCancelEnabled(false);


        //add doc2
        item2 = new SteppersItem();
        item2.setLabel("Step2");
        item2.setFragment(new Step2());
        item2.setOnClickContinue(new OnClickContinue() {
            @Override
            public void onClick() {
                if (((Step2) item2.getFragment()).isUploadedSuccess()) {
                    steppersView.nextStep();
                } else {
                    showMessage("Please, upload photo then continue");
                }
            }
        });
        item2.setPositiveButtonEnable(true);
        //item2.setSubLabel("الفيش الجنائي");
//        item2.setSubLabel("Fisch criminal");
        item2.setSubLabel("قم بتحميل مستندات الشخصيه  / الفيش الجنائي ");
//        item2.setButtonCancelEnabled(true);
//        item2.setOnClickCancel(new OnClickCancelButton() {
//            @Override
//            public void onClick() {
//                steppersView.prevStep();
//            }
//        });


        //add doc3
        item3 = new SteppersItem();
        item3.setLabel("Step3");
        item3.setFragment(new Step3());
        item3.setOnClickContinue(new OnClickContinue() {
            @Override
            public void onClick() {
                if (((Step3) item3.getFragment()).isUploadedSuccess()) {
                    steppersView.nextStep();
                } else {
                    showMessage("Please, upload photo then continue");
                }
            }
        });
        item3.setPositiveButtonEnable(true);
        //item3.setSubLabel("رخصة السياره : ظهر الرخصه");
//        item3.setSubLabel("Car license : Back");
        item3.setSubLabel("يرجي تحميل مستندات السياره الخصه بك  / رخصة السياره : ظهر الرخصه \n للقيادة مع أوبر، يجب أن تكون سيارتك موديل 2004 أو أحدث، وأن تكون ذات سيدان متوسط أو كامل الحجم والتي تستوعب 4-8 ركاب بشكل مريح.");
//        item3.setButtonCancelEnabled(true);
//        item3.setOnClickCancel(new OnClickCancelButton() {
//            @Override
//            public void onClick() {
//                steppersView.prevStep();
//            }
//        });


        //add doc4
        item4 = new SteppersItem();
        item4.setLabel("Step4");
        item4.setFragment(new Step4());
        item4.setOnClickContinue(new OnClickContinue() {
            @Override
            public void onClick() {
                if (((Step4) item4.getFragment()).isUploadedSuccess()) {
                    steppersView.nextStep();
                } else {
                    showMessage("Please, upload photo then continue");
                }
            }
        });
        item4.setPositiveButtonEnable(true);
//        item4.setSubLabel("رخصة القياده : وش الرخصه");
//        item4.setSubLabel("Car license : Front");
        item4.setSubLabel("يرجي تحميل مستندات السياره الخصه بك  / رخصة السياره : وش  الرخصه \n للقيادة مع أوبر، يجب أن تكون سيارتك موديل 2004 أو أحدث، وأن تكون ذات سيدان متوسط أو كامل الحجم والتي تستوعب 4-8 ركاب بشكل مريح.");
//        item4.setButtonCancelEnabled(true);
//        item4.setOnClickCancel(new OnClickCancelButton() {
//            @Override
//            public void onClick() {
//                steppersView.prevStep();
//            }
//        });


        item5 = new SteppersItem();
        item5.setLabel("Step5");
        item5.setFragment(new Step5AddCat());
        item5.setOnClickContinue(new OnClickContinue() {
            @Override
            public void onClick() {
//                if (((Step4) item4.getFragment()).isUploadedSuccess()) {
//                    steppersView.nextStep();
//                } else {
//                    showMessage("Please, upload photo then continue");
//                }
                activateCarOwner();
            }
        });
        item5.setPositiveButtonEnable(true);
        //item5.setSubLabel(getResources().getString(R.string.chips_add_rem));

        steps.add(item5);
        steps.add(item1);
        steps.add(item2);
        steps.add(item3);
        steps.add(item4);


        steppersView.setConfig(steppersViewConfig);
        steppersView.setItems(steps);
        steppersView.build();


    }

    private void activateCarOwner() {
        //add the car model
        //add the cat name
        //set status activated in friebase database
        DatabaseReference refMain = FirebaseDatabase.getInstance().getReference("Workers");
        final DatabaseReference refDrivers = refMain.child(DriverSignInOutActivity.TAG_DRIVER);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null)
            return;
        refDrivers.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    UserDriver userDriver = dataSnapshot.getValue(UserDriver.class);


                    //edit the current user driver if exists
                    assert userDriver != null;
                    refDrivers.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(
                                    new UserDriver(userDriver.getUserEmail(),
                                            userDriver.getUserPass(),
                                            userDriver.getUserName(),
                                            userDriver.getUserPhone(),
                                            userDriver.getCity(),
                                            ((Step5AddCat) item5.getFragment()).getSpinnerModelNumber().getSelectedItem().toString(),
                                            getCatNameFromSpinner(),
//                                            "2010",
//                                            "ونش",
                                            userDriver.isDriverStatus()
                                    )
                            )
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private String getCatNameFromSpinner() {
        //((Step5AddCat) item5.getFragment()).getSpinnerModelNumber().getSelectedItem().toString()
        String tagKnickName = "";
        int pos = ((Step5AddCat) item5.getFragment()).getSpinnerCatName().getSelectedItemPosition();
        switch (pos) {
            case 0:
                tagKnickName = CAT_FAMILY;
                break;
            case 1:
                tagKnickName = CAT_HIGH;
                break;
            case 2:
                tagKnickName = CAT_CHEAP;
                break;
            case 3:
                tagKnickName = CAT_NEEDS;
                break;
            case 4:
                tagKnickName = CAT_WENSH;
                break;
            case 5:
                tagKnickName = CAT_TRANSPORT;
                break;
            default:
                break;
        }
        return tagKnickName;
    }

    public Uri getFilePath1() {
        return filePath1;
    }

    public void setFilePath1(Uri filePath1) {
        this.filePath1 = filePath1;
    }

    public Uri getFilePath2() {
        return filePath2;
    }

    public void setFilePath2(Uri filePath2) {
        this.filePath2 = filePath2;
    }

    public Uri getFilePath3() {
        return filePath3;
    }

    public void setFilePath3(Uri filePath3) {
        this.filePath3 = filePath3;
    }

    public Uri getFilePath4() {
        return filePath4;
    }

    public void setFilePath4(Uri filePath4) {
        this.filePath4 = filePath4;
    }

    private void showPreviewBeforeUpload() {

        View viewPreview = LayoutInflater.from(getActivity()).inflate(R.layout.layout_preview, null);

        ImageView ivDoc1, ivDoc2, ivDoc3, ivDoc4;
        ivDoc1 = viewPreview.findViewById(R.id.ivDoc1Preview);
        ivDoc2 = viewPreview.findViewById(R.id.ivDoc2Preview);
        ivDoc3 = viewPreview.findViewById(R.id.ivDoc3Preview);
        ivDoc4 = viewPreview.findViewById(R.id.ivDoc4Preview);

        filePath1 = ((Step1) item1.getFragment()).getFilePath();
        filePath2 = ((Step2) item2.getFragment()).getFilePath();
        filePath3 = ((Step3) item3.getFragment()).getFilePath();
        filePath4 = ((Step4) item4.getFragment()).getFilePath();


        ivDoc1.setImageURI(filePath1);
        ivDoc2.setImageURI(filePath2);
        ivDoc3.setImageURI(filePath3);
        ivDoc4.setImageURI(filePath4);


        Button btnUploadNow, btnCancel;
        btnCancel = viewPreview.findViewById(R.id.btnCancel);
        btnUploadNow = viewPreview.findViewById(R.id.btnUploadNow);
        btnUploadNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadAllPhotos();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogPrev.dismiss();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewPreview);
        alertDialogPrev = builder.create();
        alertDialogPrev.setCancelable(false);
        alertDialogPrev.setCanceledOnTouchOutside(false);
        if (!alertDialogPrev.isShowing()) {
            alertDialogPrev.show();
        }

    }

    private void showMessage(String message) {

        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setTitle("Warning")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void addNewOne() {
        context = getContext();

        final SteppersView steppersView = parentView.findViewById(R.id.steppersView);

        SteppersView.Config steppersViewConfig = new SteppersView.Config();
        steppersViewConfig.setOnFinishAction(new OnFinishAction() {
            @Override
            public void onFinish() {
                Toast.makeText(context, "setOnFinishAction", Toast.LENGTH_SHORT).show();

            }
        });

        steppersViewConfig.setOnCancelAction(new OnCancelAction() {
            @Override
            public void onCancel() {
                //steppersView.prevStep();
                Toast.makeText(context, "setOnCancelAction", Toast.LENGTH_SHORT).show();
                steppersView.removeAllViews();

                //init();
            }
        });

        steppersViewConfig.setOnChangeStepAction(new OnChangeStepAction() {
            @Override
            public void onChangeStep(int position, SteppersItem activeStep) {
                Toast.makeText(context, "setOnChangeStepAction", Toast.LENGTH_SHORT).show();

            }
        });

        steppersViewConfig.setFragmentManager(getActivity().getSupportFragmentManager());
        ArrayList<SteppersItem> steps = new ArrayList<>();

        //add doc 1
        SteppersItem item1 = new SteppersItem();
        item1.setLabel("Step1");
        item1.setFragment(new Step1());
        item1.setOnClickContinue(new OnClickContinue() {
            @Override
            public void onClick() {
                steppersView.nextStep();
            }
        });
        item1.setPositiveButtonEnable(true);
        //item1.setSubLabel("رخصة القياده");
        item1.setSubLabel("Driving license");
        //item1.setButtonCancelEnabled(false);


        //add doc2
        SteppersItem item2 = new SteppersItem();
        item2.setLabel("Step2");
        item2.setFragment(new Step2());
        item2.setOnClickContinue(new OnClickContinue() {
            @Override
            public void onClick() {
                steppersView.nextStep();
            }
        });
        item2.setPositiveButtonEnable(true);
        //item2.setSubLabel("الفيش الجنائي");
        item2.setSubLabel("Fisch criminal");
//        item2.setButtonCancelEnabled(true);
//        item2.setOnClickCancel(new OnClickCancelButton() {
//            @Override
//            public void onClick() {
//                steppersView.prevStep();
//            }
//        });


        //add doc3
        SteppersItem item3 = new SteppersItem();
        item3.setLabel("Step3");
        item3.setFragment(new Step3());
        item3.setOnClickContinue(new OnClickContinue() {
            @Override
            public void onClick() {
                steppersView.nextStep();
            }
        });
        item3.setPositiveButtonEnable(true);
        //item3.setSubLabel("رخصة السياره : ظهر الرخصه");
        item3.setSubLabel("Car license : Back");
//        item3.setButtonCancelEnabled(true);
//        item3.setOnClickCancel(new OnClickCancelButton() {
//            @Override
//            public void onClick() {
//                steppersView.prevStep();
//            }
//        });


        //add doc4
        SteppersItem item4 = new SteppersItem();
        item4.setLabel("Step4");
        item4.setFragment(new Step4());
        item4.setOnClickContinue(new OnClickContinue() {
            @Override
            public void onClick() {
                steppersView.nextStep();
            }
        });
        item4.setPositiveButtonEnable(true);
//        item4.setSubLabel("رخصة القياده : وش الرخصه");
        item4.setSubLabel("Car license : Front");
//        item4.setButtonCancelEnabled(true);
//        item4.setOnClickCancel(new OnClickCancelButton() {
//            @Override
//            public void onClick() {
//                steppersView.prevStep();
//            }
//        });

        steps.add(item1);
        steps.add(item2);
        steps.add(item3);
        steps.add(item4);


        steppersView.setConfig(steppersViewConfig);
        steppersView.setItems(steps);
        steppersView.build();


    }

    private void uploadAllPhotos() {


        //creating reference to firebase storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://whatsapp-38bc2.appspot.com/");    //change the url according to your firebase app

        if (filePath1 == null || filePath2 == null || filePath3 == null || filePath4 == null) {
            Toast.makeText(context, "Uploading all docs now ..... ", Toast.LENGTH_SHORT).show();
            return;
        }
        filePaths = new Uri[]{filePath1, filePath2, filePath3, filePath4};
        uploadStepByStep(filePaths[count]);
//        new AsyncUploadingAllDocs(getActivity()).execute(filePath1, filePath2, filePath3, filePath4);


    }

    private void uploadStepByStep(Uri filePath) {
        dialogUpload = new SpotsDialog(context, "Uploading Doc " + index + "  ...");
        dialogUpload.show();
        index++;


        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "image_" + dateFormat.format(date) + ".jpg";
        //StorageReference childRef = storageRef.child(enteredPhoneNumber).child("image_" + fileName + ".jpg");
        StorageReference childRef = storageRef.child(fileName);
        //uploading the image
        UploadTask uploadTask = childRef.putFile(this.filePaths[count]);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (dialogUpload.isShowing()) {
                    dialogUpload.dismiss();
                }
                String imgDownloadedUrl = taskSnapshot.getDownloadUrl().toString();
                listDocsUrls.add(imgDownloadedUrl);
                if (count == filePaths.length - 1) {
                    locker = !locker;
                }
                if (locker) {
                    uploadStepByStep(filePaths[++count]);
                }


//                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                    if (firebaseUser != null) {
//                        DatabaseReference reference = FirebaseDatabase.getInstance()
//                                .getReference()
//                                .child(DriverSignInOutActivity.TAG_DRIVER)
//                                .child(firebaseUser.getUid())
//                                .setValue()
//                        //String imageUploadId = reference.push().getKey();
//                        //reference.child(imageUploadId).setValue(imageUploadInfo);
//                    }else {
//
//                    }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });

    }

    public class AsyncUploadingAllDocs extends AsyncTask<Uri, Void, String> {

        public AsyncUploadingAllDocs(Context context) {
            if (listDocsUrls != null) {
                listDocsUrls.clear();
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Uri... filePaths) {


            for (int i = 0; i < filePaths.length; i++) {
                dialogUpload = new SpotsDialog(context, "Uploading Doc " + i + "  ...");
                dialogUpload.show();

                date = new Date();
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                fileName = "image_" + dateFormat.format(date) + ".jpg";
                //StorageReference childRef = storageRef.child(enteredPhoneNumber).child("image_" + fileName + ".jpg");
                StorageReference childRef = storageRef.child(fileName1);
                //uploading the image
                UploadTask uploadTask = childRef.putFile(filePaths[i]);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (dialogUpload.isShowing()) {
                            dialogUpload.dismiss();
                        }
                        imgDownloadedUrl = taskSnapshot.getDownloadUrl().toString();
                        listDocsUrls.add(imgDownloadedUrl);
//                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//                    if (firebaseUser != null) {
//                        DatabaseReference reference = FirebaseDatabase.getInstance()
//                                .getReference()
//                                .child(DriverSignInOutActivity.TAG_DRIVER)
//                                .child(firebaseUser.getUid())
//                                .setValue()
//                        //String imageUploadId = reference.push().getKey();
//                        //reference.child(imageUploadId).setValue(imageUploadInfo);
//                    }else {
//
//                    }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                    }
                });
            }

            return imgDownloadedUrl;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (listDocsUrls != null) {
                Log.e("ListDocsUrls", listDocsUrls.toString());
            }


        }


    }


//    private void addImgUrlToDbRef() {
//        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
////        sharedPreferences = getActivity().getSharedPreferences(MainActivity.KEY_PHONE_NUMBER, MainActivity.MODE);
////        editor = sharedPreferences.edit();
////                        if (sharedPreferences.contains(MainActivity.KEY_IMAGES_UPLOADED)) {
////                            keyImagesUploaded = sharedPreferences.getString(MainActivity.KEY_IMAGES_UPLOADED, "");
////                        }
////                        if (sharedPreferences.contains(MainActivity.KEY_IMAGES_FAV)) {
////                            keyFavImages = sharedPreferences.getString(MainActivity.KEY_IMAGES_FAV, "");
////                        }
//
//        if (sharedPreferences.contains(MainActivity.KEY_PHONE_NUMBER)) {
//            enteredPhoneNumber = sharedPreferences.getString(MainActivity.KEY_PHONE_NUMBER, "");
//            //if (!TextUtils.isEmpty(keyFavImages)){}
//            // Assign FirebaseDatabase instance with root database name.
//            //databaseReference = FirebaseDatabase.getInstance().getReference("selfie-competition-app-201300");
//            //databaseReference = FirebaseDatabase.getInstance().getReference();//root
//            //if (!TextUtils.isEmpty(keyImagesUploaded)) {
//            // Getting image upload ID.
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(enteredPhoneNumber).child("uploadedImages");
//            String imageUploadId = reference.push().getKey();
//            reference.child(imageUploadId).setValue(imageUploadInfo);
//            //}
//            Toast.makeText(context, enteredPhoneNumber, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Phone number Not found !!! ", Toast.LENGTH_SHORT).show();
//        }
//
//    }


}
