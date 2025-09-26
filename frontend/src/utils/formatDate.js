export function formatDate(dateString) {
  const [year, month, day] = dateString.split("-"); // Separa o ano, mês e dia
  return `${day}/${month}/${year}`;
}
