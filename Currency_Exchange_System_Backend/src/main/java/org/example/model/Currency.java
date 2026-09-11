package org.example.model;

public class Currency {

    private long id;
    private String code;
    private String name;
    private String symbol;
    private boolean isActive;

    public Currency() {}

    public Currency(String code, Long id, String name, String symbol) {
        this.code = code;
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.isActive = true;
    }

    public Currency(String code, String name, String symbol) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.isActive = true;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public  void setActive(boolean active) {
        this.isActive = active;
    }
}
