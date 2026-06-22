package com.zzz.ss4.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzz.ss4.R;
import com.zzz.ss4.constant.Constant;
import com.zzz.ss4.model.Team;
import com.zzz.ss4.ui.ActivityIUD;
import com.zzz.ss4.util.BitmapUtil;

import java.util.List;

public class TeamAdapter extends BaseAdapter {
  private final Context context;
  private final int layout;
  private final List<Team> teams;

  public TeamAdapter(Context context, int layout, List<Team> teams) {
    this.context = context;
    this.layout = layout;
    this.teams = teams;
  }

  @Override
  public int getCount() {
    return teams.size();
  }

  @Override
  public Object getItem(int i) {
    return teams.get(i);
  }

  @Override
  public long getItemId(int i) {
    return teams.get(i).getId();
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    if (view == null) {
      LayoutInflater inflater =
          (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(layout, viewGroup, false);
    }
    TextView tvName = view.findViewById(R.id.tv_name);
    TextView tvStadium = view.findViewById(R.id.tv_stadium);
    TextView tvCapacity = view.findViewById(R.id.tv_capacity);
    ImageView imgLogo = view.findViewById(R.id.img_logo_fc);
    Button btnUpdate = view.findViewById(R.id.btn_update);
    btnUpdate.setText(Constant.UPDATE);

    Team team = teams.get(i);
    tvName.setText(team.getName());
    tvStadium.setText(team.getStadium());
    tvCapacity.setText(team.getCapacity());

    if (team.getLogo() != null) {
        Glide.with(context).load(team.getLogo()).circleCrop().into(imgLogo);
    } else {
        imgLogo.setImageResource(R.drawable.java); // Default image
    }

    btnUpdate.setOnClickListener(v -> {
      Intent intent = new Intent(context, ActivityIUD.class);
      intent.putExtra(Constant.ACTION, Constant.UPDATE);
      intent.putExtra(Constant.ID, team.getId());
      context.startActivity(intent);
    });

    return view;
  }


}
