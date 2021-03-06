package com.com.technoparkproject.view.adapters.main_list_records;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.com.technoparkproject.R;
import com.com.technoparkproject.models.Topic;
import com.com.technoparkproject.models.TopicTypes;

class ItemTopicNameViewHolder extends RecyclerView.ViewHolder {
    private final TextView textViewTitle;

    ItemTopicNameViewHolder(@NonNull View itemView) {
        super(itemView);
        this.textViewTitle = itemView.findViewById(R.id.mlr_text_view_title);
    }

    void bindViewHolder(Topic topic) {
        String currentName = getTopicPrefixByType(topic.type) + topic.name.toLowerCase();
        textViewTitle.setText(currentName);
    }

    private String getTopicPrefixByType(TopicTypes type) {
        String prefix = "";
        if(type == TopicTypes.TOPIC_THEMATIC) {
            prefix = "#";
        }
        if(type == TopicTypes.TOPIC_FRIEND) {
            prefix = "§";
        }
        return prefix;
    }
}
