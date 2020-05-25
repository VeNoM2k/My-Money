package com.example.mymoney;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ExpenseRecyclerAdapter extends FirestoreRecyclerAdapter<Expense, ExpenseRecyclerAdapter.ExpenseViewHolder> {

    private static final String TAG = "ExpenseRecyclerAdapter";
    ExpenseListener expenseListener;

    public ExpenseRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Expense> options, ExpenseListener expenseListener) {
        super(options);
        this.expenseListener = expenseListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull Expense model) {
        holder.expamounttextView.setText(model.getAmount());
        holder.expdatetextView.setText(model.getShowdate());
        holder.expdestextView.setText(model.getDes());
        CharSequence datecharseq = DateFormat.format("EEEE, MMM d, yyyy h:mm:ss a", model.getCreated().toDate());
        holder.expcreatedtextView.setText(datecharseq);
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_exp, parent, false);
        return new ExpenseViewHolder(view);
    }

    class ExpenseViewHolder extends RecyclerView.ViewHolder {

        TextView expamounttextView, expdatetextView, expdestextView, expcreatedtextView, textView7, textView6, textView8;

        public ExpenseViewHolder(@NonNull View itemView) {

            super(itemView);

            expamounttextView = itemView.findViewById(R.id.expamounttextView);
            expdatetextView = itemView.findViewById(R.id.expdatetextView);
            expdestextView = itemView.findViewById(R.id.expdestextView);
            expcreatedtextView = itemView.findViewById(R.id.expcreatedtextView);
            textView6 = itemView.findViewById(R.id.textView6);
            textView7 = itemView.findViewById(R.id.textView7);
            textView8 = itemView.findViewById(R.id.textView8);
        }

        public void deleteitem() {
            expenseListener.handleDeleteItem(getSnapshots().getSnapshot(getAdapterPosition()));
        }

    }

    interface ExpenseListener {
        public void handleDeleteItem(DocumentSnapshot snapshot);
    }

}
