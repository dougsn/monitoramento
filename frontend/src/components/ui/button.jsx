import { AbsoluteCenter, Button as ChakraButton, Span } from "@chakra-ui/react";
import { BeatLoader } from "react-spinners";
import * as React from "react";

export const Button = React.forwardRef(function Button(props, ref) {
  const {
    type,
    color = "blue",
    loading,
    disabled,
    loadingText,
    children,
    ...rest
  } = props;
  return (
    <ChakraButton
      size={{ base: "sm", md: "md" }}
      type={type}
      colorPalette={color}
      disabled={loading || disabled}
      isLoading={loading}
      ref={ref}
      {...rest}
    >
      {loading && !loadingText ? (
        <>
          <AbsoluteCenter display="inline-flex">
            <BeatLoader size={8} color="white" />
          </AbsoluteCenter>
          <Span opacity={0}>{children}</Span>
        </>
      ) : loading && loadingText ? (
        <>
          <BeatLoader size={8} color="white" />
          {loadingText}
        </>
      ) : (
        children
      )}
    </ChakraButton>
  );
});
