/* eslint-disable react-hooks/exhaustive-deps */
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
import { useForm } from "react-hook-form";
import { InputGroup } from "../../components/ui/input-group";
import { Field } from "../../components/ui/field";
import { Button } from "../../components/ui/button";
import { Alert } from "../../components/ui/alert";
import { SkeletonTable } from "../../components/ui/skeleton";
import { HeadingTitle } from "../../components/ui/heading";
import { FormSelect } from "../../components/ui/select";
import { MdDvr } from "react-icons/md";

const UpdateDvrFormSchema = yup.object().shape({
  nome: yup.string().required("Nome obrigatório"),
  idStatus: yup.number().required("Status obrigatório"),
});

export const UpdateDvr = () => {
  const [dvr, setDvr] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const [isLoadingBtn, setIsLoadingBtn] = useState(false);
  const [status, setStatus] = useState([]);
  const [erro, setErro] = useState(false);

  const navigate = useNavigate();
  const { id } = useParams();

  const { register, handleSubmit, formState, control, setValue } = useForm({
    resolver: yupResolver(UpdateDvrFormSchema),
  });

  const handleUpdateDvr = async (data) => {
    const newDvr = {
      id: id,
      nome: data.nome.trim(),
      idStatus: data.idStatus,
    };
    setIsLoadingBtn(true);
    try {
      const request = await api.put("/api/dvr", newDvr);
      if (request.status === 200) {
        toaster.create({
          title: "Dvr atualizado com sucesso!",
          type: "success",
        });
        setTimeout(() => {
          setIsLoadingBtn(false);
          navigate(`/dvr`);
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

  const getDvrById = async () => {
    if (!id) return;
    try {
      const request = await api.get(`/api/dvr/${id}`);
      if (request.data) {
        setErro(false);
        setDvr(request.data);
        setValue("idStatus", String(request.data.statusId));
        setValue("nome", request.data.nome);
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

  const getStatus = async () => {
    setIsLoading(true);
    try {
      const request = await api.get(`/api/status/find-all`);
      setStatus(request.data);

      setTimeout(() => {
        setIsLoading(false);
      }, 800);
    } catch (error) {
      setIsLoading(false);

      toaster.create({
        title: error.response.data.error,
        type: "error",
        closable: true,
        duration: 2000,
      });
      return null;
    }
  };

  useEffect(() => {
    getStatus();
    getDvrById();
  }, [id]);

  return (
    <>
      {isLoading ? (
        <Box
          as="form"
          flex="1"
          borderRadius={8}
          p={["6", "8"]}
          onSubmit={handleSubmit(handleUpdateDvr)}
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
          dvr="error"
          title="Falha ao obter dados do dvr"
          children={"Tente novamente mais tarde"}
        />
      ) : (
        <Box
          as="form"
          flex="1"
          borderRadius={8}
          p={["6", "8"]}
          onSubmit={handleSubmit(handleUpdateDvr)}
        >
          <Flex
            mb="8"
            justify="space-between"
            align="center"
            direction="column"
          >
            <HeadingTitle name={`Editar Dvr: ${dvr?.nome}`} />

            <VStack gap={10} spacing="8" w={"100%"} alignItems={"stretch"}>
              <Separator my="6" />
              <SimpleGrid minChildWidth="240px" columns={3} gap={10}>
                <Field label={"Nome"} errorText={formState.errors.nome}>
                  <InputGroup startElement={<MdDvr color="gray.300" />}>
                    <Input
                      size={"md"}
                      placeholder="Nome"
                      {...register("nome")}
                    />
                  </InputGroup>
                </Field>
                <FormSelect
                  {...register("idStatus")}
                  name="idStatus"
                  label="Status"
                  items={status}
                  placeholder={dvr.nomeStatus}
                  control={control}
                  formState={formState.errors.idStatus}
                />
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
