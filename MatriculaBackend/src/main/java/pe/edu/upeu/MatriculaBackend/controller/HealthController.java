package pe.edu.upeu.MatriculaBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
@Tag(name = "Health Check", description = "Endpoint de verificación de estado y conexión a la base de datos")
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping
    @Operation(summary = "Verificar salud del sistema y conexión a Oracle")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());

        try {
            // Consulta simple para validar conectividad con la BBDD
            jdbcTemplate.execute("SELECT 1 FROM DUAL");
            response.put("status", "UP");
            response.put("database", "Oracle DB Connected");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("database", "Oracle DB Unreachable: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }
}