import { BrowserRouter, Outlet, Route, Routes } from "react-router-dom";
import { Flex } from "@chakra-ui/react";

import { Login } from "../pages/usuario/Login";
import { HeaderApp } from "../components/HeaderApp";

// Status
import { Status } from "../pages/status/index";
import { CreateStatus } from "../pages/status/create";
import { UpdateStatus } from "../pages/status/update";

// Dvr
import { Dvr } from "../pages/dvr/index";
import { CreateDvr } from "../pages/dvr/create";
import { UpdateDvr } from "../pages/dvr/update";

// Câmera
import { Camera } from "../pages/camera/index";
import { CreateCamera } from "../pages/camera/create";
import { UpdateCamera } from "../pages/camera/update";

// Relatório
import { CreateRelatorio } from "../pages/relatorio/create";

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
              {/* Status */}
              <Route
                path="/status"
                element={
                  <PrivateRoute>
                    <Status />
                  </PrivateRoute>
                }
              />
              <Route
                path="/status/novo"
                element={
                  <PrivateRoute>
                    <CreateStatus />
                  </PrivateRoute>
                }
              />
              <Route
                path="/status/atualizar/:id"
                element={
                  <PrivateRoute>
                    <UpdateStatus />
                  </PrivateRoute>
                }
              />

              {/* Dvr */}
              <Route
                path="/dvr"
                element={
                  <PrivateRoute>
                    <Dvr />
                  </PrivateRoute>
                }
              />
              <Route
                path="/dvr/novo"
                element={
                  <PrivateRoute>
                    <CreateDvr />
                  </PrivateRoute>
                }
              />
              <Route
                path="/dvr/atualizar/:id"
                element={
                  <PrivateRoute>
                    <UpdateDvr />
                  </PrivateRoute>
                }
              />

              {/* Camera */}
              <Route
                path="/camera"
                element={
                  <PrivateRoute>
                    <Camera />
                  </PrivateRoute>
                }
              />
              <Route
                path="/camera/novo"
                element={
                  <PrivateRoute>
                    <CreateCamera />
                  </PrivateRoute>
                }
              />
              <Route
                path="/camera/atualizar/:id"
                element={
                  <PrivateRoute>
                    <UpdateCamera />
                  </PrivateRoute>
                }
              />
              {/* Relatório */}
              <Route
                path="/relatorio/criar"
                element={
                  <PrivateRoute>
                    <CreateRelatorio />
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
