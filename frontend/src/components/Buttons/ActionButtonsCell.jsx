import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Table, HStack, Button } from "@chakra-ui/react";
import { FaPencilAlt, FaTrash } from "react-icons/fa";
import api from "../../services/api";
import { toaster } from "../../components/ui/toaster";
import {
  DialogRoot,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogBody,
  DialogFooter,
} from "../../components/ui/dialog";

export const ActionButtonsCell = ({
  id,
  basePath,
  onDeleteSuccess,
  endpoint,
  title,
  name,
}) => {
  const navigate = useNavigate();

  const [isOpen, setIsOpen] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);

  const handleNavigate = (action) => {
    navigate(`${basePath}/${action}/${id}`);
  };

  const handleDelete = async () => {
    setIsDeleting(true);
    try {
      await api.delete(`/api/${endpoint}/${id}`);

      toaster.create({
        title: `${title} excluído(a) com sucesso!`,
        type: "success",
      });

      setIsOpen(false);

      if (onDeleteSuccess) {
        onDeleteSuccess(id);
      }
    } catch (error) {
      toaster.create({
        title: error.response.data.errorMessage,
        type: "error",
      });
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <>
      <Table.Cell data-label="Ações:">
        <HStack justifyContent={"flex-end"} spacing={4}>
          <Button
            size={{ base: "xs", md: "sm" }}
            colorPalette="yellow"
            variant="subtle"
            onClick={() => handleNavigate("atualizar")}
          >
            <FaPencilAlt />
          </Button>
          <Button
            variant="subtle"
            size={{ base: "xs", md: "sm" }}
            colorPalette="red"
            onClick={() => setIsOpen(true)}
          >
            <FaTrash />
          </Button>
        </HStack>
      </Table.Cell>

      <DialogRoot
        placement={"center"}
        open={isOpen}
        onOpenChange={(e) => setIsOpen(e.open)}
      >
        <DialogContent margin={5}>
          <DialogHeader>
            <DialogTitle>Confirmar Exclusão</DialogTitle>
          </DialogHeader>
          <DialogBody>
            Tem certeza que deseja excluir <b>{name}</b> ? Esta ação não pode
            ser desfeita.
          </DialogBody>
          <DialogFooter>
            <Button
              colorPalette={"blue"}
              _dark={{
                bg: "blue.700",
                color: "white",
                _hover: { bg: "blue.600" },
              }}
              onClick={() => setIsOpen(false)}
            >
              Cancelar
            </Button>
            <Button
              colorPalette="red"
              _dark={{
                bg: "red.700",
                color: "white",
                _hover: { bg: "red.600" },
              }}
              onClick={handleDelete}
              loading={isDeleting}
            >
              Excluir
            </Button>
          </DialogFooter>
        </DialogContent>
      </DialogRoot>
    </>
  );
};
