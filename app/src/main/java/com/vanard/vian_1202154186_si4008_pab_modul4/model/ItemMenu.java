package com.vanard.vian_1202154186_si4008_pab_modul4.model;

public class ItemMenu {
    String name;
    String harga;
    String desc;
    String img;

    public ItemMenu() {
    }

    public ItemMenu(String name, String harga, String desc, String img) {
        this.name = name;
        this.harga = harga;
        this.desc = desc;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
