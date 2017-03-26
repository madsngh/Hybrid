package a6thsense.com.e_retail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Name extends AppCompatActivity {
    EditText yourname;
    Button yournamenext;
    public static String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        yourname= (EditText) findViewById(R.id.yourname);
        yournamenext= (Button) findViewById(R.id.yournamenext);
        yournamenext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=yourname.getText().toString();
                    if(name.length()==0){
                        Toast.makeText(Name.this,"Name cannot be null",Toast.LENGTH_SHORT).show();
                    }
                else {
                        startActivity(new Intent(Name.this,MailId.class));
                    }
            }
        });
    }
}