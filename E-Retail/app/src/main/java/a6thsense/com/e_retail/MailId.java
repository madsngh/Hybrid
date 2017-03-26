package a6thsense.com.e_retail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MailId extends AppCompatActivity {
EditText yourmailid;
    Button yourmailidnext;
public static String email=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_id);
        yourmailid= (EditText) findViewById(R.id.youremaiid);
        yourmailidnext= (Button) findViewById(R.id.youremaiidnext);
        yourmailidnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=yourmailid.getText().toString();
                if(email.length()==0){
                    Toast.makeText(MailId.this,"Email id cannot be null",Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(MailId.this,finalpassordcoformation.class));
                }
            }
        });
    }
}
