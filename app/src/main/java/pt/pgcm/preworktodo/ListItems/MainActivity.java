package pt.pgcm.preworktodo.ListItems;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pt.pgcm.preworktodo.EditItems.EditItemActivity;
import pt.pgcm.preworktodo.R;

public class MainActivity extends AppCompatActivity {
    private ListView mItemsList;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mItems;
    private final int REQUEST_CODE = 20;
    private final static String TAG = "pt.pgcm.MainActivity";
    private final static String TODOS_FILENAME = "todos.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mItems = readItems(TODOS_FILENAME);

        mItemsList = (ListView) findViewById(R.id.rvItems);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mItems);
        mItemsList.setAdapter(mAdapter);

        setupListViewListener();
    }

    private void setupListViewListener() {
        mItemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), EditItemActivity.class);
                i.putExtra("pos", position);
                i.putExtra("todo", mItems.get(position));
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        mItemsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            //Remove Item
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Removed item " + (position + 1), Toast.LENGTH_SHORT).show();
                mItems.remove(position);
                mAdapter.notifyDataSetChanged();
                writeItems(TODOS_FILENAME, mItems);
                return true;
            }
        });
    }


    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        if (itemText.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.error_empty_todo, Toast.LENGTH_SHORT).show();
            return;
        }

        mItems.add(itemText);
        etNewItem.setText("");
        writeItems(TODOS_FILENAME, mItems);
    }

    private ArrayList<String> readItems(String filename) {
        ArrayList<String> items;
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, filename);

        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
        return items;

    }

    private void writeItems(String filename, ArrayList<String> items) {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, filename);

        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            Log.e(TAG, "IOException - writing in " + todoFile.toString());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        writeItems(TODOS_FILENAME, mItems);
        Log.d(TAG, "onStop Called - items wrote");
    }

    // Intent Result
    @Override
    protected void onActivityResult(int position, int resultCode, Intent data) {
        // position is defined above
        if (data == null) {
            return;
        }

        int data_pos = data.getExtras().getInt("pos", -1);

        Log.d(TAG, "OnActivityResult pos: " + data_pos);

        if (data_pos > mItems.size() - 1 || data_pos < 0)
            return;

        // Extract name value from result extras
        String todo_text = data.getExtras().getString("todo");

        if (todo_text != null) {
            mItems.set(data_pos, todo_text);
            mAdapter.notifyDataSetChanged();
        }


        Log.d(TAG, "datasetChanged");
    }
}

