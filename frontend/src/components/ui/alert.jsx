import { Alert as ChakraAlert } from "@chakra-ui/react";
import * as React from "react";

export const Alert = React.forwardRef(function Alert(props, ref) {
  const { title, children, icon, status, endElement, ...rest } = props;
  return (
    <ChakraAlert.Root
      ref={ref}
      {...rest}
      status={status}
      flexDirection="column"
      alignItems="center"
      textAlign="center"
      display="flex"
      height="400px"
    >
      {children ? (
        <ChakraAlert.Content
          flexDirection="column"
          alignItems="center"
          textAlign="center"
          justifyContent="center"
          gap={3}
        >
          <ChakraAlert.Indicator boxSize="40px">{icon}</ChakraAlert.Indicator>
          <ChakraAlert.Title fontSize="xl">{title}</ChakraAlert.Title>
          <ChakraAlert.Description fontSize="lg" fontWeight="500">
            {children}
          </ChakraAlert.Description>
        </ChakraAlert.Content>
      ) : (
        <ChakraAlert.Title flex="1">{title}</ChakraAlert.Title>
      )}
      {endElement}
    </ChakraAlert.Root>
  );
});
