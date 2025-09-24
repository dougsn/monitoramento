import {
  Box,
  Flex,
  HStack,
  Input,
  Separator,
  SimpleGrid,
  VStack,
} from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { toaster } from "../../components/ui/toaster";
import api from "../../services/api";
import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { InputGroup } from "../../components/ui/input-group";
import { FaUserAlt } from "react-icons/fa";
import { Field } from "../../components/ui/field";
import { Button } from "../../components/ui/button";
import { HeadingTitle } from "../../components/ui/heading";
import { IoMdColorPalette } from "react-icons/io";

const CreateStatusFormSchema = yup.object().shape({
  nome: yup.string().required("Nome obrigatório"),
  cor: yup.string().required("Cor obrigatória"),
});

export const CreateStatus = () => {
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const { register, handleSubmit, formState } = useForm({
    resolver: yupResolver(CreateStatusFormSchema),
  });

  const handleCreateStatus = async (data) => {
    const newStatus = {
      nome: data.nome.trim(),
      cor: data.cor.trim(),
    };
    setIsLoading(true);
    try {
      const request = await api.post("/api/status", newStatus);

      if (request.status == 201) {
        toaster.create({
          title: "Status criado com sucesso!",
          type: "success",
          position: "top-right",
          duration: 3000,
          isClosable: true,
        });
        setTimeout(() => {
          navigate(`/status`);
        }, 1000);
      }
    } catch (error) {
      setIsLoading(false);

      if (error.message == "Network Error") {
        toaster.create({
          title: "Serviço indisponível no momento, tento novamente mais tarde",
          type: "error",
          position: "top-right",
          duration: 3000,
          isClosable: true,
        });
        return false;
      }

      if (error.response.data.status == 400) {
        toaster.create({
          title: "Revise os dados inseridos",
          type: "info",
          position: "top-right",
          duration: 3000,
          isClosable: true,
        });
        return false;
      }

      toaster.create({
        title: error.response.data.error || error.response.data.errorMessage,
        type: "error",
        position: "top-right",
        duration: 3000,
        isClosable: true,
      });
    }
  };

  return (
    <Box
      as="form"
      flex="1"
      borderRadius={8}
      p={["6", "8"]}
      onSubmit={handleSubmit(handleCreateStatus)}
    >
      <Flex mb="8" justify="space-between" align="center" direction="column">
        <HeadingTitle name={"Criar Status"} />
        <VStack gap={10} spacing="8" w={"100%"} alignItems={"stretch"}>
          <Separator my="6" />
          <SimpleGrid minChildWidth="240px" columns={3} gap={10}>
            <Field label={"Nome"} errorText={formState.errors.nome}>
              <InputGroup startElement={<FaUserAlt color="gray.300" />}>
                <Input size={"md"} placeholder="Nome" {...register("nome")} />
              </InputGroup>
            </Field>
            <Field label={"Cor"} errorText={formState.errors.cor}>
              <InputGroup startElement={<IoMdColorPalette color="gray.300" />}>
                <Input size={"md"} placeholder="Cor" {...register("cor")} />
              </InputGroup>
            </Field>
          </SimpleGrid>
        </VStack>

        <Flex w={"100%"} mt="8" justify="flex-end">
          <HStack spacing="4">
            <Button
              _dark={{
                bg: "gray.900",
                color: "white",
                _hover: { bg: "gray.600" },
              }}
              type="button"
              onClick={() => navigate(-1)}
              colorPalette="gray"
            >
              Voltar
            </Button>
            <Button
              _dark={{
                bg: "blue.700",
                color: "white",
                _hover: { bg: "blue.600" },
              }}
              type="submit"
              loading={isLoading}
            >
              Salvar
            </Button>
          </HStack>
        </Flex>
      </Flex>
    </Box>
  );
};
