package com.example.brewchat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brewchat.R;
import com.quickblox.users.model.QBUser;

import java.util.List;

/**
 * Created by jon on 29/04/15.
 */
public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<QBUser> entryList;

    public ContactsRecyclerAdapter(Context context, List<QBUser> contacts) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        entryList = contacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.contact_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QBUser entry = entryList.get(position);
        String name;
        // Graceful decline into less friendly name displays.
        if (entry.getFullName() != null) name = entry.getFullName();
        else if (entry.getEmail() != null) name = entry.getEmail();
        else name = entry.getLogin();
        holder.contactName.setText(name);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView contactImage;
        TextView contactName;

        public ViewHolder(View view) {
            super(view);
            contactImage = (ImageView) view.findViewById(R.id.contact_image);
            contactName = (TextView) view.findViewById(R.id.contact_name);
        }
    }

}
