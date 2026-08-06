package org.example.model;

public class Currency {

    private long id;
    private String code;
    private String name;
    private String symbol;

    public Currency() {}

    public Currency(String code, Long id, String name, String symbol) {
        this.code = code;
        this.id = id;
        this.name = name;
        this.symbol = symbol;
    }

    public Currency(String code, String name, String symbol) {
        this.code = code;
        this.id = id;
        this.name = name;
        this.symbol = symbol;
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
}
