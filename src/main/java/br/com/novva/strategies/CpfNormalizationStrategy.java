package br.com.novva.strategies;

public class CpfNormalizationStrategy implements NormalizationStrategy {
    @Override
    public String normalize(String cpf) {
        return cpf != null ? cpf.replaceAll("[^0-9]", "") : "";
    }
}