package com.example.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.wechat.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class TakePhotoActivity extends Activity {

	
	
	private static String TAG = "TakePhotoActivity";
	
	/**
	 * Building Camera App
	 * The general steps for creating a custom camera interface for your application are as follows:
	 * 1. Detect and Access Camera - Create code to check for the existence of cameras and request access.
	 * 2. Create a Preview Class - Create a camera preview class that extends SurfaceView and implements the SurfaceHolder interface. This class previews the live images from the camera.
	 * 3. Build a Preview Layout - Once you have the camera preview class, create a view layout that incorporates the preview and the user interface controls you want.
	 * 4. Setup Listeners for Capture - Connect listeners for your interface controls to start image or video capture in response to user actions, such as pressing a button.
	 * 5. Capture and Save Files - Setup the code for capturing pictures or videos and saving the output.
	 * 6. Release the Camera - After using the camera, your application must properly release it for use by other applications.
	 */

	private Camera 			sCamera 	= null;
	private CameraPreview 	sCP 		= null;
	private Bitmap 			sBitmap 	= null;
	
	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	private Camera setupCamera() {
		Camera c = null;
		//to use Context in Fragment, use getActivity()
		//if need Context exist after Activity being destroyed, use getActivity().getApplicationContext()
		if(checkCameraHardware(getApplicationContext())) {
			try	{
				c = Camera.open();
			}
			catch (Exception e){
				// Camera is not available (in use or does not exist)
				
			}
		}
		return c;
	}
	
	/** A basic Camera preview class */
	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	    private SurfaceHolder mHolder;
	    private Camera mCamera;

	    public CameraPreview(Context context, Camera camera) {
	        super(context);
	        mCamera = camera;

	        // Install a SurfaceHolder.Callback so we get notified when the
	        // underlying surface is created and destroyed.
	        mHolder = getHolder();
	        mHolder.addCallback(this);
	        // deprecated setting, but required on Android versions prior to 3.0
	        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    }

	    public void surfaceCreated(SurfaceHolder holder) {
	        // The Surface has been created, now tell the camera where to draw the preview.
	        try {
	            mCamera.setPreviewDisplay(holder);
	            mCamera.startPreview();
	        } catch (IOException e) {
	            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
	        }
	    }

	    public void surfaceDestroyed(SurfaceHolder holder) {
	        // empty. Take care of releasing the Camera preview in your activity.
	    	mCamera.stopPreview();
	    	mCamera.release();
	    	mCamera = null;
	    }

	    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	        // If your preview can change or rotate, take care of those events here.
	        // Make sure to stop the preview before resizing or reformatting it.

	        if (mHolder.getSurface() == null){
	          // preview surface does not exist
	          return;
	        }

	        // stop preview before making changes
	        try {
	            mCamera.stopPreview();
	        } catch (Exception e){
	          // ignore: tried to stop a non-existent preview
	        }

	        // set preview size and make any resize, rotate or
	        // reformatting changes here

	        // start preview with new settings
	        try {
	            mCamera.setPreviewDisplay(mHolder);
	            mCamera.startPreview();

	        } catch (Exception e){
	            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
	        }
	    }
	}
	
	private PictureCallback mPicture = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {

	        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
	        Log.i(TAG, "Create media file");
	        if (pictureFile == null){
	            Log.d(TAG, "Error creating media file, check storage permissions: ");
	            return;
	        }

	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            fos.write(data);
	            fos.close();
	        } catch (FileNotFoundException e) {
	            Log.d(TAG, "File not found: " + e.getMessage());
	        } catch (IOException e) {
	            Log.d(TAG, "Error accessing file: " + e.getMessage());
	        }
	    }
	};
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.example.webaddressscanner/files");
	    
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        //mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
	        //Log.i(TAG, "Get file: " + mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
	        mediaFile = new File(mediaStorageDir, "IMG_"+ timeStamp + ".jpg");
	        Log.i(TAG, "Create picture: " + mediaStorageDir + "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        //mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp + ".mp4");
	        mediaFile = new File(mediaStorageDir, "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	
    public void releaseCamera(){
    	Log.i(TAG, "Release Camera");
        if (sCamera != null){
            sCamera.release();        // release the camera for other applications
            sCamera = null;
        }
    }
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_photo_activity);
		
		sCamera = setupCamera();
		sCP = new CameraPreview(getApplicationContext(), sCamera);
		
		FrameLayout preview = (FrameLayout)findViewById(R.id.camera_preview);
		preview.addView(sCP);
		
		ImageButton captureButton = (ImageButton)findViewById(R.id.imagebutton_capture);
		captureButton.setOnClickListener(
			    new View.OnClickListener() {
			        @Override
			        public void onClick(View v) {
			            // get an image from the camera
			            sCamera.takePicture(null, null, mPicture);
			        }
			    }
		);
	}
}
