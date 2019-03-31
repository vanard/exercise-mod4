package com.vanard.vian_1202154186_si4008_pab_modul4;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vanard.vian_1202154186_si4008_pab_modul4.model.ItemMenu;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

    private List<ItemMenu> menuList;
    private Context context;

    public MenuAdapter(List<ItemMenu> menuList, Context context) {
        this.menuList = menuList;
        this.context = context;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MenuHolder(LayoutInflater.from(context).inflate(R.layout.item_menu, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder menuHolder, int i) {
        ItemMenu menu = menuList.get(i);
        menuHolder.tvName.setText(menu.getName());
        menuHolder.tvHarga.setText("Rp. "+menu.getHarga());
        Picasso.get().load(menu.getImg()).into(menuHolder.img);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    class MenuHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvName, tvHarga;

        MenuHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_item_menu);
            tvName = itemView.findViewById(R.id.tv_name_item);
            tvHarga = itemView.findViewById(R.id.tv_harga_item);
        }
    }
}
