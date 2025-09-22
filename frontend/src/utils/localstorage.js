export const setLocalStorageData = (data, value) => {
  localStorage.setItem(data, value);
};

export const getLocalStorageData = (data) => {
  return localStorage.getItem(data);
};

export const setToken = (token) => {
  localStorage.setItem("token", token);
};

export const getToken = () => {
  return localStorage.getItem("token");
};

export const deleteToken = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("CSRF_TOKEN");
};
