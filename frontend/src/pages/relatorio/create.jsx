import {
  Box,
  Flex,
  Heading,
  HStack,
  Input,
  Separator,
  SimpleGrid,
  Textarea,
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
import { formatDate } from "../../utils/formatDate";

const CreateRelatorioFormSchema = yup.object().shape({
  dia: yup.string().required("O dia é obrigatório"),
  descricao: yup.string().notRequired(),
  dvrs: yup.array().of(
    yup.object().shape({
      dvrId: yup.number().required("Dvr obrigatório").positive().integer(),
      statusIdDvr: yup
        .number()
        .required("Status do dvr obrigatório")
        .positive()
        .integer(),
      cameras: yup.array().of(
        yup.object().shape({
          cameraId: yup
            .number()
            .required("Câmera obrigatória")
            .positive()
            .integer(),
          statusId: yup
            .number()
            .required("Status da câmera obrigatória")
            .positive()
            .integer(),
        })
      ),
    })
  ),
});

export const CreateRelatorio = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingBtn, setIsLoadingBtn] = useState(false);
  const [status, setStatus] = useState([]);
  const [dvr, setDvr] = useState([]);
  const navigate = useNavigate();

  const { register, handleSubmit, formState, control } = useForm({
    resolver: yupResolver(CreateRelatorioFormSchema),
  });
  const handleCreateRelatorio = async (data) => {
    setIsLoadingBtn(true);
    const newRelatorio = {
      descricao: data.descricao.trim(),
      dia: formatDate(data.dia),
      dvrs: data.dvrs.map((dMap) => ({
        dvrId: dMap.dvrId,
        statusIdDvr: dMap.statusIdDvr,
        cameras: dMap.cameras?.map((cMap) => ({
          cameraId: cMap.cameraId,
          statusId: cMap.statusId,
        })),
      })),
    };
    try {
      const request = await api.post(
        "/api/relatorio/create-relatorio",
        newRelatorio
      );

      if (request.status == 200) {
        toaster.create({
          title: "Relatório criado com sucesso!",
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

  const getDvr = async () => {
    setIsLoading(true);
    try {
      const request = await api.get(`/api/dvr/find-all`);
      setDvr(request.data);
      console.log(request.data);

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
    getDvr();
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
          onSubmit={handleSubmit(handleCreateRelatorio)}
        >
          <Flex
            mb="8"
            justify="space-between"
            align="center"
            direction="column"
          >
            <HeadingTitle name={"Preenchimento do Relatório Diário"} />
            <VStack gap={10} spacing="8" w={"100%"} alignItems={"stretch"}>
              <Separator my="6" />
              <SimpleGrid minChildWidth="240px" columns={3} gap={10}>
                <Field
                  label={"1. Selecione a data de preenchimento do relatório:"}
                  errorText={formState.errors.dia}
                >
                  <InputGroup label={"dia"} errorText={formState.errors.dia}>
                    <Input type="date" {...register("dia")} />
                  </InputGroup>
                </Field>
              </SimpleGrid>
              <Heading
                size={{ base: "sm", md: "sm" }}
                fontWeight="500"
                textAlign={"start"}
                w="100%"
              >
                2. Faça o preenchimento do status de cada câmera:
              </Heading>
              <Flex flex={"wrap"} flexDir={"column"} gap={10}>
                {dvr.map((d, i) => (
                  <Box key={i}>
                    <Heading
                      size={{ base: "2xl", md: "3xl" }}
                      fontWeight="500"
                      textAlign={"start"}
                      w="100%"
                    >
                      DVR: {d.label}
                    </Heading>
                    <Separator my="6" />
                    <SimpleGrid minChildWidth="240px" columns={3} gap={10}>
                      {d.cameras.map((c, index) => (
                        <>
                          <FormSelect
                            key={c.id}
                            {...register(
                              `dvrs.[${i}].cameras.[${index}].statusId`
                            )}
                            name={`dvrs.[${i}].cameras.[${index}].statusId`}
                            label={`${c.nome}`}
                            items={status}
                            placeholder="Selecione o status"
                            control={control}
                            formState={
                              formState.errors.dvrs?.[i]?.cameras?.[index]
                                ?.statusId
                            }
                          />
                          <Input
                            type="hidden"
                            value={c.id}
                            {...register(
                              `dvrs[${i}].cameras.[${index}].cameraId`
                            )}
                          />
                        </>
                      ))}
                    </SimpleGrid>
                  </Box>
                ))}
              </Flex>
              <Heading
                size={{ base: "sm", md: "sm" }}
                fontWeight="500"
                textAlign={"start"}
                w="100%"
              >
                3. Faça o preenchimento do status de cada DVR:
              </Heading>
              <SimpleGrid minChildWidth="240px" columns={3} gap={10}>
                {dvr.map((d, idx) => (
                  <>
                    <FormSelect
                      label={`${d.label}`}
                      key={d.value}
                      {...register(`dvrs.[${idx}].statusIdDvr`)}
                      name={`dvrs.[${idx}].statusIdDvr`}
                      items={status}
                      placeholder="Selecione o status"
                      control={control}
                      formState={formState.errors.dvrs?.[idx]?.statusIdDvr}
                    />
                    <Input
                      type="hidden"
                      value={String(d.value)}
                      {...register(`dvrs.[${idx}].dvrId`)}
                    />
                  </>
                ))}
              </SimpleGrid>

              <Heading
                size={{ base: "sm", md: "sm" }}
                fontWeight="500"
                textAlign={"start"}
                w="100%"
              >
                4. Caso tenha alguma observação relacionada ao relatório ou
                sobre alguma câmera, informe no campo abaixo:
              </Heading>
              <SimpleGrid minChildWidth="240px" columns={3} gap={10}>
                <Field formState={formState.errors.descricao}>
                  <Textarea
                    {...register("descricao")}
                    placeholder="Descrição do relatório"
                  />
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
