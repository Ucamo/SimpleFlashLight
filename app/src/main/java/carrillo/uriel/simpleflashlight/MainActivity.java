package carrillo.uriel.simpleflashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.hardware.camera2.*;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;



public class MainActivity extends AppCompatActivity {

    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Parameters param;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First check if device is supporting flashlight or not
        hasFlash= getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!hasFlash)
        {
            //device doesn't support flash
            //Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Error");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which)
                {
                    //closing the application
                    finish();
                }
            });
            alert.show();
        }
        getCamera();

        ToggleButton flashSwitch = (ToggleButton) findViewById(R.id.flash_switch);
        flashSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
           @Override
            public void onCheckedChanged(CompoundButton butttonView, boolean isChecked)
           {
               //TODO Auto-generated method stub
               if(isChecked) {
                   turnOnFlash();
               }else{
                   turnOffFlash();
               }
           }
        });
    }

    private void getCamera(){
        if(camera == null){
            try{
                camera = Camera.open();
                param = camera.getParameters();
            }catch(RuntimeException e){
                Log.v("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }

    //Turning On flash
    @Override
    protected void onStop() {
        super.onStop();
        if(camera !=null){
            camera.release();
            camera=null;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }
    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    private void turnOnFlash(){
        if(!isFlashOn){
            if(camera==null || param==null){
                return;
            }
            param = camera.getParameters();
            param.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(param);
            camera.startPreview();
            isFlashOn=true;

            Log.v("AndroidATC","Flash has been turned on ...");
        }
    }

    //Turning Off flash
    private void turnOffFlash(){
        if(isFlashOn){
            if(camera == null || param==null){
                return;
            }
            param=camera.getParameters();
            param.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(param);
            camera.stopPreview();
            isFlashOn=false;
            Log.v("AndroidATC", "Flash has been turned off ...");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
