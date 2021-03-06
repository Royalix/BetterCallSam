package fr.kounecorp.bettercallsam.AccueilAndConso;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fr.kounecorp.bettercallsam.R;

public class ConsoActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    ListView mListView;
    Button delAllConso;
    TextView nomtexte,degtexte,nbverretexte,heuretexte;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conso);
        mDatabaseHelper = new DatabaseHelper(this);
        mListView = (ListView) findViewById(R.id.listViewConso);
        delAllConso = (Button) findViewById(R.id.delAllConso);
        nomtexte = (TextView)findViewById(R.id.alctexte);
        degtexte=findViewById(R.id.degtexte);
        nbverretexte = findViewById(R.id.nbverretexte);
        heuretexte = findViewById(R.id.heuretexte);
        Intent intent = getIntent();
        final String IDUser = intent.getExtras().getString("ID");
        Cursor data = mDatabaseHelper.getConsoWhereID(IDUser);
        ArrayList<Consommer> consoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(new Date());

        while (data.moveToNext()) {
            String idAlc = data.getString(1);
            int nbV = data.getInt(3);
            String heure = data.getString(4);
            int deg = mDatabaseHelper.getDegWhereID(idAlc);
            String nomAlc = mDatabaseHelper.getDataWhereId(Integer.parseInt(idAlc)).getNomAlc();
            String datehCString = heure.substring(0,10);
            Log.e("TESTTTTTT",datehCString);
            Log.e("TESTTTTTT2",strDate);

            Consommer C = new Consommer(nomAlc, nbV, heure, deg);
            consoList.add(C);

        }




        ConsoListAdapter adapter = new ConsoListAdapter(this,R.layout.adapter_view,consoList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myintent = new Intent(view.getContext(),ConsoJour.class);
                myintent.putExtra("position",position);
                myintent.putExtra("iduser",IDUser);
                startActivity(myintent);
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);


        delAllConso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setCancelable(true);
                builder.setTitle("Suppression Consommation");
                builder.setMessage("Voulez-vous vraiment supprimer toute votre consommation ? ");
                builder.setPositiveButton("Oui",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabaseHelper.supprimerTouteConso(IDUser);
                            }
                        });
                builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }


}