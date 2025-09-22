import {
  Box,
  Flex,
  Heading,
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
import { Controller, useForm } from "react-hook-form";
import { formatNumber } from "../../utils/formatNumber";
import { InputGroup } from "../../components/ui/input-group";
import { FaUserAlt } from "react-icons/fa";
import { Field } from "../../components/ui/field";
import { Switch } from "../../components/ui/switch";
import { CurrencyInput } from "../../components/ui/input";
import { Button } from "../../components/ui/button";
import { HeadingTitle } from "../../components/ui/heading";

const CreateColaboradorFormSchema = yup.object().shape({
  nome: yup.string().required("Nome obrigatório"),
  salario: yup.string().required("Salário obrigatório"),
  descontoVt: yup
    .boolean()
    .required("A aceitação do desconto de VT é obrigatória."),
});

export const CreateColaborador = () => {
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const { register, handleSubmit, formState, control } = useForm({
    resolver: yupResolver(CreateColaboradorFormSchema),
    defaultValues: {
      descontoVt: false, // É uma boa prática definir um valor padrão
    },
  });

  const handleCreateColaborador = async (data) => {
    const newColaborador = {
      nome: data.nome.trim(),
      salario: formatNumber(data.salario).replace("R$", "").trim(),
      descontoVt: data.descontoVt,
    };
    setIsLoading(true);
    try {
      const request = await api.post("/api/colaborador", newColaborador);

      if (request.status == 201) {
        toaster.create({
          title: "Colaborador criado com sucesso!",
          type: "success",
          position: "top-right",
          duration: 3000,
          isClosable: true,
        });
        setTimeout(() => {
          navigate(`/colaborador`);
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
      onSubmit={handleSubmit(handleCreateColaborador)}
    >
      <Flex mb="8" justify="space-between" align="center" direction="column">
        <HeadingTitle name={"Criar Colaborador"} />
        <VStack gap={10} spacing="8" w={"100%"} alignItems={"stretch"}>
          <Separator my="6" />
          <SimpleGrid minChildWidth="240px" columns={3} gap={10}>
            <Field label={"Nome"} errorText={formState.errors.nome}>
              <InputGroup startElement={<FaUserAlt color="gray.300" />}>
                <Input size={"md"} placeholder="Nome" {...register("nome")} />
              </InputGroup>
            </Field>

            <CurrencyInput
              label={"Salário"}
              {...register("salario")}
              error={formState.errors.salario}
            />
          </SimpleGrid>
          <SimpleGrid minChildWidth="240px" columns={1} gap={10}>
            <Controller
              name="descontoVt"
              control={control}
              render={({ field: { onChange, value, ref } }) => (
                <Field
                  label={"Descontar Vale Transporte?"}
                  errorText={formState.errors.descontoVt}
                >
                  <Switch isChecked={value} onChange={onChange} ref={ref} />
                </Field>
              )}
            />
          </SimpleGrid>
        </VStack>

        <Flex w={"100%"} mt="8" justify="flex-end">
          <HStack spacing="4">
            <Button type="button" onClick={() => navigate(-1)} color="gray">
              Voltar
            </Button>
            <Button type="submit" loading={isLoading}>
              Salvar
            </Button>
          </HStack>
        </Flex>
      </Flex>
    </Box>
  );
};
