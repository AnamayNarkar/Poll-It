import LoginPage from './pages/loginPage/LoginPage'

import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import ProtectedRoute from './services/ProtectedRouteForUser';
import ProtectedRouteForUser from './services/ProtectedRouteForUser';

function App() {

  const router = createBrowserRouter([
    {
      path: "/test",
      element: <div>Test</div>
    },
    {
      path: "/home",
      element: <ProtectedRouteForUser element={<div>Important</div>} />
    },
    {
      path: "/auth",
      element: <LoginPage />,
    },

  ]);

  return (
    <>
      <RouterProvider router={router} />
    </>
  )
}

export default App
