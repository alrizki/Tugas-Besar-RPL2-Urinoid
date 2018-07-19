package urinoid.urinoid.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import urinoid.urinoid.Login;
import urinoid.urinoid.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import clarifai2.dto.prediction.Color;
import urinoid.urinoid.Registrasi;

public class RecognizeConceptsAdapter extends RecyclerView.Adapter<RecognizeConceptsAdapter.Holder> {

  @NonNull
  private List<Color> concepts = new ArrayList<>();

  public RecognizeConceptsAdapter setData(@NonNull List<Color> concepts) {
    this.concepts = concepts;
    notifyDataSetChanged();
    return this;
  }

  @Override
  public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.clarifai_itemconcept, parent, false));
  }

  @Override
  public void onBindViewHolder(Holder holder, int position) {
    final Color concept = concepts.get(position);
    holder.label.setText(concept.hex() != null ? concept.hex() : concept.hex());
    holder.probability.setText(String.valueOf(concept.webSafeHex()));
  }

  @Override
  public int getItemCount() {
    return concepts.size();
  }


  static final class Holder extends RecyclerView.ViewHolder {

    @BindView(R.id.label)
    TextView label;
    @BindView(R.id.probability)
    TextView probability;

    public Holder(View root) {
      super(root);
      ButterKnife.bind(this, root);
    }
  }

}
