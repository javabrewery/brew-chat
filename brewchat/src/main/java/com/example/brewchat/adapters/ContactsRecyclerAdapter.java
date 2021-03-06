package com.example.brewchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brewchat.R;
import com.example.brewchat.activities.ChatActivity;
import com.example.brewchat.domain.User;

import java.util.List;

/**
 * Created by jon on 29/04/15.
 */
public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<User> entryList;

    public ContactsRecyclerAdapter(Context context, List<User> contacts) {
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
        User entry = entryList.get(position);
        String name;
        // Graceful decline into less friendly name displays.
        if (entry.getName() != null) name = entry.getName();
        else if (entry.getEmail() != null) name = entry.getEmail();
        else name = entry.getLogin();
        holder.contactName.setText(name);

        holder.view.setOnClickListener(new OnContactClickListener(entry));

        // TODO look into contact images. Maybe via gravitar? https://en.gravatar.com/site/implement
        holder.contactImage.setImageResource(R.drawable.ic_account_box_grey_800_48dp);
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
            this.view = view;
            contactImage = (ImageView) view.findViewById(R.id.contact_image);
            contactName = (TextView) view.findViewById(R.id.contact_name);
        }
    }

    class OnContactClickListener implements View.OnClickListener {

        private User user;

        public OnContactClickListener(User user) {
            this.user = user;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(ChatActivity.EXTRA_IS_PRIVATE_CHAT, true);
            intent.putExtra(ChatActivity.EXTRA_PRIVATE_CHAT_USER, user);
            context.startActivity(intent);
        }
    }

}
