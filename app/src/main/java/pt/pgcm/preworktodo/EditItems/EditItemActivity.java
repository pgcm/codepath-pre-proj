package pt.pgcm.preworktodo.EditItems;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.pgcm.preworktodo.R;

public class EditItemActivity extends AppCompatActivity {
    private EditText etTodo;
    private Button btnSave;
    protected int position;
    private final static String TAG = "pt.pgcm.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        position = getIntent().getIntExtra("pos", 0);
        String todo_text = getIntent().getStringExtra("todo");

        etTodo = (EditText) findViewById(R.id.edEditItem);
        etTodo.setText(todo_text);
        etTodo.requestFocus();

        btnSave = (Button) findViewById(R.id.btnEditSave);
        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onSubmit(v, String.valueOf(etTodo.getText()), position)) {
                    Toast.makeText(getApplicationContext(), R.string.error_empty_todo, Toast.LENGTH_SHORT).show();
                }
                ;
            }
        });

    }

    public boolean onSubmit(View v, String text, int position) {
        if (text == null || text.isEmpty()) {
            return false;
        }

        Intent data = new Intent();

        data.putExtra("todo", text);
        data.putExtra("pos", position);
        Log.d(TAG, text);

        setResult(RESULT_OK, data);
        this.finish();
        return true;
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }


    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        Intent data = new Intent();
        this.finish();
    }
}
