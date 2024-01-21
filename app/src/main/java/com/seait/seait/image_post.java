package com.seait.seait;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class image_post extends AppCompatActivity {

    private TextView temp;
    private ImageView imageView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post);

        temp = findViewById(R.id.textView3);
        imageView = findViewById(R.id.imageView2);
        editText = findViewById(R.id.editText);

        String[] image = getIntent().getStringExtra("mytext").split(":");
        String imageURI = getIntent().getStringExtra("image");

        temp.setText("Classified image: \n" + image[0]);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("image"));


        imageView.setImageURI(imageUri);

        Button btn = (Button)findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(image_post.this, home.class);
                myIntent.putExtra("user_input", editText.getText());
                myIntent.putExtra("image", imageURI);
                startActivity(myIntent);
            }
        });
    }
}