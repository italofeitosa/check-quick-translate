package com.br.italofeitosa.quicktranslate.ui.adaptee;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.br.italofeitosa.quicktranslate.R;
import com.br.italofeitosa.quicktranslate.model.Resource;

import java.util.List;

/**
 * @author italofeitosa on 15/06/17.
 */

public class ResourceAdapter extends  RecyclerView.Adapter<ResourceAdapter.ViewHolder> {

    private List<Resource> mResources;

    public ResourceAdapter(List<Resource> resources) {
        mResources = resources;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView resourceTextView;

        public TextView updateTextView;

        public TextView valueTextView;

        public ViewHolder(View itemView) {

            super(itemView);

            resourceTextView = (TextView) itemView.findViewById(R.id.resource_id);
            updateTextView = (TextView) itemView.findViewById(R.id.upadate_at);
            valueTextView = (TextView) itemView.findViewById(R.id.value);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View resourceView = inflater.inflate(R.layout.content_item_resource, parent, false);

        ViewHolder viewHolder = new ViewHolder(resourceView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Resource resource = mResources.get(position);

        TextView resourceTextView = viewHolder.resourceTextView;
        resourceTextView.setText(resource.getResourceId());

        TextView updateTextView = viewHolder.updateTextView;
        updateTextView.setText(resource.getUpdatedAt().toString());

        TextView valueTextView = viewHolder.valueTextView;
        valueTextView.setText(resource.getValue());

    }

    @Override
    public int getItemCount() {
        return mResources.size();
    }
}
