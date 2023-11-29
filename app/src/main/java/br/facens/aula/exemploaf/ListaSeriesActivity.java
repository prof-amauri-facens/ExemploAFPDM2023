package br.facens.aula.exemploaf;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ListaSeriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private SeriesAdapter seriesAdapter;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_series);

        recyclerView = findViewById(R.id.recycler_view_series);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btnAdicionar = findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(view -> {
            // Chama um método para abrir um diálogo e permitir a adição de uma nova série
            abrirDialogoAdicionarSerie();
        });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail().replace(".", ",");
            Log.d("EMAIL #################", userEmail);
            databaseReference = FirebaseDatabase.getInstance().getReference().child(userEmail);

        }

        seriesAdapter = new SeriesAdapter();
        recyclerView.setAdapter(seriesAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Serie> series = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("ENTROU #################", snapshot.toString());
                    String serieNome = snapshot.getKey(); // Obtém o nome da série (The Office, Friends)
                    int episodio = snapshot.child("episodio").getValue(Integer.class); // Obtém o valor do episódio
                    String categoria = snapshot.child("categoria").getValue(String.class); // Obtém o valor da categoria
                    Serie serie = new Serie(serieNome, episodio, categoria);
                    series.add(serie);

                }
                seriesAdapter.setSeriesList(series);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Trate erros de leitura do banco de dados, se necessário
            }
        });
    }

    private void abrirDialogoAdicionarSerie() {
        // Implemente aqui a lógica para abrir um diálogo de inserção de dados
        // Pode ser um AlertDialog com campos para inserir nome, episódio e categoria
        // Ao clicar no botão "Adicionar", pegue os valores e adicione-os ao Firebase
        // Use um EditText para obter o nome da série, um EditText para o episódio e um Spinner para a categoria
        // Após obter esses valores, adicione ao Firebase da seguinte maneira:
        // databaseReference.child(nomeDaSerie).setValue(novoObjetoSerie);

        // Exemplo de uso de um AlertDialog para inserir os dados
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_adicionar_serie, null);

        EditText etNomeSerie = dialogView.findViewById(R.id.etNomeSerie);
        EditText etEpisodio = dialogView.findViewById(R.id.etEpisodio);
        Spinner spinnerCategoria = dialogView.findViewById(R.id.spinnerCategoria);

        builder.setView(dialogView)
                .setTitle("Adicionar Série")
                .setPositiveButton("Adicionar", (dialog, which) -> {
                    String nomeSerie = etNomeSerie.getText().toString();
                    int episodio = Integer.parseInt(etEpisodio.getText().toString());
                    String categoria = spinnerCategoria.getSelectedItem().toString();

                    Serie novaSerie = new Serie(nomeSerie, episodio, categoria);
                    databaseReference.child(nomeSerie).setValue(novaSerie);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
