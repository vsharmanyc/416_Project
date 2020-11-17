package com.panthers.main.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StateDto {
    private String state;

    public StateDto(@JsonProperty("state") String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}