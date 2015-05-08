package com.example.brewchat.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brewchat.Application;
import com.example.brewchat.R;
import com.example.brewchat.adapters.ChatHistoryRecyclerAdapter;
import com.example.brewchat.domain.ChatGroup;
import com.example.brewchat.domain.ChatHistory;
import com.example.brewchat.domain.ChatMessage;
import com.example.brewchat.domain.User;
import com.example.brewchat.events.GroupMessageReceivedEvent;
import com.example.brewchat.events.MessageReceivedEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity {

    public static final String EXTRA_CHAT_GROUP = "chatgroup";
    public static final String EXTRA_PRIVATE_CHAT_USER = "user";
    public static final String EXTRA_IS_PRIVATE_CHAT = "private";

    private ChatGroup chatGroup;
    private User otherPerson;
    private ChatHistoryRecyclerAdapter adapter;

    @InjectView(R.id.messages_recyclerview)
    RecyclerView messagesRecyclerView;

    @InjectView(R.id.message_textview)
    EditText messageText;

    @OnClick(R.id.send_button)
    void onClick() {
        String message = messageText.getText().toString();
        if (message.equals("")) {
            Toast.makeText(this, "You must enter a message to send", Toast.LENGTH_SHORT).show();
            return;
        }
        if (otherPerson != null) {
            Application.getChatService().sendMessage(otherPerson, message);
            messageText.setText("");
            adapter.addMessage(Application.chatService.getCurrentUser(), message);
        } else if (chatGroup != null) {
            Application.getChatService().sendMessage(chatGroup, message);
            messageText.setText("");
            adapter.addMessage(Application.getChatService().getCurrentUser(), message);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ChatHistory history;
        if (getIntent().getBooleanExtra(EXTRA_IS_PRIVATE_CHAT, false)) {
            chatGroup = null;
            otherPerson = (User) getIntent().getSerializableExtra(EXTRA_PRIVATE_CHAT_USER);
            if (otherPerson == null)
                throw new IllegalArgumentException("Must provide a user to private chat with if " +
                        "using EXTRA_IS_PRIVATE_CHAT=true");
            // TODO figure out a way to get the private chat history
            history = new ChatHistory();
        } else {
            chatGroup = (ChatGroup) getIntent().getExtras().getSerializable(EXTRA_CHAT_GROUP);
            history = chatGroup.getChatHistory();
        }
        adapter = new ChatHistoryRecyclerAdapter(this, history);
        messagesRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (chatGroup != null) Application.getChatService().joinChatGroup(chatGroup);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(MessageReceivedEvent event) {
        ChatMessage message = event.getMessage();
        if (otherPerson != null && otherPerson.getId() == message.getSender().getId()) {
            adapter.addMessage(otherPerson, message.getMessage());
            messagesRecyclerView.scrollToPosition(adapter.getItemCount()-1);
        }
    }

    public void onEventMainThread(GroupMessageReceivedEvent event) {
        if (chatGroup != null && event.getGroupId() == chatGroup.getId()) {
            adapter.addMessage(event.getMessage().getSender(), event.getMessage().getMessage());
            messagesRecyclerView.scrollToPosition(adapter.getItemCount()-1);
        }
    }

}
