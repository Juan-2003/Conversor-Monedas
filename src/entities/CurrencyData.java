package entities;

public record CurrencyData(String base_code,
                           String target_code,
                           double conversion_result) {
}
