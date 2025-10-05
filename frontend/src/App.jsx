import AppRouter from './routes/AppRouter';
import { Toaster } from 'react-hot-toast';

export default function App() {
  return (
    <>
      <AppRouter />
      <Toaster position="top-right" />
    </>
  );
}
