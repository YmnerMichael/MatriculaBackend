package pe.edu.upeu.MatriculaBackend.dto;

import java.math.BigDecimal;

public interface MatriculadosPorCursoDTO {
    String getCodigo();
    String getCurso();
    Long getMatriculados();
    BigDecimal getMontoRecaudado();
}