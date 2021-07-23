package com.sanslayshare.addfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

              ImageView im = findViewById(R.id.im);
        ImageView imi = findViewById(R.id.imi);
              Animation am = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
              im.startAnimation(am);
              imi.startAnimation(am);

              Thread timer =new Thread() {

                  @Override

                  public void run() {

                      try {
                            sleep(2000);
                            Intent inte = new Intent(getApplicationContext(),file_activity.class);
                            startActivity(inte);
                            finish();
                            super.run();
                      }catch(InterruptedException e)
                      {
                          e.printStackTrace();
                      }
                  }



              };
            timer.start();

       // utilities.Companion.checkPermission(this);


}

  




}
