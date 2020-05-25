package com.example.mymoney;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class GroupRecyclerAdaptor extends FirestoreRecyclerAdapter<Group, GroupRecyclerAdaptor.GroupViewHolder>{

    GroupListener groupListener;
    public GroupRecyclerAdaptor(@NonNull FirestoreRecyclerOptions<Group> options, GroupListener groupListener) {
        super(options);
        this.groupListener = groupListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupViewHolder holder, int position, @NonNull Group model) {
        holder.groupnameTextView.setText(model.getGroupname());
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_group, parent, false);
        return new GroupViewHolder(view);
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
         TextView groupnameTextView;
        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupnameTextView = itemView.findViewById(R.id.groupnameTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    groupListener.handleEditGroup(snapshot);
                }
            });
        }
    }

    interface GroupListener {
        public void handleEditGroup(DocumentSnapshot snapshot);
    }
}
