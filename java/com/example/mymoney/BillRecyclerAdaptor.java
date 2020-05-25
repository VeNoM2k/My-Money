package com.example.mymoney;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class BillRecyclerAdaptor extends FirestoreRecyclerAdapter<Bill, BillRecyclerAdaptor.BillViewHolder> {

    BillListener billListener;
    private static final String TAG = "BillRecyclerAdaptor";

    public BillRecyclerAdaptor(@NonNull FirestoreRecyclerOptions<Bill> options, BillListener billListener) {
        super(options);
        this.billListener = billListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull BillViewHolder holder, int position, @NonNull Bill model) {
        holder.billAmount.setText(model.getAmount());
        holder.billDate.setText(model.getShowdate());
        holder.billDes.setText(model.getDescription());
        CharSequence datecharseq = DateFormat.format("EEEE, MMM d, yyyy h:mm:ss a", model.getCreated().toDate());
        holder.created.setText(datecharseq);
        holder.impcheckBox.setChecked(model.isImp());
        holder.paidcheckBox.setChecked(model.isPaid());
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.viewbill, parent, false);
        return new BillViewHolder(view);
    }

    class BillViewHolder extends RecyclerView.ViewHolder {

        TextView amount;
        TextView billAmount;
        TextView dateofbill;
        TextView billDate;
        TextView desofbill;
        TextView billDes;
        TextView created;
        CheckBox impcheckBox, paidcheckBox;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amountofbill);
            billAmount = itemView.findViewById(R.id.billAmount);
            dateofbill = itemView.findViewById(R.id.dateofbill);
            billDate = itemView.findViewById(R.id.billDate);
            desofbill = itemView.findViewById(R.id.desofbill);
            billDes = itemView.findViewById(R.id.billDes);
            created = itemView.findViewById(R.id.created);
            impcheckBox = itemView.findViewById(R.id.impcheckBox);
            paidcheckBox = itemView.findViewById(R.id.paidcheckBox);

            /*paidcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    Bill bill = getItem(getAdapterPosition());
                    if (bill.isPaid() != isChecked) {
                        billListener.handleCheckChanged(isChecked, snapshot);
                    }
                }
            });*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    billListener.handleEditBill(snapshot);
                }
            });
        }
        public void deleteItem() {
            billListener.handleDeleteItem(getSnapshots().getSnapshot(getAdapterPosition()));
        }
    }

    interface BillListener {
        //public void handleCheckChanged(boolean isChecked, DocumentSnapshot snapshot);
        public void handleEditBill(DocumentSnapshot snapshot);
        public void handleDeleteItem(DocumentSnapshot snapshot);
    }
}
