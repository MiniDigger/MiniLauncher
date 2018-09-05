package me.minidigger.minecraftlauncher.mojangapi.model;

public class SalesStats {

    private int total;
    private int last24h;
    private double saleVelocityPerSeconds;

    public int getTotal() {
        return total;
    }

    public int getLast24h() {
        return last24h;
    }

    public double getSaleVelocityPerSeconds() {
        return saleVelocityPerSeconds;
    }

    @Override
    public String toString() {
        return "SalesStats [total=" + total + ", last24h=" + last24h + ", saleVelocityPerSeconds="
                + saleVelocityPerSeconds + "]";
    }
}
