"use client";

import {
  Button,
  ButtonGroup,
  Flex,
  IconButton,
  Pagination,
} from "@chakra-ui/react";
import { LuChevronLeft, LuChevronRight } from "react-icons/lu";

export function AppPagination({
  count,
  pageSize,
  page,
  onPageChange,
  showCounter = true,
  counterFormat = "long",
}) {
  if (count <= pageSize) {
    return null;
  }

  // Objeto de tradução (sem tipagem no parâmetro 'details')
  const translations = {
    pageRangeText: (details) =>
      `${details.start} - ${details.end} de ${details.total}`,
  };

  return (
    <Pagination.Root
      count={count}
      pageSize={pageSize}
      page={page}
      onPageChange={onPageChange}
      width="full"
      translations={translations}
    >
      <Flex justify="space-between" align="center">
        {showCounter && <Pagination.PageText format={counterFormat} />}

        <ButtonGroup variant="outline" size="sm">
          <Pagination.PrevTrigger asChild>
            <IconButton aria-label="Página anterior">
              <LuChevronLeft />
            </IconButton>
          </Pagination.PrevTrigger>

          <Pagination.Items
            render={(pageItem) => (
              <Pagination.Item
                page={pageItem.value}
                asChild
                _selected={{
                  bg: "blue.500",
                  borderColor: "blue.500",
                  color: "white",
                  _dark: { bg: "blue.700", borderColor: "blue.700" },
                }}
              >
                <Button variant={{ base: "ghost", _selected: "outline" }}>
                  {pageItem.value}
                </Button>
              </Pagination.Item>
            )}
          />

          <Pagination.NextTrigger asChild>
            <IconButton aria-label="Próxima página">
              <LuChevronRight />
            </IconButton>
          </Pagination.NextTrigger>
        </ButtonGroup>
      </Flex>
    </Pagination.Root>
  );
}
