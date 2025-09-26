package com.monitoramento.dto.relatorio.dashboard;

import com.monitoramento.dto.camera_relatorio.ViewCameraRelatorio;
import com.monitoramento.dto.dvr_relatorio.ViewDvrRelatorio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewDashboard {
    private List<QuantidadeGeral> quantidadeGeralDvr;
    private List<QuantidadeGeral> quantidadeGeralCamera;
}
