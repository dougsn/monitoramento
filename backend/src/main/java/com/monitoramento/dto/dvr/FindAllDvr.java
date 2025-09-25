package com.monitoramento.dto.dvr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@AllArgsConstructor
@Getter
@Setter
public class FindAllDvr implements Serializable {
    private Long value;
    private String label;
}
