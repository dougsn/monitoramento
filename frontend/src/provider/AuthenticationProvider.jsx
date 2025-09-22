import { createContext, useState, useEffect } from "react";
import api from "../services/api";
import { Navigate } from "react-router-dom";
import { deleteToken, getToken } from "../utils/localstorage";
import { Flex, Spinner } from "@chakra-ui/react";

export const AuthenticationContext = createContext({});

export const AuthenticationProvider = (props) => {
  const [userData, setUserData] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  const getUserData = async () => {
    const userToken = getToken();

    if (userToken) {
      try {
        const response = await api.get("/api/auth/user");

        if (response.status === 200) {
          setUserData(response.data);
          setIsAuthenticated(true);
        }
      } catch {
        deleteToken();
        setIsAuthenticated(false);
      }
    } else {
      deleteToken();
      setIsAuthenticated(false);
    }
    setLoading(false); // Set loading to false after authentication check
  };

  const getData = async () => {
    await getUserData();

    // Verifica se o estado de autenticação foi determinado
    if (loading) return;

    if (!isAuthenticated) {
      if (
        location.pathname !== "/" &&
        location.pathname !== "/auth/login" &&
        location.pathname !== "/auth/registro"
      ) {
        return <Navigate to="/auth/login" />;
      }
    }
  };

  useEffect(() => {
    getData();
  }, [isAuthenticated, loading]); // Dependencies to re-check when loading or auth status changes

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
    );
  }

  return (
    <AuthenticationContext.Provider
      value={{
        userData,
        setUserData,
        isAuthenticated,
        setIsAuthenticated,
      }}
    >
      {props.children}
    </AuthenticationContext.Provider>
  );
};
