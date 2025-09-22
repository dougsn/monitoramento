import { forwardRef } from "react";
import { Input } from "@chakra-ui/react";
import { NumericFormat } from "react-number-format";
import { Field } from "./field";

export const CurrencyInput = forwardRef(
  ({ name, label, error, ...rest }, ref) => {
    return (
      <Field errorText={error} label={label}>
        <NumericFormat
          name={name}
          customInput={Input} // Usa o Input do Chakra como base
          placeholder="R$ 0,00"
          prefix="R$ "
          thousandSeparator="."
          decimalSeparator=","
          decimalScale={2} 
          fixedDecimalScale={true} // Mantém as casas decimais mesmo que sejam zero
          allowNegative={false} // Não permite salários negativos
          // --- Integração com React Hook Form e outros ---
          getInputRef={ref} // Passa a ref para o input interno
          {...rest} // Passa outras props como onBlur, onChange, etc.
        />
      </Field>
    );
  }
);
