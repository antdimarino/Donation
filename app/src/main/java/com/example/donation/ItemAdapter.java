package com.example.donation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

public class ItemAdapter extends ArrayAdapter<ItemProject> {


    public ItemAdapter(Context context, int resource, List<ItemProject> objects){
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_insertion, null);
        }


        TextView goal = (TextView) convertView.findViewById(R.id.messageTextView);
        TextView description = (TextView) convertView.findViewById(R.id.nameTextView);
        Button button = (Button) convertView.findViewById(R.id.button);

        final ItemProject insertion = getItem(position);

        goal.setVisibility(View.VISIBLE);
        goal.setText(insertion.getTitolo());
        description.setText(insertion.getDescrizione());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getContext(), Donation.class );
                //getContext().startActivity(intent);
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setData(Uri.parse("mailto:"+insertion.getEmail()));
                //i.putExtra(Intent.EXTRA_EMAIL, "michele.zito96@gmail.com");
                i.putExtra(Intent.EXTRA_SUBJECT, "I would donate to the Project:"+insertion.getTitolo());
                i.putExtra(Intent.EXTRA_TEXT, "Greetings "+insertion.getOnlus()+" .\nI am interested in donating to this project. Please re-contact me, to establish a payment method.");
                if(i.resolveActivity(getContext().getPackageManager()) != null)
                    getContext().startActivity(i);
            }
        });

        return convertView;
    }
}
