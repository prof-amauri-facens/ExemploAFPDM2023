package br.facens.aula.exemploaf;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {

    private List<Serie> seriesList;

    public void setSeriesList(List<Serie> seriesList) {
        this.seriesList = seriesList;
        notifyDataSetChanged();
    }

    static class SeriesViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTextView, episodioTextView, categoriaTextView;
        Button btnRemover;

        SeriesViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.text_view_nome_serie);
            episodioTextView = itemView.findViewById(R.id.text_view_episodio);
            categoriaTextView = itemView.findViewById(R.id.text_view_categoria);
            btnRemover = itemView.findViewById(R.id.btnRemover);
        }
    }

    @NonNull
    @Override
    public SeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serie, parent, false);
        return new SeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesViewHolder holder, int position) {
        Serie serie = seriesList.get(position);
        holder.nomeTextView.setText(serie.getNomeSerie());
        holder.episodioTextView.setText(String.valueOf(serie.getEpisodio()));
        holder.categoriaTextView.setText(serie.getCategoria());

        holder.btnRemover.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION && seriesList != null) {
                Serie serieRemover = seriesList.get(adapterPosition);
                String serieNome = serieRemover.getNomeSerie();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userEmail = user.getEmail().replace(".", ",");
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(userEmail);

                    databaseReference.child(serieNome).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(view.getContext(), "Removido com sucesso", Toast.LENGTH_SHORT).show();
                                // Remova o item da lista local apenas se a remoção no Firebase for bem-sucedida
                                seriesList.remove(serieRemover);
                                notifyItemRemoved(adapterPosition);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(view.getContext(), "Falha ao remover do Firebase", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return seriesList != null ? seriesList.size() : 0;
    }
}
