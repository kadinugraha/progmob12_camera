package id.ac.ukdw.progmob12a_camera;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_SOUND = 2;
    private static final int REQUEST_TEXTTOSPEECH = 3;

    private Button btnCamera;
    private ImageView imgFoto;
    private String pathFoto;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("sp", MODE_PRIVATE);

        imgFoto = (ImageView)findViewById(R.id.imgFoto);
        btnCamera = (Button)findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        if(savedInstanceState != null){
            if(savedInstanceState.getString("pathFoto") != null){
                Bitmap bitmap = BitmapFactory.decodeFile(savedInstanceState.getString("pathFoto"));
                imgFoto.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("pathFoto", sp.getString("pathFoto",null));
        super.onSaveInstanceState(outState);
    }

    public void takePhoto(){
        File image = createFile();
        if(image != null){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CAMERA:
                Bitmap bitmap = BitmapFactory.decodeFile(sp.getString("pathFoto",null));
                imgFoto.setImageBitmap(bitmap);
                break;
            case REQUEST_SOUND:
                break;
            case REQUEST_TEXTTOSPEECH:
                break;
        }
    }

    //membuat file kosong untuk menampung foto hasil kamera
    public File createFile(){
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "PROGMOB_"+timestamp;
        File storage = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );
        File image = null;
        try{
            image = File.createTempFile(
                    fileName,
                    ".jpg",
                    storage
            );
            pathFoto = image.getAbsolutePath();
            SharedPreferences.Editor edit = sp.edit();
            edit.putString("pathFoto",pathFoto);
            edit.commit();
        }catch(Exception e){
        }
        return image;
    }
}
