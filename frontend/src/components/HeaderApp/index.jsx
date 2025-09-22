import { Flex, Stack, useMediaQuery } from "@chakra-ui/react";
import { ButtonsNavigateApp } from "./ButtonsNavigateApp";
import { ProfileApp } from "./ProfileApp";

export const HeaderApp = () => {
  const [isLargerThan850] = useMediaQuery("(max-width: 850px)");
  const [isLargerThan450] = useMediaQuery("(min-width: 450px)");

  return (
    <Flex
      as="header"
      w="100%"
      h="20"
      mx="auto"
      px="6"
      boxShadow="lg"
      align="center"
    >
      {/* {isLargerThan850 && <MenuApp />} */}
      {/* {isLargerThan450 && <LogoApp />} */}
      <Stack direction="row" ml={10}>
        <ButtonsNavigateApp />
      </Stack>
      <Flex align="center" ml="auto">
        <ProfileApp />
      </Flex>
    </Flex>
  );
};
