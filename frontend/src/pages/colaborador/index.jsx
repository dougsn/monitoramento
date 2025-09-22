import {
  Box,
  Button,
  Flex,
  Heading,
  Icon,
  Table,
  useMediaQuery,
} from "@chakra-ui/react";
import { useNavigate } from "react-router-dom";
import { ActionButtonsCell } from "../../components/Buttons/ActionButtonsCell";
import { useEffect, useState } from "react";
import { toaster } from "../../components/ui/toaster";
import api from "../../services/api";
import { Alert } from "../../components/ui/alert";
import { SkeletonTable } from "../../components/ui/skeleton";
import { formatNumberFrag } from "../../utils/formatNumberFrag";
import { RiAddLine } from "react-icons/ri";
import { HeadingTitle } from "../../components/ui/heading";

export const Colaborador = () => {
  const [colaborador, setColaborador] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [erro, setErro] = useState(false);
  const [isEmpty, setIsEmpty] = useState(false);

  const navigate = useNavigate();
  const [isLargerThan768] = useMediaQuery("(max-width: 768px)");

  const getColaborador = async () => {
    try {
      const request = await api.get("/api/colaborador");
      setColaborador(request.data);
      if (request.data.length === 0) {
        setIsEmpty(true);
      }
      setTimeout(() => {
        setIsLoading(false);
      }, 800);
    } catch (error) {
      setIsLoading(false);
      setErro(true);
      toaster.create({
        title: error.response.data.error,
        type: "error",
        closable: true,
        duration: 2000,
      });
      return null;
    }
  };

  const handleDeletionSuccess = (deletedId) => {
    setColaborador((prevColaboradores) =>
      prevColaboradores.filter((c) => c.id !== deletedId)
    );
  };

  useEffect(() => {
    getColaborador();
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  }, []);

  return (
    <Box display={"flex"} flexDirection={"column"} p="8" w={"100%"}>
      {isLargerThan768 ? (
        <Flex mb="8" justify="space-around" align="center">
          <HeadingTitle name={"Colaboradores"} />
          <Button
            size="xs"
            fontSize="sm"
            onClick={() => navigate("/colaborador/novo/")}
            colorPalette={"blue"}
          >
            <Icon as={RiAddLine} />
          </Button>
        </Flex>
      ) : (
        <Flex mb="8" justify="space-between" align="center">
          <HeadingTitle name={"Colaboradores"} />
          <Button
            colorPalette={"blue"}
            onClick={() => navigate("/colaborador/novo/")}
          >
            Criar novo
          </Button>
        </Flex>
      )}

      {isLoading ? (
        <SkeletonTable rows={6} columns={3} />
      ) : erro ? (
        <Alert
          status="error"
          title="Falha ao obter dados dos colaboradores"
          children={"Tente novamente mais tarde"}
        />
      ) : isEmpty ? (
        <Alert
          status="info"
          title="Não há dados"
          children={"Cadastre um novo colaborador"}
        />
      ) : (
        <>
          <Table.ScrollArea>
            <Table.Root
              fontSize={{ base: "sm", md: "md" }}
              size={{ base: "sm", md: "lg" }}
              interactive
              className="responsive-table"
            >
              <Table.Header>
                <Table.Row>
                  <Table.ColumnHeader>Nome</Table.ColumnHeader>
                  <Table.ColumnHeader>Salário</Table.ColumnHeader>
                  <Table.ColumnHeader>Desconto</Table.ColumnHeader>
                  <Table.ColumnHeader textAlign="end">Ações</Table.ColumnHeader>
                </Table.Row>
              </Table.Header>
              <Table.Body>
                {colaborador.map((item) => (
                  <Table.Row fontWeight={"semibold"} key={item.id}>
                    <Table.Cell data-label="Nome:">{item.nome}</Table.Cell>
                    <Table.Cell data-label="Salário:">
                      {formatNumberFrag(item.salario)}
                    </Table.Cell>
                    <Table.Cell data-label="Desconto:">
                      {item.descontoVt ? "Sim" : "Não"}
                    </Table.Cell>
                    <ActionButtonsCell
                      id={item.id}
                      basePath="/colaborador"
                      onDeleteSuccess={handleDeletionSuccess}
                      endpoint={"colaborador"}
                      title={"Colaborador"}
                      name={item.nome}
                    />
                  </Table.Row>
                ))}
              </Table.Body>
            </Table.Root>
          </Table.ScrollArea>
        </>
      )}
    </Box>
  );
};
