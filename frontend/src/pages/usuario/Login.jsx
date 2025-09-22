import { Flex, Heading, Input, Stack } from "@chakra-ui/react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useContext, useState } from "react";
import { useForm } from "react-hook-form";
import * as yup from "yup";
import { Field } from "../../components/ui/field";
import { ColorModeButton } from "../../components/ui/color-mode";
import { PasswordInput } from "../../components/ui/password-input";
import { Button } from "../../components/ui/button";
import { setToken } from "../../utils/localstorage";
import { getUserData } from "../../utils/utils";
import { AuthenticationContext } from "../../provider/AuthenticationProvider";
import { useNavigate } from "react-router-dom";
import { toaster } from "../../components/ui/toaster";
import api from "../../services/api";
import { HeaderApp } from "../../components/HeaderApp";

const CreateUserFormSchema = yup.object().shape({
  email: yup
    .string()
    .required("O e-mail é obrigatório")
    .email("E-mail inválido."),
  password: yup.string().required("A senha é obrigatória"),
});

export const Login = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [jwtToken, setJwtToken] = useState(false);
  const navigate = useNavigate();

  const { setIsAuthenticated, setUserData } = useContext(AuthenticationContext);

  const { register, handleSubmit, formState } = useForm({
    resolver: yupResolver(CreateUserFormSchema),
    mode: "onChange",
  });

  const handleSignIn = async (data) => {
    const newUser = {
      email: data.email.trim(),
      password: data.password.trim(),
    };
    setIsLoading(true);

    try {
      const request = await api.post("api/auth/public/signin", newUser);
      setJwtToken(request.data.jwtToken);

      if (request.status === 200) {
        setToken(request.data.jwtToken);
        const userLogin = await getUserData();

        toaster.create({
          title: "Login realizado com sucesso!",
          type: "success",
          closable: true,
          duration: 1000,
        });
        setUserData(userLogin);
        setIsAuthenticated(true);
        setTimeout(() => {
          setIsLoading(false);
          if (userLogin.data.role !== "ROLE_ADMIN") {
            navigate(`/colaborador`);
          } else {
            navigate(`/colaborador`);
          }
        }, 1000);
      }
    } catch (error) {
      setIsLoading(false);
      if (error.message == "Network Error") {
        toaster.create({
          title: "Serviço indisponível no momento, tento novamente mais tarde",
          type: "error",
          closable: true,
          duration: 3000,
        });
        return false;
      }

      if (error.response.status == 429) {
        toaster.create({
          title: "Limite de requisições excedido, aguarde 1 minuto!",
          type: "error",
          closable: true,
          duration: 3000,
        });
        return false;
      }

      if (error.response.status == 404) {
        toaster.create({
          title: "Revise os dados inseridos",
          type: "info",
          closable: true,
          duration: 3000,
        });
        return false;
      }
      toaster.create({
        title: error.response.data.errorMessage,
        type: "error",
        closable: true,
        duration: 3000,
      });
    }
  };

  return (
    <>
      <Flex h={"100vh"} p={8} align="center" justify="center">
        <Flex w="100%" flexDir="column" maxW="md">
          <Stack
            as="form"
            // ✅ Usando a função com o nome correto
            onSubmit={handleSubmit(handleSignIn)}
            spacing={10}
          >
            <Flex
              justifyContent="center"
              flexDir="column"
              alignItems="center"
              gap={10}
            >
              <Heading
                fontSize={{ base: "22px", md: "24px", xl: "30px" }}
                size="lg"
                fontWeight="500"
              >
                Entrar no sistema
              </Heading>
            </Flex>

            <Field label={"Email"} errorText={formState.errors.email}>
              <Input {...register("email")} />
            </Field>

            <Field errorText={formState.errors.password} label={"Senha"}>
              <PasswordInput {...register("password")} />
            </Field>

            {/* Adicionando a prop isLoading ao botão */}
            <Button type="submit" loading={isLoading}>
              Entrar
            </Button>
          </Stack>
        </Flex>
      </Flex>
    </>
  );
};
