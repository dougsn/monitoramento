import { BrowserRouter, Outlet, Route, Routes } from "react-router-dom";
import { Flex } from "@chakra-ui/react";

import { Login } from "../pages/usuario/Login";
import { HeaderApp } from "../components/HeaderApp";

// Colaborador
import { Colaborador } from "../pages/colaborador";
import { CreateColaborador } from "../pages/colaborador/create";
import { UpdateColaborador } from "../pages/colaborador/update";

import PrivateRoute from "../provider/PrivateRoute";
import { AuthenticationProvider } from "../provider/AuthenticationProvider";

const AppRoutes = () => {
  return (
    <>
      <AuthenticationProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<AuthLayout />}>
              <Route path="" element={<Login />} />
            </Route>
            <Route element={<LayoutWithHeader />}>
              {/* Colaborador */}
              <Route
                path="/colaborador"
                element={
                  <PrivateRoute>
                    <Colaborador />
                  </PrivateRoute>
                }
              />
              <Route
                path="/colaborador/novo"
                element={
                  <PrivateRoute>
                    <CreateColaborador />
                  </PrivateRoute>
                }
              />
              <Route
                path="/colaborador/atualizar/:id"
                element={
                  <PrivateRoute>
                    <UpdateColaborador />
                  </PrivateRoute>
                }
              />
            </Route>
          </Routes>
        </BrowserRouter>
      </AuthenticationProvider>
    </>
  );
};

const AuthLayout = () => {
  return <Outlet />;
};

const LayoutWithHeader = () => {
  return (
    <>
      <HeaderApp />
      <Flex
        w="100%"
        flexDir={"column"}
        mt="6"
        maxWidth={1480}
        mx="auto"
        p="6"
        justifyContent={"center"}
      >
        <Outlet />
      </Flex>
    </>
  );
};

export default AppRoutes;
