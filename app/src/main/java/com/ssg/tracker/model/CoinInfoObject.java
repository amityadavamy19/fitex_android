package com.ssg.tracker.model;

public class CoinInfoObject
{
    private String date;

    private String u_id;

    private String coins;

    private String id;

    private String remarks;

    private String status;

    public CoinInfoObject(String date, String u_id, String coins, String id, String remarks, String status) {
        this.date = date;
        this.u_id = u_id;
        this.coins = coins;
        this.id = id;
        this.remarks = remarks;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
