package com.ptpal.ptpal.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ptpal.ptpal.R;
import com.ptpal.ptpal.model.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionListAdapter extends ArrayAdapter<Session>
{
    private Context context;
    private int resource;

    public SessionListAdapter(Context context, int resource, ArrayList<Session> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
       String createdDate = getItem(position).getCreatedDate();
       String exercise = getItem(position).getExercise();
       Double duration = getItem(position).getDuration();
       int pronations = getItem(position).getPronations();
       int overExertions = getItem(position).getOverExertions();
       int overExtensions = getItem(position).getOverExtensions();

       Session session = new Session();
       session.setCreatedDate(createdDate);
       session.setDuration(duration);
       session.setPronations(pronations);
       session.setOverExertions(overExertions);
       session.setOverExtensions(overExtensions);

       LayoutInflater inflater = LayoutInflater.from(context);
       convertView = inflater.inflate(resource, parent, false);

       TextView tvCreatedDate = (TextView) convertView.findViewById(R.id.textViewHistoryCreatedDate);
       TextView tvExercise = (TextView) convertView.findViewById(R.id.textViewHistoryExercise);
       TextView tvDuration = (TextView) convertView.findViewById(R.id.textViewHistoryDuration);
       TextView tvPronations = (TextView) convertView.findViewById(R.id.textViewHistoryPronations);
       TextView tvOverExertions = (TextView) convertView.findViewById(R.id.textViewHistoryOverExertions);
       TextView tvOverExtensions = (TextView) convertView.findViewById(R.id.textViewHistoryOverExtensions);

       tvCreatedDate.setText(createdDate);
       tvExercise.setText(exercise);
       tvDuration.setText(String.valueOf(duration));
       tvPronations.setText(String.valueOf(pronations));
       tvOverExertions.setText(String.valueOf(overExertions));
       tvOverExtensions.setText(String.valueOf(overExtensions));
       return convertView;
    }
}
