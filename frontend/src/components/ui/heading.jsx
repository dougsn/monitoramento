import { forwardRef } from "react";
import { Heading } from "@chakra-ui/react";

export const HeadingTitle = forwardRef(({ name }) => {
  return (
    <Heading
      name={name}
      size={{ base: "2xl", md: "3xl" }}
      fontWeight="500"
      textAlign={"start"}
      w="100%"
    >
      {name}
    </Heading>
  );
});
