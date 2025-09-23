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
import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
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
import { Alert } from "../../components/ui/alert";
import { SkeletonTable } from "../../components/ui/skeleton";
import { HeadingTitle } from "../../components/ui/heading";

const UpdateStatusFormSchema = yup.object().shape({
  nome: yup.string().required("Nome obrigatório"),
  salario: yup.string().required("Salário obrigatório"),
  descontoVt: yup
    .boolean()
    .required("A aceitação do desconto de VT é obrigatória."),
});

export const UpdateStatus = () => {
  const [status, setStatus] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isLoadingBtn, setIsLoadingBtn] = useState(false);
  const [erro, setErro] = useState(false);

  const navigate = useNavigate();
  const { id } = useParams();

  const { register, handleSubmit, formState, control, reset } = useForm({
    resolver: yupResolver(UpdateStatusFormSchema),
  });

  const handleUpdateStatus = async (data) => {
    const newStatus = {
      id: id,
      nome: data.nome.trim(),
      salario: formatNumber(data.salario).replace("R$", "").trim(),
      descontoVt: data.descontoVt,
    };
    setIsLoadingBtn(true);
    try {
      const request = await api.put("/api/status", newStatus);
      if (request.status === 200) {
        toaster.create({
          title: "Status atualizado com sucesso!",
          type: "success",
        });
        setTimeout(() => {
          setIsLoadingBtn(false);
          navigate(`/status`);
        }, 800);
      }
    } catch (error) {
      const errorTitle =
        error.message === "Network Error"
          ? "Serviço indisponível"
          : error.response?.data?.error ||
            error.response?.data?.errorMessage ||
            "Ocorreu um erro";
      toaster.create({ title: errorTitle, type: "error" });
    }
  };

  useEffect(() => {
    const getStatusById = async () => {
      if (!id) return;
      try {
        const request = await api.get(`/api/status/${id}`);
        if (request.data) {
          setErro(false);
          setStatus(request.data);

          const formData = {
            ...request.data,
            descontoVt: request.data.descontoVt === true,
          };

          reset(formData); // Usando reset para popular o formulário
        } else {
          setErro(true);
        }
      } catch (error) {
        console.log(error);
        setErro(true);
      } finally {
        setTimeout(() => {
          setIsLoading(false);
        }, 800);
      }
    };
    getStatusById();
  }, [id, reset]);

  return (
    <>
      {isLoading ? (
        <Box
          as="form"
          flex="1"
          borderRadius={8}
          p={["6", "8"]}
          onSubmit={handleSubmit(handleUpdateStatus)}
        >
          <Flex
            mb="8"
            justify="space-between"
            align="center"
            direction="column"
          >
            <SkeletonTable rows={1} columns={1} />
            <VStack gap={20} spacing="8" w={"100%"} alignItems={"stretch"}>
              <SimpleGrid></SimpleGrid>
              <SimpleGrid>
                <SkeletonTable rows={3} columns={2} />
              </SimpleGrid>
            </VStack>
          </Flex>
        </Box>
      ) : erro ? (
        <Alert
          status="error"
          title="Falha ao obter dados do status"
          children={"Tente novamente mais tarde"}
        />
      ) : (
        <Box
          as="form"
          flex="1"
          borderRadius={8}
          p={["6", "8"]}
          onSubmit={handleSubmit(handleUpdateStatus)}
        >
          <Flex
            mb="8"
            justify="space-between"
            align="center"
            direction="column"
          >
            <HeadingTitle name={`Editar Status: ${status?.nome}`} />

            <VStack gap={10} spacing="8" w={"100%"} alignItems={"stretch"}>
              <Separator my="6" />
              <SimpleGrid minChildWidth="240px" columns={3} gap={10}>
                <Field label={"Nome"} errorText={formState.errors.nome}>
                  <InputGroup startElement={<FaUserAlt color="gray.300" />}>
                    <Input
                      size={"md"}
                      placeholder="Nome"
                      {...register("nome")}
                    />
                  </InputGroup>
                </Field>

                <Controller
                  name="salario"
                  control={control}
                  render={({ field, fieldState }) => (
                    <CurrencyInput
                      label={"Salário"}
                      error={fieldState.error}
                      {...field}
                    />
                  )}
                />
              </SimpleGrid>
              <SimpleGrid minChildWidth="240px" columns={1} gap={10}>
                <Controller
                  name="descontoVt"
                  control={control}
                  render={({ field }) => {
                    return (
                      <Field
                        label={"Descontar Vale Transporte?"}
                        errorText={formState.errors.descontoVt?.message}
                      >
                        <Switch
                          isChecked={field.value}
                          onChange={field.onChange}
                          ref={field.ref}
                        />
                      </Field>
                    );
                  }}
                />
              </SimpleGrid>
            </VStack>

            <Flex w={"100%"} mt="8" justify="flex-end">
              <HStack spacing="4">
                <Button type="button" onClick={() => navigate(-1)} color="gray">
                  Voltar
                </Button>
                <Button type="submit" loading={isLoadingBtn}>
                  Salvar
                </Button>
              </HStack>
            </Flex>
          </Flex>
        </Box>
      )}
    </>
  );
};
