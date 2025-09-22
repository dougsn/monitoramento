export const formatNumberFrag = (value) => 
  value !== null ? new Intl.NumberFormat("pt-BR", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(value) : '';
