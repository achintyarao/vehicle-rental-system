package com.backend.vehiclerental.enums;

import lombok.Getter;

@Getter
public enum Pricing {
    FLAT(1.0),
    DYNAMIC(1.1);

    private Double increase;

    Pricing(Double increase){
        this.increase = increase;
    }
}
