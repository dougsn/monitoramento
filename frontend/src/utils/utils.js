import { useContext } from "react";
import { AuthenticationContext } from "../provider/AuthenticationProvider";
import api from "../services/api";
import { deleteToken, getToken } from "./localstorage";
import { useNavigate } from "react-router-dom";

export const getUserData = async () => {
  const userToken = getToken();

  if (userToken) {
    const response = await api.get("/api/auth/user");
    return response;
  }
};

export const logoutUser = () => {
  const { setIsAuthenticated, setUserData } = useContext(AuthenticationContext);
  const navigate = useNavigate();

  setIsAuthenticated(false);
  setUserData({});
  deleteToken();

  navigate("/");
};
