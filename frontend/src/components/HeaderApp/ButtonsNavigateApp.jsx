import { Flex, Box, useMediaQuery } from "@chakra-ui/react";
import { useContext } from "react";
import { AuthenticationContext } from "../../provider/AuthenticationProvider";
import { NavItem } from "../ui/nav-item";

export const ButtonsNavigateApp = () => {
  const { isAuthenticated } = useContext(AuthenticationContext);

  const [isLargerThan1140] = useMediaQuery("(min-width: 1140px)");

  const renderAllLinks = () => {
    return (
      <>
        <NavItem to="/status" pathSegment="status">
          Status
        </NavItem>
        <NavItem to="/filial" pathSegment="filial">
          Filial
        </NavItem>
        <NavItem to="/operacional" pathSegment="operacional">
          Operacional
        </NavItem>
        <NavItem to="/ensino-fundamental" pathSegment="ensino-fundamental">
          Ensino Fundamental
        </NavItem>
        <NavItem to="/pre" pathSegment="pre">
          Pre
        </NavItem>
        <NavItem to="/ensino-medio" pathSegment="ensino-medio">
          Ensino Médio
        </NavItem>
        <NavItem to="/user" pathSegment="user">
          Usuário
        </NavItem>
      </>
    );
  };

  return (
    <Flex align="center">
      <Box mr="4" textAlign="right">
        <Box display={"flex"} gap={5}>
          {isAuthenticated && <>{isLargerThan1140 && renderAllLinks()}</>}
        </Box>
      </Box>
    </Flex>
  );
};
