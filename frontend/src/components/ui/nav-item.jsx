import { Button } from "@chakra-ui/react";
import { Link as RouterLink, useLocation } from "react-router-dom";

export const NavItem = ({ to, pathSegment, children }) => {
  const location = useLocation();
  const isActive = location.pathname.includes(pathSegment);

  // Sintaxe do Panda CSS para cores que mudam com o tema.
  // Não precisamos mais do `useColorMode` aqui!
  const activeBgColor = { base: "gray.100", _dark: "gray.900" };

  return (
    <Button
      // A prop `as` faz o Button ser renderizado como um RouterLink
      as={RouterLink}
      to={to}
      variant="surface" // Usamos um variant para remover estilos padrão de botão
      px={4}
      py={2}
      transition="all 0.2s"
      borderRadius="md"
      borderWidth="1px"
      bgColor={isActive ? activeBgColor : "transparent"}
      // O Panda CSS entende condicionais de tema dentro da prop `css`
      css={{
        "&:hover": {
          bgColor: activeBgColor,
        },
      }}
    >
      {children}
    </Button>
  );
};
