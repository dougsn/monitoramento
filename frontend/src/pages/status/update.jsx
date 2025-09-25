import {
  Box,
  Flex,
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
import {  useForm } from "react-hook-form";
import { InputGroup } from "../../components/ui/input-group";
import { Field } from "../../components/ui/field";
import { Button } from "../../components/ui/button";
import { Alert } from "../../components/ui/alert";
import { SkeletonTable } from "../../components/ui/skeleton";
import { HeadingTitle } from "../../components/ui/heading";
import { IoMdColorPalette } from "react-icons/io";
import { HiStatusOnline } from "react-icons/hi";

const UpdateStatusFormSchema = yup.object().shape({
  nome: yup.string().required("Nome obrigatório"),
  cor: yup.string().required("Cor obrigatória"),
});

export const UpdateStatus = () => {
  const [status, setStatus] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isLoadingBtn, setIsLoadingBtn] = useState(false);
  const [erro, setErro] = useState(false);

  const navigate = useNavigate();
  const { id } = useParams();

  const { register, handleSubmit, formState, reset } = useForm({
    resolver: yupResolver(UpdateStatusFormSchema),
  });

  const handleUpdateStatus = async (data) => {
    const newStatus = {
      id: id,
      nome: data.nome.trim(),
      cor: data.cor.trim(),
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
      setIsLoadingBtn(false);
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
                <SkeletonTable rows={2} columns={2} />
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
                  <InputGroup startElement={<HiStatusOnline color="gray.300" />}>
                    <Input
                      size={"md"}
                      placeholder="Nome"
                      {...register("nome")}
                    />
                  </InputGroup>
                </Field>
                <Field label={"Cor"} errorText={formState.errors.cor}>
                  <InputGroup
                    startElement={<IoMdColorPalette color="gray.300" />}
                  >
                    <Input size={"md"} placeholder="Cor" {...register("cor")} />
                  </InputGroup>
                </Field>
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
