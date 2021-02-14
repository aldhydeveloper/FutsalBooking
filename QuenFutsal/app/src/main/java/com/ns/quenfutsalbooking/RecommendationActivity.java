package com.ns.quenfutsalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecommendationActivity extends AppCompatActivity {

    ImageView btnBackRec;
    TextView tvPlaceRec, tvLantaiRec, tvPriceRec;
    RadioGroup rdGroup;
    RadioButton rb1, rb2;
    CheckBox chOutdoor, chIndoor;
    Button btnRec;

    Dialog dialog;

    String getBola, getLapangan, getLantai, getPrice, getDoor1 = "0", getDoor2 = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        btnBackRec = findViewById(R.id.btnBackRec);
        tvPlaceRec = findViewById(R.id.tvPlaceRec);
        tvLantaiRec = findViewById(R.id.tvLantaiRec);
        tvPriceRec = findViewById(R.id.tvPriceRec);
        chOutdoor = findViewById(R.id.chOutdoor);
        chIndoor = findViewById(R.id.chIndoor);
        btnRec = findViewById(R.id.btnRec);
        rdGroup = findViewById(R.id.rdGroup);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);


        btnRec.setOnClickListener(v ->
                initRec());

        btnBackRec.setOnClickListener(v -> super.onBackPressed());

        initKec();
        initFloor();
        initPrice();
    }

    private void initRec() {
        if (rb1.isChecked()) {
            getBola = rb1.getText().toString();
        }

        if (rb2.isChecked()) {
            getBola = rb2.getText().toString();
        }

        if (chOutdoor.isChecked())
            getDoor1 = chOutdoor.getText().toString();

        if (chIndoor.isChecked())
            getDoor2 = chIndoor.getText().toString();

        startActivity(new Intent(this, ResultActivity.class)
                .putExtra("daerah", getLapangan)
                .putExtra("door1",getDoor1)
                .putExtra("door2",getDoor2)
                .putExtra("lantai",getLantai)
                .putExtra("bola",getBola)
                .putExtra("price",getPrice)
        );

        Toast.makeText(this, getLapangan + "\n" + getDoor1 + "\n" + getDoor2 + "\n" +
                getLantai + "\n" + getBola + "\n" + getPrice, Toast.LENGTH_SHORT).show();
    }

    private void initFloor() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Rumput Sintetis");
        arrayList.add("Rumput Biasa");
        arrayList.add("Matras");

        tvLantaiRec.setOnClickListener(v -> {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_spinner);
            dialog.getWindow().setLayout(650, 800);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            EditText editText = dialog.findViewById(R.id.edtText);
            ListView listView = dialog.findViewById(R.id.listSpinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this
                    , android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener((parent, view, position, id) -> {
                tvLantaiRec.setText(adapter.getItem(position));
                getLantai = adapter.getItem(position);
                dialog.dismiss();
            });
        });
    }

    private void initPrice() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(" < Rp 50.000");
        arrayList.add("Rp 50.000 - 80.000");
        arrayList.add(" > Rp 80.000");

        tvPriceRec.setOnClickListener(v -> {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_spinner);
            dialog.getWindow().setLayout(650, 800);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            EditText editText = dialog.findViewById(R.id.edtText);
            ListView listView = dialog.findViewById(R.id.listSpinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this
                    , android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener((parent, view, position, id) -> {
                tvPriceRec.setText(adapter.getItem(position));
                getPrice = adapter.getItem(position);
                dialog.dismiss();
            });
        });
    }

    private void initKec() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Coblong");
        arrayList.add("Antapani");
        arrayList.add("Pasteur");
        arrayList.add("Lengkong");

        tvPlaceRec.setOnClickListener(v -> {
            dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_spinner);
            dialog.getWindow().setLayout(650, 800);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            EditText editText = dialog.findViewById(R.id.edtText);
            ListView listView = dialog.findViewById(R.id.listSpinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this
                    , android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            listView.setOnItemClickListener((parent, view, position, id) -> {
                tvPlaceRec.setText(adapter.getItem(position));
                getLapangan = adapter.getItem(position);
                dialog.dismiss();
            });
        });
    }

    @Override
    public void onBackPressed() {
    }
}