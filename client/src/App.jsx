import LoginPage from './pages/loginPage/LoginPage'

import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import ProtectedRoute from './services/ProtectedRouteForUser';
import ProtectedRouteForUser from './services/ProtectedRouteForUser';
import DashboardPage from './pages/dashboardPage/DashboardPage';

function App() {

  const router = createBrowserRouter([
    {
      path: "/test",
      element: <div>Test</div>
    },
    {
      path: "/auth",
      element: <LoginPage />,
    },
    {
      path: "/home",
      element: <ProtectedRouteForUser element={<DashboardPage feedType="home" />} />
    },
    {
      path: "/popular",
      element: <ProtectedRouteForUser element={<DashboardPage feedType="popular" />} />
    },
    {
      path: "/u/:usernameParam",
      element: <ProtectedRouteForUser element={<DashboardPage feedType="user" />} />
    },
    {
      path: "/t/:tagParam",
      element: <ProtectedRouteForUser element={<DashboardPage feedType="tag" />} />
    }

  ]);

  return (
    <>
      <RouterProvider router={router} />
    </>
  )
}

export default App
