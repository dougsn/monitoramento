/* eslint-disable react-hooks/exhaustive-deps */
import {
  Box,
  Button,
  Flex,
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
import { RiAddLine } from "react-icons/ri";
import { HeadingTitle } from "../../components/ui/heading";
import { AppPagination } from "../../components/ui/pagination";

export const Dvr = () => {
  const [dvr, setDvr] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [erro, setErro] = useState(false);
  const [isEmpty, setIsEmpty] = useState(false);

  const [pageInfo, setPageInfo] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);

  const navigate = useNavigate();
  const [isLargerThan768] = useMediaQuery("(max-width: 768px)");

  const getDvr = async () => {
    setIsLoading(true);
    try {
      const request = await api.get(`/api/dvr?page=${currentPage}`);
      const responseData = request.data;

      setDvr(responseData._embedded.allDvrList);
      setPageInfo(responseData.page);

      if (responseData._embedded.allDvrList.length === 0) {
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
    setDvr((prevDvr) => prevDvr.filter((c) => c.id !== deletedId));
    getDvr(currentPage);
  };

  useEffect(() => {
    getDvr(currentPage);
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  }, [currentPage]);

  const handlePageChange = (details) => {
    setCurrentPage(details.page - 1);
  };

  return (
    <Box display={"flex"} flexDirection={"column"} p="8" w={"100%"}>
      {isLargerThan768 ? (
        <Flex mb="8" justify="space-around" align="center">
          <HeadingTitle name={"Dvr"} />
          <Button
            _dark={{
              bg: "blue.700",
              color: "white",
              _hover: { bg: "blue.600" },
            }}
            size="xs"
            fontSize="sm"
            onClick={() => navigate("/dvr/novo/")}
            colorPalette={"blue"}
          >
            <Icon as={RiAddLine} />
          </Button>
        </Flex>
      ) : (
        <Flex mb="8" justify="space-between" align="center">
          <HeadingTitle name={"Dvr"} />
          <Button
            _dark={{
              bg: "blue.700",
              color: "white",
              _hover: { bg: "blue.600" },
            }}
            colorPalette={"blue"}
            onClick={() => navigate("/dvr/novo/")}
          >
            Criar novo
          </Button>
        </Flex>
      )}

      {isLoading ? (
        <SkeletonTable rows={6} columns={3} />
      ) : erro ? (
        <Alert
          dvr="error"
          title="Falha ao obter dados dos dvr"
          children={"Tente novamente mais tarde"}
        />
      ) : isEmpty ? (
        <Alert
          dvr="info"
          title="Não há dados"
          children={"Cadastre um novo dvr"}
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
                  <Table.ColumnHeader>Status</Table.ColumnHeader>
                  <Table.ColumnHeader textAlign="end">Ações</Table.ColumnHeader>
                </Table.Row>
              </Table.Header>
              <Table.Body>
                {dvr.map((item) => (
                  <Table.Row fontWeight={"semibold"} key={item.id}>
                    <Table.Cell>{item.nome}</Table.Cell>
                    <Table.Cell>{item.nomeStatus}</Table.Cell>
                    <ActionButtonsCell
                      id={item.id}
                      basePath="/dvr"
                      onDeleteSuccess={handleDeletionSuccess}
                      endpoint={"dvr"}
                      title={"Dvr"}
                      name={item.nome}
                    />
                  </Table.Row>
                ))}
              </Table.Body>
            </Table.Root>
          </Table.ScrollArea>
          {pageInfo && pageInfo.totalPages > 1 && (
            <Flex mt="8" justify="end" align="end">
              <AppPagination
                count={pageInfo.totalElements}
                pageSize={pageInfo.size}
                page={currentPage + 1}
                onPageChange={handlePageChange}
              />
            </Flex>
          )}
        </>
      )}
    </Box>
  );
};
