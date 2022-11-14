package com.hola.tareabaseexterna;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ArrayList<Persona> listado = new ArrayList<>();
    ArrayAdapter<Persona> adapter;
    //DECLARAMOS VARIABLES
    EditText rut, nom, ape;
    ListView listapersona;
    Persona personaSelected;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ENLAZAMOS LOS COMPONENTES CON LAS VARIABLES
        rut=(EditText)findViewById(R.id.editRut);
        nom=(EditText) findViewById(R.id.editNom);
        ape=(EditText) findViewById(R.id.editApe);
        listapersona=(ListView)findViewById(R.id.lista);

        inicializarfirebase();
        listardatos();

        listapersona.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personaSelected = (Persona) parent.getItemAtPosition(position);
                rut.setText(personaSelected.getRut());
                nom.setText(personaSelected.getNombre());
                ape.setText(personaSelected.getApellido());
            }
        });
    }

    private void listardatos() {
        databaseReference.child("Persona").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listado.clear();
                for(DataSnapshot objetodata : snapshot.getChildren()){
                    Persona p = objetodata.getValue(Persona.class);
                    listado.add(p);

                    adapter = new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1, listado);
                    listapersona.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inicializarfirebase() {
        FirebaseApp.initializeApp(this);
        //PASAMOS LA INSTANCIA Y LA REFERENCIA DE LA BD
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        Toast.makeText(getApplicationContext(),"Ejecuta",Toast.LENGTH_SHORT).show();
    }

    public void registrarDatos(View view){
        String Rut = rut.getText().toString();
        String nombre = nom.getText().toString();
        String apellido = ape.getText().toString();
        
        if(Rut.equals("")||nombre.equals("")||apellido.equals("")){
            rut.setError("Campo requerido");
            nom.setError("Campo requerido");
            ape.setError("Campo requerido");
        }else{
            Persona p = new Persona();
            p.setUid(UUID.randomUUID().toString());
            p.setRut(Rut);
            p.setNombre(nombre);
            p.setApellido(apellido);
            databaseReference.child("Persona").child(p.getUid()).setValue(p);
            Toast.makeText(getApplicationContext(),"Persona registrada", Toast.LENGTH_SHORT).show();
        }
        rut.setText("");
        nom.setText("");
        ape.setText("");
    }

    public void buscarDatos(View view){

        /*String Rut = rut.getText().toString();

        Query query = databaseReference.child("Persona").orderByChild("Rut").equalTo(Rut);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               listado.clear();
               for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                   listado.add(dataSnapshot.getValue(Persona.class));

               }
               adapter = new ArrayAdapter<Persona>(MainActivity.this, android.R.layout.simple_list_item_1, listado);
               listapersona.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listado.clear();
                for(DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Persona p = objSnaptshot.getValue(Persona.class);

                    String Nombre = p.getNombre();
                    listado.add(p);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    public void modificarDatos(View view){
        String Rut = rut.getText().toString();
        String nombre = nom.getText().toString();
        String apellido = ape.getText().toString();

        if(Rut.equals("")||nombre.equals("")||apellido.equals("")){
            rut.setError("Campo requerido");
            nom.setError("Campo requerido");
            ape.setError("Campo requerido");
        }else{
            Persona p2 = new Persona();
            p2.setUid(personaSelected.getUid());
            p2.setRut(rut.getText().toString().trim());
            p2.setNombre(nom.getText().toString().trim());
            p2.setApellido(ape.getText().toString().trim());
            databaseReference.child("Persona").child(p2.getUid()).setValue(p2);
            Toast.makeText(this, "Registro modificado", Toast.LENGTH_SHORT).show();
        }
        rut.setText("");
        nom.setText("");
        ape.setText("");
    }

    public void eliminarDatos(View view){
        String Rut = rut.getText().toString();
        String nombre = nom.getText().toString();
        String apellido = ape.getText().toString();

        if(Rut.equals("")||nombre.equals("")||apellido.equals("")){
            rut.setError("Campo requerido");
            nom.setError("Campo requerido");
            ape.setError("Campo requerido");
        }else {
            Persona p3 = new Persona();
            p3.setUid(personaSelected.getUid());
            databaseReference.child("Persona").child(p3.getUid()).removeValue();
            Toast.makeText(this, "Registro eliminado", Toast.LENGTH_SHORT).show();
        }

        rut.setText("");
        nom.setText("");
        ape.setText("");
    }
}