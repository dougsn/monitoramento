import { Field as ChakraField } from "@chakra-ui/react";
import * as React from "react";

export const Field = React.forwardRef(function Field(props, ref) {
  const { label, children, helperText, errorText, optionalText, ...rest } =
    props;
  return (
    <ChakraField.Root alignItems={"stretch"} ref={ref} invalid={!!errorText} {...rest}>
      {label && (
        <ChakraField.Label>
          {label}
          <ChakraField.RequiredIndicator fallback={optionalText} />
        </ChakraField.Label>
      )}
      {children}
      {helperText && (
        <ChakraField.HelperText>{helperText}</ChakraField.HelperText>
      )}
      {errorText && (
        <ChakraField.ErrorText>{errorText.message}</ChakraField.ErrorText>
      )}
    </ChakraField.Root>
  );
});
