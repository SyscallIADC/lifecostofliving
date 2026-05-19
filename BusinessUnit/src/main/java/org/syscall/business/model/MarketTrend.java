package org.syscall.business.model;

public enum MarketTrend {
    STABLE("✅", "Mercado Estable"),
    INFLATION("🔴", "Inflación Activa"),
    DEFLATION("🔵", "Deflación / Abaratamiento"),
    VOLATILE("⚠️", "Alta Volatilidad");

    private final String emoji;
    private final String description;

    MarketTrend(String emoji, String description) {
        this.emoji = emoji;
        this.description = description;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getDescription() {
        return description;
    }
}