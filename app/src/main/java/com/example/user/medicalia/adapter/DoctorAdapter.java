package com.example.user.medicalia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.medicalia.R;
import com.example.user.medicalia.models.Doctor;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by USER on 08/05/2016.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private final Context context;
    private List<Doctor> doctorsList;

    public DoctorAdapter(Context context, List<Doctor> doctorsList) {
        this.context = context;
        if (doctorsList == null){
            doctorsList = new ArrayList<Doctor>();
        }
        this.doctorsList = doctorsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Doctor currentDoctor = doctorsList.get(position);
        holder.txt_name.setText(currentDoctor.getName());
    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.container)
        public LinearLayout rl_container;

        @Bind(R.id.category_name)
        public TextView txt_name;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
