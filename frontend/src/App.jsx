import { Toaster } from "./components/ui/toaster";
import AppRoutes from "./routes/Routes";
function App() {
  return (
    <>
      <AppRoutes />
      <Toaster />
    </>
  );
}

export default App;
