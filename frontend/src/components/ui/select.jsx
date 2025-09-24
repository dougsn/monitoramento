"use client";

import {
  Portal,
  Select as ChakraSelect,
  createListCollection,
} from "@chakra-ui/react";
import { Controller } from "react-hook-form";
import { Field } from "./field";

const BaseSelect = ({ items, placeholder, value, onChange, error }) => {
  const dataList = createListCollection({
    items,
    getItemId: (item) => item.id,
  });

  return (
    <ChakraSelect.Root
      collection={dataList}
      size="sm"
      value={value}
      onValueChange={(details) => onChange(details.value)}
      invalid={!!error}
    >
      <ChakraSelect.HiddenSelect />
      <ChakraSelect.Control>
        <ChakraSelect.Trigger>
          <ChakraSelect.ValueText placeholder={placeholder} />
        </ChakraSelect.Trigger>
        <ChakraSelect.IndicatorGroup>
          <ChakraSelect.Indicator />
        </ChakraSelect.IndicatorGroup>
      </ChakraSelect.Control>
      <Portal>
        <ChakraSelect.Positioner>
          <ChakraSelect.Content>
            {dataList.items.map((data) => (
              <ChakraSelect.Item item={data} key={data.value}>
                {data.label}
              </ChakraSelect.Item>
            ))}
          </ChakraSelect.Content>
        </ChakraSelect.Positioner>
      </Portal>
    </ChakraSelect.Root>
  );
};

export const FormSelect = ({
  name,
  label,
  items,
  placeholder,
  control,
  formState,
}) => {
  return (
    <Field label={label} errorText={formState}>
      <Controller
        name={name}
        control={control}
        render={({ field }) => (
          <BaseSelect
            value={field.value}
            onChange={field.onChange}
            items={items}
            placeholder={placeholder}
            error={formState}
          />
        )}
      />
    </Field>
  );
};
