import {
  Box,
  Button,
  ButtonGroup,
  Flex,
  Heading,
  Icon,
  IconButton,
  Pagination,
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
import { LuChevronLeft, LuChevronRight } from "react-icons/lu";
import { AppPagination } from "../../components/ui/pagination";

export const Status = () => {
  const [status, setStatus] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [erro, setErro] = useState(false);
  const [isEmpty, setIsEmpty] = useState(false);

  const [pageInfo, setPageInfo] = useState(null);
  const [currentPage, setCurrentPage] = useState(0);

  const navigate = useNavigate();
  const [isLargerThan768] = useMediaQuery("(max-width: 768px)");

  const getStatus = async () => {
    setIsLoading(true);
    try {
      const request = await api.get(`/api/status`);
      const responseData = request.data;

      setStatus(responseData._embedded.allStatusList);
      setPageInfo(responseData.page);

      if (responseData._embedded.allStatusList.length === 0) {
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
    setStatus((prevStatus) => prevStatus.filter((c) => c.id !== deletedId));
  };

  useEffect(() => {
    getStatus(currentPage);
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
          <HeadingTitle name={"Status"} />
          <Button
            _dark={{
              bg: "blue.700",
              color: "white",
              _hover: { bg: "blue.600" },
            }}
            size="xs"
            fontSize="sm"
            onClick={() => navigate("/status/novo/")}
            colorPalette={"blue"}
          >
            <Icon as={RiAddLine} />
          </Button>
        </Flex>
      ) : (
        <Flex mb="8" justify="space-between" align="center">
          <HeadingTitle name={"Status"} />
          <Button
            _dark={{
              bg: "blue.700",
              color: "white",
              _hover: { bg: "blue.600" },
            }}
            colorPalette={"blue"}
            onClick={() => navigate("/status/novo/")}
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
          title="Falha ao obter dados dos status"
          children={"Tente novamente mais tarde"}
        />
      ) : isEmpty ? (
        <Alert
          status="info"
          title="Não há dados"
          children={"Cadastre um novo status"}
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
                  <Table.ColumnHeader>Cor</Table.ColumnHeader>
                  <Table.ColumnHeader textAlign="end">Ações</Table.ColumnHeader>
                </Table.Row>
              </Table.Header>
              <Table.Body>
                {status.map((item) => (
                  <Table.Row fontWeight={"semibold"} key={item.id}>
                    <Table.Cell data-label="Nome:">{item.nome}</Table.Cell>
                    <Table.Cell data-label="Desconto:">{item.cor}</Table.Cell>
                    <ActionButtonsCell
                      id={item.id}
                      basePath="/status"
                      onDeleteSuccess={handleDeletionSuccess}
                      endpoint={"status"}
                      title={"Status"}
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
