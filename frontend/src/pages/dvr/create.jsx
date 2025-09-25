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
import { useEffect, useState } from "react";
import { toaster } from "../../components/ui/toaster";
import api from "../../services/api";
import * as yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { InputGroup } from "../../components/ui/input-group";
import { Field } from "../../components/ui/field";
import { Button } from "../../components/ui/button";
import { HeadingTitle } from "../../components/ui/heading";
import { FormSelect } from "../../components/ui/select";
import { SkeletonTable } from "../../components/ui/skeleton";
import { MdDvr } from "react-icons/md";

const CreateDvrFormSchema = yup.object().shape({
  nome: yup.string().required("Nome obrigatório"),
  idStatus: yup.number().required("Status obrigatório").positive().integer(),
});

export const CreateDvr = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingBtn, setIsLoadingBtn] = useState(false);
  const [status, setStatus] = useState([]);
  const navigate = useNavigate();

  const { register, handleSubmit, formState, control } = useForm({
    resolver: yupResolver(CreateDvrFormSchema),
  });

  const handleCreateDvr = async (data) => {
    const newDvr = {
      nome: data.nome.trim(),
      idStatus: data.idStatus,
    };
    setIsLoadingBtn(true);
    try {
      const request = await api.post("/api/dvr", newDvr);

      if (request.status == 201) {
        toaster.create({
          title: "Dvr criado com sucesso!",
          type: "success",
          position: "top-right",
          duration: 3000,
          isClosable: true,
        });
        setTimeout(() => {
          navigate(`/dvr`);
        }, 1000);
      }
    } catch (error) {
      setIsLoadingBtn(false);

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

      toaster.create({
        title: error.response.data.error || error.response.data.errorMessage,
        type: "error",
        position: "top-right",
        duration: 3000,
        isClosable: true,
      });
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
  }, []);

  return (
    <>
      {isLoading ? (
        <Flex mb="8" justify="space-between" align="center" direction="column">
          <SkeletonTable rows={1} columns={1} />
          <VStack gap={20} spacing="8" w={"100%"} alignItems={"stretch"}>
            <SimpleGrid></SimpleGrid>
            <SimpleGrid>
              <SkeletonTable rows={2} columns={2} />
            </SimpleGrid>
          </VStack>
        </Flex>
      ) : (
        <Box
          as="form"
          flex="1"
          borderRadius={8}
          p={["6", "8"]}
          onSubmit={handleSubmit(handleCreateDvr)}
        >
          <Flex
            mb="8"
            justify="space-between"
            align="center"
            direction="column"
          >
            <HeadingTitle name={"Criar Dvr"} />
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
                  placeholder="Selecione o status"
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
                <Button
                  _dark={{
                    bg: "blue.700",
                    color: "white",
                    _hover: { bg: "blue.600" },
                  }}
                  type="submit"
                  loading={isLoadingBtn}
                >
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
