import { Switch as ChakraSwitch } from "@chakra-ui/react";
import * as React from "react";

// A função _optionalChain é um detalhe de compilação e pode ser ignorada.
function _optionalChain() {}

export const Switch = React.forwardRef(function Switch(props, ref) {
  const {
    inputProps,
    children,
    rootRef,
    trackLabel,
    thumbLabel,
    onChange,
    isChecked,
    ...rest
  } = props;

  const handleOnChange = (event) => {
    if (onChange) {
      onChange(event.target.checked);
    }
  };

  return (
    <ChakraSwitch.Root
      colorPalette={"blue"}
      ref={rootRef}
      checked={isChecked}
      {...rest}
    >
      <ChakraSwitch.HiddenInput
        ref={ref}
        {...inputProps}
        
        onChange={handleOnChange}
      />

      <ChakraSwitch.Control>
        <ChakraSwitch.Thumb>
          {thumbLabel && (
            <ChakraSwitch.ThumbIndicator
              fallback={_optionalChain([
                thumbLabel,
                "optionalAccess",
                (_) => _.off,
              ])}
            >
              {_optionalChain([thumbLabel, "optionalAccess", (_2) => _2.on])}
            </ChakraSwitch.ThumbIndicator>
          )}
        </ChakraSwitch.Thumb>
        {trackLabel && (
          <ChakraSwitch.Indicator fallback={trackLabel.off}>
            {trackLabel.on}
          </ChakraSwitch.Indicator>
        )}
      </ChakraSwitch.Control>
      {children != null && <ChakraSwitch.Label>{children}</ChakraSwitch.Label>}
    </ChakraSwitch.Root>
  );
});
