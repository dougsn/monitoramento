import {
  Skeleton as ChakraSkeleton,
  Circle,
  Stack,
  Table,
  // ... importe outros componentes que você já tem
} from "@chakra-ui/react";
import * as React from "react";

export const SkeletonCircle = React.forwardRef(
  function SkeletonCircle(props, ref) {
    const { size, ...rest } = props;
    return (
      <Circle size={size} asChild ref={ref}>
        <ChakraSkeleton {...rest} />
      </Circle>
    );
  }
);

export const SkeletonText = React.forwardRef(function SkeletonText(props, ref) {
  const { noOfLines = 3, gap, ...rest } = props;
  return (
    <Stack gap={gap} width="full" ref={ref}>
      {Array.from({ length: noOfLines }).map((_, index) => (
        <ChakraSkeleton
          height="4"
          key={index}
          {...props}
          _last={{ maxW: "80%" }}
          {...rest}
        />
      ))}
    </Stack>
  );
});

export const SkeletonTable = React.forwardRef(
  function SkeletonTable(props, ref) {
    const { rows = 5, columns = 3, ...rest } = props;

    return (
      <Table.Root ref={ref} {...rest}>
        <Table.Body>
          {Array.from({ length: rows }).map((_, rowIndex) => (
            <Table.Row key={`skeleton-row-${rowIndex}`}>
              {Array.from({ length: columns }).map((_, colIndex) => (
                <Table.Cell key={`skeleton-col-${rowIndex}-${colIndex}`}>
                  <ChakraSkeleton height="7" w="full" />
                </Table.Cell>
              ))}
            </Table.Row>
          ))}
        </Table.Body>
      </Table.Root>
    );
  }
);

export const Skeleton = ChakraSkeleton;
