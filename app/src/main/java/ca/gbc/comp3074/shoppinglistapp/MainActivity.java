package ca.gbc.comp3074.shoppinglistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ItemAdapter adapter;
    private RecyclerView recyclerView;
    private List<Item> itemList = new ArrayList<>();
    private DbHelper db;

    private EditText itemInput;
    private Button addItemBtn;

    private void addRows(){
        if(db.getAllItems().isEmpty()){
            db.insertItem("Bananas");
            db.insertItem("Baby Diaper");
            db.insertItem("Hand Sanitizers");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildRecyclerView();

        itemInput = findViewById(R.id.item_input);
        addItemBtn = findViewById(R.id.add_button);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = itemInput.getText().toString().trim();

                if(newItem.length() == 0){
                    Toast.makeText(getApplicationContext(), "Item cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    itemInput.getText().clear();
                    insertItem(newItem);
                }
            }
        });

    }

    private void insertItem(String newItem) {
        db = new DbHelper(this);
        long rowID = db.insertItem(newItem);
        itemList.add(db.getItem(rowID));
        adapter.notifyItemInserted(itemList.size()-1);
    }

    private void buildRecyclerView() {
        db = new DbHelper(this);

        addRows();

        recyclerView = findViewById(R.id.recycler_view);

        itemList.addAll(db.getAllItems());

        adapter = new ItemAdapter(this, itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    private void removeItem(int position) {
        db = new DbHelper(this);
        int numberOfRowsDeleted = db.deleteItem(itemList.get(position));
        if(numberOfRowsDeleted > 0){
            itemList.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }
}



























