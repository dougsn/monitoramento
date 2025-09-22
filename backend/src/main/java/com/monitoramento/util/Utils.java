package com.monitoramento.util;

import com.monitoramento.service.exceptions.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class Utils {


    @Transactional(readOnly = true)
    public void serviceAfterOrBeforeDates(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) throw new BadRequestException("A data de início não pode ser após a data de fim.");
        if (end.isBefore(start)) throw new BadRequestException("A data de fim não pode ser anterior a data de início.");
    }

    public String formatData(LocalDateTime data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return data.format(formatter);
    }

    public static String formatarNumero(double valor) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat formato = new DecimalFormat("#,##0.00", symbols);
        return formato.format(valor);
    }


}
