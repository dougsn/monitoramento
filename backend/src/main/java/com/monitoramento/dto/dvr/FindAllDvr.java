package com.monitoramento.dto.dvr;

import com.monitoramento.dto.camera.AllCamera;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@AllArgsConstructor
@Getter
@Setter
public class FindAllDvr implements Serializable {
    private Long value;
    private String label;
    private List<AllCamera> cameras;
}
