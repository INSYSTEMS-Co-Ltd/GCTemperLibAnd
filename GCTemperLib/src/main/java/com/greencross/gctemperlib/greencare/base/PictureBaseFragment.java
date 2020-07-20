package com.greencross.gctemperlib.greencare.base;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.greencross.gctemperlib.greencare.component.CDialog;
import com.greencross.gctemperlib.greencare.util.cameraUtil.OnPermssionCallBackListener;
import com.greencross.gctemperlib.greencare.util.cameraUtil.ProviderUtil;
import com.greencross.gctemperlib.greencare.util.cameraUtil.RuntimeUtil;
import com.greencross.gctemperlib.R;
import com.greencross.gctemperlib.greencare.util.IntentUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PictureBaseFragment extends BaseFragment {
    private final String TAG = PictureBaseFragment.class.getSimpleName();

    protected static final int REQUEST_TAKE_PHOTO = 1111;     // 카메라
    public static final int REQUEST_GALLERY = 2222;        // 갤러리
    protected static final int REQUEST_IMAGE_CROP = 3333;     // 사진 잘라내기

    protected ImageView ivPreview;  // 사진을 보여줄 이미지 뷰 Child에서 세팅
    protected Uri outputFileUri;
    protected String imagefileName = null;

    //2019-03-24 park cropimage에서 이미지 사이즈 조절이 필요하지 않을 경우 false 사용(CommunityWriteFragment.java) 기본값은 true
    protected boolean isResizing=true;

    public void setPreviewImageView(ImageView ivPreview) {
        this.ivPreview = ivPreview;
    }

    public void onCamera() {
        RuntimeUtil.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, RuntimeUtil.PERMISSION_ALBUM, new OnPermssionCallBackListener() {
            @Override
            public void OnGrantPermission() {
                RuntimeUtil.checkPermission(getActivity(), getActivity().getWindow().getDecorView(), Manifest.permission.CAMERA, RuntimeUtil.PERMISSION_CAMERA, null, new OnPermssionCallBackListener() {
                    @Override
                    public void OnGrantPermission() {
                        takePhoto();
//                        }
                    }
                });
            }
        });
    }

    /**
     * 카메라 실행하기
     */
    /**
     * 카메라에서 이미지 가져오기
     */
    private File mPhotoFile;
    private void takePhoto() {
        mPhotoFile = createPhotoFile();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            outputFileUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName()+".provider", mPhotoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함
            //intent.putExtra("return-data", true);  // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
            Log.i(TAG, "takePhoto.outputFileUri="+outputFileUri);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);

        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 임시로 사용할 파일의 경로를 생성
            outputFileUri = Uri.fromFile(mPhotoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            Log.i(TAG, "takePhoto.outputFileUri="+outputFileUri);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    /**
     * 사진 파일로 사용할 파일 생성
     * @return
     */
    private File createPhotoFile() {
        File photoFile = null;
        String fileName = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 임시로 사용할 파일의 경로를 생성
            if (getContext().getCacheDir().exists() == false) {
                getContext().getCacheDir().mkdirs();
            }
            photoFile = new File(getContext().getCacheDir(), fileName);
        } else {
            photoFile = new File(Environment.getExternalStorageDirectory(), fileName);
        }

        return photoFile;
    }

    public void onGallery() {
        RuntimeUtil.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, RuntimeUtil.PERMISSION_ALBUM, new OnPermssionCallBackListener() {
            @Override
            public void OnGrantPermission() {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RuntimeUtil.PERMISSION_CAMERA) {
            if (RuntimeUtil.verifyPermissions(getActivity(), grantResults)) {
                onCamera();
            }
        } else if (requestCode == RuntimeUtil.PERMISSION_ALBUM) {
            if (RuntimeUtil.verifyPermissions(getActivity(), getActivity().getWindow().getDecorView(), grantResults)) {
                onGallery();
            }
        }
    }

    /**
     * 이미지 잘라내기 실행
     */
    public void cropImage() {
        try {
            getContext().grantUriPermission(getContext().getPackageName()
                    , outputFileUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(outputFileUri, "image/*");
            Log.i(TAG, "cropImage outputFileUri=" + outputFileUri);
            List<ResolveInfo> list = getContext().getPackageManager().queryIntentActivities(intent, 0);
//        getContext().grantUriPermission(list.get(0).activityInfo.packageName, outputFileUri,
//                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            int size = list.size();
            if (size == 0) {
                Toast.makeText(getContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
                return;
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getContext().grantUriPermission(list.get(0).activityInfo.packageName, outputFileUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }



                if (isResizing) {
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                }


                // 자른 이미지 관련 경로 및 권한 처리
                File tempFile = ProviderUtil.getOutputMediaFile();
                if (TextUtils.isEmpty(imagefileName) == false)
                    tempFile = ProviderUtil.getOutputMediaFile(imagefileName);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    outputFileUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName()+".provider", tempFile);
                    getContext().grantUriPermission(list.get(0).activityInfo.packageName, outputFileUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    outputFileUri = Uri.fromFile(tempFile);
                }

                intent.putExtra("scale", true);
                intent.putExtra("crop", "true");
                intent.putExtra("return-data", false);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

                Log.i(TAG, "cropImage outputFileUri=" + outputFileUri);


                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContext().grantUriPermission(res.activityInfo.packageName, outputFileUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));


                startActivityForResult(i, REQUEST_IMAGE_CROP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeCropImage(Bitmap bitmap, String filePath) {
        File copyFile = new File(filePath);
        BufferedOutputStream out = null;
        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Bitmap 파일로 저장
     * @param bitmap
     * @param path
     * @return
     */
    public String saveBitmapToFile(Bitmap bitmap, String path) {
        File tempFile = new File(path);

        if (imagefileName != null) {
            tempFile = ProviderUtil.getOutputFile(imagefileName + ".jpg");
            outputFileUri = Uri.fromFile(tempFile);
        }

        try {
            tempFile.createNewFile();

//            Log.i(TAG, "tempFile="+tempFile.getPath());
            FileOutputStream out = new FileOutputStream(tempFile);
            if (out != null) {
                Log.i(TAG, "bitmap="+bitmap);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out); // 넘거 받은 bitmap을 jpg 저장해줌
//                Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),  bitmap.getHeight(), false);
                out.close(); // 마무리로 닫아줍니다.
            }
            Log.i(TAG, "saveBitmapToFile.path=" + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "saveBitmapToFile=" + tempFile.getAbsolutePath());
        return tempFile.getAbsolutePath(); // 임시파일 저장경로를 리턴해주면 끝!
    }

    /**
     * 이미지를 회전시킵니다.
     *
     * @param bitmap  비트맵 이미지
     * @return 회전된 이미지
     */
    public Bitmap rotate(Bitmap bitmap, String path) {

        // 이미지를 상황에 맞게 회전시킨다
        try {
            ExifInterface exif = new ExifInterface(path);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degrees = exifOrientationToDegrees(exifOrientation);

            Bitmap retBitmap = bitmap;

            if (degrees != 0 && bitmap != null) {
                Matrix m = new Matrix();
                m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

                try {
                    Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                    if (bitmap != converted) {
                        retBitmap = converted;
                        bitmap.recycle();
                        bitmap = null;
                    }
                } catch (OutOfMemoryError ex) {
                    // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
                }
            }
            return retBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 이미지를 회전시킵니다.
     *
     * @param imageFile  비트맵 이미지
     * @return 회전된 이미지
     */
    public void rotate(File imageFile) {
        // 이미지를 상황에 맞게 회전시킨다
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), outputFileUri);

            ExifInterface exif = new ExifInterface(imageFile.getPath());
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degrees = exifOrientationToDegrees(exifOrientation);

            Bitmap retBitmap = bitmap;

            if (degrees != 0 && bitmap != null) {
                Matrix m = new Matrix();
                m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

                try {
                    Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                    if (bitmap != converted) {
                        retBitmap = converted;
                        bitmap.recycle();
                    }
                } catch (OutOfMemoryError ex) {
                    // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
                }
            }

            FileOutputStream out = new FileOutputStream(imageFile.getPath());
            try {
                if (out != null && retBitmap != null) {
                    Log.i(TAG, "rotate bitmap="+retBitmap);
                    retBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out); // 넘거 받은 bitmap을 png로 저장해줌
                    out.close(); // 마무리로 닫아줍니다.
                }
                Log.i(TAG, "rotate saveBitmapToFile.path=" + imageFile.getPath());
            } catch (IOException e) {
                e.printStackTrace();
                if (out != null)
                    out.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * EXIF정보를 회전각도로 변환하는 메서드
     *
     * @param exifOrientation EXIF 회전각
     * @return 실제 각도
     */
    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY || requestCode == REQUEST_TAKE_PHOTO) {
                if (data != null && requestCode == REQUEST_GALLERY)
                    outputFileUri = data.getData();

                if (outputFileUri != null) {

                    if (requestCode == REQUEST_GALLERY) {
                        try {

                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), outputFileUri);
                            String filePath = IntentUtil.getFilePath(getContext(), outputFileUri);
                            bitmap = rotate(bitmap, filePath);
                            String tempFile = saveBitmapToFile(bitmap, filePath);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                outputFileUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName()+".provider", new File(tempFile));
                            } else {
                                outputFileUri = Uri.fromFile(new File(tempFile));
                            }

                            cropImage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (requestCode == REQUEST_TAKE_PHOTO) {
                        Log.i(TAG, "onActivityResult.imgFile="+mPhotoFile.getPath());
                        rotate(mPhotoFile);    // 회전 보정
                        cropImage();
                    }

                }
            } else if (requestCode == REQUEST_IMAGE_CROP) {
                Log.i(TAG, "onActivityResult.REQUEST_IMAGE_CROP.data=" + data);
//                Log.i(TAG, "onActivityResult.REQUEST_IMAGE_CROP.path=" +  ProviderUtil.getOutputFile(outputFileUri).getPath());
                Log.i(TAG, "onActivityResult.REQUEST_IMAGE_CROP.path=" +  outputFileUri.getPath());


                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), outputFileUri);

                    //여기서는 ImageView에 setImageBitmap을 활용하여 해당 이미지에 그림을 띄우시면 됩니다.
                    if (ivPreview != null) {
                        ivPreview.setImageBitmap(bitmap);
                    } else {
                        Toast.makeText(getContext(), "Preview가 등록되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                    }

                    // 썸네일로 보이기
//                    Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 300, 300);
//                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
//                    thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs); //이미지가 클 경우 OutOfMemoryException 발생이 예상되어 압축
//                    ivPreview.setImageBitmap(bitmap);
                    // Crop 이미지 저장하기
//                    saveBitmapToFile(bitmap, ProviderUtil.getOutputFilePath(outputFileUri));

                    storeCropImage(bitmap, ProviderUtil.getOutputFilePath(outputFileUri));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            }
        }
    }



    private CDialog mStepDlg;
    public void showSelectGalleryCamera() {
        View.OnClickListener dlgClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vId = v.getId();

                if (mStepDlg != null) {
                    mStepDlg.dismiss();
                }
                if (R.id.select_gallery_btn == vId) {
                    onGallery();
                } else if (R.id.select_camera_btn == vId) {
                    onCamera();
                }
            }
        };

        /**
         * 데이터소스 선택
         */
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_listview_gallery_camera, null);
        view.findViewById(R.id.select_gallery_btn).setOnClickListener(dlgClickListener);
        view.findViewById(R.id.select_camera_btn).setOnClickListener(dlgClickListener);

        mStepDlg = CDialog.showDlg(getContext(), view);
        mStepDlg.setTitle("사진 선택");
    }


}
