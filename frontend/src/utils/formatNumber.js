export function formatNumber(value) {
  // Remove todos os pontos (separadores de milhar) e substitui a vírgula pelo ponto (separador decimal)
  const formattedValue = value.replace(/\./g, "").replace(",", ".");
  return formattedValue;
}
