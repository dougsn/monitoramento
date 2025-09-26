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
    private Long statusIdDvr;
    private String nomeStatus;
    private List<AllCamera> cameras;
}
