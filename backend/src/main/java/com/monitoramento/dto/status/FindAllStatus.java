package com.monitoramento.dto.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@AllArgsConstructor
@Getter
@Setter
public class FindAllStatus implements Serializable {
    private Long value;
    private String label;
}
