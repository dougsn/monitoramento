import { useContext } from "react";
import { Navigate } from "react-router-dom";
import { AuthenticationContext } from "./AuthenticationProvider";
import { Flex, Spinner } from "@chakra-ui/react";

const PrivateRoute = ({ children }) => {
  const { isAuthenticated, loading } = useContext(AuthenticationContext);

  // Não exibe o conteúdo até que a verificação de autenticação seja concluída
  if (loading) {
    return (
      <Flex
        w={"100%"}
        minH={"100vh"}
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        textAlign="center"
        height="400px"
      >
        <Spinner
          size="xl"
          speed=".45s"
          emptyColor="gray.200"
          color="blue.500"
        />
      </Flex>
    ); // Ou outro componente de loading
  }

  return isAuthenticated ? children : <Navigate to="/auth/login" />;
};

export default PrivateRoute;
