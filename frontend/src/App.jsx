import AppRouter from './routes/AppRouter';
import { Toaster } from 'react-hot-toast';

export default function App() {
  return (
    <>
      <AppRouter />
      <Toaster 
        position="top-right"
        toastOptions={{
          success: {
            style: {
              background: '#dcfce7', 
              color: '#166534',      
            },
          },
          error: {
            style: {
              background: '#fee2e2',
              color: '#991b1b',      
            },
          },
        }}
      />
    </>
  );
}
