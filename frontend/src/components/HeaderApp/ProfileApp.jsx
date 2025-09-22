import { Flex, Box, Stack, Separator, Portal } from "@chakra-ui/react";
import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthenticationContext } from "../../provider/AuthenticationProvider";
import { deleteToken } from "../../utils/localstorage";
import { ColorModeButton } from "../ui/color-mode";
import {
  MenuContent,
  MenuItem,
  MenuItemGroup,
  MenuRoot,
  MenuSeparator,
  MenuTrigger,
} from "../ui/menu";
import { Avatar } from "../ui/avatar";
import { toaster } from "../../components/ui/toaster";

export const ProfileApp = () => {
  const { setIsAuthenticated, setUserData, userData } = useContext(
    AuthenticationContext
  );

  const navigate = useNavigate();

  const logoutUser = () => {
    toaster.create({
      title: "Até a próxima!",
      type: "success",
    });
    setIsAuthenticated(false);
    setUserData({});
    deleteToken();

    navigate("/");
  };

  const myAccount = () => {
    navigate("/minha-conta");
  };

  return (
    <Flex align="center">
      <Box mr="4" textAlign="right">
        <Box display={"flex"} gap={5}>
          <Stack direction="row">
            <ColorModeButton />
            <Separator
              orientation="vertical"
              borderColor="gray.300"
              m="0 10px"
            />
            <Flex alignItems={"center"}>
              <MenuRoot>
                <MenuTrigger>
                  <Avatar name={userData ? userData.username : ""} />
                </MenuTrigger>
                <Portal>
                  <MenuContent>
                    <MenuItemGroup>
                      <MenuItem
                        onClick={myAccount}
                        cursor={"pointer"}
                        value="bold"
                      >
                        Minha Conta
                      </MenuItem>
                    </MenuItemGroup>
                    <MenuSeparator />
                    <MenuItemGroup>
                      <MenuItem
                        onClick={logoutUser}
                        cursor={"pointer"}
                        value="left"
                      >
                        Sair
                      </MenuItem>
                    </MenuItemGroup>
                  </MenuContent>
                </Portal>
              </MenuRoot>
            </Flex>
          </Stack>
        </Box>
      </Box>
    </Flex>
  );
};
