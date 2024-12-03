import { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const ProtectedRouteForUser = ({ element }) => {
    const navigate = useNavigate();

    const [isLoading, setIsLoading] = useState(true);
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        const checkUser = async () => {
            try {
                const response = await axios.get("/user/verifyUser");
                if (response.status >= 200 && response.status < 300) {
                    setIsAuthenticated(true);
                }
            } catch (error) {
                if (error.response && error.response.status === 401) {
                    navigate("/auth");
                } else {
                    console.error("Error verifying user:", error);
                }
            } finally {
                setIsLoading(false);
            }
        };

        checkUser();
    }, [navigate]);

    if (isLoading) {
        return <div style={{ backgroundColor: '#242424', color: '#fff', textAlign: 'center', padding: '20px' }}>Loading...</div>;
    }

    return isAuthenticated ? element : null;
};

export default ProtectedRouteForUser;